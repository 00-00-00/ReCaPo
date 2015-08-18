package com.ground0.recapo.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ground0.recapo.DataHandler.DataBaseConnection;
import com.ground0.recapo.DataHandler.Project;
import com.ground0.recapo.LandingActivity;
import com.ground0.recapo.R;
import com.ground0.recapo.UI.ProjectListAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * Created by Arjun on 05-06-2015.
 */

public class ProjectSelectFragment extends Fragment {
    View rootView;
    Context context;
    static Vector<Project> projects=new Vector<Project>();
    Connection connection;
    ListView list;
    View progressBar;

    public static String QUERY_GET_PROJECTS = "SELECT \n" +
            "    *\n" +
            "FROM\n" +
            "    project\n" +
            "WHERE\n" +
            "    flag = 'public'\n" +
            "        AND (startDatetime <= NOW())\n" +
            "        AND (endDatetime >= NOW() OR endDatetime = 0);\n";


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstance) {
        this.context = getActivity();
        if (parent == null)
            return null;
        View view = layoutInflater.inflate(R.layout.fragment_project_select, parent, false);
        rootView = view;
        list = (ListView) rootView.findViewById(R.id.project_select_list);
        progressBar=rootView.findViewById(R.id.progressBar);

        ConnectToDB connectToDB=new ConnectToDB();
        connectToDB.execute();

        return rootView;
    }

    class ConnectToDB extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            connection = DataBaseConnection.getConnection();
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(QUERY_GET_PROJECTS);

                while (resultSet.next()) {
                    Project project=new Project(resultSet.getString("name"), resultSet.getInt("id"));

                    Log.i(LandingActivity.TAG, "NAME:" + resultSet.getString("name"));
                    projects.add(project);
                    Log.i(LandingActivity.TAG, "VECTOR:" + projects.lastElement().name);
                }
            connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for(int i=0;i<projects.size();i++)
                Log.i(LandingActivity.TAG,"VECTOR @ POST :"+projects.get(i).name);
            list.setAdapter(new ProjectListAdapter(context, projects));
            list.invalidate();
            progressBar.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);

        }
    }

}
