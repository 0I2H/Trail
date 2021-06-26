package com.example.trail.base;

import androidx.databinding.Observable;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.network.retrofit.RetrofitService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

//@HiltViewModel
public abstract class BaseViewModel extends ViewModel {

    /** SavedStateHandle allows ViewModel to access
     * the saved state and arguments
     * of the associated Fragment or Activity. */
    private SavedStateHandle savedStateHandle;

    private NetworkHelper networkHelper;
//    private WeakReference<N> navigator;

    // CompositeDisposable: Observable의 onComplete() 호출되면 데이터의 subscribe 종료 됨
    private CompositeDisposable compositeDisposable;


    public BaseViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        this.compositeDisposable = new CompositeDisposable();       // for Observing Observable in RxJava
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    // Prevent memory leak when View is destroyed and Observing is done
    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public NetworkHelper getNetworkHelper() {
        return networkHelper;
    }

    public void setNetworkHelper(NetworkHelper networkHelper) {
        this.networkHelper = networkHelper;
    }

    public RetrofitService getRetrofitService() {
        return networkHelper.getRetrofitService();
    }
//
//    public N getNavigator() {
//        return navigator.get();
//    }
//
//    public void setNavigator(N navigator) {
//        this.navigator = new WeakReference<>(navigator);
//    }

}
