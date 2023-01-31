package com.example.whatsappclone.fragments;

import android.content.Context;
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
import com.example.whatsappclone.databinding.FragmentAddTextStoryBinding;
import com.example.whatsappclone.models.txtStoryModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addTextStory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addTextStory extends Fragment {
    FragmentAddTextStoryBinding binding;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference reference=firebaseDatabase.getReference();
    onStatusAddedLitener litener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addTextStory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addTextStory.
     */
    // TODO: Rename and change types and number of parameters
    public static addTextStory newInstance(String param1, String param2) {
        addTextStory fragment = new addTextStory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof chat_numbers.onCallChatFragmentLitener)
            litener=(onStatusAddedLitener) context;
        else
            throw  new RuntimeException("Please implement onStatusAddedLitener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        litener=null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddTextStoryBinding.inflate(inflater,container,false);
        binding.txtStory.requestFocus();
        binding.txtStory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!binding.txtStory.getText().toString().isEmpty())
                    binding.lnAddTxtStory.setVisibility(View.VISIBLE);
                else
                    binding.lnAddTxtStory.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.btnAddTextStory.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String key=reference.child("textStory").push().getKey().toString();
                reference.child("textStory").child(key).setValue(new txtStoryModel(binding.txtStory.getText().toString(),
                        getCurrantDate(),auth.getCurrentUser().getPhoneNumber(),key)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Story Added Successfully", Toast.LENGTH_SHORT).show();
                        litener.statusAdded(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Story Added Failed",e.getMessage());
                    }
                });
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
    public interface onStatusAddedLitener{void statusAdded(boolean flag);}
}