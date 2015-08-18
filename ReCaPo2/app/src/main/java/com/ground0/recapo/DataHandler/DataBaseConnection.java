package com.ground0.recapo.DataHandler;

import android.util.Log;

import com.ground0.recapo.LandingActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Arjun on 13-05-2015.
 */
public class DataBaseConnection {
    private static DataBaseConnection instance=new DataBaseConnection();
    private static final String URL="jdbc:mysql://recapo.de:3306/recapo-1.2";
    private static final String USER="androidapp";
    private static final String PASSWORD="HGsZxJ9QIKOrXJNEA";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    private DataBaseConnection()
    {
        try {
            Class.forName(DRIVER_CLASS);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection()
    {
        Connection connection=null;
        try{
            connection= DriverManager.getConnection(URL,USER,PASSWORD);
            Log.i(LandingActivity.TAG,"Connection received:"+connection);

        } catch(SQLException e){
            e.printStackTrace();
        }
        return connection;
    }
    public static Connection getConnection() {
        return instance.createConnection();
    }
}
