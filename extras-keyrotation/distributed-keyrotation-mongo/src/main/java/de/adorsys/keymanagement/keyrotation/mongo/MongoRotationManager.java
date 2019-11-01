package de.adorsys.keymanagement.keyrotation.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import org.bson.Document;
import org.bson.types.Binary;

import java.time.Duration;
import java.time.Instant;

import static com.mongodb.client.model.Filters.eq;

@Getter
@RequiredArgsConstructor
public class MongoRotationManager implements KeyStorePersistence, RotationLocker {

    private final String keyStoreId;
    private final LockProvider lockProvider;
    private final MongoClient client;
    private final String databaseName;
    private final String keyStoreCollectionName;
    private final LockingTaskExecutor executor;
    private final Duration lockAtMost;

    public MongoRotationManager(
            String keyStoreId,
            MongoClient client,
            String databaseName,
            String lockCollectionName,
            String keyStoreCollectionName,
            Duration lockAtMost) {
        this.keyStoreId = keyStoreId;
        this.lockProvider = new MongoLockProvider(client, databaseName, lockCollectionName);
        this.executor = new DefaultLockingTaskExecutor(lockProvider);
        this.client = client;
        this.databaseName = databaseName;
        this.keyStoreCollectionName = keyStoreCollectionName;
        this.lockAtMost = lockAtMost;
    }

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
    public byte[] read() {
        Document keyStoreDoc = client.getDatabase(databaseName).getCollection(keyStoreCollectionName)
                .find(eq("id", keyStoreId))
                .first();
        Binary blob = keyStoreDoc.get("keystore", Binary.class);
        return blob.getData();
    }

    @Override
    @SneakyThrows
    public void write(byte[] keyStore) {
        MongoCollection<Document> docs = client.getDatabase(databaseName).getCollection(keyStoreCollectionName);
        docs.updateOne(
                eq("id", keyStoreId),
                new BasicDBObject("keystore", new Binary(keyStore)),
                new UpdateOptions().upsert(true)
        );
    }

    @Override
    public void executeWithLock(Runnable runnable) {
        executor.executeWithLock(runnable, new LockConfiguration(keyStoreId, Instant.now().plus(lockAtMost)));
    }
}
