package com.aienglish.core.database.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase
import java.io.File
import java.util.Properties

/**
 * Tạo Desktop (JVM) SQLite driver với tự động migration schema.
 *
 * Cơ chế hoạt động:
 * - DB mới: gọi Schema.create() để tạo toàn bộ schema mới nhất
 * - DB cũ: đọc user_version từ SQLite PRAGMA, sau đó gọi Schema.migrate()
 *   để tự chạy các file .sqm còn thiếu theo thứ tự
 *
 * Khi thêm schema mới:
 * 1. Tạo file [N].sqm với câu lệnh ALTER TABLE
 * 2. Tăng version = N trong build.gradle.kts
 * Không cần sửa file này.
 */
fun createDesktopDriver(): SqlDriver {
    val dbDir = File(System.getProperty("user.home"), ".ai-english")
    dbDir.mkdirs()
    val dbFile = File(dbDir, "ai_english_v4.db")
    val isNew = !dbFile.exists()

    val driver = JdbcSqliteDriver(
        url = "jdbc:sqlite:${dbFile.absolutePath}",
        properties = Properties().apply { put("foreign_keys", "true") }
    )

    if (isNew) {
        // Fresh install: tạo schema mới nhất
        AIEnglishDatabase.Schema.create(driver)
    } else {
        // Existing DB: kiểm tra version và migrate nếu cần
        val currentVersion = driver
            .executeQuery(null, "PRAGMA user_version", { cursor ->
                app.cash.sqldelight.db.QueryResult.Value(
                    if (cursor.next().value) cursor.getLong(0) ?: 0L else 0L
                )
            }, 0)
            .value

        val schemaVersion = AIEnglishDatabase.Schema.version
        if (currentVersion < schemaVersion) {
            AIEnglishDatabase.Schema.migrate(
                driver = driver,
                oldVersion = currentVersion,
                newVersion = schemaVersion
            )
            // Cập nhật user_version sau khi migrate
            driver.execute(null, "PRAGMA user_version = $schemaVersion", 0)
        }
    }

    return driver
}
