package com.example.trail.view.profile;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.databinding.ActivityProfileBinding;
import com.example.trail.databinding.ActivityWalkthroughBinding;
import com.example.trail.network.helper.NetworkHelper;
import com.example.trail.utils.ImageFileUtils;
import com.example.trail.view.login.LoginViewModel;
import com.example.trail.view.walkthrough.WalkthroughViewModel;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.example.trail.constants.AppConstants.PICK_FROM_CAMERA;
import static com.example.trail.constants.AppConstants.PICK_FROM_GALLERY;

@AndroidEntryPoint
public class ProfileActivity extends BaseActivity<ActivityProfileBinding, ProfileViewModel> {

    public static final String TAG = "WalkthroughActivity";

    ProfileViewModel viewModel;

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    private ActivityProfileBinding binding;

    public static File imagePath;


    ImageFileUtils imageFileUtils;

    private ActivityResultLauncher<Intent> cameraPhotoResultLauncher;
    private ActivityResultLauncher<Intent> galleryPhotoResultLauncher;


    // File of user's profile image, uploaded to server
    private File profileImageFile;



    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    public ProfileViewModel getViewModel() {
        return viewModel;
    }


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(this.getApplication(), this)).get(ProfileViewModel.class);
        viewModel.setNetworkHelper(networkHelper);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        // fixme temp
        getPermission(this);



        /** 중요 */ // make sure to call this before the activity is displayed,
        // and also before it starts to observe any live data - 06.26
        initActivityResultLaunchers();

        observeViewModel();

        //CODE: 포토
        //  file:/data/user/
        imagePath = getExternalFilesDir("Images");  // 이미지 저장된 파일 공간
        if (!imagePath.exists()) {   // 없으면 만듦
            imagePath.mkdirs();
        }
    }


    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }


    @Override
    public void observeViewModel() {
        viewModel.getProfilePhotoClicked().observe(this, state -> {
            if (state) {
                viewModel.getProfilePhotoClicked().setValue(false);
                // Open dialog and get a Intent to start (gallery | camera)
                imageFileUtils = new ImageFileUtils(this, true);  // todo if error, erase
                imageFileUtils.getDialogEventLiveData().observe(this, action -> {
                    // deprecated
                    //  startActivityForResult(action, action.getIntExtra("requestCode", -1));
                    switch (action.getIntExtra("requestCode", 0)) {
                        case PICK_FROM_GALLERY:
                            galleryPhotoResultLauncher.launch(action);  // new way for startActivityForResult()
                            break;
                        case PICK_FROM_CAMERA:
                            cameraPhotoResultLauncher.launch(action);  // new way for startActivityForResult()
                            break;
                        default:        // pressed 'CANCEL' btn in dialog
                            break;
                    }
                });
            }
        });

        viewModel.getServerResponseLiveData().observe(this, response -> {
            Log.i(TAG, response.toString());
        });
    }

    private void initActivityResultLaunchers() {
        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        /** Photo from camera */
        cameraPhotoResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() == null) {
                            Log.i(TAG, "<Empty> No photo was taken from camera");
                            return;
                        } else {
                            Bundle extras = result.getData().getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            binding.profilePhoto.setImageBitmap(imageBitmap);
                            binding.profilePhoto.setClipToOutline(true);
                            binding.profilePhoto.setAdjustViewBounds(true);

                            profileImageFile = ImageFileUtils.bitmapToFile(getBaseContext(), imageBitmap, "profile_");

                            try {
                                setImage(profileImageFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        /** Photo from gallery */
        galleryPhotoResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() == null) {
                            Log.i(TAG, "<Empty> No photo selected from gallery");
                            return;
                        } else {
                            Uri photoUri = result.getData().getData();

                            // photoUri 형태: content://(도메인 com.example.trail).fileprovider/photos/(이미지 타입)_(시간).png
                            Cursor cursor = null;               // cursor를 통해 스키마를 content://에서 file://로 변경할 것임 (사진이 저장된 절대경로 받아오는 과정)

                            try {
                                String[] proj = {MediaStore.Images.Media.DATA};
                                assert photoUri != null;
                                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                                assert cursor != null;
                                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                                cursor.moveToFirst();

                                profileImageFile = new File(cursor.getString(column_index));
                                try {
                                    setImage(profileImageFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } finally {
                                if (cursor != null) {
                                    cursor.close();
                                }
                            }
                        }
                    }
                }
        );
    }

    private void setImage(File file) throws IOException {
        // 사진의 절대경로 불러와 bitmap 파일로 변형 --> ImageView에 이미지 넣음
        Bitmap originalBm = null;          // 변환된 bitmap
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                originalBm = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file))); // 이 파일 내용을 bitmap으로 decode
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                originalBm = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (originalBm != null) {
            //croppedBm = Bitmap.createScaledBitmap(originalBm, 130, 130, true);
            binding.profilePhoto.setImageBitmap(originalBm);
            binding.profilePhoto.setClipToOutline(true);

            binding.profilePhoto.setAdjustViewBounds(true);

            uploadProfileImage();
        }
    }

    private void uploadProfileImage () throws IOException {
        viewModel.uploadProfileImage(profileImageFile, appPreferencesHelper.getUserID());
    }
}
