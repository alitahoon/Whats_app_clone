package com.example.whatsappclone.assitents;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;
import com.example.whatsappclone.R;

public class loading_dialog extends ProgressDialog {
    Context context;
    Dialog dialog;
    String type;

    public loading_dialog(Context context,String type) {
        super(context);
        this.context = context;
        this.type=type;
    }
    public void showDialog(String title){
        dialog=new Dialog(context, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialCalendar_Fullscreen);
        switch (type){
            case "codeVerfication":
                dialog.setContentView(R.layout.fragment_loading_dialog);
                break;
            case "chatList":
                dialog.setContentView(R.layout.fragment_loading_dialog_firestore);
                break;
            default:
                dialog.setContentView(R.layout.fragment_loading_dialog);
                break;
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView di_txt=dialog.findViewById(R.id.title_dialog_loding_fragment);
        di_txt.setText(title);
        dialog.create();
        dialog.show();
    }
    public  void hideDialog(){ dialog.dismiss();}

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }
}
