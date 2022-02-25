package uz.akmal.furortask.ui.screens

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
    var name = ""
    var address = ""
    var cost = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadViews()
        viewModel.getList()
        clickReceiver()
        observe()
    }

    private fun loadViews() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
        }
        adapter = ItemAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = adapter
    }

    private fun clickReceiver() {
        adapter.itemClickListener {
            findNavController().navigate(MainScreenDirections.actionMainScreenToBottomSheetDialog(it.toInt()))
        }
        binding.apply {
            more.setOnClickListener { }
            search.setOnClickListener { }
            fab.setOnClickListener {
                openDialog()
            }
        }
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
                    val list = it.data as ArrayList<GetItemResponse>
                    adapter.submitList(list)

                }
                else -> {
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun openDialog() {
        val builder = AlertDialog.Builder(context)
        val binding = uz.akmal.furortask.databinding.ItemDialogBinding.inflate(LayoutInflater.from(context), null, false)
        builder.setView(binding.root)

        builder.apply {
            setTitle("Fill the form")
            setPositiveButton("OK") { dialog, which ->
                if (binding.inputName.text.isNullOrBlank()) {
                    binding.name.error = "Required"
                }
                if (binding.inputAddress.text.isNullOrEmpty()) {
                    binding.inputAddress.error = "Required"
                }
                if (binding.inputCost.text.isNullOrBlank()) {
                    binding.cost.error = "Required"
                } else {
                    name = binding.inputName.text.toString()
                    address = binding.inputAddress.text.toString()
                    cost = binding.inputCost.text.toString().toInt()
                }
                Toast.makeText(context, "$name, $address, $cost", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            show()
        }
    }
}