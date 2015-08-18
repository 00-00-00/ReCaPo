package com.ground0.recapo.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ground0.recapo.DataHandler.FileWriter;
import com.ground0.recapo.DataHandler.Item;
import com.ground0.recapo.DataHandler.ResultTask;
import com.ground0.recapo.LandingActivity;
import com.ground0.recapo.R;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by Arjun on 10-05-2015.
 */
public class HeaderFooterAdapter {
    Context context;
    Vector<Item> data;

    LinearLayout parent;
    int projectID;

    public  int convertToDp(int pixel)
    {
        float scale=context.getResources().getDisplayMetrics().density;
        int dpasPix=(int)(pixel*scale+0.5f);

        return dpasPix;
    }
    public TextView setTextParams(String string)
    {
        TextView textView=new TextView(context);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity=Gravity.CENTER_VERTICAL|Gravity.LEFT;
        textView.setLayoutParams(params);
        textView.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);
        textView.setText(string);


        return textView;
    }

    public View getDivider()
    {


        View view=new View(context);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                (1,LinearLayout.LayoutParams.MATCH_PARENT);
        int dpasPix=convertToDp(10);
        params.setMargins(dpasPix, dpasPix, dpasPix, dpasPix);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.GRAY);
        return view;
    }

    public HeaderFooterAdapter(final Context context, final int projectID,Vector<Item> data,  LinearLayout viewGroup, int id)
    {
        this.projectID=projectID;

        this.context=context;
        this.data=data;
        this.parent=viewGroup;
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int link=(Integer)v.getTag();
                if (v.getId() == R.id.header_links) {

                    Log.i(LandingActivity.TAG,"Header link number :"+link+"@"+System.currentTimeMillis());
                } else if (v.getId() == R.id.footer_links) {

                    Log.i(LandingActivity.TAG,"Footer link number :"+link+"@"+System.currentTimeMillis());
                }
                ResultTask resultTask=new ResultTask(link,System.currentTimeMillis(),projectID);
                FileWriter fileWriter=new FileWriter(context,"resulttask");
                try {
                    fileWriter.writeObject(context,resultTask);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(context,LandingActivity.class);
                intent.putExtra("parent",link);
                intent.putExtra("id",projectID);
                context.startActivity(intent);
    }
};

        for(int i=0;i<data.size();i++)
        {
            TextView textView;
            if(data.get(i).linktoitemID==0)
                 textView=setTextParams(LandingActivity.itemName.get(data.get(i).itemID));
            else
                textView=setTextParams(LandingActivity.itemName.get(data.get(i).linktoitemID));
            textView.setId(id);
            textView.setTag(data.get(i).ID);
            textView.setOnClickListener(onClickListener);
            parent.addView(textView);
            if(i+1!=data.size())parent.addView(getDivider());
        }




    }
}
