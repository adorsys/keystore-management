CREATE TABLE rotation_lock
(
    name       VARCHAR(64),
    lock_until TIMESTAMP(3) NULL,
    locked_at  TIMESTAMP(3) NULL,
    locked_by  VARCHAR(255),
    PRIMARY KEY (name)
);

CREATE TABLE keystore
(
    id       VARCHAR(64),
    keystore BYTEA
);