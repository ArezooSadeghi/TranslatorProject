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
import com.example.siptranslatorproject.databinding.FragmentAddViaTranslateDialogBinding;
import com.example.siptranslatorproject.model.Model;
import com.example.siptranslatorproject.viewmodel.ModelViewModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class AddViaTranslateDialogFragment extends DialogFragment {
    private FragmentAddViaTranslateDialogBinding binding;
    private ModelViewModel viewModel;
    private Model model;
    private String finglishName;
    private FirebaseTranslator persianEnglishTranslator;

    private static final String ARGS_MODEL = "model";
    public static final String TAG = AddViaTranslateDialogFragment.class.getSimpleName();

    public static AddViaTranslateDialogFragment newInstance(Model model) {
        AddViaTranslateDialogFragment fragment = new AddViaTranslateDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_MODEL, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewModel();
        initVariables();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.fragment_add_via_translate_dialog,
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

    private void initVariables() {
        finglishName = "";

        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions
                .Builder()
                .setSourceLanguage(FirebaseTranslateLanguage.FA)
                .setTargetLanguage(FirebaseTranslateLanguage.EN)
                .build();

        persianEnglishTranslator = FirebaseNaturalLanguage
                .getInstance()
                .getTranslator(options);
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(ModelViewModel.class);
    }

    private void initViews() {
        model = (Model) getArguments().getSerializable(ARGS_MODEL);
        binding.tvPersianName.setText(model.getPersianName());
    }

    private void downloadModel() {
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions
                .Builder()
                .build();

        persianEnglishTranslator
                .downloadModelIfNeeded(conditions)
                .addOnSuccessListener(isSuccess -> translate(binding.tvPersianName.getText().toString()))
                .addOnFailureListener(exception -> handleError(exception.getMessage()));
    }

    private void translate(String persianName) {
        String[] partsOfPersianName = persianName.split(" ");
        for (int i = 0; i < partsOfPersianName.length; i++) {
            persianEnglishTranslator
                    .translate("خانم ".concat(partsOfPersianName[i]))
                    .addOnSuccessListener(finglishName -> {
                        finglishName = finglishName.replaceAll("Ms. ", "").replaceAll("Mrs. ", "").replaceAll("Miss. ", "").replaceAll("Ms ", "").replaceAll("Mrs ", "").replaceAll("Miss ", "");
                        this.finglishName = this.finglishName.concat(finglishName).concat(" ");
                        binding.edTxtFinglishName.setText(this.finglishName);
                    })
                    .addOnFailureListener(exception -> handleError(exception.getMessage()));
        }
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

        binding.fabTranslate.setOnClickListener(view -> downloadModel());
    }
}