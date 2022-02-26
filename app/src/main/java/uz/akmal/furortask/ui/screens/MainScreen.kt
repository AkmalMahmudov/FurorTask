package uz.akmal.furortask.ui.screens

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.akmal.furortask.R
import uz.akmal.furortask.databinding.FragmentMainBinding
import uz.akmal.furortask.databinding.ItemDialogBinding
import uz.akmal.furortask.model.data.response.GetItemResponse
import uz.akmal.furortask.ui.adapters.ItemAdapter
import uz.akmal.furortask.util.CurrencyEvent
import uz.akmal.furortask.util.EventBus
import uz.akmal.furortask.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ItemAdapter
    private val perPage = 5
    private var dialog: BottomSheetDialog? = null
    private var currentPage = 1
    private val navController by lazy { findNavController() }
    var connection: Boolean? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        connection = true
        super.onViewCreated(view, savedInstanceState)
        checkInternet()
        loadViews()
        viewModel.getListPaging(1, perPage)
        viewModel.getItemsRoom()
//        clickReceiver()
        observe()
//        addDialog()
    }

    private fun addDialog(it: GetItemResponse) {
        dialog = BottomSheetDialog()
        dialog!!.setTargetFragment(this, 1)
        dialog!!.itemClickListener { item ->
            val list = adapter.currentList.toMutableList()
            list.remove(item)
            adapter.submitList(list)
        }
        dialog!!.itemEdit { oldItem, newItem ->
            val list = adapter.currentList.toMutableList()
            val index = list.indexOf(oldItem)
            list.remove(oldItem)
            list.add(index, newItem)
            adapter.submitList(list)
        }
        dialog?.item = it
        dialog?.show(parentFragmentManager, dialog?.tag)
    }

    private fun loadViews() {
        adapter = ItemAdapter()
        binding.recycler.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = adapter
        binding.recycler.addOnScrollListener(scrollListener)
    }


    private fun clickReceiver() {
        if (connection == false) {
            adapter.itemClickListener {
//            if (navController.currentDestination?.id == R.id.mainScreen) {
//                dialog?.item=it
//                dialog?.show(parentFragmentManager, dialog?.tag)
                addDialog(it)
//                navController.navigate(MainScreenDirections.actionMainScreenToBottomSheetDialog(it))

            }
            binding.apply {
                fab.setOnClickListener {
                    openDialog()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observe() {
        viewModel.getPaeList.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is CurrencyEvent.Failure -> {
                        internetDialog()
                        binding.progressbar.isVisible = false
                        binding.mode.visibility = View.VISIBLE
                        connection = false
                        clickReceiver()

                        Snackbar.make(binding.root, it.errorText, Snackbar.LENGTH_SHORT).show()
                    }
                    is CurrencyEvent.Loading -> {
                        binding.progressbar.isVisible = true
                    }
                    is CurrencyEvent.Success<*> -> {
                        connection = true
                        binding.progressbar.isVisible = false
                        val list = it.data as ArrayList<GetItemResponse>
                        val list2 = adapter.currentList.toMutableList()
                        list2.addAll(list)
                        adapter.submitList(list2)
//                        viewModel.deleteAllRoom()
                        viewModel.insertAllRoom(list)
                        clickReceiver()
                    }
                    else -> {
                    }
                }
                viewModel.navigate()
            }
        }
        viewModel.insertItem.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is CurrencyEvent.Failure -> {
                        Snackbar.make(binding.root, it.errorText, Snackbar.LENGTH_SHORT).show()
                    }
                    is CurrencyEvent.Loading -> {
                    }
                    is CurrencyEvent.Success<*> -> {
                        Toast.makeText(context, "this item successfully added", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {
                    }
                }
            }
        }

        viewModel.getItemsRoom.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDialog() {
        var named = ""
        var addressed = ""
        var costed = 0F

        val binding = ItemDialogBinding.inflate(LayoutInflater.from(context), null, false)
        val alertDialog = Dialog(requireContext())
        alertDialog.apply {
            setTitle("Fill the form")
            setContentView(binding.root)
            setCancelable(false)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            create()
            show()
        }
        binding.apply {
            ok.setOnClickListener {
                if (inputName.text.isNullOrEmpty()) {
                    binding.name.error = "Required"
                } else {
                    binding.name.error = null
                    named = inputName.text.toString()
                }
                if (inputAddress.text.isNullOrEmpty()) {
                    binding.address.error = "Required"
                } else {
                    binding.address.error = null
                    addressed = inputAddress.text.toString()
                }
                if (inputCost.text.isNullOrEmpty()) {
                    binding.cost.error = "Required"
                } else {
                    binding.cost.error = null
                    costed = inputCost.text.toString().toFloat()
                }

                if (!inputName.text.isNullOrEmpty() && !inputAddress.text.isNullOrEmpty() && !inputCost.text.isNullOrEmpty()) {
                    val date = getCurrentDateTime()
                    val dateInString = date.toString("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    viewModel.insertItem(addressed, costed.toInt(), dateInString, named, 1)
                    alertDialog.dismiss()
                }
            }
            cancel.setOnClickListener {
                alertDialog.cancel()
            }
        }
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (currentPage++ == 1 && dy <= 0) {
                viewModel.getListPaging(currentPage, perPage)
//                currentPage++
            } else {
                if (layoutManager.findLastVisibleItemPosition() >= currentPage * perPage - 1) {
                    viewModel.getListPaging(currentPage, perPage)
                    currentPage++
                    Log.d("mkm", "onScrolled: $currentPage ")
                }
            }
        }
    }

    private fun checkInternet() {
        CoroutineScope(Dispatchers.Main).launch {
            EventBus.internet.collectLatest {
                if (!it) {
                    binding.mode.visibility = View.VISIBLE
                    connection = false
                    Toast.makeText(context, "room  $connection", Toast.LENGTH_SHORT).show()
                } else {
                    binding.mode.visibility = View.GONE
                    connection = true
                    Toast.makeText(context, "retrofit $connection", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun internetDialog() {
        AlertDialog.Builder(context)
            .setTitle("Connection lost\n to make changes go online")
            .setIcon(R.drawable.ic_no_internet)
            .setPositiveButton("OK") { _, _ -> }
            .setCancelable(false)
            .create().show()
    }

    private fun check(): Boolean {
        var answer = false
        CoroutineScope(Dispatchers.Main).launch {
            EventBus.internet.collectLatest {
                answer = it
                return@collectLatest
            }
        }
        return answer
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.navigate()
    }
}