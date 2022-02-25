package uz.akmal.furortask.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.akmal.furortask.R
import uz.akmal.furortask.databinding.DialogBottomBinding
import uz.akmal.furortask.viewModel.MainViewModel

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
                Toast.makeText(context, "${navArgs.itemNumber} deleted", Toast.LENGTH_SHORT).show()
        }
    }
}