CREATE DATABASE MomentCopywriter;
GO

USE MomentCopywriter;
GO

CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    password_salt VARCHAR(32) NOT NULL,
    phone NVARCHAR(20) NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    create_time DATETIME NOT NULL DEFAULT GETDATE()
);
GO

CREATE TABLE copywriting_records (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NULL,
    scene NVARCHAR(200) NOT NULL,
    mood NVARCHAR(50) NULL,
    style NVARCHAR(50) NULL,
    keywords NVARCHAR(200) NULL,
    generated_content NVARCHAR(MAX) NOT NULL,
    ai_model NVARCHAR(100) NULL,
    create_time DATETIME NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_copywriting_records_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE SET NULL
);
GO

CREATE INDEX idx_copywriting_records_user_time
    ON copywriting_records(user_id, create_time DESC);
GO
