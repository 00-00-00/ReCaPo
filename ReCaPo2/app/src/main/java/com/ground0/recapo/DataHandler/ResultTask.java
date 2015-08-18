package com.ground0.recapo.DataHandler;

import com.ground0.recapo.LandingActivity;

/**
 * Created by Arjun on 23-06-2015.
 */
public class ResultTask {
    public int id;
    public int item;
    public long time;
    public int projectID;

    public ResultTask(int id,long time,int projectID)
    {
        this.id=id;
        this.item= LandingActivity.itemID.get(id);
        this.time=time;
        this.projectID=projectID;
    }
}
