package com.example.sprite.custommusic.page3;

import android.app.Activity;
import android.os.Bundle;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxJavaTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>
                () {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.isDisposed();
                emitter.onNext("");
                emitter.onComplete();
                emitter.onError(new Throwable());
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
    }
}
