package com.example.whatsappclone.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.example.whatsappclone.assitents.onRCVChatClickListener;
import com.example.whatsappclone.assitents.onRCVChatMessageClickListener;
import com.example.whatsappclone.models.messageModel;
import com.example.whatsappclone.models.userProfileData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class chatNumbers_RCV_adapter_RealTime extends FirebaseRecyclerAdapter<userProfileData, chatNumbers_RCV_adapter_RealTime.newUserViewholder> {
    Context context;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference reference=storage.getReference();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    onRCVChatClickListener listener;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    ArrayList<messageModel> messageModels=new ArrayList<>();
    String sender=firebaseAuth.getCurrentUser().getPhoneNumber();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public chatNumbers_RCV_adapter_RealTime(Context context, @NonNull FirebaseRecyclerOptions<userProfileData> options, onRCVChatClickListener listener) {
        super(options);
        this.context=context;
        this.listener=listener;
    }




    @Override
    protected void onBindViewHolder(@NonNull newUserViewholder holder, int position, @NonNull userProfileData model) {
        holder.profileName.setText(model.getName());
        //get profile image from firebase storage
            reference= storage.getReference().child("usersImages/"+model.getPhone()+"."+model.getExention());
            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri.toString()).fit().centerCrop()
                            .placeholder(R.drawable.account_circle_white)
                            .into(holder.profileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Storage Error ",e.getMessage());
                }
            });
            String reciver ="+2"+model.getPhone();
            databaseReference.child("chats").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        messageModel newModel=snapshot1.getValue(messageModel.class);
//                        Toast.makeText(context, "c "+newModel.getSender(), Toast.LENGTH_SHORT).show();
                        if (newModel.getSender().equals(sender)
                                &&newModel.getReciever().equals(reciver)||
                                newModel.getSender().equals(reciver)
                                        &&newModel.getReciever().equals(sender)){
                            messageModels.add(snapshot1.getValue(messageModel.class));
                            holder.lastMessage.setText(messageModels.get(messageModels.size()-1).getMessages().toString());
                            holder.timeForLastMessage.setText(messageModels.get(messageModels.size()-1).getCurrantTime().toString());
                        }
                        }
//                    Toast.makeText(context, "size "+messageModels.size(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
//        try {
//            reference= storage.getReference().child("usersImages/"+model.getPhone()+"."+model.getExention());
//            File f= File.createTempFile("tempFile",model.getExention());
//            reference.getFile(f).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    holder.profileImage.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.i("Error ",e.getMessage());
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(model);
            }
        });

    }
    @Override
    public void onDataChanged() {
        // Called each time there is a new query snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
//        Toast.makeText(context, "new query", Toast.LENGTH_SHORT).show();
    }


    @NonNull
    @Override
    public newUserViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_chat_layout,null,false);
        return new newUserViewholder(view);
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
