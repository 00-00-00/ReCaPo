package com.ground0.recapo.DataHandler;

import android.util.Log;

import com.ground0.recapo.LandingActivity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Arjun on 08-06-2015.
 */
public class Item {
    public String flag;
    public int ID,itemID,containerID,sectionID,LFT,RGT,linktoIA,linktoitemID;

    public Item(ResultSet resultSet)
    {
        try {
            flag=resultSet.getString("flag");
            ID=resultSet.getInt("ID");
            itemID=resultSet.getInt("itemID");
            containerID=resultSet.getInt("containerID");
            sectionID=resultSet.getInt("sectionID");
            LFT=resultSet.getInt("LFT");
            RGT=resultSet.getInt("RGT");
            linktoIA=resultSet.getInt("linkToInformationarchitectureID");
            linktoitemID=resultSet.getInt("linktoitemID");
            Log.i(LandingActivity.TAG,"Item object :"+itemID+">>"+flag+"created");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Item()
    {
        flag="flag";

    }


}
