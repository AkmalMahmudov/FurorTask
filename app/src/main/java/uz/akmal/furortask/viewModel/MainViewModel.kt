package uz.akmal.furortask.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.akmal.furortask.model.MainRepository
import uz.akmal.furortask.util.CurrencyEvent
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private var _getFiltersList = MutableLiveData<CurrencyEvent>()
    val filtersList: LiveData<CurrencyEvent> get() = _getFiltersList

    fun getList() {
        CoroutineScope(Dispatchers.IO).launch {
            _getFiltersList.postValue(CurrencyEvent.Loading)
            viewModelScope.launch {
                _getFiltersList.value = repository.getItemsList()
            }
        }
    }

}