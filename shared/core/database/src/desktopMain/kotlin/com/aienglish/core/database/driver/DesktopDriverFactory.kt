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
        } else {
            // Đảm bảo mọi bảng và chỉ mục mới nhất đều tồn tại (CREATE TABLE/INDEX IF NOT EXISTS an toàn và idempotent)
            AIEnglishDatabase.Schema.create(driver)
            driver.execute(null, "PRAGMA user_version = $targetVersion", 0)
        }

        // Bổ sung các cột mới nếu bảng vocabulary_items đã tồn tại từ schema cũ
        try {
            driver.execute(null, "ALTER TABLE vocabulary_items ADD COLUMN part_of_speech TEXT", 0)
        } catch (_: Exception) {}
        try {
            driver.execute(null, "ALTER TABLE vocabulary_items ADD COLUMN collocations TEXT", 0)
        } catch (_: Exception) {}
        try {
            driver.execute(null, "UPDATE vocabulary_items SET part_of_speech = 'noun' WHERE part_of_speech IS NULL OR part_of_speech = ''", 0)
        } catch (_: Exception) {}
        try {
            driver.execute(null, "UPDATE vocabulary_items SET collocations = '' WHERE collocations IS NULL", 0)
        } catch (_: Exception) {}
        try {
            driver.execute(null, "UPDATE vocabulary_items SET word = 'Integration', phonetic = '/ˌɪntɪˈɡreɪʃn/', part_of_speech = 'noun', meaning = 'Sự tích hợp, hợp nhất — quá trình kết hợp các thành phần phần mềm hoặc hệ thống vào một thể thống nhất.', collocations = 'continuous integration, system integration, seamless integration, CI pipeline', example = 'Our IT team implements continuous integration to deploy bug-free builds automatically.' WHERE id = '1' OR word = 'Continuous Integration'", 0)
        } catch (_: Exception) {}
        try {
            driver.execute(null, "UPDATE vocabulary_items SET word = 'Acquisition', phonetic = '/ˌækwɪˈzɪʃn/', part_of_speech = 'noun', meaning = 'Sự thu nạp, tiếp nhận hoặc mua lại (trong nhân sự: thu hút nhân tài; trong kinh doanh: sáp nhập doanh nghiệp).', collocations = 'talent acquisition, customer acquisition, acquisition strategy', example = 'Our HR department is revamping its talent acquisition strategy to attract top engineers.' WHERE id = '3' OR word = 'Talent Acquisition'", 0)
        } catch (_: Exception) {}
        try {
            driver.execute(null, "UPDATE vocabulary_items SET word = 'Bias', phonetic = '/ˈbaɪəs/', part_of_speech = 'noun', meaning = 'Thiên kiến, định kiến — xu hướng phán đoán hoặc đánh giá thiên lệch làm sai lệch nhận thức.', collocations = 'cognitive bias, confirmation bias, unconscious bias', example = 'Psychologists study how cognitive bias influences consumer purchasing decisions.' WHERE id = '4' OR word = 'Cognitive Bias'", 0)
        } catch (_: Exception) {}
    }

    return driver
}
