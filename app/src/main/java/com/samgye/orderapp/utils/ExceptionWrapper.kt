package com.samgye.orderapp.utils

import java.io.IOException

/**
 * @suppress
 * 인터셉터 등 네트워킹 내부에서 발생한 IOException 외 모든 예외를 onFailure로 전달 가능하도록 IOException으로 래핑
 */
class ExceptionWrapper(
    val origin: Throwable
): IOException()

/**
 * @suppress
 */
val Throwable.origin
    get() = if (this is ExceptionWrapper) origin else this
