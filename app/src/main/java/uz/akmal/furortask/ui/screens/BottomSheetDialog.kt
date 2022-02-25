package uz.akmal.furortask.ui.screens

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.akmal.furortask.R
import uz.akmal.furortask.databinding.DialogBottomBinding
import uz.akmal.furortask.util.CurrencyEvent
import uz.akmal.furortask.viewModel.MainViewModel

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
        binding.c1.setOnClickListener {
            Toast.makeText(context, "${navArgs.itemNumber} edited", Toast.LENGTH_SHORT).show()
        }
        binding.c2.setOnClickListener {
            viewModel.deleteItem(navArgs.itemNumber)
            Toast.makeText(context, "${navArgs.itemNumber} deleted", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        viewModel.deleteItem.observe(this) {
            when (it) {
                is CurrencyEvent.Failure -> {
                    Snackbar.make(binding.root, it.errorText, Snackbar.LENGTH_SHORT).show()
                }
                is CurrencyEvent.Loading -> {

                }
                is CurrencyEvent.Success<*> -> {
                    dialog.dismiss()
                    Toast.makeText(context, "${it.data}", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                else -> {
                }
            }
        }
    }
}