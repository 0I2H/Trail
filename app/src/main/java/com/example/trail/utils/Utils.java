package com.example.trail.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.provider.Settings;
import android.util.Base64;
import android.util.Patterns;

import androidx.core.app.ActivityCompat;

import com.example.trail.R;
import com.example.trail.constants.ApiConstants;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

import static com.example.trail.constants.AppConstants.PERMISSION_REQUEST_CODE;

public class Utils {

    /**
     * Returns the {@code object} object as a JSON object.
     *
     * @param object The {@link Object}.
     */
    public static String jsonConverter(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    /**
     * Returns the {@code location} object as a human readable string.
     *
     * @param location The {@link Location}.
     */
    public static String locationToText(Location location) {
        return location != null ? "" + '(' + location.getLatitude() + ", " + location.getLongitude() + ')' : "Unknown location";
    }

    public final static String[] requestPermission = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

//    public static void getPermissionTotal(Activity activity) {
//        ActivityCompat.requestPermissions(activity, requestPermission, PERMISSION_REQUEST_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 &&
//                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission is granted. Continue the action or workflow
//                    // in your app.
//                }  else {
//                    // Explain to the user that the feature is unavailable because
//                    // the features requires a permission that the user has denied.
//                    // At the same time, respect the user's decision. Don't link to
//                    // system settings in an effort to convince the user to change
//                    // their decision.
//                }
//                return;
//        }
//        // Other 'case' lines to check for other
//        // permissions this app might request.
//    }


    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.dialog_photo);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }


    public static String createDefaultImageUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(ApiConstants.BASE_URL).append("images/default/no_image.jpg");
        return sb.toString();
    }

    public static final String getLanguage() {
        return Resources.getSystem().getConfiguration().getLocales().get(0).getLanguage();
    }

//        public static String getHashKey(Context context) {
//            String hashKey = null;
//            try {
//                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
//                for (Signature signature : info.signatures) {
//                    MessageDigest md = MessageDigest.getInstance("SHA");
//                    md.update(signature.toByteArray());
//                    hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                    Dlog.e("key_hash=>" + hashKey);
//                }
//            } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//
//            return hashKey;
//        }

//        private static boolean isBirthDtFormatValid(String birthDt) {
//            if(!birthDt.matches("(^[0-9]*$)")) return false;
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
//            dateFormat.setLenient(false);
//            return dateFormat.parse(birthDt, new ParsePosition(0)) != null;
//        }

//        public static boolean isBirthDtValid(String birthDt) {
//            return isBirthDtFormatValid(birthDt) && CommonUtils.isAge14(birthDt);
//        }

//        public static boolean isAge14(String birth) {
//            int birthYear = Integer.parseInt(birth.substring(0, 4));
//            int birthMonth = Integer.parseInt(birth.substring(4, 6));
//            int birthDay = Integer.parseInt(birth.substring(6));
//            Dlog.e(birthYear + "년 " + birthMonth + "월d " + birthDay);
//            Calendar current = Calendar.getInstance();
//            int currentYear = current.get(Calendar.YEAR);
//            int currentMonth = current.get(Calendar.MONTH) + 1;
//            int currentDay = current.get(Calendar.DAY_OF_MONTH);
//            Dlog.e(currentYear + "년 " + currentMonth + "월 " + currentDay);
//            int age = currentYear - birthYear;
//            if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
//                age--;
//            Dlog.e(age + "살");
//            return age >= 14;
//        }

//        public static boolean isNameValid(String nickName) {
//            // 한글/영어/숫자 2~10자
//            if (nickName.length() >= 2 && nickName.length() <= 10) {
//                for (int i = 0; i < nickName.length(); i++) {
//                    if (!Character.isLetterOrDigit(nickName.charAt(i)))
//                        return false;
//                }
//                Dlog.i(nickName);
//                return true;
//            } else
//                return false;
//        }
//
//        public static boolean isPasswordValid(String password) {
//            // 영어/숫자/특수문자 8~10자
//            if (password.length() >= 8 && password.length() <= 16) {
////            if (Pattern.matches("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`]+$", password)){
//                return Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z])[a-zA-Z\\d~`!@#$%\\^&*()\\-]{8,16}$", password);
//            }
//            return false;
//        }
//
//        public static String generateOptionCode(ArrayList<Boolean> optionArray) {
//            StringBuilder optionCode = new StringBuilder();
//            for (int i = 0; i < optionArray.size(); i++) {
//                optionCode.append(optionArray.get(i).equals(Boolean.TRUE) ? 1 : 0);
//            }
//            optionArray.clear();
//            return optionCode.toString();
//        }

//        public static boolean isValidOptionCode(String optionCode) {
//            for (int i = 0; i < optionCode.length(); i++) {
//                if (optionCode.charAt(i) != '0' && optionCode.charAt(i) != '1')
//                    return false;
//            }
//            return true;
//        }
//
//        public static ArrayList<Boolean> generateOptionArray(String optionCode) {
//            ArrayList<Boolean> optionArray = new ArrayList<>();
//            for (int i = 0; i < optionCode.length(); i++) {
//                if (optionCode.charAt(i) == '0')
//                    optionArray.add(false);
//                else if (optionCode.charAt(i) == '1')
//                    optionArray.add(true);
//            }
//            for(int i=optionCode.length();i<10;i++)
//                optionArray.add(false);
//            return optionArray;
//        }
//
//        public static String createDefaultImageUrl() {
//            StringBuilder sb = new StringBuilder();
//            sb.append(ApiConstants.BASE_URL).append("images/default/no_image.jpg");
//            return sb.toString();
//        }
//        public static String createReviewImageUrl(String storeSn, String reviewSn, String fileName) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(ApiConstants.BASE_URL).append("images/review/").append(storeSn).append("/").append(reviewSn).append("/").append(fileName);
//            return sb.toString();
//        }

//        public static String hashWithSHA256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            digest.reset();
//
//            byte[] byteStreamData = digest.digest(input.getBytes(StandardCharsets.UTF_8));
//            StringBuilder sb = new StringBuilder();
//
//            for (byte byteData : byteStreamData) {
//                sb.append(Integer.toString((byteData & 0xff) + 0x100, 16).substring(1));
//            }
//            return sb.toString();
//        }
//
//        public static boolean isNaturalOneLetter(String input) {
//            Pattern PATTERN_KOREAN = Pattern.compile("^[가-힣]*$");
//            Pattern PATTERN_ENGLISH = Pattern.compile("^[a-zA-Z\\s]+$");
//            Pattern PATTERN_JAPANESE = Pattern.compile("/[一-龠]+|[ぁ-ゔ]+|[ァ-ヴー]+|[a-zA-Z0-9]+|[ａ-ｚＡ-Ｚ０-９]+|[々〆〤]+/u");
//            return input != null && (PATTERN_KOREAN.matcher(input).matches() || PATTERN_ENGLISH.matcher(input).matches() || PATTERN_JAPANESE.matcher(input).matches());
//        }


//        public static int getAgeFromBirthday(String strBirth) {
//            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
//            Date birthday = null;
//            boolean parsed=false;
//            try {
//                birthday = transFormat.parse(strBirth);
//                parsed =true;
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            if(parsed){
//                Calendar birth = new GregorianCalendar();
//                Calendar today = new GregorianCalendar();
//                birth.setTime(birthday);
//                today.setTime(new Date());
//                return today.get(Calendar.YEAR) - birth.get(Calendar.YEAR) + 1;
//            }
//            else
//                return 20;
//        }
}
