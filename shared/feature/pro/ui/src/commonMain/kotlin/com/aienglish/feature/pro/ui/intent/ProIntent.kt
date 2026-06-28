package com.aienglish.feature.pro.ui.intent

sealed interface ProIntent {
    data object ShowVip : ProIntent
    data object HideVip : ProIntent
    data class SetVipPaid(val isPaid: Boolean) : ProIntent
    data object SyncState : ProIntent
}
