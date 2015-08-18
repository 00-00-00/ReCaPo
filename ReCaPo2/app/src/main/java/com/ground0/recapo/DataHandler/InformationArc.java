package com.ground0.recapo.DataHandler;

import android.util.Log;

import com.google.common.collect.Multimap;
import com.ground0.recapo.LandingActivity;

import java.util.Collection;
import java.util.Vector;

/**
 * Created by Arjun on 08-06-2015.
 */
public class InformationArc {
    Vector<Item> items;
    Multimap<Integer, Integer> containermapitem;

    int parent;
    Vector<Item> headers = new Vector<>(),
            footers = new Vector<>(),
            article = new Vector<>(),
            aside = new Vector<>();

    public Vector<Item> getHeaders() {
        return headers;
    }

    public Vector<Item> getFooters() {
        return footers;
    }

    public Vector<Item> getArticle() {
        return article;
    }

    public Vector<Item> getAside() {
        return aside;
    }

    /*
        This function should parse the IA and get the immediate children with respect to the
        sections and store it in respective data structures.
         */
    public InformationArc(Vector<Item> items, Multimap<Integer, Integer> container) {
        this.items = items;
        this.containermapitem = container;
    }


    public void parseIA() {
        Log.i(LandingActivity.TAG, "Parent is :" + parent);
        for (Item i : items) {
            if ("item".equals(i.flag)) {
                switch (i.sectionID) {
                    case 1:
                        Log.i(LandingActivity.TAG, "Header item added : " + i.itemID);
                        headers.add(i);
                        break;
                    case 2:
                        Log.i(LandingActivity.TAG, "Article item added : " + i.itemID);
                        article.add(i);
                        break;
                    case 3:
                        Log.i(LandingActivity.TAG, "Aside item added : " + i.itemID);
                        aside.add(i);
                        break;
                    case 4:
                        Log.i(LandingActivity.TAG, "Footer item added : " + i.itemID);
                        footers.add(i);
                        break;
                }
            }

        }

//        for(Item i:items) {
//            //find the parent
//            if (i.ID == parent) {
//                Log.i(LandingActivity.TAG, "Parent found, Id:" + i.ID);
//                pointer = i.LFT;
//                item=i;
//            }
//        }
//        Log.i(LandingActivity.TAG,"Loop exit");
//
//        //find the node that is adjacent (has left child +1 )
//        while(pointer+1<item.RGT)
//        {
//            for(Item child:items)
//            {
//                if(child.LFT>=pointer+1)
//                {
//                    //push it as a child
//                    Log.i(LandingActivity.TAG,"Child :"+child.itemID);
//                    if("item".equals(child.flag))
//                    {
//                        int tempID=0;
//                        //check for link to ia
//                        /*
//                        if(child.linktoitemID!=0)
//                        {
//                            tempID=child.linktoitemID;
//                        }
//                        else
//                        {
//                            tempID=child.ID;
//                        }*/
//
//                        switch (child.sectionID)
//                        {
//                            case 1:
//                                Log.i(LandingActivity.TAG,"Header item added : "+tempID);
//                                headers.add(child);
//                                break;
//                            case 2:
//                                Log.i(LandingActivity.TAG,"Article item added : "+tempID);
//                                article.add(child);
//                                break;
//                            case 3:
//                                Log.i(LandingActivity.TAG,"Aside item added : "+tempID);
//                                aside.add(child);
//                                break;
//                            case 4:
//                                Log.i(LandingActivity.TAG,"Footer item added : "+tempID);
//                                footers.add(child);
//                                break;
//                        }
//                    }
//                    if("container".equals(child.flag))
//                    {
//                        Collection<Integer> i = containermapitem.get(child.ID);
//
//                    }
//
//            pointer=child.RGT;
//            Log.i(LandingActivity.TAG,"New pointer :"+pointer);
//            }
//        }
//        }

    }
 /*
    public InformationArc(Vector<Item> items)
    {

    } */

}