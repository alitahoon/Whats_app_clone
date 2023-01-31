package com.example.whatsappclone.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapters.chatMessages_RCV_adapter_RealTime;
import com.example.whatsappclone.adapters.messageAdapter;
import com.example.whatsappclone.assitents.onRCVChatMessageClickListener;
import com.example.whatsappclone.databinding.FragmentChatBinding;
import com.example.whatsappclone.models.chatMessagesModel;
import com.example.whatsappclone.models.chatModel;
import com.example.whatsappclone.models.messageModel;
import com.example.whatsappclone.models.userProfileData;
import com.example.whatsappclone.models.usersChatsModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.core.Query;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chat extends Fragment {
    FirebaseAuth auth;
    FragmentChatBinding binding;
    String sender;
    ArrayList<messageModel>fchat=new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    com.example.whatsappclone.adapters.messageAdapter newAdapter;
    FirebaseRecyclerOptions<messageModel>options;
    chatMessages_RCV_adapter_RealTime messageAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_reciverModel = "reciverModel";

    // TODO: Rename and change types of parameters
    private userProfileData mReciverModel;

    public chat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param reciverModel Parameter 1.
     * @return A new instance of fragment chat.
     */
    // TODO: Rename and change types and number of parameters
    public static chat newInstance(userProfileData reciverModel ) {
        chat fragment = new chat();
        Bundle args = new Bundle();
        args.putSerializable(ARG_reciverModel, reciverModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        messageAdapter.stopListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReciverModel = (userProfileData) getArguments().getSerializable(ARG_reciverModel);
        }
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        auth=FirebaseAuth.getInstance();
        sender=auth.getCurrentUser().getPhoneNumber();
//        reference.child("chats").addValueEventListener(new ValueEventListener() {
//            List<String,>
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snapshot1:snapshot.getChildren()){
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Failed in loading chats ",error.getMessage());
//            }
//        });

//        Query query=reference.child("chats").
//        options = new FirebaseRecyclerOptions.Builder<messageModel>()
//                .setQuery(reference.child("chats").child(sender).orderByChild("reciever").equalTo("+2"+mReciverModel.getPhone()), messageModel.class)
//                .build();
//        messageAdapter=new chatMessages_RCV_adapter_RealTime(getContext(), options,
//                new onRCVChatMessageClickListener() {
//                    @Override
//                    public void onClick(messageModel model) {
//
//                    }
//                });
//        newAdapter.notifyDataSetChanged();
//        messageAdapter.startListening();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatBinding.inflate(inflater,container,false);
        binding.chatTxtMessage.requestFocus();
        String reciver="+2"+mReciverModel.getPhone();
        reference.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fchat.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    messageModel newModel=snapshot1.getValue(messageModel.class);
                    if (newModel.getSender().equals(sender)
                            &&newModel.getReciever().equals(reciver)||
                            newModel.getSender().equals(reciver)
                                    &&newModel.getReciever().equals(sender)){
                        fchat.add(snapshot1.getValue(messageModel.class));
                    }
                    newAdapter=new messageAdapter(fchat, getActivity(), new onRCVChatMessageClickListener() {
                        @Override
                        public void onClick(messageModel model) {
                        }
                    });
                    binding.chatRCVList.setAdapter(newAdapter);
                    binding.chatRCVList.smoothScrollToPosition(newAdapter.getItemCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.chatTxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.chatTxtMessage.getText().toString().isEmpty()){
                    binding.chatBtnCamera.setVisibility(View.VISIBLE);
                    binding.chatBtnMicSend.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_mic_24));
                }
                else{
                    binding.chatBtnCamera.setVisibility(View.GONE);
                    binding.chatBtnMicSend.setImageDrawable(getActivity().getDrawable(R.drawable.ic_baseline_send_50_white));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.chatBtnMicSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String currentDateTimeString = DateFormat.getDateTimeInstance()
                        .format(new java.util.Date());
                List<String> chatMembers=new ArrayList<>();
                chatMembers.add(sender);
                chatMembers.add("+2"+mReciverModel.getPhone());
                String chatKey = sender+"+2"+mReciverModel.getPhone();
                String chatKeyRev = "+2"+mReciverModel.getPhone()+sender;
                String messageKey = reference.child("chats").child("chatMessages").push().getKey();

                reference.child("chats").push().setValue(new messageModel(
                        binding.chatTxtMessage.getText().toString(),
                        sender,
                        "+2"+mReciverModel.getPhone()
                        ,getCurrantDate()
                )).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.chatTxtMessage.getText().clear();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Faild to send Message ",e.getMessage());
                    }
                });


//                reference.child("usersChats").push().setValue(new usersChatsModel(
//                        sender,chatKey
//                ));
//                reference.child("chats").push().child(sender).push().setValue(new messageModel(
//                       ,
//                        auth.getCurrentUser().getPhoneNumber().toString(),
//                        "+2"+mReciverModel.getPhone(),getCurrantDate()
//                )).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(getActivity(), "Send", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("Message Send Error",e.getMessage());
//                    }
//                });
//                messageAdapter.notifyDataSetChanged();
//                binding.chatRCVList.smoothScrollToPosition(messageAdapter.getItemCount());
//                binding.chatTxtMessage.getText().clear();

            }
        });
        return binding.getRoot();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrantDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String timeNow=dtf.format(now);
        String hours=timeNow.split(" ",2)[1].split(":",3)[0];
        String minutes=timeNow.split(" ",2)[1].split(":",3)[1];
        if (Integer.parseInt(hours)>=12&&Integer.parseInt(hours)<13){
            return hours+":"+minutes+" PM";
        }
        else if(Integer.parseInt(hours)>=13){
            return Integer.parseInt(hours)-12+":"+minutes+" PM";
        }
        else{
            return hours+":"+minutes+"AM";
        }
    }
}