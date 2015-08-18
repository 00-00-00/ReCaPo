package com.ground0.recapo.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ground0.recapo.DataHandler.DataBaseConnection;
import com.ground0.recapo.DataHandler.FileWriter;
import com.ground0.recapo.LandingActivity;
import com.ground0.recapo.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Result;

/**
 * Created by Arjun on 24-04-2015.
 */
public class TaskFragment extends Fragment {

    View rootView;
    static Context context;
    static int projectID;
    static FileWriter fileWriter;
    static FileWriter writeAnswer;
    static boolean write_flag;

    @Override
    public View onCreateView(LayoutInflater layoutInflater,ViewGroup parent,Bundle savedInstance)
    {
        if(parent==null)
        {
            return null;
        }
        View view=layoutInflater.inflate(R.layout.fragment_task,parent,false);
        rootView =view;

        LandingActivity activity=(LandingActivity)getActivity();
        projectID=activity.projectID;
        context=getActivity();
        Log.i(LandingActivity.TAG, "@TaskFragment : " + projectID);

        //Checks if the file has been written in before
        fileWriter=new FileWriter(getActivity(),"task_number");
        writeAnswer=new FileWriter(getActivity(),"task_answer");
        try {
            int i=(Integer)fileWriter.readObject(getActivity());
            Log.i(LandingActivity.TAG,"@TaskFragment : reading task number :"+i);
            if(i==0)
                write_flag=false;
            else
                write_flag=true;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        LoadTaskfromDB loadTaskfromDB=new LoadTaskfromDB();
        loadTaskfromDB.execute();

        //Listeners for the buttons.... Implement functionality later.
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                if(v.getId()==R.id.back)
                {
                    toast=Toast.makeText(getActivity(),"Back",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(v.getId()==R.id.restart)
                {
                    toast=Toast.makeText(getActivity(),"Restart",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(v.getId()==R.id.skip)
                {
                    toast=Toast.makeText(getActivity(),"Info",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };


        Button button=(Button) rootView.findViewById(R.id.back);
        button.setOnClickListener(listener);
        button=(Button) rootView.findViewById(R.id.restart);
        button.setOnClickListener(listener);
        button=(Button) rootView.findViewById(R.id.skip);
        button.setOnClickListener(listener);

        return rootView;
    }

    class LoadTaskfromDB extends AsyncTask<Void,Void,String>
    {
        Connection connection;
        String Query="select * from task where projectID="+projectID;




        @Override
        protected String doInBackground(Void... params) {
            String text=new String();
            int task_answer;
            connection= DataBaseConnection.getConnection();
            try {
                Statement statement=connection.createStatement();
                ResultSet resultSet=statement.executeQuery(Query);
                if(write_flag){
                    int i;
                    if(resultSet.absolute((Integer)fileWriter.readObject(context)+1))
                    {
                        text=resultSet.getString("task");
                        task_answer=resultSet.getInt("itemID");
                        writeAnswer.writeObject(context,task_answer);
                    }
                    else
                    {
                        Toast toast=Toast.makeText(context,"End of tasks",Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
                else if(resultSet.next())
                {
                    text=resultSet.getString("task");
                    task_answer=resultSet.getInt("itemID");
                    writeAnswer.writeObject(context,task_answer);

                }
                else
                {
                    Toast toast=Toast.makeText(context,"End of tasks",Toast.LENGTH_SHORT);
                    toast.show();
                }


            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return text;
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

            TextView textView=(TextView)rootView.findViewById(R.id.task);
            textView.setText(text);
        }
    }


}
