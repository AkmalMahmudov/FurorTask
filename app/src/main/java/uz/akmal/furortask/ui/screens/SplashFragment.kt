package uz.akmal.furortask.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import uz.akmal.furortask.R
import uz.akmal.furortask.util.EventBus

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val navController by lazy { findNavController() }

    private var _internet = MutableLiveData<Boolean>()
    val internet: LiveData<Boolean> get() = _internet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scope.launch {
            EventBus.internet.collectLatest {
                if (!it) {
                    _internet.postValue(true)
                } else {
                    _internet.postValue(false)
                }
            }
            delay(2250)
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToMainScreen())
        }
    }
}