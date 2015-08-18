package com.ground0.recapo.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ground0.recapo.R;
import com.ground0.recapo.UI.NavigationDrawerAdapter;

import java.util.Vector;

/**
 * Created by Arjun on 27-04-2015.
 */
public class AsideFragment extends Fragment {

    View rootView;
    @Override
    public View onCreateView(LayoutInflater layoutInflater,ViewGroup parent,Bundle savedInstance)
    {
        if(parent==null)
            return null;
        View view=layoutInflater.inflate(R.layout.fragment_aside,parent,false);
        rootView=view;

        ListView listView=(ListView)rootView.findViewById(R.id.aside_list);

        //Random sample list for sidebar ListView
        Vector<String> data=new Vector<>(7);
        for(int i=0;i<7;i++){data.add("Hello W"+i);}


        return rootView;
    }
}
