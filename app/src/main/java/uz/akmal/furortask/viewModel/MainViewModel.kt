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
import uz.akmal.furortask.model.data.request.InsertItemRequest
import uz.akmal.furortask.model.data.request.UpdateItemRequest
import uz.akmal.furortask.model.data.response.GetItemResponse
import uz.akmal.furortask.util.CurrencyEvent
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private var _getPageList = MutableLiveData<CurrencyEvent?>()
    val getPaeList: LiveData<CurrencyEvent?> get() = _getPageList

    private var _insertItem = MutableLiveData<CurrencyEvent?>()
    val insertItem: LiveData<CurrencyEvent?> get() = _insertItem

    private var _updateItem = MutableLiveData<CurrencyEvent?>()
    val updateItem: LiveData<CurrencyEvent?> get() = _updateItem

    private var _deleteItem = MutableLiveData<CurrencyEvent?>()
    val deleteItem: LiveData<CurrencyEvent?> get() = _deleteItem

    private var _getItemsRoom = MutableLiveData<List<GetItemResponse>>()
    val getItemsRoom: LiveData<List<GetItemResponse>> get() = _getItemsRoom

    fun getListPaging(page: Int, perPage: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _getPageList.postValue(CurrencyEvent.Loading)
            viewModelScope.launch {
                _getPageList.value = repository.getItemsAll(page, perPage)
            }
        }
    }

    fun insertItem(address: String, cost: Int, created_date: String, name_uz: String, product_type_id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _insertItem.postValue(CurrencyEvent.Loading)
            viewModelScope.launch {
                _insertItem.value = repository.insertItem(InsertItemRequest(address, cost, created_date, name_uz, product_type_id))
            }
        }
    }

    fun updateItem(address: String, cost: Int, created_date: String, id: Int, name_uz: String, product_type_id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _updateItem.postValue(CurrencyEvent.Loading)
            viewModelScope.launch {
                _updateItem.value = repository.updateItem(UpdateItemRequest(address, cost, created_date, id, name_uz, product_type_id))
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

    fun navigateUpdate() {
        _updateItem.value = null
    }

    fun insertAllRoom(list: List<GetItemResponse>) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertAllRoom(list)
        }
    }

    fun getItemsRoom() {
        CoroutineScope(Dispatchers.IO).launch {
            _getItemsRoom.postValue(repository.getItemsRoom())
        }
    }

    fun insertItemRoom(item: GetItemResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertRoom(item)
        }
    }

    fun updateItemRoom(item: GetItemResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.updateRoom(item)
        }
    }

    fun deleteItemRoom(item: GetItemResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteRoom(item)
        }
    }

    fun deleteAllRoom() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAllRoom()
        }
    }
}