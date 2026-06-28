package com.aienglish.core.database.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.aienglish.core.database.AIEnglishDatabase

fun createAndroidDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(AIEnglishDatabase.Schema, context, "ai_english_v3.db")
