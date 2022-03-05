package com.example.siptranslatorproject.ui.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.siptranslatorproject.R;
import com.example.siptranslatorproject.databinding.FragmentAddManualDialogBinding;
import com.example.siptranslatorproject.model.Model;
import com.example.siptranslatorproject.viewmodel.ModelViewModel;

public class AddManualDialogFragment extends DialogFragment {
    private FragmentAddManualDialogBinding binding;
    private ModelViewModel viewModel;
    private Model model;

    private static final String ARGS_MODEL = "model";
    public static final String TAG = AddManualDialogFragment.class.getSimpleName();

    public static AddManualDialogFragment newInstance(Model model) {
        AddManualDialogFragment fragment = new AddManualDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_MODEL, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewModel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_add_manual_dialog,
                null,
                false);

        initViews();
        handleEvents();

        AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(binding.getRoot())
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        return dialog;
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(ModelViewModel.class);
    }

    private void initViews() {
        model = (Model) getArguments().getSerializable(ARGS_MODEL);
        binding.tvPersianName.setText(model.getPersianName());
        binding.edTxtFinglishName.setText(model.getFinglishName());
    }

    private void handleError(String msg) {
        ErrorDialogFragment fragment = ErrorDialogFragment.newInstance(msg);
        fragment.show(getParentFragmentManager(), ErrorDialogFragment.TAG);
    }

    private void handleEvents() {
        binding.btnClose.setOnClickListener(view -> dismiss());

        binding.btnSave.setOnClickListener(view -> {
            if (binding.edTxtFinglishName.getText().toString().isEmpty()) {
                handleError("لطفا فیلد مربوطه را پر کنید");
            } else {
                String finglishName = binding.edTxtFinglishName.getText().toString();
                model.setFinglishName(finglishName);
                viewModel.getSaveClicked().setValue(model);
                dismiss();
            }
        });
    }
}