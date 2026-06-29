package com.aienglish.core.database.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase

private const val DB_NAME = "ai_english.db"

/**
 * Tạo Android SQLite driver.
 *
 * DB duy nhất: ai_english.db — không bao giờ đổi tên.
 * - Fresh install: Schema.create() tạo toàn bộ schema mới nhất.
 * - Upgrade: AndroidSqliteDriver tự gọi Schema.migrate(oldVersion, newVersion),
 *   chạy các file .sqm theo thứ tự.
 *
 * Khi thay đổi schema:
 * 1. Cập nhật trực tiếp file .sq
 * 2. Tạo [N].sqm với ALTER TABLE
 * 3. Tăng version = N trong build.gradle.kts
 */
fun createAndroidDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = AIEnglishDatabase.Schema,
        context = context,
        name = DB_NAME
    )
