package uz.akmal.furortask.ui.screens

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.akmal.furortask.R
import uz.akmal.furortask.databinding.FragmentMainBinding
import uz.akmal.furortask.model.data.response.GetItemResponse
import uz.akmal.furortask.ui.adapters.ItemAdapter
import uz.akmal.furortask.util.CurrencyEvent
import uz.akmal.furortask.viewModel.MainViewModel

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    lateinit var adapter: ItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadViews()
        viewModel.getList()
        clickReceiver()
        observe()
//        adapter.submitList(listOf(GetItemResponse("London", 15.0, 224455, 44, "Lewis Hamilton", 5)))
    }

    private fun loadViews() {
        adapter = ItemAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.recycler.adapter = adapter
    }

    private fun clickReceiver() {

    }

    private fun observe() {
        viewModel.filtersList.observe(viewLifecycleOwner) {
            when (it) {
                is CurrencyEvent.Failure -> {
                    Snackbar.make(binding.root, it.errorText, Snackbar.LENGTH_SHORT).show()
                }
                is CurrencyEvent.Loading -> {
                    binding.progressbar.isVisible = true
                }
                is CurrencyEvent.Success<*> -> {
                    binding.progressbar.isVisible = false
                    val list = it.data as List<GetItemResponse>
//                    val ls = adapter2.currentList.toMutableList()
//                    ls.addAll(list.shortLotBeans)
//                    adapter2.notifyDataSetChanged()
                    adapter.submitList(list)
                }
                else -> {
                }
            }
        }
    }
}