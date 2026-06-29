package com.aienglish.core.database.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase

/**
 * Tạo Android SQLite driver với tự động migration schema.
 *
 * Cơ chế hoạt động:
 * - DB mới (fresh install): Schema.create() tạo toàn bộ schema mới nhất
 * - DB cũ (upgrade): Schema.migrate(oldVersion, newVersion) tự chạy
 *   các file .sqm theo thứ tự (ví dụ: 2.sqm → 3.sqm → 4.sqm)
 *
 * Khi thêm schema mới:
 * 1. Tạo file [N].sqm với câu lệnh ALTER TABLE
 * 2. Tăng version = N trong build.gradle.kts
 * Không cần sửa file này.
 */
fun createAndroidDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = AIEnglishDatabase.Schema,
        context = context,
        name = "ai_english_v4.db"
    )
