package com.example.siptranslatorproject.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.siptranslatorproject.R;
import com.example.siptranslatorproject.databinding.FragmentAddViaSpeechRecognizerDialogBinding;
import com.example.siptranslatorproject.model.Model;
import com.example.siptranslatorproject.viewmodel.ModelViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class AddViaSpeechRecognizerDialogFragment extends DialogFragment {
    private FragmentAddViaSpeechRecognizerDialogBinding binding;
    private ModelViewModel viewModel;
    private Model model;
    private ActivityResultLauncher activityResultLauncherSpeechRecognizer, activityResultLauncherRecordAudioPermission;

    private static final String ARGS_MODEL = "model";
    public static final String TAG = AddViaSpeechRecognizerDialogFragment.class.getSimpleName();

    public static AddViaSpeechRecognizerDialogFragment newInstance(Model model) {
        AddViaSpeechRecognizerDialogFragment fragment = new AddViaSpeechRecognizerDialogFragment();
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
                R.layout.fragment_add_via_speech_recognizer_dialog,
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

    private void initVariables() {
        activityResultLauncherSpeechRecognizer = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    ArrayList<String> results = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    binding.edTxtFinglishName.setText(results.get(0));
                }
            }
        });

        activityResultLauncherRecordAudioPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), (ActivityResultCallback<Boolean>) granted -> {
            if (granted)
                recordAudio();
            else
                handleError("اجازه ضبط صدا داده نشد");
        });
    }

    private void initViews() {
        model = (Model) getArguments().getSerializable(ARGS_MODEL);
        binding.tvPersianName.setText(model.getPersianName());
    }

    private boolean hasRecordAudioPermission() {
        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestRecordAudioPermission() {
        activityResultLauncherRecordAudioPermission.launch(Manifest.permission.RECORD_AUDIO);
    }

    private void recordAudio() {
        Intent starter = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        starter.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        starter.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            activityResultLauncherSpeechRecognizer.launch(starter);
            binding.edTxtFinglishName.setText("");
        } catch (ActivityNotFoundException exception) {
            handleError(exception.getMessage());
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

        binding.fabVoice.setOnClickListener(view -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (hasRecordAudioPermission()) {
                    recordAudio();
                } else {
                    requestRecordAudioPermission();
                }
            } else {
                recordAudio();
            }
        });
    }
}