package com.example.whatsappclone.adapters;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.assitents.DifferentColorCircularBorder;
import com.example.whatsappclone.assitents.statusClickListener;
import com.example.whatsappclone.models.txtStoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class story_RCV_adapter extends RecyclerView.Adapter<story_RCV_adapter.newtxtStoryViewholder> {
    Context context;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser fUser;
    ArrayList<txtStoryModel> txtStoryModelArrayList=new ArrayList<>();
    ArrayList<String>phoneNumberlist=new ArrayList<>();
    int storyCounter=2;
    statusClickListener listener;
    boolean flag=false;

    public story_RCV_adapter(Context context,ArrayList<txtStoryModel> txtStoryModelArrayList,statusClickListener listener) {
        this.txtStoryModelArrayList = txtStoryModelArrayList;
        this.context=context;
        this.listener=listener;
    }

    @NonNull
    @Override
    public story_RCV_adapter.newtxtStoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.status_rcv_element_layout,null,false);
        return new newtxtStoryViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull newtxtStoryViewholder holder, int position) {
        txtStoryModel model=txtStoryModelArrayList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(model.getPublisherPhone());
            }
        });
        if (phoneNumberlist.contains(model.getPublisherPhone())){
            Log.e("phoneNumberlist","Skip");
            Log.e("storyCounter",""+storyCounter);
            holder.publisherName.setVisibility(View.GONE);
            holder.story_txt.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.itemView.setVisibility(View.GONE);
            storyCounter++;
        }else {
            phoneNumberlist.add(model.getPublisherPhone());
            holder.story_txt.setText(model.getText());
            holder.publisherName.setText(model.getPublisherPhone().substring(2));
            holder.time.setText(model.getTime());
//            border.addBorderPortion(context, Color.RED, 0, 40);
//            border.addBorderPortion(context, Color.GREEN, 40, 90);
//            border.addBorderPortion(context, Color.BLUE, 90, 270);
//            border.addBorderPortion(context, 0xFF123456, 270, 360);
        }
//        holder.status_count.setText(""+storyCounter);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!flag){
//                    flag=true;
//                    holder.story_txt.animate().translationX(100).setDuration(1000).start();
//                    holder.publisherName.animate().translationX(100).setDuration(1000).start();
//                    holder.time.animate().translationX(100).setDuration(1000).start();
//                }else {
//                    holder.story_txt.animate().translationX(0).setDuration(1000).start();
//                    holder.publisherName.animate().translationX(0).setDuration(1000).start();
//                    holder.time.animate().translationX(0).setDuration(1000).start();
//                }
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return txtStoryModelArrayList.size();
    }

    public class newtxtStoryViewholder extends RecyclerView.ViewHolder{
        TextView story_txt,publisherName,time,status_count;
        public newtxtStoryViewholder(@NonNull View itemView) {
            super(itemView);
            story_txt=itemView.findViewById(R.id.txt_story_text);
            publisherName=itemView.findViewById(R.id.story_userProfileName);
            time=itemView.findViewById(R.id.story_publishTime);
            status_count=itemView.findViewById(R.id.status_count);

        }
    }

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

}
