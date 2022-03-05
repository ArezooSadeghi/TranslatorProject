package com.example.siptranslatorproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siptranslatorproject.R;
import com.example.siptranslatorproject.databinding.ModelAdapterItemBinding;
import com.example.siptranslatorproject.model.Model;
import com.example.siptranslatorproject.viewmodel.ModelViewModel;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.List;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ModelHolder> {
    private Context context;
    private ModelViewModel viewModel;
    private List<Model> modelList;

    public ModelAdapter(ModelViewModel viewModel, List<Model> modelList) {
        this.viewModel = viewModel;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ModelHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.model_adapter_item,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModelHolder holder, int position) {
        Model model = modelList.get(position);
        holder.bind(model);
        holder.binding.ivMore.setOnClickListener(view -> {
            PowerMenu powerMenu = new PowerMenu.Builder(context)
                    .addItem(new PowerMenuItem("حذف"))
                    .addItem(new PowerMenuItem("ویرایش"))
                    .addItem(new PowerMenuItem("افزودن نام فینگلیش دستی"))
                    .addItem(new PowerMenuItem("افزودن نام فینگلیش با voice"))
                    .addItem(new PowerMenuItem("افزودن نام فینگلیش با translate"))
                    .setDivider(context.getResources().getDrawable(R.drawable.custom_menu_divider))
                    .setWidth(400)
                    .build();

            powerMenu.setOnMenuItemClickListener((i, item) -> {
                if (i == 0) {
                    viewModel.getDeleteClicked().setValue(model);
                } else if (i == 1) {
                    viewModel.getEditClicked().setValue(model);
                } else if (i == 2) {
                    viewModel.getAddManualClicked().setValue(model);
                } else if (i == 3) {
                    viewModel.getAddViaSpeechRecognizerClicked().setValue(model);
                } else if (i == 4) {
                    viewModel.getAddViaTranslateClicked().setValue(model);
                }
                powerMenu.dismiss();
            });
            powerMenu.showAsDropDown(view);
        });
    }

    @Override
    public int getItemCount() {
        return modelList != null ? modelList.size() : 0;
    }

    public class ModelHolder extends RecyclerView.ViewHolder {
        private ModelAdapterItemBinding binding;

        public ModelHolder(ModelAdapterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Model model) {
            binding.tvPersianName.setText(model.getPersianName());

            if (!model.getFinglishName().isEmpty()) {
                binding.tvFinglishName.setVisibility(View.VISIBLE);
                binding.tvFinglishName.setText(model.getFinglishName());
            } else {
                binding.tvFinglishName.setVisibility(View.GONE);
            }
        }
    }
}
