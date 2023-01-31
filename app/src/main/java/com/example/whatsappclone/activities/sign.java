package com.example.whatsappclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.ActivitySignBinding;
import com.example.whatsappclone.fragments.chat_inbox;
import com.example.whatsappclone.fragments.codeVerfication;

public class sign extends AppCompatActivity implements codeVerfication.onVerficationCompleteListener {
    ActivitySignBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // try block to hide Action bar
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
            window.setStatusBarColor(getColor(R.color.backGroundDarkGreen
            ));
        }
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.setCustomAnimations(R.anim.pop_enter,R.anim.pop_exit);
        ft.replace(R.id.codeVerficationFragmentContainer,new codeVerfication());
        ft.commit();
    }

    @Override
    public void onVerficationSuccess(Boolean result) {
        if (result){
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
        }
    }
}