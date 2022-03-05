package com.example.siptranslatorproject.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.siptranslatorproject.R;
import com.example.siptranslatorproject.adapter.ModelAdapter;
import com.example.siptranslatorproject.databinding.FragmentModelBinding;
import com.example.siptranslatorproject.model.Model;
import com.example.siptranslatorproject.ui.dialog.AddManualDialogFragment;
import com.example.siptranslatorproject.ui.dialog.AddViaSpeechRecognizerDialogFragment;
import com.example.siptranslatorproject.ui.dialog.AddViaTranslateDialogFragment;
import com.example.siptranslatorproject.viewmodel.ModelViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelFragment extends Fragment {
    private FragmentModelBinding binding;
    private ModelViewModel viewModel;
    private List<Model> modelList;

    public static ModelFragment newInstance() {
        ModelFragment fragment = new ModelFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewModel();
        initVariables();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_model,
                container,
                false);

        initViews();
        setupAdapter();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObserver();
    }

    private void createViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(ModelViewModel.class);
    }

    private void initVariables() {
        modelList = new ArrayList() {{
            add(new Model("آرزو صادقی", ""));
            add(new Model("نگار امامیان", ""));
            add(new Model("غزاله شفیعی", ""));
            add(new Model("فائزه نور محمد زاده", ""));
            add(new Model("زهرا کمرئی", ""));
            add(new Model("ویکا غلامی", ""));
            add(new Model("فریده فرهادی", ""));
            add(new Model("نگار امین قیداری", ""));
            add(new Model("فرنوش میران زاده", ""));
            add(new Model("مهسا رحیم زاده", ""));
            add(new Model("کیمیا بزرگمهر", ""));
            add(new Model("رها آذری", ""));
            add(new Model("افسانه مردانه", ""));
            add(new Model("شیرین حیدری", ""));
            add(new Model("احسان صادقی", ""));
            add(new Model("ابراهیم صادقی", ""));
            add(new Model("دانیال عباسی", ""));
            add(new Model("محمد رضا محمدی", ""));
            add(new Model("اصغر فرهادی", ""));
            add(new Model("نیوشا افخمی", ""));
            add(new Model("علی پور ولی", ""));
            add(new Model("عطیه احمدی", ""));
            add(new Model("کمال صادق آبادی", ""));
            add(new Model("مهدی کوچالی", ""));
        }};
    }

    private void initViews() {
        binding.recyclerViewModel.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.custom_divider_recycler_view)));
        binding.recyclerViewModel.addItemDecoration(dividerItemDecoration);
    }

    private void setupAdapter() {
        ModelAdapter adapter = new ModelAdapter(viewModel, modelList);
        binding.recyclerViewModel.setAdapter(adapter);
    }

    private void setupObserver() {
        viewModel.getAddManualClicked().observe(getViewLifecycleOwner(), model -> {
            AddManualDialogFragment fragment = AddManualDialogFragment.newInstance(model);
            fragment.show(getParentFragmentManager(), AddManualDialogFragment.TAG);
        });

        viewModel.getAddViaSpeechRecognizerClicked().observe(getViewLifecycleOwner(), model -> {
            AddViaSpeechRecognizerDialogFragment fragment = AddViaSpeechRecognizerDialogFragment.newInstance(model);
            fragment.show(getParentFragmentManager(), AddViaSpeechRecognizerDialogFragment.TAG);
        });

        viewModel.getAddViaTranslateClicked().observe(getViewLifecycleOwner(), model -> {
            AddViaTranslateDialogFragment fragment = AddViaTranslateDialogFragment.newInstance(model);
            fragment.show(getParentFragmentManager(), AddViaTranslateDialogFragment.TAG);
        });

        viewModel.getSaveClicked().observe(getViewLifecycleOwner(), person -> {
            for (Model p : this.modelList) {
                if (p.getModelID().equals(person.getModelID())) {
                    p.setFinglishName(person.getFinglishName());
                }
            }
            setupAdapter();
        });

        viewModel.getDeleteClicked().observe(getViewLifecycleOwner(), model -> {
            for (Model m : modelList) {
                if (m.getModelID().equals(model.getModelID())) {
                    m.setFinglishName("");
                    break;
                }
            }
            setupAdapter();
        });

        viewModel.getEditClicked().observe(getViewLifecycleOwner(), model -> {
            AddManualDialogFragment fragment = AddManualDialogFragment.newInstance(model);
            fragment.show(getParentFragmentManager(), AddManualDialogFragment.TAG);
        });
    }
}