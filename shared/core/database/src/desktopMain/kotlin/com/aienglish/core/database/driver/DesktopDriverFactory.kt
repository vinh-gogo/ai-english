package com.aienglish.core.database.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase
import java.io.File
import java.util.Properties

fun createDesktopDriver(): SqlDriver {
    val dbDir = File(System.getProperty("user.home"), ".ai-english")
    dbDir.mkdirs()
    val dbFile = File(dbDir, "ai_english_v2.db")
    val isNew = !dbFile.exists()
    val driver = JdbcSqliteDriver(
        url = "jdbc:sqlite:${dbFile.absolutePath}",
        properties = Properties().apply { put("foreign_keys", "true") }
    )
    if (isNew) AIEnglishDatabase.Schema.create(driver)
    return driver
}
