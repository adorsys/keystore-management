package de.adorsys.keymanagement.keyrotation.jdbc;

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
import net.javacrumbs.shedlock.provider.jdbc.JdbcLockProvider;
import net.javacrumbs.shedlock.support.StorageBasedLockProvider;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@RequiredArgsConstructor
public class JdbcRotationManager implements KeyStorePersistence, RotationLocker {

    private final String keyStoreId;
    private final LockProvider lockProvider;
    private final DataSource dataSource;
    private final String keyStoreTableName;
    private final LockingTaskExecutor executor;
    private final Duration lockAtMost;

    /**
     * KeyStore persistence and rotation locking belong to same Database.
     */
    public JdbcRotationManager(
            String keyStoreId,
            DataSource dataSource,
            String lockTableName,
            String keyStoreTableName,
            Duration lockAtMost) {
        this.keyStoreId = keyStoreId;
        this.lockProvider = new JdbcLockProvider(dataSource, lockTableName);
        this.executor = new DefaultLockingTaskExecutor(lockProvider);
        this.dataSource = dataSource;
        this.keyStoreTableName = keyStoreTableName;
        this.lockAtMost = lockAtMost;
    }

    /**
     * KeyStore persistence happens in JDBC, but locking is provided by other provider
     * (other RDBMS/database, Redis,...)
     */
    public JdbcRotationManager(
            String keyStoreId,
            DataSource dataSource,
            LockProvider lockProvider,
            String keyStoreTableName,
            Duration lockAtMost) {
        this.keyStoreId = keyStoreId;
        this.lockProvider = lockProvider;
        this.executor = new DefaultLockingTaskExecutor(lockProvider);
        this.dataSource = dataSource;
        this.keyStoreTableName = keyStoreTableName;
        this.lockAtMost = lockAtMost;
    }

    @Override
    @SneakyThrows
    @SuppressFBWarnings(value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING",
            justification = "Table name is unbindable and is trusted code-provided")
    @Nullable
    public byte[] read() {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn
                        .prepareStatement("SELECT keystore FROM " + keyStoreTableName + " WHERE id = ?")
        ) {
            stmt.setString(1, keyStoreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return rs.getBytes(1);
            }
        }
    }

    @Override
    @SneakyThrows
    @SuppressFBWarnings(value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING",
            justification = "Table name is unbindable and is trusted code-provided")
    public void write(byte[] keyStore) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn
                        .prepareStatement("UPDATE " + keyStoreTableName + " SET keystore = ? WHERE id = ?")
        ) {
            conn.setAutoCommit(false);
            setKeyStore(1, keyStore, stmt);
            stmt.setString(2, keyStoreId);
            int cnt = stmt.executeUpdate();

            if (0 == cnt) {
                doInsert(conn, keyStore);
            }

            conn.commit();
        }
    }

    @Override
    public void executeWithLock(Runnable runnable) {

        executor.executeWithLock(runnable, new LockConfiguration(Instant.now(), keyStoreId, lockAtMost, Duration.of(5, ChronoUnit.MILLIS)));
    }

    @Override
    public void clearCache() {
        if (lockProvider instanceof StorageBasedLockProvider) {
            ((StorageBasedLockProvider) lockProvider).clearCache();
        }
    }

    private void setKeyStore(int pos, byte[] keyStore, PreparedStatement stmt) throws SQLException {
        ByteArrayInputStream is = new ByteArrayInputStream(keyStore);
        stmt.setBinaryStream(pos, is, is.available());
    }

    @SuppressFBWarnings(value = "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING",
            justification = "Table name is unbindable and is trusted code-provided")
    private void doInsert(Connection conn, byte[] keyStore) throws SQLException {
        try (PreparedStatement insert = conn
                .prepareStatement("INSERT INTO " + keyStoreTableName + " (id, keystore) VALUES (?, ?)")) {
            insert.setString(1, keyStoreId);
            setKeyStore(2, keyStore, insert);
            insert.executeUpdate();
        }
    }
}
