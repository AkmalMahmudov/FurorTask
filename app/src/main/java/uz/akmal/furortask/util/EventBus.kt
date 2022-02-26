package uz.akmal.furortask.util

import kotlinx.coroutines.flow.MutableSharedFlow

object EventBus {
    val internet = MutableSharedFlow<Boolean>()
}