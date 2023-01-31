package com.example.whatsappclone.fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.FragmentAccountDetailsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link accountDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class accountDetails extends Fragment {
    FragmentAccountDetailsBinding binding;
    FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference();
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    StorageReference storageReference=firebaseStorage.getReference();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ProfileName = "param1";
    private static final String ARG_ProfilePhone= "param2";
    private static final String ARG_ProfileOnlineStatus = "param3";
    private static final String ARG_ProfileImageExtention = "param4";

    // TODO: Rename and change types of parameters
    private String mProfileName;
    private String mProfilePhone;
    private String mProfileOnlineStatus;
    private String mProfileImageExtention;

    public accountDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ProfileName Parameter 1.
     * @param ProfilePhone Parameter 2.
     * @param ProfileOnlineStatus Parameter 2.
     * @param ProfileImageExtention Parameter 2.
     * @return A new instance of fragment accountDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static accountDetails newInstance(String ProfileName, String ProfilePhone, String ProfileOnlineStatus,String ProfileImageExtention) {
        accountDetails fragment = new accountDetails();
        Bundle args = new Bundle();
        args.putString(ARG_ProfileName, ProfileName);
        args.putString(ARG_ProfilePhone, ProfilePhone);
        args.putString(ARG_ProfileOnlineStatus, ProfileOnlineStatus);
        args.putString(ARG_ProfileImageExtention, ProfileImageExtention);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProfileName = getArguments().getString(ARG_ProfileName);
            mProfilePhone = getArguments().getString(ARG_ProfilePhone);
            mProfileOnlineStatus = getArguments().getString(ARG_ProfileOnlineStatus);
            mProfileImageExtention = getArguments().getString(ARG_ProfileImageExtention);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAccountDetailsBinding.inflate(inflater,container,false);
        binding.accountDetailsProfileName.setText(mProfileName);
        binding.accountDetailsProfileNumber.setText(mProfilePhone);
        binding.accountDetailsProfileOnlineStatus.setText(mProfileOnlineStatus);        //get profile image from firebase storage
        Toast.makeText(getActivity(), "url "+"usersImages/"+mProfilePhone+"."+mProfileImageExtention, Toast.LENGTH_SHORT).show();
        storageReference= firebaseStorage.getReference().child("usersImages/"+mProfilePhone+"."+mProfileImageExtention);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).fit().centerCrop()
                        .placeholder(R.drawable.account_circle_white)
                        .into(binding.accountDetailsProfileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Storage Error ",e.getMessage());
            }
        });

        return binding.getRoot();
    }
}