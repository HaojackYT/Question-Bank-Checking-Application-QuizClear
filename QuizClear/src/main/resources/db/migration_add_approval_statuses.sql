-- Migration script to add approved and rejected statuses to tasks table
-- Run this if you have an existing database

USE QuizClear;

-- Add the new enum values to the existing status column
ALTER TABLE tasks MODIFY COLUMN status ENUM('pending', 'in_progress', 'completed', 'approved', 'rejected', 'cancelled') DEFAULT 'pending';

-- Verify the change
DESCRIBE tasks;
