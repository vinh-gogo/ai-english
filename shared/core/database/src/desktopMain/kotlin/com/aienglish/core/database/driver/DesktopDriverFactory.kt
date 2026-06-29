package com.aienglish.core.database.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase
import java.io.File
import java.util.Properties

/**
 * Tạo Desktop (JVM) SQLite driver.
 *
 * Schema được định nghĩa hoàn toàn trong các file .sq.
 * Không còn migration files (.sqm) — schema là final version.
 *
 * Khi thay đổi schema trong tương lai:
 * 1. Cập nhật trực tiếp file .sq tương ứng
 * 2. Tạo file [N].sqm cho ALTER TABLE / data migration
 * 3. Tăng version = N trong build.gradle.kts
 */
fun createDesktopDriver(): SqlDriver {
    val dbDir = File(System.getProperty("user.home"), ".ai-english")
    dbDir.mkdirs()
    val dbFile = File(dbDir, "ai_english_v5.db")
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
