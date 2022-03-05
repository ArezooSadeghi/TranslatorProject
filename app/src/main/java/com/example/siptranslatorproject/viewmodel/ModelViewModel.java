package com.example.siptranslatorproject.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.siptranslatorproject.model.Model;

public class ModelViewModel extends ViewModel {
    private SingleLiveEvent<Model> addManualClicked = new SingleLiveEvent<>();
    private SingleLiveEvent<Model> addViaSpeechRecognizerClicked = new SingleLiveEvent<>();
    private SingleLiveEvent<Model> addViaTranslateClicked = new SingleLiveEvent<>();
    private SingleLiveEvent<Model> saveClicked = new SingleLiveEvent<>();
    private SingleLiveEvent<Model> editClicked = new SingleLiveEvent<>();
    private SingleLiveEvent<Model> deleteClicked = new SingleLiveEvent<>();

    public SingleLiveEvent<Model> getAddManualClicked() {
        return addManualClicked;
    }

    public SingleLiveEvent<Model> getAddViaSpeechRecognizerClicked() {
        return addViaSpeechRecognizerClicked;
    }

    public SingleLiveEvent<Model> getAddViaTranslateClicked() {
        return addViaTranslateClicked;
    }

    public SingleLiveEvent<Model> getSaveClicked() {
        return saveClicked;
    }

    public SingleLiveEvent<Model> getEditClicked() {
        return editClicked;
    }

    public SingleLiveEvent<Model> getDeleteClicked() {
        return deleteClicked;
    }
}
