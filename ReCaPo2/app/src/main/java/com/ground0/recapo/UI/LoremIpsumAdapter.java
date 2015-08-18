package com.ground0.recapo.UI;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ground0.recapo.DataHandler.FileWriter;
import com.ground0.recapo.DataHandler.Item;
import com.ground0.recapo.DataHandler.ResultTask;
import com.ground0.recapo.LandingActivity;
import com.ground0.recapo.R;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import de.svenjacobs.loremipsum.LoremIpsum;

/**
 * Created by Arjun on 06-05-2015.
 */
public class LoremIpsumAdapter {
    String loremIpsumText;    //contains the loren ipsum content
    Vector<Item> data;  //the links-->change ds according to the requirement

    Context context;
    LoremIpsum loremIpsum;
    ViewGroup viewGroup;
    int projectID;

    public int randomGen(int min,int max)
    {
        Random rand=new Random();
        int random=rand.nextInt((max-min)+1)+min;
        return random;
    }

    public TextView setParamLI(String string)
    {
        TextView textView=new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextAppearance(context, android.R.style.TextAppearance_Medium);
        textView.setText(string);
        return  textView;
    }

    public TextView setParamLinks(String string)
    {
        TextView textView=new TextView(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextAppearance(context, android.R.style.TextAppearance_Large);
        textView.setText(string);
        return  textView;
    }

    public LoremIpsumAdapter(final Context context,final int projectID, Vector<Item> data,  ViewGroup viewGroup)
    {
        loremIpsumText =new String();
        loremIpsum=new LoremIpsum();

        this.projectID=projectID;
        this.data=data;
        this.context=context;


        for(int i=0,k=0;i<data.size();i++)
        {
            loremIpsumText="";
            for(int j=0;j<randomGen(1,3);j++){

                loremIpsumText=loremIpsum.getWords(1,k)+" ";
                TextView textView=setParamLI(loremIpsumText);
                viewGroup.addView(textView);
                k++;
            }

            TextView textView;
            if(data.get(i).linktoitemID==0)
                textView=setParamLinks(LandingActivity.itemName.get(data.get(i).itemID));
            else
                textView=setParamLinks(LandingActivity.itemName.get(data.get(i).linktoitemID));
            viewGroup.addView(textView);
            textView.setId(R.id.article_links);
            textView.setTag(data.get(i).ID);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.article_links) {
                        int link = (Integer)v.getTag();
                        Log.i(LandingActivity.TAG, "The article link number is :" + link + "@" + System.currentTimeMillis());
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
                }
            });

            for(int j=0;j<randomGen(0,3);j++){

                loremIpsumText=loremIpsum.getWords(1,k)+" ";
                textView=setParamLI(loremIpsumText);
                viewGroup.addView(textView);
                k++;
            }
        }
    }
}
