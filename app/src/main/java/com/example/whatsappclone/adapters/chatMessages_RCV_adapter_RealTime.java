package com.example.whatsappclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.assitents.onRCVChatMessageClickListener;
import com.example.whatsappclone.models.messageModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class chatMessages_RCV_adapter_RealTime extends FirebaseRecyclerAdapter<messageModel, chatMessages_RCV_adapter_RealTime.newMessageViewholder> {
    Context context;
    onRCVChatMessageClickListener listener;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser fUser;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public chatMessages_RCV_adapter_RealTime(Context context, @NonNull FirebaseRecyclerOptions<messageModel> options, onRCVChatMessageClickListener listener) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull newMessageViewholder holder, int position, @NonNull messageModel model) {
        holder.message.setText(model.getMessages());
        holder.time.setText(model.getCurrantTime());
    }

    @NonNull
    @Override
    public newMessageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_rcv_item_layout_right,null,false);
            return new newMessageViewholder(view);
        }else {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_rcv_item_layout_left,null,false);
            return new newMessageViewholder(view);
        }

    }


    protected class newMessageViewholder extends RecyclerView.ViewHolder{
        TextView message,time;
        ConstraintLayout messageBody;
        public newMessageViewholder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.chatRCV_item_message);
            time=itemView.findViewById(R.id.chat_itemRCV_messageTime);
            messageBody=itemView.findViewById(R.id.chatMessageItemBody);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser=auth.getCurrentUser();
        if (getItem(position).getSender().equals(fUser.getPhoneNumber()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }
}
