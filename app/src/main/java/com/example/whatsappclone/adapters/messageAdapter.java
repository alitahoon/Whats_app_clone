package com.example.whatsappclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.assitents.onRCVChatMessageClickListener;
import com.example.whatsappclone.models.messageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.messageViewHolder> {
    ArrayList<messageModel> messageModels=new ArrayList<>();
    Context context;
    onRCVChatMessageClickListener listener;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser fUser;

    public messageAdapter(ArrayList<messageModel> messageModels, Context context, onRCVChatMessageClickListener listener) {
        this.messageModels = messageModels;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_rcv_item_layout_left,null,false);
            return new messageViewHolder(view);
        }else {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_rcv_item_layout_right,null,false);
            return new messageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull messageViewHolder holder, int position) {
        messageModel model=messageModels.get(position);
        holder.message.setText(model.getMessages());
        holder.time.setText(model.getCurrantTime());
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class messageViewHolder extends RecyclerView.ViewHolder {
        TextView message,time;
        public messageViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.chatRCV_item_message);
            time=itemView.findViewById(R.id.chat_itemRCV_messageTime);
        }
    }
    @Override
    public int getItemViewType(int position) {
        fUser=auth.getCurrentUser();
        if (messageModels.get(position).getSender().equals(fUser.getPhoneNumber()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }
}
