package com.example.smartcity.Fragments.Actualites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActualitesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ActualitesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}