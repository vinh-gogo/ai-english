package com.aienglish.core.database.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase
import java.io.File
import java.util.Properties

private val LEGACY_DB_NAMES = listOf(
    "ai_english.db",
    "ai_english_v1.db",
    "ai_english_v2.db",
    "ai_english_v3.db",
    "ai_english_v4.db"
)

private const val CURRENT_DB_NAME = "ai_english_v5.db"

/**
 * Tạo Desktop (JVM) SQLite driver.
 * Tự động dọn dẹp các file database cũ (v1–v4) trong ~/.ai-english/ khi khởi động.
 *
 * Khi thay đổi schema trong tương lai:
 * 1. Cập nhật trực tiếp file .sq tương ứng
 * 2. Tạo file [N].sqm cho ALTER TABLE / data migration
 * 3. Tăng version = N trong build.gradle.kts
 * 4. Thêm tên DB cũ vào LEGACY_DB_NAMES nếu đổi tên file
 */
fun createDesktopDriver(): SqlDriver {
    val dbDir = File(System.getProperty("user.home"), ".ai-english")
    dbDir.mkdirs()

    // Dọn dẹp các file DB cũ thành rác
    LEGACY_DB_NAMES.forEach { oldName ->
        try {
            File(dbDir, oldName).takeIf { it.exists() }?.delete()
        } catch (_: Exception) { }
    }

    val dbFile = File(dbDir, CURRENT_DB_NAME)
    val isNew = !dbFile.exists()

    val driver = JdbcSqliteDriver(
        url = "jdbc:sqlite:${dbFile.absolutePath}",
        properties = Properties().apply { put("foreign_keys", "true") }
    )

    if (isNew) {
        AIEnglishDatabase.Schema.create(driver)
    }

    return driver
}
