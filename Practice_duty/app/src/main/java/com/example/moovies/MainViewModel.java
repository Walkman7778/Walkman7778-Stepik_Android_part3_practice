package com.example.moovies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {


    private final MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);

    private static final String TAG = "MainViewModel";

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    private int page = 1;

    public MainViewModel(@NonNull Application application) {
        super(application);
        loadMovies();
    }


    public LiveData<List<Movie>> getMovies() {
        return movies;
    }


    public LiveData<Boolean> getIsloading() {
        return isloading;
    }

    public void loadMovies() {
        Boolean loading = isloading.getValue();
        if (loading != null && loading) {
            return;
        }
        Disposable disposable = ApiFactory.apiService.loadMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isloading.setValue(true);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isloading.setValue(false);
                    }
                })
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Throwable {
                        List<Movie> loadedMovies = movies.getValue();


                        if (loadedMovies != null) {
                            loadedMovies.addAll(movieResponse.getMovies());
                        } else {
                            movies.setValue(movieResponse.getMovies());
                        }

                        Log.d(TAG, String.valueOf(page));
                        page++;
                    }
                });
        compositeDisposable.add(disposable);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
