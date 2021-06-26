package com.example.trail.view.record;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import com.example.trail.BR;
import com.example.trail.R;
import com.example.trail.base.BaseActivity;
import com.example.trail.database.AppPreferencesHelper;
import com.example.trail.databinding.ActivityRecordBinding;
import com.example.trail.model.pin.PinDTO;
import com.example.trail.network.helper.NetworkHelper;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.example.trail.constants.AppConstants.EXTRA_PIN_DTO;

@AndroidEntryPoint
public class RecordActivity extends BaseActivity<ActivityRecordBinding, RecordViewModel> {

    public static final String TAG = "RecordActivity";

    RecordViewModel viewModel;

    @Inject
    AppPreferencesHelper appPreferencesHelper;
    @Inject
    NetworkHelper networkHelper;

    private ActivityRecordBinding binding;

    private PinDTO pinDTO;

    // 갤러리 사진 가져오는 것을 위해서 / 카메라 사진 촬영 가져오는 것을 위해서
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    public RecordViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this,
                new SavedStateViewModelFactory(this.getApplication(),
                this))
                .get(RecordViewModel.class);
        viewModel.setNetworkHelper(networkHelper);

        binding = getViewDataBinding();
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        pinDTO = (PinDTO) getIntent().getSerializableExtra(EXTRA_PIN_DTO);

        observeViewModel();
    }

    @Override
    public void observeViewModel() {
        viewModel.getSaveClicked().observe(this, state -> {
            if (state) {
                viewModel.getSaveClicked().setValue(false);
                packPinData();
                viewModel.uploadRecord(pinDTO);
            }
        });
    }

    private void packPinData() {
//        pinDTO.setPlaceName(binding.);
//        ...
        // todo ...
    }

    // 갤러리 접속 또는 카메라 키기
    public void getPhoto(){
        //black-jin0427.tistory.com/120 (CODE:11.07)
        //11.29 이거 말고 black-jin0427.tistory.com/121 보삼
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(galleryIntent, PICK_FROM_ALBUM);
    }
//
//
//    private void setImage(boolean makeCopy) throws IOException {
//        // 사진의 절대경로 불러와 bitmap 파일로 변형 --> ImageView에 이미지 넣음
//        Bitmap originalBm = null;          // 변환된 bitmap
//        Bitmap croppedBm;            // 크롭된 bitmap (thumbnail용, 저장용)
//        if(Build.VERSION.SDK_INT >= 29){
//            try {
//                originalBm = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), Uri.fromFile(tempFile))); // 이 파일 내용을 bitmap으로 decode
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//        } else {
//            try {
//                originalBm = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(tempFile));
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//        if(originalBm != null){
//            //croppedBm = Bitmap.createScaledBitmap(originalBm, 130, 130, true);
//            btnAddImage.setImageBitmap(originalBm);
//        }
//    private void compressImage() throws IOException {       // 현재 버튼 썸네일을 파일로 저장(원본X)
//        File copyFile = createImageFile();          // 복사할 file
//        FileOutputStream fOut = new FileOutputStream(copyFile);
//        Bitmap temp = ((BitmapDrawable) btnAddImage.getDrawable()).getBitmap();
//        temp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//        fOut.flush();
//        fOut.close();
//        photo = copyFile.getName();
//    }
//
//
//    public void takePhoto() {
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        //사진찍고+crop+rotate(-90도)+reduce size 까지 한 다음에 그거에다가 파일명 주고 저장할거라 원본은 저장X)
//        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
//    }
//
//    private File createImageFile() throws IOException {     // 카메라 추가인 경우
//        // 이미지 파일 이름 (CODE: 포토) 즉 DB의 photo에 들어갈 것: "zekak_(시간)*.jpg")
//        String timeStamp = new SimpleDateFormat("yyMMdd_HHmm").format(new Date());
//        String ImageFileName = "zekak_"+timeStamp;
//
//        // 이미지 저장될 폴더 이름, xml/filepaths참고 (Android/data/com.example.zekak/files/)
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        if(!storageDir.exists()){   // 없으면 만듦
//            storageDir.mkdirs();
//        }
//
//        // 빈 파일 생성
//        File image = File.createTempFile(ImageFileName, ".jpg", storageDir);
//        return image;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Photo from gallery
//        if(requestCode == PICK_FROM_ALBUM) {
//            if(data == null){
//                return;
//            }
//            Uri photoUri = data.getData();      // 갤러리에서 선택한 이미지의 Uri 받아옴
//            // (CODE: 포토) photoUri 형태: content://(도메인 com.example.zekak).fileprovider/photos/zekak_(시간).jpg.
//            Cursor cursor = null;               // cursor를 통해 스키마를 content://에서 file://로 변경할 것임 (사진이 저장된 절대경로 받아오는 과정)
//
//            try {
//                String[] proj = {MediaStore.Images.Media.DATA };
//                assert photoUri != null;
//                cursor = getContentResolver().query(photoUri, proj, null, null, null);
//
//                assert cursor != null;
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//                cursor.moveToFirst();
//
//                tempFile = new File(cursor.getString(column_index));    // 사진을 임시파일에 저장, 뷰 thumbnail위해서
//            } finally {
//                if(cursor != null) {
//                    cursor.close();
//                }
//            }
//
//            try{
//                setImage(true);         // 갤러리 사진 뷰에 적용, 복사본 생성
//                compressImage();
//            } catch (IOException e){}
//        }
//
//        // Photo from camera
//        if(requestCode == PICK_FROM_CAMERA) {
//            //Uri photoUri = data.getData();
//            if (resultCode == RESULT_OK) {      // 이부분 https://developer.android.com/training/camera/photobasics?hl=ko#java 참고
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                btnAddImage.setImageBitmap(imageBitmap);
//                try {
//                    compressImage();     // just
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
////
//            // 사진이 너무 커서 크롭, 회전, 크기줄이기 다 한담에 그거 우리 어플 파일에 새로 복사 <아래 사용X>
////            try{
////                tempFile = new File(photoUri.getPath().toString());
////                setImage(true);
////            } catch (IOException e){}
//        }
//
//        // ERROR 처리
//        if (resultCode != Activity.RESULT_OK) {
//
//            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
//
//            if(tempFile != null) {
//                if (tempFile.exists()) {
//                    if (tempFile.delete()) {
//                        Log.e("zekak", tempFile.getAbsolutePath() + " 삭제 성공");
//                        tempFile = null;
//                    }
//                }
//            }
//        }
//    }
}
