package uz.akmal.furortask.ui.screens

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.akmal.furortask.R
import uz.akmal.furortask.databinding.FragmentMainBinding
import uz.akmal.furortask.databinding.ItemDialogBinding
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
    private val perPage = 5
    private var currentPage = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadViews()
//        viewModel.getList()
        viewModel.getListPaging(currentPage++, perPage)
        clickReceiver()
        observe()
    }

    private fun loadViews() {

        adapter = ItemAdapter()
        binding.recycler.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = adapter
        binding.recycler.addOnScrollListener(scrollListener)
    }

    private fun clickReceiver() {
//        adapter.itemClickListener {
//            findNavController().navigate(MainScreenDirections.actionMainScreenToBottomSheetDialog(it.toInt()))
//        }
        binding.apply {
            more.setOnClickListener { }
            search.setOnClickListener { }
            fab.setOnClickListener {

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
                    val list2 = it.data as ArrayList<GetItemResponse>
                    list.addAll(list2)
                    adapter.submitList(list)

                }
                else -> {
                }
            }
        }
        viewModel.getPaeList.observe(viewLifecycleOwner) {
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
                    val list2 = adapter.currentList()
                    list.addAll(list2)
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
//        val inflater: LayoutInflater = layoutInflater
//        val dialogLayout = inflater.inflate(R.layout.item_dialog, null)
        val binding = ItemDialogBinding.inflate(LayoutInflater.from(context), null, false)

        builder.apply {
            setTitle("Fill the form")
            setPositiveButton("OK") { dialog, which ->
                name = binding.inputName.text.toString()
                address = binding.inputAddress.text.toString()
                cost = binding.inputCost.text.toString().toInt()
            }
            setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            show()
        }

    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (currentPage == 1 && dy <= 0) {
                viewModel.getListPaging(currentPage, perPage)
                currentPage++
            } else {
                if (layoutManager.findLastVisibleItemPosition() >= currentPage * perPage - 1) {
                    viewModel.getListPaging(currentPage, perPage)
                    currentPage++
                    Log.d("mkm", "onScrolled: $currentPage ")
                }
            }
        }
    }
}