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
    private var _getPageList = MutableLiveData<CurrencyEvent?>()
    val getPaeList: LiveData<CurrencyEvent?> get() = _getPageList
    private var _deleteItem = MutableLiveData<CurrencyEvent?>()
    val deleteItem: LiveData<CurrencyEvent?> get() = _deleteItem

    fun getListPaging(page: Int, perPage: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _getPageList.postValue(CurrencyEvent.Loading)
            viewModelScope.launch {
                _getPageList.value = repository.getItemsAll(page, perPage)
            }
        }
    }

    fun deleteItem(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _deleteItem.postValue(CurrencyEvent.Loading)
            viewModelScope.launch {
                _deleteItem.value = repository.getDeleteItem(id)
            }
        }
    }

    fun navigate() {
        _getPageList.value = null
    }

    fun navigateDelete() {
        _deleteItem.value = null
    }
}