package com.ground0.recapo.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.ground0.recapo.DataHandler.DataBaseConnection;
import com.ground0.recapo.LandingActivity;
import com.ground0.recapo.LoginActivity;
import com.ground0.recapo.R;
import com.ground0.recapo.UI.NavigationDrawerAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Created by Arjun on 28-05-2015.
 */
public class LoginFragment extends Fragment {
    Connection connection;
    View rootView;
    LoginActivity lg;





    @Override
    public View onCreateView(LayoutInflater layoutInflater,ViewGroup parent,Bundle savedInstance)
    {
        if(parent==null)
            return null;
        View view=layoutInflater.inflate(R.layout.fragment_login,parent,false);
        rootView=view;
        lg= (LoginActivity) this.getActivity();

        ViewHolder.username= (EditText) rootView.findViewById(R.id.login_username);
        ViewHolder.password= (EditText) rootView.findViewById(R.id.login_password);
        ViewHolder.accept= (FloatingActionButton) rootView.findViewById(R.id.login_accept);
        final Intent landingPage=new Intent(getActivity(), LandingActivity.class);




        ViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.login_accept)
                {
                    //Show the list of available projects

                    Runnable runnable=new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //The fragment will connect with the database
                                //Call the activity to add the next fragment
                                lg.selectProject();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    Thread networkThread=new Thread(runnable);
                    networkThread.start();


                }
            }
        });
        return rootView;
    }



    private static class ViewHolder {
        public static EditText username,password;
        public static FloatingActionButton accept;
    }

}
