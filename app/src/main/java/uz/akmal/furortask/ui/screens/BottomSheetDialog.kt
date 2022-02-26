package uz.akmal.furortask.ui.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.akmal.furortask.R
import uz.akmal.furortask.databinding.DialogBottomBinding
import uz.akmal.furortask.databinding.ItemDialogBinding
import uz.akmal.furortask.model.data.response.GetItemResponse
import uz.akmal.furortask.util.CurrencyEvent
import uz.akmal.furortask.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BottomSheetDialog : BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogBottomBinding::bind)
    private val navArgs: BottomSheetDialogArgs by navArgs()
    private val viewModel: MainViewModel by viewModels()
    var item:GetItemResponse?=null
    var newItem:GetItemResponse?=null
    private var itemClickListener: ((GetItemResponse) -> Unit)? = null
    fun itemClickListener(block: (GetItemResponse) -> Unit) {
        itemClickListener = block
    }
    private var itemEdit: ((GetItemResponse, GetItemResponse) -> Unit)? = null
    fun itemEdit(block: (GetItemResponse, GetItemResponse) -> Unit) {
        itemEdit = block
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObServer()
        binding.c1.setOnClickListener {
            openDialog(item!!)
        }
        binding.c2.setOnClickListener {
            viewModel.deleteItem(item!!.id)
        }
    }

    private fun setUpObServer() {
        viewModel.deleteItem.observe(this) {
            if (it != null) {
                when (it) {
                    is CurrencyEvent.Failure -> {
                        Snackbar.make(binding.root, it.errorText, Snackbar.LENGTH_SHORT).show()
                    }
                    is CurrencyEvent.Loading -> {
                    }
                    is CurrencyEvent.Success<*> -> {
                        itemClickListener?.invoke(item!!)
                        viewModel.deleteItemRoom(item!!)
                        Toast.makeText(context, "${item!!.id} deleted", Toast.LENGTH_SHORT).show()
                        this.dismiss()
                    }
                    else -> {
                    }
                }
                viewModel.navigateDelete()
            }
        }
        viewModel.updateItem.observe(this) {
            if (it != null) {
                when (it) {
                    is CurrencyEvent.Failure -> {
                        Snackbar.make(binding.root, it.errorText, Snackbar.LENGTH_SHORT).show()
                    }
                    is CurrencyEvent.Loading -> {
                    }
                    is CurrencyEvent.Success<*> -> {
                        this.dismiss()
                        itemEdit?.invoke(item!!,newItem!!)
                        viewModel.updateItemRoom(newItem!!)
                        Toast.makeText(context, "${item!!.id} edited", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
                viewModel.navigateUpdate()
            }
        }
    }

    private fun openDialog(item:GetItemResponse) {
        var named = ""
        var addressed = ""
        var costed = 0F

        val binding = ItemDialogBinding.inflate(LayoutInflater.from(context), null, false)
        binding.inputAddress.setText(item.address)
        binding.inputName.setText(item.name_uz)
        binding.inputCost.setText(item.cost.toString())
        val alertDialog = Dialog(requireContext())
        alertDialog.apply {
            setTitle("Fill the form")
            setContentView(binding.root)
            setCancelable(false)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
                    newItem= GetItemResponse(id,addressed,costed.toDouble(), date.time,named,1)
                    viewModel.updateItem(addressed, costed.toInt(), dateInString, id, named, 1)

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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.navigateDelete()
    }
}