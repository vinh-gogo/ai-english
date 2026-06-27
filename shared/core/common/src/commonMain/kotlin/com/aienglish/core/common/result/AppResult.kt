package com.aienglish.core.common.result

import arrow.core.Either

typealias AppResult<T> = Either<AppError, T>

sealed class AppError(open val message: String) {
    data class Network(override val message: String) : AppError(message)
    data class Database(override val message: String) : AppError(message)
    data class Unknown(override val message: String) : AppError(message)
}
