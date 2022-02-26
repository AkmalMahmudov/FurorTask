package uz.akmal.furortask.ui.screens

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.akmal.furortask.R
import uz.akmal.furortask.databinding.DialogBottomBinding
import uz.akmal.furortask.databinding.ItemDialogBinding
import uz.akmal.furortask.util.CurrencyEvent
import uz.akmal.furortask.viewModel.MainViewModel
import java.util.*

@AndroidEntryPoint
class BottomSheetDialog : BottomSheetDialogFragment() {
    private val binding by viewBinding(DialogBottomBinding::bind)
    private val navArgs: BottomSheetDialogArgs by navArgs()
    private val viewModel: MainViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObServer()
        binding.c1.setOnClickListener {
            openDialog(navArgs.itemNumber)
        }
        binding.c2.setOnClickListener {
            viewModel.deleteItem(navArgs.itemNumber)
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
                        this.dismiss()
                        Toast.makeText(context, "${navArgs.itemNumber} deleted", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
                viewModel.navigateDelete()
            }
        }
        viewModel.updateItem.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is CurrencyEvent.Failure -> {
                        Snackbar.make(binding.root, it.errorText, Snackbar.LENGTH_SHORT).show()
                    }
                    is CurrencyEvent.Loading -> {
                    }
                    is CurrencyEvent.Success<*> -> {
                        this.dismiss()
                        Toast.makeText(context, "${navArgs.itemNumber} edited", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
                viewModel.navigateUpdate()
            }
        }
    }

    private fun openDialog(id: Int) {
        var named = ""
        var addressed = ""
        var costed = 0F

        val binding = ItemDialogBinding.inflate(LayoutInflater.from(context), null, false)
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
                    val date = Calendar.getInstance().time
                    viewModel.updateItem(addressed, costed.toInt(), date.toString(), id, named, 1)
                    alertDialog.dismiss()
                }
            }
            cancel.setOnClickListener {
                alertDialog.cancel()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.navigateDelete()
    }
}