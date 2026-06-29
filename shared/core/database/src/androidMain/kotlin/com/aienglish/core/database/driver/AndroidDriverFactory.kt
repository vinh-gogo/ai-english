package com.aienglish.core.database.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase

private val LEGACY_DB_NAMES = listOf(
    "ai_english.db",
    "ai_english_v1.db",
    "ai_english_v2.db",
    "ai_english_v3.db",
    "ai_english_v4.db"
)

private const val CURRENT_DB_NAME = "ai_english_v5.db"

/**
 * Tạo Android SQLite driver.
 * Tự động dọn dẹp các file database cũ (v1–v4) khi khởi động.
 *
 * Khi thay đổi schema trong tương lai:
 * 1. Cập nhật trực tiếp file .sq tương ứng
 * 2. Tạo file [N].sqm cho ALTER TABLE / data migration
 * 3. Tăng version = N trong build.gradle.kts
 * 4. Thêm tên DB cũ vào LEGACY_DB_NAMES nếu đổi tên file
 */
fun createAndroidDriver(context: Context): SqlDriver {
    // Dọn dẹp các file DB cũ thành rác
    LEGACY_DB_NAMES.forEach { oldName ->
        try {
            context.deleteDatabase(oldName)
        } catch (_: Exception) { }
    }

    return AndroidSqliteDriver(
        schema = AIEnglishDatabase.Schema,
        context = context,
        name = CURRENT_DB_NAME
    )
}
