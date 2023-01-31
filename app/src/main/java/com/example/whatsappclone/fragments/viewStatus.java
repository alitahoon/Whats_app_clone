package com.example.whatsappclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.FragmentViewStatusBinding;
import com.example.whatsappclone.models.txtStoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link viewStatus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class viewStatus extends Fragment {
    FragmentViewStatusBinding binding;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference();
    ArrayList<txtStoryModel> txtStoryModelArrayList=new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ٍStatusPublisherPhone= "param1";

    // TODO: Rename and change types of parameters
    private String mStatusPublisherPhone;
    private String mParam2;

    public viewStatus() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param statusPublisherPhone Parameter 1.
     * @return A new instance of fragment viewStatus.
     */
    // TODO: Rename and change types and number of parameters
    public static viewStatus newInstance(String statusPublisherPhone) {
        viewStatus fragment = new viewStatus();
        Bundle args = new Bundle();
        args.putString(ARG_ٍStatusPublisherPhone, statusPublisherPhone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStatusPublisherPhone = getArguments().getString(ARG_ٍStatusPublisherPhone);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentViewStatusBinding.inflate(inflater,container,false);
        //get status data from firebase
        reference.child("textStory").orderByChild("publisherPhone").equalTo(mStatusPublisherPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    if (snapshot1.getValue(txtStoryModel.class).getPublisherPhone().equals(mStatusPublisherPhone)){
                        txtStoryModelArrayList.add(snapshot1.getValue(txtStoryModel.class));
                    }
                }
                    txtStoryModel model=snapshot.getValue(txtStoryModel.class);
                    binding.viewStatusTxtStatusPublishDate.setText(model.getTime());
                    binding.viewStatusTxtProfileName.setText(model.getPublisherPhone());
                    binding.viewStatusstatustext.setText(model.getText());
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }
}