package com.example.whatsappclone.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.adapters.chatNumbers_RCV_adapter_RealTime;
import com.example.whatsappclone.assitents.loading_dialog;
import com.example.whatsappclone.assitents.onRCVChatClickListener;
import com.example.whatsappclone.databinding.FragmentChatNumbersBinding;
import com.example.whatsappclone.models.userProfileData;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chat_numbers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chat_numbers extends Fragment{
    FragmentChatNumbersBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    chatNumbers_RCV_adapter_RealTime newAdapter;
    ArrayList<userProfileData>userProfileDataArrayList=new ArrayList<>();
    loading_dialog pd;
    onCallChatFragmentLitener litener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TapType = "TapType";

    // TODO: Rename and change types of parameters
    private String mtype;

    public chat_numbers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type Parameter 1.
     * @return A new instance of fragment chatList.
     */
    // TODO: Rename and change types and number of parameters
    public static chat_numbers newInstance(String type) {
        chat_numbers fragment = new chat_numbers();
        Bundle args = new Bundle();
        args.putString(ARG_TapType, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onCallChatFragmentLitener)
            litener=(onCallChatFragmentLitener) context;
        else
            throw  new RuntimeException("Please implement onCallChatFragmentLitener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        newAdapter.stopListening();
        litener=null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mtype = getArguments().getString(ARG_TapType);
        }
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        switch (mtype){
            case "chat":
                FirebaseRecyclerOptions<userProfileData> options =
                        new FirebaseRecyclerOptions.Builder<userProfileData>()
                                .setQuery(reference.child("UsersData"), userProfileData.class)
                                .build();
                newAdapter=new chatNumbers_RCV_adapter_RealTime(getContext(), options, new onRCVChatClickListener() {
                    @Override
                    public void onClick(userProfileData model) {
                        litener.onCall(model);
                    }
                });
                newAdapter.startListening();
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatNumbersBinding.inflate(inflater,container,false);

        switch (mtype){
            case "chat":
                newAdapter.notifyDataSetChanged();
                binding.chatListRCV.setAdapter(newAdapter);
                break;
        }
        return binding.getRoot();
    }
    public interface onCallChatFragmentLitener{void onCall(userProfileData model);}
}