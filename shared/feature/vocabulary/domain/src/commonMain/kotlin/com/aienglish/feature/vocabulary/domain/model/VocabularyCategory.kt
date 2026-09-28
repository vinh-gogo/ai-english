package com.aienglish.feature.vocabulary.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class VocabularyCategory(
    val code: String,
    val displayName: String,
    val iconEmoji: String
) {
    GENERAL("GENERAL", "Chung", "🌐"),
    IT("IT", "Công nghệ thông tin", "💻"),
    MARKETING("MARKETING", "Marketing", "📢"),
    HR("HR", "Nhân sự", "👥"),
    PSYCHOLOGY("PSYCHOLOGY", "Tâm lý học", "🧠"),
    AGRICULTURE("AGRICULTURE", "Nông nghiệp", "🌾"),
    FOOD_BEVERAGE("FOOD_BEVERAGE", "Ẩm thực & Đồ ăn", "🍔"),
    BUSINESS_FINANCE("BUSINESS_FINANCE", "Kinh doanh & Tài chính", "💼"),
    HEALTHCARE("HEALTHCARE", "Y tế & Sức khỏe", "🏥"),
    TRAVEL_TOURISM("TRAVEL_TOURISM", "Du lịch & Khách sạn", "✈️"),
    DAILY_LIFE("DAILY_LIFE", "Đời sống hàng ngày", "☕");

    companion object {
        fun fromCode(code: String?): VocabularyCategory {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: GENERAL
        }
    }
}
