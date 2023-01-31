package com.example.whatsappclone.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.models.userProfileData;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class chatNumbers_RCV_adapter extends RecyclerView.Adapter<chatNumbers_RCV_adapter.newUserViewholder>{
    Context context;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference reference=storage.getReference();
    File f;
    List<userProfileData> usersList=new ArrayList<>();

    public chatNumbers_RCV_adapter(Context context, List<userProfileData> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public newUserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_chat_layout,null,false);
        return new newUserViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newUserViewholder holder, int position) {
         userProfileData newUser=usersList.get(position);
        holder.profileName.setText(newUser.getName());
        Toast.makeText(context, "name "+newUser.getName(), Toast.LENGTH_SHORT).show();
        //get profile image from firebase storage
        try {
            reference= storage.getReference().child("usersImages/"+newUser.getPhone()+newUser.getExention());
            f= File.createTempFile("tempFile",newUser.getExention());
            reference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    holder.profileImage.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
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

    @Override
    public int getItemCount() {
        return 0;
    }

    protected class newUserViewholder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView profileName,lastMessage,unReadMessagesNumber,timeForLastMessage;
        public newUserViewholder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.profileImage);
            profileName=itemView.findViewById(R.id.profileName);
            timeForLastMessage=itemView.findViewById(R.id.messageTime);
            lastMessage=itemView.findViewById(R.id.lastMessage);
            unReadMessagesNumber=itemView.findViewById(R.id.unReadMessagesNumber);
        }
    }

}
