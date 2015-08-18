package com.ground0.recapo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ground0.recapo.DataHandler.FileWriter;
import com.ground0.recapo.Fragments.LoginFragment;
import com.ground0.recapo.Fragments.ProjectSelectFragment;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Arjun on 05-06-2015.
 */
public class LoginActivity extends ActionBarActivity {
    public static LoginFragment loginFragment;
    public static ProjectSelectFragment projectSelectFragment;

    @Override
    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login);


        loginFragment=new LoginFragment();
        projectSelectFragment=new ProjectSelectFragment();

        FileWriter writer=new FileWriter(this,"task_number");
        FileWriter writer1=new FileWriter(this,"task_answer");
        FileWriter writer2=new FileWriter(this,"location");
        int i=0;
        try {
            writer.writeObject(this,i);
            writer1.writeObject(this,i);
            Stack<Integer> x=new Stack<>();
            writer2.writeObject(this,x);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(savedInstance==null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_login, loginFragment)
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .commit();
        }


    }

    public void selectProject()
    {
        getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_login,projectSelectFragment)
                        .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                        .addToBackStack(null)
                        .commit();
         }

}
