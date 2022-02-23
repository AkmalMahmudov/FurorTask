package uz.akmal.furortask.util

import uz.akmal.furortask.model.data.response.GetItemResponse

sealed class CurrencyEvent {
    data class Success<T>(val data: T?): CurrencyEvent()
    class Failure(val errorText: String): CurrencyEvent()
    object Loading : CurrencyEvent()
    object Empty : CurrencyEvent()
}