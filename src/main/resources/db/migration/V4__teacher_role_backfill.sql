-- V4__teacher_role_backfill.sql
-- Idempotent: safe to run on a fresh DB (no rows match) or on an existing DB with
-- pre-role teachers (the column was added in V1 with a DEFAULT, so this is mainly
-- a safety net for older schemas).

UPDATE teachers SET role = 'TEACHER' WHERE role IS NULL;
