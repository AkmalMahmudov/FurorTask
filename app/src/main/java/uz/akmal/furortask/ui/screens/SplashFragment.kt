package uz.akmal.furortask.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import uz.akmal.furortask.R

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val navController by lazy { findNavController() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scope.launch {
            delay(2250)
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToMainScreen())
        }
    }
}