package com.example.whatsappclone.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.whatsappclone.models.tabLayoutModel;

import java.util.ArrayList;

public class tabLayoutAdapter extends FragmentStateAdapter {
    ArrayList<tabLayoutModel> tabLayoutModelArrayList;

    public tabLayoutAdapter(@NonNull FragmentActivity fragmentActivity,ArrayList<tabLayoutModel> tabLayoutModelArrayList) {
        super(fragmentActivity);
        this.tabLayoutModelArrayList=tabLayoutModelArrayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return tabLayoutModelArrayList.get(position).getFragment();
    }

    @Override
    public int getItemCount() {
        return tabLayoutModelArrayList.size();
    }
}
