package com.example.whatsappclone.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapters.tabLayoutAdapter;
import com.example.whatsappclone.databinding.FragmentChatInboxBinding;
import com.example.whatsappclone.models.tabLayoutModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chat_inbox#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chat_inbox extends Fragment {
    FragmentChatInboxBinding binding;
    ArrayList<tabLayoutModel>tabLayoutModelArrayList=new ArrayList<>();
    com.example.whatsappclone.adapters.tabLayoutAdapter newtabLayoutAdapter;
    onTabClickedListener listener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_imageExtention = "imageEntention";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mImageExtention;
    private String mParam2;

    public chat_inbox() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param imageEntention Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chat_inbox.
     */
    // TODO: Rename and change types and number of parameters
    public static chat_inbox newInstance(String imageEntention, String param2) {
        chat_inbox fragment = new chat_inbox();
        Bundle args = new Bundle();
        args.putString(ARG_imageExtention, imageEntention);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onTabClickedListener)
            listener=(onTabClickedListener) context;
        else
            throw new RuntimeException("Please implement onStatusTabClickedListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageExtention = getArguments().getString(ARG_imageExtention);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatInboxBinding.inflate(inflater,container,false);
        tabLayoutModelArrayList.add(new tabLayoutModel(new chat_numbers().newInstance("groups"),"GROUPS"));
        tabLayoutModelArrayList.add(new tabLayoutModel(new chat_numbers().newInstance("chat"),"CHATS"));
        tabLayoutModelArrayList.add(new tabLayoutModel(new status(),"STATUS"));
        tabLayoutModelArrayList.add(new tabLayoutModel(new chat_numbers().newInstance("calls"),"CALLS"));
        newtabLayoutAdapter =new tabLayoutAdapter(getActivity(),tabLayoutModelArrayList);
        binding.viewPager.setAdapter(newtabLayoutAdapter);
        binding.tableLayout.setScrollPosition(1,1,false);
        binding.viewPager.setCurrentItem(1);
        binding.tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText() != null)
                listener.tabClicked(tab.getText().toString());
                else
                    listener.tabClicked("null");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getText()== "STATUS")
                    listener.tabClicked("STATUSUnSelected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        new TabLayoutMediator(binding.tableLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setIcon(R.drawable.ic_baseline_groups_24);
//                        tab.setCustomView(R.layout.tab_groups_custom_view);
                        break;
                    case 1:
                        tab.setText("CHATS");
//                        tab.setCustomView(R.layout.tab_chats_custom_view);
                        break;
                    case 2:
                        tab.setText("STATUS");
//                        tab.setCustomView(R.layout.tab_status_custom_view);
                        break;
                    case 3:
                        tab.setText("CALLS");
//                        tab.setCustomView(R.layout.tab_calls_custom_view);
                        break;
                }
            }
        }).attach();
        return binding.getRoot();
    }
    public interface onTabClickedListener{void tabClicked(String tabName);}
}