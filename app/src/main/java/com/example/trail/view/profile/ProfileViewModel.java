package com.example.trail.view.profile;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.trail.base.BaseViewModel;
import com.example.trail.model.message.MessageDTO;
import com.example.trail.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.trail.constants.ApiConstants.BASE_URL;

@HiltViewModel
public class ProfileViewModel extends BaseViewModel {

    private static final String TAG = "ProfileViewModel";

    private MutableLiveData<Boolean> profilePhotoClicked;

    private MutableLiveData<MessageDTO> serverResponseLiveData;

    @Inject
    public ProfileViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        profilePhotoClicked = new MutableLiveData<>(false);
        serverResponseLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getProfilePhotoClicked() {
        return profilePhotoClicked;
    }

    public MutableLiveData<MessageDTO> getServerResponseLiveData() {
        return serverResponseLiveData;
    }

    public void setProfileImage() {
        profilePhotoClicked.setValue(true);
    }

    public void uploadProfileImage (File file, int userId) throws IOException {

        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), imageRequestBody);
//        MultipartBody.Part image = MultipartBody.Part.createFormData("image", "true", imageRequestBody);
//
//        RequestBody body1 = RequestBody.create(MediaType.parse("text/plain"), Utils.jsonConverter(userId));
//
//        try {
//            getCompositeDisposable()
//                    .add(getRetrofitService().uploadProfileImage(image, body)
//                            .subscribeOn(getNetworkHelper().getSchedulerIo())
//                            .observeOn(getNetworkHelper().getSchedulerUi())
//                            .subscribe(serverResponseLiveData::setValue,
//                                    throwable -> Log.e(TAG+"서버", throwable.getMessage())));
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }

//        uploadFile(file);

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), imageRequestBody)
                .addFormDataPart("userId", Utils.jsonConverter(userId))
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/user/profile-upload")
                .method("POST", body)
                .build();

        class WorkerThread extends Thread {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    Log.i(TAG, response.body().string());

                } catch (Exception e) {
                    Log.e(TAG, "");
                }
            }
        }

        WorkerThread w = new WorkerThread();
        w.start();

    }

//    private void uploadFile(File file) {
//        RequestBody imageRequestBody = RequestBody.create(MediaType.parse(""), file);
//        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), imageRequestBody);
//
//
//        // add another part within the multipart request
//        String descriptionString = "hello, this is description speaking";
//        RequestBody description =
//                RequestBody.create(
//                        okhttp3.MultipartBody.FORM, descriptionString);
//
//        // finally, execute the request
//        Call<ResponseBody> call = getRetrofitService().uploadProfileImageTest(image, description);
//        call.enqueue(new Callback<ResponseBody>() {
//
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                Log.v("Upload", "success");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("Upload error:", t.getMessage());
//            }
//        });
//    }
}
