package com.example.trail.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.PermissionRequest;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.example.trail.BR;
import com.example.trail.BuildConfig;
import com.example.trail.R;
import com.example.trail.databinding.DialogPhotoBinding;
import com.example.trail.view.profile.ProfileActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import static com.example.trail.constants.AppConstants.PICK_FROM_CAMERA;
import static com.example.trail.constants.AppConstants.PICK_FROM_GALLERY;

public class ImageFileUtils {

    public static final String TAG = "ImageFileUtils";

    private DialogPhotoBinding binding;


    final Dialog pickImageDialog;

    private Context activityContext;

    private MutableLiveData<Intent> dialogEventLiveData;


    public ImageFileUtils(Context context) {
        activityContext = context;

        binding = DialogPhotoBinding.inflate(LayoutInflater.from(context));
        binding.setImageUtil(this);
        binding.executePendingBindings();

        dialogEventLiveData = new MutableLiveData<>();

        pickImageDialog = new Dialog(context);
        pickImageDialog.setContentView(binding.getRoot());
        pickImageDialog.show();
    }

    public MutableLiveData<Intent> getDialogEventLiveData() {
        return dialogEventLiveData;
    }

    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("requestCode", PICK_FROM_CAMERA);

        pickImageDialog.dismiss();

        dialogEventLiveData.setValue(cameraIntent);
    }

    public void openGallery() {

        // black-jin0427.tistory.com/121
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        galleryIntent.putExtra("requestCode", PICK_FROM_GALLERY);

        pickImageDialog.dismiss();

        dialogEventLiveData.setValue(galleryIntent);
    }

    public void closeDialog() {
        pickImageDialog.dismiss();
    }

//    private File createEmptyFile() throws IOException {     // 카메라 추가인 경우
//        // 이미지 파일 이름 (CODE: 포토) 즉 DB의 photo에 들어갈 것: "zekak_(시간)*.jpg")
//        String timeStamp = new SimpleDateFormat("yyMMdd_HHmm").format(new Date());
//        String ImageFileName = "trail_" + timeStamp;
//
//        // 이미지 저장될 폴더 이름, xml/filepaths참고 (Android/data/com.example.zekak/files/)
//        File storageDir = activityContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        if (!storageDir.exists()) {   // 없으면 만듦
//            storageDir.mkdirs();
//        }
//
//        // 빈 파일 생성
//        File image = File.createTempFile(ImageFileName, ".jpg", storageDir);
//        return image;
//    }

    public File compressImage(Bitmap bitmap) throws IOException {       // 현재 버튼 썸네일을 파일로 저장(원본X)
        File file = bitmapToFile(activityContext, bitmap, "compressed_");          // 복사할 file
        FileOutputStream fOut = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 1, fOut);
        fOut.flush();
        fOut.close();

        return file;
    }

    /**
     * create a File with the bitmap data
     *
     * @param bitmap
     * @return File
     * @throws IOException
     */
    public static File bitmapToFile(Context context, Bitmap bitmap, String frontFileName) { // File name like "image.png"

        // 이미지 파일 이름 (CODE: 포토) 즉 DB의 photo에 들어갈 것: "zekak_(시간)*.jpg")
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmm").format(new Date());
        String fileName = frontFileName + timeStamp;


        //create a file to write bitmap data
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileName);
            file.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file; // it will return null
        }
    }
}

//    Button btnTakePhoto;
//    ImageView ivPreview;
//
//    String mCurrentPhotoPath;

//    @Override
//    public void onClick(View view) {
//        if (view == btnTakePhoto) {
//            MainActivityPermissionsDispatcher.startCameraWithCheck(this);
//        }
//    }
//
//    ////////////
//    // Camera //
//    ////////////
//
//    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    void startCamera() {
//        try {
//            dispatchTakePictureIntent();
//        } catch (IOException e) {
//        }
//    }
//
//    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    void showRationaleForCamera(final PermissionRequest request) {
//        new AlertDialog.Builder(this)
//                .setMessage("Access to External Storage is required")
//                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        request.proceed();
//                    }
//                })
//                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        request.cancel();
//                    }
//                })
//                .show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            // Show the thumbnail on ImageView
//            Uri imageUri = Uri.parse(mCurrentPhotoPath);
//            File file = new File(imageUri.getPath());
//            try {
//                InputStream ims = new FileInputStream(file);
//                ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
//            } catch (FileNotFoundException e) {
//                return;
//            }
//
//            // ScanFile so it will be appeared on Gallery
//            MediaScannerConnection.scanFile(MainActivity.this,
//                    new String[]{imageUri.getPath()}, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                        }
//                    });
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DCIM), "Camera");
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
//        return image;
//    }
//
//    private void dispatchTakePictureIntent() throws IOException {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                return;
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = Uri.fromFile(createImageFile());
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }
