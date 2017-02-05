package me.andrewtam.rapp.presenter;

import android.util.Log;

import java.io.File;

import javax.inject.Inject;

import me.andrewtam.rapp.RappApplication;
import me.andrewtam.rapp.model.Lyrics;
import me.andrewtam.rapp.model.api.RappClient;
import me.andrewtam.rapp.view.MainView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainPresenter implements Presenter<MainView> {
    @Inject
    RappClient client;
    private MainView view;

    @Override
    public void attachView(MainView view) {
        RappApplication.getPresenterComponent().inject(this);
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void upload(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<Lyrics> call = client.sendRapAudio(body);
        call.enqueue(new Callback<Lyrics>() {
            @Override
            public void onResponse(Call<Lyrics> call, Response<Lyrics> response) {
                if (response.isSuccessful()) {
                    Lyrics lyrics = response.body();
                    for (int i = 0; i < lyrics.lines.length; i++) {
                        Log.d("der", lyrics.lines[i]);
                    }
                }
            }

            @Override
            public void onFailure(Call<Lyrics> call, Throwable t) {
                Log.d("der", "WTF: " + t.getMessage());
            }
        });
    }
}