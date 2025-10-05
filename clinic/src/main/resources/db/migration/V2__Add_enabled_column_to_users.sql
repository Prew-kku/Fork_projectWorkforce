-- ที่ไฟล์: src/main/resources/db/migration/V2__Add_enabled_column_to_users.sql
ALTER TABLE users ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT TRUE;