package com.aienglish.core.database.driver

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase
import java.io.File
import java.util.Properties

private const val DB_NAME = "ai_english.db"

/**
 * Tạo Desktop (JVM) SQLite driver.
 *
 * DB duy nhất: ai_english.db — không bao giờ đổi tên.
 * - Fresh install: Schema.create() tạo toàn bộ schema mới nhất và ghi user_version.
 * - Upgrade: đọc PRAGMA user_version, gọi Schema.migrate(old, new) để chạy
 *   các file .sqm còn thiếu theo thứ tự, sau đó cập nhật user_version.
 *
 * Khi thay đổi schema:
 * 1. Cập nhật trực tiếp file .sq
 * 2. Tạo [N].sqm với ALTER TABLE
 * 3. Tăng version = N trong build.gradle.kts
 */
fun createDesktopDriver(): SqlDriver {
    val dbDir = File(System.getProperty("user.home"), ".ai-english")
    dbDir.mkdirs()
    val dbFile = File(dbDir, DB_NAME)
    val isNew = !dbFile.exists()

    val driver = JdbcSqliteDriver(
        url = "jdbc:sqlite:${dbFile.absolutePath}",
        properties = Properties().apply { put("foreign_keys", "true") }
    )

    if (isNew) {
        // Fresh install: tạo schema và ghi version
        AIEnglishDatabase.Schema.create(driver)
        driver.execute(null, "PRAGMA user_version = ${AIEnglishDatabase.Schema.version}", 0)
    } else {
        // Upgrade: kiểm tra version và migrate nếu cần
        val currentVersion = driver
            .executeQuery(null, "PRAGMA user_version", { cursor ->
                QueryResult.Value(if (cursor.next().value) cursor.getLong(0) ?: 0L else 0L)
            }, 0)
            .value

        val targetVersion = AIEnglishDatabase.Schema.version
        if (currentVersion < targetVersion) {
            try {
                driver.execute(null, "PRAGMA foreign_keys = OFF", 0)
                AIEnglishDatabase.Schema.migrate(driver, currentVersion, targetVersion)
                driver.execute(null, "PRAGMA user_version = $targetVersion", 0)
            } catch (e: Exception) {
                println("Database migration failed: ${e.message}. Ensuring all tables exist...")
                AIEnglishDatabase.Schema.create(driver)
                driver.execute(null, "PRAGMA user_version = $targetVersion", 0)
            } finally {
                driver.execute(null, "PRAGMA foreign_keys = ON", 0)
            }
        }
    }

    return driver
}
