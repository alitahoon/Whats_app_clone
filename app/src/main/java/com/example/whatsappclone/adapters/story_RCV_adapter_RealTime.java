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
import com.example.whatsappclone.models.txtStoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class story_RCV_adapter_RealTime extends FirebaseRecyclerAdapter<txtStoryModel, story_RCV_adapter_RealTime.newtxtStoryViewholder> {
    Context context;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser fUser;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public story_RCV_adapter_RealTime(@NonNull FirebaseRecyclerOptions<txtStoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull story_RCV_adapter_RealTime.newtxtStoryViewholder holder, int position, @NonNull txtStoryModel model) {
        holder.story_txt.setText(model.getText());
        holder.publisherName.setText(model.getPublisherPhone().substring(2));
        holder.time.setText(model.getTime());

    }

    @NonNull
    @Override
    public story_RCV_adapter_RealTime.newtxtStoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.status_rcv_element_layout,null,false);
        return new newtxtStoryViewholder(v);
    }

    public class newtxtStoryViewholder extends RecyclerView.ViewHolder{
        TextView story_txt,publisherName,time;
        public newtxtStoryViewholder(@NonNull View itemView) {
            super(itemView);
            story_txt=itemView.findViewById(R.id.txt_story_text);
            publisherName=itemView.findViewById(R.id.story_userProfileName);
            time=itemView.findViewById(R.id.story_publishTime);
        }
    }

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

}
