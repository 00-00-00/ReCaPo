package com.ground0.recapo.UI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ground0.recapo.DataHandler.FileWriter;
import com.ground0.recapo.DataHandler.Item;
import com.ground0.recapo.DataHandler.ResultTask;
import com.ground0.recapo.LandingActivity;
import com.ground0.recapo.R;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by Arjun on 23-04-2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    Vector<Item> data;

    Context context;
    LayoutInflater layoutInflater;
    int projectID;

    public NavigationDrawerAdapter(Context context,int projectID, Vector<Item> data )
    {
        this.projectID=projectID;
        this.context=context;
        this.data=data;

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
        return R.id.aside_links;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            if(layoutInflater==null)layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.side_bar_list_item,parent,false);
            ViewHolder.textView=(TextView)convertView.findViewById(R.id.side_bar_list_item_text);
        }
        if(data.get(position).linktoitemID==0)
            ViewHolder.textView.setText(LandingActivity.itemName.get(data.get(position).itemID));
        else
            ViewHolder.textView.setText(LandingActivity.itemName.get(data.get(position).linktoitemID));

        convertView.setTag(data.get(position).ID);
        convertView.setId(R.id.aside_links);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(v.getId()==R.id.aside_links) {
                   int item = (Integer) v.getTag();
                   Log.i(LandingActivity.TAG, "The link on the aside menu is :" + item + "@" + System.currentTimeMillis());
                   ResultTask resultTask=new ResultTask(item,System.currentTimeMillis(),projectID);
                   FileWriter fileWriter=new FileWriter(context,"resulttask");
                   try {
                       fileWriter.writeObject(context,resultTask);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   Intent intent=new Intent(context,LandingActivity.class);
                   intent.putExtra("parent",item);
                   intent.putExtra("id",projectID);
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
