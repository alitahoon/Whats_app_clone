package com.example.whatsappclone.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.activities.MainActivity;
import com.example.whatsappclone.adapters.story_RCV_adapter;
import com.example.whatsappclone.assitents.statusClickListener;
import com.example.whatsappclone.databinding.FragmentStatusBinding;
import com.example.whatsappclone.models.txtStoryModel;
import com.example.whatsappclone.models.userProfileData;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link status#newInstance} factory method to
 * create an instance of this fragment.
 */
public class status extends Fragment {
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference reference=storage.getReference();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FragmentStatusBinding binding;
    ArrayList<txtStoryModel>txtStoryModelArrayList=new ArrayList<>();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    onStatusAddListener listener;
    FirebaseRecyclerOptions options;
    story_RCV_adapter newAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ImageExtention = "ImageExtention";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mImageExtention;
    private String mParam2;

    public status() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageExtention Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment story.
     */
    // TODO: Rename and change types and number of parameters
    public static status newInstance(String imageExtention, String param2) {
        status fragment = new status();
        Bundle args = new Bundle();
        args.putString(ARG_ImageExtention, imageExtention);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onStatusAddListener)
            listener=(onStatusAddListener) context;
        else
            throw new RuntimeException("Please Implement onStatusAddListener");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageExtention = getArguments().getString(ARG_ImageExtention);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        auth=FirebaseAuth.getInstance();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentStatusBinding.inflate(inflater,container,false);
        //get status
        databaseReference.child("textStory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    txtStoryModelArrayList.add(snapshot1.getValue(txtStoryModel.class));
                }
                newAdapter=new story_RCV_adapter(getContext(), txtStoryModelArrayList, new statusClickListener() {
                    @Override
                    public void onClick(String statusID) {
                        listener.getStatusClickedFragment(statusID);
                    }
                });
                binding.statusRcv.setAdapter(newAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //get User Phone using it to get his profile image from database
        databaseReference.child("UsersData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    if (auth.getCurrentUser().getPhoneNumber().equals("+2"+snapshot1.getValue(userProfileData.class).getPhone())){

                        //get profile image from firebase storage
                        reference= storage.getReference().child("usersImages/"+ auth.getCurrentUser().getPhoneNumber().substring(2)+
                                "."+snapshot1.getValue(userProfileData.class).getExention());
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri.toString()).fit().centerCrop()
                                        .placeholder(R.drawable.account_circle_white)
                                        .into(binding.statusProfileImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("Storage Error ",e.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError",error.getMessage());
            }
        });
        binding.relativeLayoutAddStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getStatusAddFragment(true);
            }
        });


        return binding.getRoot();
    }
    public interface onStatusAddListener{
        void getStatusAddFragment(boolean flag);
        void getStatusClickedFragment(String statusID);}
}