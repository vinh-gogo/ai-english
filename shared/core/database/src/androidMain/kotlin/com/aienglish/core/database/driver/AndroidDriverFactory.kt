package com.aienglish.core.database.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase

/**
 * Tạo Android SQLite driver.
 *
 * Schema được định nghĩa hoàn toàn trong các file .sq.
 * Không còn migration files (.sqm) — schema là final version.
 *
 * Khi thay đổi schema trong tương lai:
 * 1. Cập nhật trực tiếp file .sq tương ứng
 * 2. Tạo file [N].sqm cho ALTER TABLE / data migration
 * 3. Tăng version = N trong build.gradle.kts
 * AndroidSqliteDriver tự động gọi Schema.migrate() khi phát hiện version cũ hơn.
 */
fun createAndroidDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = AIEnglishDatabase.Schema,
        context = context,
        name = "ai_english_v5.db"
    )
