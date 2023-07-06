package de.adorsys.keymanagement.keyrotation.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import net.javacrumbs.shedlock.support.StorageBasedLockProvider;
import org.bson.Document;
import org.bson.types.Binary;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Getter
@RequiredArgsConstructor
public class MongoRotationManager implements KeyStorePersistence, RotationLocker {

    private static final int LOCK_DURATION = 5;
    private final String keyStoreId;
    private final LockProvider lockProvider;
    private final MongoClient client;
    private final String databaseName;
    private final String keyStoreCollectionName;
    private final LockingTaskExecutor executor;
    private final Duration lockAtMost;

    /**
     * KeyStore persistence and rotation locking belong to same Database.
     */
    public MongoRotationManager(
            String keyStoreId,
            MongoClient client,
            String databaseName,
            String lockCollectionName,
            String keyStoreCollectionName,
            Duration lockAtMost) {
        this.keyStoreId = keyStoreId;
        this.lockProvider = new MongoLockProvider(client.getDatabase(databaseName).getCollection(lockCollectionName));
        this.executor = new DefaultLockingTaskExecutor(lockProvider);
        this.client = client;
        this.databaseName = databaseName;
        this.keyStoreCollectionName = keyStoreCollectionName;
        this.lockAtMost = lockAtMost;
    }

    /**
     * KeyStore persistence happens in Mongo, but locking is provided by other provider
     * (other RDBMS/database, Redis,...)
     */
    public MongoRotationManager(
            String keyStoreId,
            MongoClient client,
            LockProvider lockProvider,
            String databaseName,
            String keyStoreCollectionName,
            Duration lockAtMost) {
        this.keyStoreId = keyStoreId;
        this.lockProvider = lockProvider;
        this.executor = new DefaultLockingTaskExecutor(lockProvider);
        this.client = client;
        this.databaseName = databaseName;
        this.keyStoreCollectionName = keyStoreCollectionName;
        this.lockAtMost = lockAtMost;
    }

    @Override
    @SneakyThrows
    @Nullable
    @SuppressFBWarnings(
            value = "PZLA_PREFER_ZERO_LENGTH_ARRAYS",
            justification = "Null here means missing object and is API documented"
    )
    public byte[] read() {
        Document keyStoreDoc = client.getDatabase(databaseName).getCollection(keyStoreCollectionName)
                .find(eq("id", keyStoreId))
                .first();

        if (null == keyStoreDoc) {
            return null;

        }
        Binary blob = keyStoreDoc.get("keystore", Binary.class);
        return blob.getData();
    }

    @Override
    @SneakyThrows
    public void write(byte[] keyStore) {
        MongoCollection<Document> docs = client.getDatabase(databaseName).getCollection(keyStoreCollectionName);
        docs.updateOne(
                eq("id", keyStoreId),
                set("keystore", new Binary(keyStore)),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public void executeWithLock(Runnable runnable) {
        executor.executeWithLock(runnable, new LockConfiguration(Instant.now(), keyStoreId, lockAtMost,
                Duration.of(LOCK_DURATION, ChronoUnit.MILLIS)));
    }

    @Override
    public void clearCache() {
        if (lockProvider instanceof StorageBasedLockProvider) {
            ((StorageBasedLockProvider) lockProvider).clearCache();
        }
    }
}
