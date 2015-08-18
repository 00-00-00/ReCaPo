package com.ground0.recapo.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ground0.recapo.DataHandler.Project;
import com.ground0.recapo.LandingActivity;
import com.ground0.recapo.LoginActivity;
import com.ground0.recapo.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by Arjun on 05-06-2015.
 */
public class ProjectListAdapter extends BaseAdapter {
    Vector<Project> data;
    LayoutInflater layoutInflater;
    Context context;

    public ProjectListAdapter(Context context,Vector<Project> data) {
        this.context=context;
        this.data=data;
        for(int i=0;i<data.size();i++)
            Log.i(LandingActivity.TAG,"VECTOR @ POST :"+data.get(i).name);

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return R.id.project_select_links;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            if(layoutInflater==null)
                layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.project_list_item,parent,false);
            ViewHolder.textView=(TextView)convertView.findViewById(R.id.project_select_item_text);
        }

        Project project=data.get(position);
        ViewHolder.textView.setText(project.name);


        convertView.setTag(project.id);
        convertView.setId(R.id.project_select_links);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.project_select_links) {
                    int item = (Integer) v.getTag();
                    Log.i(LandingActivity.TAG, "The link on the project menu is :" + item + "@" + System.currentTimeMillis());

                    Intent intent=new Intent(context,LandingActivity.class);
                    intent.putExtra("id",item);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder
    {
        static TextView textView;
    }
}
