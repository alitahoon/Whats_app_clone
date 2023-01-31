package com.example.whatsappclone.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.assitents.loading_dialog;
import com.example.whatsappclone.databinding.FragmentCodeVerficationBinding;
import com.example.whatsappclone.models.userProfileData;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link codeVerfication#newInstance} factory method to
 * create an instance of this fragment.
 */
public class codeVerfication extends Fragment {
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    String userPhone;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    StorageReference storage=firebaseStorage.getReference();
    Uri selectedImage=null;
    private final int RequstCodeForStudio =1;
    loading_dialog pd;
    FragmentCodeVerficationBinding binding;
    FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken token;
    private onVerficationCompleteListener listener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  static String userName;
    private static String dateSignIn;

    public codeVerfication() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment codeVerfication.
     */
    // TODO: Rename and change types and number of parameters
    public static codeVerfication newInstance(String param1, String param2) {
        codeVerfication fragment = new codeVerfication();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onVerficationCompleteListener)
            listener=(onVerficationCompleteListener) context;
        else
            throw new RuntimeException("Please implement onVerficationCompleteListener !");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        mAuth.setLanguageCode("ar");
        // Force reCAPTCHA flow
        mAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentCodeVerficationBinding.inflate(inflater,container,false);
        pd=new loading_dialog(getActivity(),"codeVerfication");
        pd.setCanceledOnTouchOutside(true);
        //check if the user is signUp
        if (checkSignUp(mAuth)){
            listener.onVerficationSuccess(true);
        }
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImage!= null && !binding.txtChooseProfileName.getText().toString().isEmpty()){
//                    Map<String,Object> userProfileData=new HashMap<>();
//                    userProfileData.put("userProfileData",new userProfileData(binding.txtChooseProfileName.getText().toString(),
//                            getCurrantTimeAndDate(),getImageExtention(selectedImage),binding.txtPhoneNumber.getText().toString().trim()));
//                    firebaseFirestore.collection("usersData").document(userPhone).set(userProfileData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Snackbar.make(getActivity(),getView(),"User Added Succefully",Snackbar.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    DatabaseReference reference=firebaseDatabase.getReference();
                    reference.child("UsersData").push().setValue(new userProfileData(binding.txtChooseProfileName.getText().toString(),
                            getCurrantTimeAndDate(),getImageExtention(selectedImage),binding.txtPhoneNumber.getText().toString().trim())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make(getActivity(),getView(),"User Added Succefully",Snackbar.LENGTH_SHORT).show();
                            try {
                                uploadImagesToStorage(selectedImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            listener.onVerficationSuccess(true);
                        }
                    });
                }

            }
        });
        //get login data
        binding.btnChooseProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequstCodeForStudio);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RequstCodeForStudio);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userPhone=binding.txtPhoneNumber.getText().toString().trim();
                //validate the phone number
                if (binding.txtPhoneNumber.getText().toString().trim().isEmpty() || binding.txtPhoneNumber.getText().toString().trim().length() != 11){
                    Snackbar.make(getActivity(),getView(),"Please enter correct phone number !",
                            Snackbar.LENGTH_SHORT).show();
                }else {
                    pd.showDialog("Wait...");
                    sendVerificationCode(mAuth, binding.txtPhoneNumber.getText().toString().trim()
                            , new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    signInWithPhoneAuthCredentia(phoneAuthCredential);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    pd.hideDialog();
                                    // This callback is invoked in an invalid request for verification is made,
                                    // for instance if the the phone number format is not valid.
                                    Log.w( "onVerificationFailed", e);
                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        // Invalid request
                                        Toast.makeText(getActivity(), "Invalid request", Toast.LENGTH_SHORT).show();
                                    } else if (e instanceof FirebaseTooManyRequestsException) {
                                        // The SMS quota for the project has been exceeded
                                        Toast.makeText(getActivity(), "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
                                    }
                                    // Show a message and update the UI
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);
                                    mVerificationId=s;
                                    token=forceResendingToken;
                                    Toast.makeText(getActivity(), "Code Sent", Toast.LENGTH_SHORT).show();
                                    //start dialog
                                    binding.lnGetPhone.setVisibility(View.GONE);
                                    binding.lnGetCode.setVisibility(View.VISIBLE);
                                    pd.hideDialog();
                                }
                            });
                }

            }
        });
        binding.btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.showDialog("Sending code Please Wait...");
                resendCodeVerification(token, mAuth, binding.txtPhoneNumber.getText().toString().trim()
                        , new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredentia(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                pd.hideDialog();
                                // This callback is invoked in an invalid request for verification is made,
                                // for instance if the the phone number format is not valid.
                                Log.w( "onVerificationFailed", e);
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    // Invalid request
                                    Toast.makeText(getActivity(), "Invalid request", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    // The SMS quota for the project has been exceeded
                                    Toast.makeText(getActivity(), "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT).show();
                                }
                                // Show a message and update the UI
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                mVerificationId=s;
                                token=forceResendingToken;

                                //start dialog
                                pd.hideDialog();
                            }
                        });
            }
        });
        binding.btnSubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.showDialog("Wait...");
                submitCodeVerification(binding.txtCode.getText().toString().trim());
            }
        });
        return binding.getRoot();
    }
    public void sendVerificationCode(FirebaseAuth mAuth, String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+20"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    public void submitCodeVerification(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredentia(credential);
    }


    private void signInWithPhoneAuthCredentia(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signInWithCredential:", "success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            pd.hideDialog();
                            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                            binding.lnGetCode.setVisibility(View.GONE);
                            binding.lotVerificationSuccess.setVisibility(View.VISIBLE);
                            binding.lotVerificationSuccess.playAnimation();
                            binding.lotVerificationSuccess.addAnimatorListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    //start main activity
                                    binding.lnGetsignData.setVisibility(View.VISIBLE);
                                    binding.lotVerificationSuccess.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("signInWithCredential:", "failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getActivity(),"The verification code entered was invalid",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void resendCodeVerification(PhoneAuthProvider.ForceResendingToken token, FirebaseAuth firebaseAuth, String phone, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        pd.showDialog("Resending Code Please Wait");
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+20"+phone)
                        .setTimeout(60L,TimeUnit.SECONDS)
                        .setActivity(getActivity())
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public interface onVerficationCompleteListener{
        void onVerficationSuccess(Boolean result);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequstCodeForStudio:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RequstCodeForStudio);
                } else {
                    Snackbar.make(getActivity(),getView(),"Please allow access to studio",
                            Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && data!=null &&requestCode == RequstCodeForStudio ){
             selectedImage=data.getData();
             binding.btnChooseProfileImage.setImageURI(data.getData());
        }
    }
    public String getCurrantTimeAndDate(){
        SimpleDateFormat  formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        return formatter.format(new Date(System.currentTimeMillis()));
    }
    public String getImageExtention(Uri uri){
        ContentResolver cR= getActivity().getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType((cR.getType(uri)));
    }
    public void uploadImagesToStorage(Uri imageUri) throws IOException {
        //upload user profile image to firebase storge
        FirebaseStorage storage;
        // Create a Cloud Storage reference from the app
        StorageReference storageRef ;
        storage=FirebaseStorage.getInstance();
        storageRef=storage.getReference();
        StorageReference riverRef = storageRef.child("usersImages/"+userPhone+"."+getImageExtention(selectedImage));
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask2 = riverRef.putBytes(data);
        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
            }
        });
//        riverRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(getActivity(), "data saved successfully", Toast.LENGTH_SHORT).show();
//                pd.hideDialog();
//                listener.onVerficationSuccess(true);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("firebase","problem on saving profile image + "+e.getMessage());
//            }
//        });
    }
    public boolean checkSignUp(FirebaseAuth auth){
        if (auth.getCurrentUser()!=null)
            return true;
        else
            return false;
    }
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    }
