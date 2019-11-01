package de.adorsys.keymanagement.keyrotation.jdbc;

import de.adorsys.keymanagement.keyrotation.api.persistence.KeyStorePersistence;
import de.adorsys.keymanagement.keyrotation.api.persistence.RotationLocker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import net.javacrumbs.shedlock.provider.jdbc.JdbcLockProvider;

import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.time.Duration;
import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class JdbcRotationManager implements KeyStorePersistence, RotationLocker {

    private final String keyStoreId;
    private final LockProvider lockProvider;
    private final DataSource dataSource;
    private final String keyStoreTableName;
    private final LockingTaskExecutor executor;
    private final Duration lockAtMost;

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
    public byte[] read() {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn
                        .prepareStatement("SELECT keystore FROM " + keyStoreTableName + " WHERE id = ?")
        ) {
            stmt.setString(0, keyStoreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Blob blob = rs.getBlob(0);
                return blob.getBytes(0, (int) blob.length());
            }
        }
    }

    @Override
    @SneakyThrows
    public void write(byte[] keyStore) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn
                        .prepareStatement("UPDATE " + keyStoreTableName + " SET keystore = ? WHERE id = ?")
        ) {
            stmt.setBlob(0, new SerialBlob(keyStore));
            stmt.setString(0, keyStoreId);
            int cnt = stmt.executeUpdate();

            if (0 == cnt) {
                doInsert(conn);
            }
        }
    }

    private void doInsert(Connection conn) throws SQLException {
        try (PreparedStatement insert = conn
                .prepareStatement("INSERT INTO " + keyStoreTableName + " (id, keystore) VALUES (?, ?)")) {
            insert.executeUpdate();
        }
    }

    @Override
    public void executeWithLock(Runnable runnable) {
        executor.executeWithLock(runnable, new LockConfiguration(keyStoreId, Instant.now().plus(lockAtMost)));
    }
}
