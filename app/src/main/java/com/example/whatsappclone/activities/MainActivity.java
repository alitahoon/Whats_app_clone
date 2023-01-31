package com.example.whatsappclone.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.example.whatsappclone.fragments.accountDetails;
import com.example.whatsappclone.fragments.addTextStory;
import com.example.whatsappclone.fragments.chat;
import com.example.whatsappclone.fragments.chat_inbox;
import com.example.whatsappclone.fragments.chat_numbers;
import com.example.whatsappclone.fragments.status;
import com.example.whatsappclone.fragments.viewStatus;
import com.example.whatsappclone.models.userProfileData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements chat_numbers.onCallChatFragmentLitener,
        status.onStatusAddListener, chat_inbox.onTabClickedListener , addTextStory.onStatusAddedLitener {
    ActivityMainBinding binding;

    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference reference=storage.getReference();
    static userProfileData chatHeaderModel=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.chatHeaderProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new accountDetails().newInstance(
                        chatHeaderModel.getName(),
                        chatHeaderModel.getPhone(),
                        chatHeaderModel.getTime(),
                        chatHeaderModel.getExention()
                ));
                binding.appBarLayoutChat.setVisibility(View.GONE);
            }
        });
        binding.chatHeaderBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.fabMain.setVisibility(View.VISIBLE);
                binding.mainAppBar.setVisibility(View.VISIBLE);
                binding.appBarLayoutChat.setVisibility(View.GONE);
                replaceFragment(new chat_inbox());

            }
        });

        try {
            this.getSupportActionBar().hide();
        }
        // catch block to handle NullPointerException
        catch (NullPointerException e) {
        }
        //set theme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.menuColor));
        }

        replaceFragment(new chat_inbox());


    }

    @Override
    public void onCall(userProfileData model) {
        binding.fabMain.setVisibility(View.GONE);
        chatHeaderModel=model;
        binding.mainAppBar.setVisibility(View.GONE);
        binding.appBarLayoutChat.setVisibility(View.VISIBLE);
        setUserChatHeader(model);
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter,R.anim.exit);
        ft.replace(R.id.mainFragmentContainer,new chat().newInstance(chatHeaderModel));
        ft.commit();
    }
    void setUserChatHeader(userProfileData model){
        //get profile image from firebase storage
        binding.chatHeaderProfileName.setText(model.getName());
        binding.chatHeaderProfileImage.setImageDrawable(getDrawable(R.drawable.account_circle_white));
        try {
            reference= storage.getReference().child("usersImages/"+model.getPhone()+"."+model.getExention());
            File f= File.createTempFile("tempFile",model.getExention());
            reference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    binding.chatHeaderProfileImage.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Error ",e.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.mainFragmentContainer, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void getStatusAddFragment(boolean flag) {
        binding.fabMain.setVisibility(View.GONE);
        binding.fabStoryTextWrite.setVisibility(View.GONE);
        binding.mainAppBar.setVisibility(View.GONE);
        replaceFragment(new addTextStory());
    }

    @Override
    public void getStatusClickedFragment(String statusID) {
        binding.fabMain.setVisibility(View.GONE);
        binding.fabStoryTextWrite.setVisibility(View.GONE);
        binding.mainAppBar.setVisibility(View.GONE);
        replaceFragment(new viewStatus().newInstance(statusID));
    }

    @Override
    public void tabClicked(String tabName) {
        switch (tabName){
            case "STATUS":
                binding.fabMain.setVisibility(View.VISIBLE);
                binding.fabStoryTextWrite.setVisibility(View.VISIBLE);
                binding.fabStoryTextWrite.animate().translationY(-160).setDuration(500).start();
                binding.fabMain.setImageDrawable(getDrawable(R.drawable.ic_baseline_photo_camera_24));
                break;
            case "STATUSUnSelected":

                binding.fabStoryTextWrite.animate().translationY(0).setDuration(500).start();
                break;
            case "CHATS":
                binding.fabMain.setVisibility(View.VISIBLE);
                binding.fabMain.setImageDrawable(getDrawable(R.drawable.ic_baseline_message_white_50));
                break;
            case "CALLS":
                binding.fabMain.setVisibility(View.VISIBLE);
                binding.fabMain.setImageDrawable(getDrawable(R.drawable.ic_baseline_add_ic_call_50_white));
                break;
            default:
                binding.fabStoryTextWrite.setVisibility(View.GONE);
                binding.fabMain.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void statusAdded(boolean flag) {
        binding.fabMain.setVisibility(View.VISIBLE);
        replaceFragment(new status());
    }
}