package com.ground0.recapo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.ground0.recapo.DataHandler.DataBaseConnection;
import com.ground0.recapo.DataHandler.FileWriter;
import com.ground0.recapo.DataHandler.InformationArc;
import com.ground0.recapo.DataHandler.Item;
import com.ground0.recapo.Fragments.TaskFragment;
import com.ground0.recapo.UI.HeaderFooterAdapter;
import com.ground0.recapo.UI.LoremIpsumAdapter;
import com.ground0.recapo.UI.NavigationDrawerAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;


public class LandingActivity extends ActionBarActivity {

    public static String TAG="com.ground0.ReCaPo";
    static GestureDetector gestureDetector;
    //Flag to determine the current fragment state
    public static String frag_state="home";
    String mTitle="ReCaPo";
    String mDrawerTitle="Aside";
    public static int projectID;
    public static int linkParent;

    static FragmentManager supportFragmentManager;

    public static TaskFragment taskFragment;

    public  PlaceholderFragment placeholderFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static Vector<Item> informationArc=new Vector<>();
    private static Multimap<Integer,Integer> containerMap= ArrayListMultimap.create();

    public static HashMap<Integer,String> itemName=new HashMap<>();
    public static HashMap<Integer,Integer> itemID=new HashMap<>();
    public static InformationArc parsedData;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        FileWriter fileWriter=new FileWriter(this,"location");
        Stack<Integer> location=new Stack<>();
        try {
          location=(Stack<Integer>)fileWriter.readObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(keyCode == KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
        {
            if(frag_state.equals("task"))
            {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
            }
            else if(location.size()>1)
            {
                location.pop();
                Intent intent=new Intent(this,LandingActivity.class);
                intent.putExtra("id",projectID);
                intent.putExtra("parent",location.pop());
                try {
                    fileWriter.writeObject(this,location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(intent);

            }
            frag_state="home";
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        return gestureDetector.onTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        projectID=getIntent().getIntExtra("id",14);
        linkParent=getIntent().getIntExtra("parent",0);

        Log.i(TAG,"@LandingActivity:onCreate");


        //connect to DB
        ViewFromDb viewFromDb=new ViewFromDb();
        viewFromDb.execute();


        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */

                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                frag_state="home";
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                frag_state="aside";
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


    }

    @Override
    public void onResume()
    {
        super.onResume();
        FileWriter fileWriter=new FileWriter(this,"location");
        try {
            Stack<Integer> location= (Stack<Integer>) fileWriter.readObject(this);
            location.push(linkParent);
            fileWriter.writeObject(this,location);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(frag_state=="task")
        {
            LandingActivity.getSupportFM().beginTransaction()
                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                    .remove(taskFragment)
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static FragmentManager getSupportFM()
    {
        return supportFragmentManager;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


        }

        if(id==R.id.action_task){

            //add the fragment to the window only if there are no previous fragments
            if(frag_state.equals("home")) {


            LandingActivity.getSupportFM().beginTransaction()
                    .setCustomAnimations(R.anim.slide_up,R.anim.slide_down)
                    .add(R.id.container, taskFragment)
                    .addToBackStack(null)
                    .commit();




                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                frag_state = "task";
            }

        }

        return super.onOptionsItemSelected(item);
    }


    public void taskAccept(View view) {
        Log.i(TAG,"Task Accepted");
        int i=0;
        FileWriter fileWriter=new FileWriter(this,"task_number");
        FileWriter readWriter=new FileWriter(this,"task_answer");
        try {
            i=(Integer)fileWriter.readObject(this);
            /*
            Get the current card and evaluvate if its a hit or miss
             */
//            Item currentCard=new Item();
//            for(Item item:informationArc)
//            {
//                if(linkParent==item.ID)currentCard=item;
//            }
            int rightAnswer=(Integer) readWriter.readObject(this);
            int selectedAnswer=itemID.get(linkParent);
            if(rightAnswer==selectedAnswer)
            {
                Log.i(TAG,"Hit");
                //add to the database with the flag hit
            }
            else
            {
                Log.i(TAG,"Miss"+itemID.get(linkParent)+">>"+(Integer)readWriter.readObject(this));
                //add to the database with the flag miss
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.writeObject(this, i + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        getSupportFragmentManager().beginTransaction()
         .detach(taskFragment)
          .attach(taskFragment)
           .commit();



    }

    public void taskSkip(View view) {
        FileWriter fileWriter=new FileWriter(this,"task_number");
        int i=0;
        try{
            //add to database with flag skipped
            i=(Integer)fileWriter.readObject(this);
            fileWriter.writeObject(this, i + 1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**AsyncTask for collecting db data**/

    class ViewFromDb extends AsyncTask<Void,Void,Void>
    {

        ArrayList<Integer> sections;
        @Override
        protected Void doInBackground(Void... params) {
            Connection connection=DataBaseConnection.getConnection();
            String query_sections="select sectionID from projectmapsection where projectID="+projectID;
            String query_parent="select ID from informationarchitecture where flag='root' and projectID="+projectID;
            String query_item_id="select ID,itemID,linkToItemID from informationarchitecture where projectID="+projectID;
            String query_item_map="select ID,name from item where projectID="+projectID;
            String query_container_map="select * from containermapitem";
            sections=new ArrayList<Integer>();
            try {
                Statement statement=connection.createStatement();
                ResultSet resultSet=statement.executeQuery(query_sections);
                while(resultSet.next())
                {
                    Log.i(TAG,"sectionIDs : "+resultSet.getInt("sectionID"));
                    sections.add(resultSet.getInt("sectionID"));
                }

                statement=connection.createStatement();
                if(linkParent==0){
                    resultSet=statement.executeQuery(query_parent);
                    while(resultSet.next())
                    {
                        linkParent=resultSet.getInt("ID");
                        Log.i(TAG,"The parent link aquired :"+linkParent);
                    }
                }
                String query_ia="SELECT DISTINCT Child.itemID, Child.ID, Child.LFT, Child.RGT,Child.flag,Child.containerID,Child.sectionID,Child.linkToInformationarchitectureID,Child.linkToItemID,Child.projectID FROM informationarchitecture AS Child, informationarchitecture AS Parent \n" +
                        "WHERE Parent.LFT < Child.LFT AND Parent.RGT > Child.RGT AND Child.projectID="+projectID+" AND Parent.projectID="+projectID+" GROUP BY Child.itemID, Child.LFT, Child.RGT HAVING MAX(Parent.ID) = "+linkParent+";";
                statement=connection.createStatement();
                resultSet=statement.executeQuery(query_ia);
                informationArc.clear();
                while(resultSet.next())
                {
                    Log.i(TAG,"items : "+resultSet.getInt("itemID"));
                    informationArc.add(new Item(resultSet));
                }
                statement=connection.createStatement();
                resultSet=statement.executeQuery(query_item_map);
                while(resultSet.next())
                {
                    Log.i(TAG,"Item maps: "+resultSet.getInt("ID")+">>"+resultSet.getString("name"));
                    itemName.put(resultSet.getInt("ID"), resultSet.getString("name"));
                }

                statement=connection.createStatement();
                resultSet=statement.executeQuery(query_item_id);
                while(resultSet.next())
                {
                    Log.i(TAG,"Container maps : "+resultSet.getInt("ID")+">>"+ resultSet.getInt("itemID"));
                    if(resultSet.getInt("linkToItemID")==0)
                        itemID.put(resultSet.getInt("ID"), resultSet.getInt("itemID"));
                    else
                        itemID.put(resultSet.getInt("ID"),resultSet.getInt("linkToItemID"));
                }

                statement=connection.createStatement();
                resultSet=statement.executeQuery(query_container_map);
                while(resultSet.next())
                {
                    Log.i(TAG,"Container maps : "+resultSet.getInt("containerID")+">>"+ resultSet.getInt("itemID"));
                    containerMap.put(resultSet.getInt("containerID"), resultSet.getInt("itemID"));
                }


                parsedData=new InformationArc(informationArc,containerMap);
                //need to change location
                parsedData.parseIA();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            supportFragmentManager=getSupportFragmentManager();

            boolean flag_header=false;
            boolean flag_article=false;
            boolean flag_aside=false;
            boolean flag_footer=false;
            for(Integer t:sections)
                Log.i(TAG,"@PostExecuteViewDB:Section values:"+t);
            for(Integer t:sections)
                switch (t) {
                    case 1:
                        flag_header = true;
                        break;
                    case 2:
                        flag_article = true;
                        break;
                    case 3:
                        flag_aside = true;
                        break;
                    case 4:
                        flag_footer = true;
                        break;
                }
            Log.i(TAG,"Section flags set."+flag_header);


            Bundle bundle=new Bundle();

            placeholderFragment=new PlaceholderFragment();
            taskFragment=new TaskFragment();
            bundle.putBoolean("flag_header", flag_header);
            bundle.putBoolean("flag_footer", flag_footer);
            bundle.putBoolean("flag_article", flag_article);
            placeholderFragment.setArguments(bundle);
            taskFragment=new TaskFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, placeholderFragment)
                    .commit();
            frag_state="home";

            LandingActivity.getSupportFM().beginTransaction()
                    .setCustomAnimations(R.anim.slide_up,R.anim.slide_down)
                    .add(R.id.container, taskFragment)
                    .addToBackStack(null)
                    .commit();

            frag_state="task";

            gestureDetector=new GestureDetector(getApplicationContext(),new MyGestureDetector());

            if(PlaceholderFragment.rootView!=null)
                PlaceholderFragment.rootView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                });

            //test data
            ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
            if(flag_aside) {
                mDrawerList.setAdapter(new NavigationDrawerAdapter(getApplicationContext(), projectID, parsedData.getAside()));
                Log.i(TAG,"Drawer list visibilty:"+mDrawerList.getVisibility()+">>"+View.VISIBLE);
                mDrawerList.invalidate();
                mDrawerList.setVisibility(View.VISIBLE);
            }
            else
            {
                mDrawerList.setVisibility(View.GONE);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mDrawerToggle.setDrawerIndicatorEnabled(false);
            }
        }
    }
    /**
     * GestureListener
     */
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        private static final int SWIPE_MIN_DISTANCE = 60;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 60;




        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
               /* if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(getApplicationContext(), "Left", Toast.LENGTH_SHORT).show();
                    if(frag_state.equals("aside"))
                    {
                        MainActivity.getSupportFM().beginTransaction()
                                .setCustomAnimations(R.anim.slide_right, R.anim.slide_left)
                                .remove(asideFragment)
                                .commit();
                        getSupportFM().popBackStack();
                        frag_state="home";
                    }


                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if(frag_state.equals("home"))
                    {
                        MainActivity.getSupportFM().beginTransaction()
                                .setCustomAnimations(R.anim.slide_right, R.anim.slide_left)
                                .add(R.id.container, asideFragment)
                                .addToBackStack(null)
                                .commit();
                        frag_state="aside";
                    }

                } else*/ if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(getApplicationContext(), "Up Swipe", Toast.LENGTH_SHORT).show();
                    if(frag_state.equals("home")) {

                        LandingActivity.getSupportFM().beginTransaction()
                                .setCustomAnimations(R.anim.slide_up,R.anim.slide_down)
                                .add(R.id.container, taskFragment)
                                .addToBackStack(null)
                                .commit();
                        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        mDrawerToggle.setDrawerIndicatorEnabled(false);

                        frag_state = "task";
                    }

                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(getApplicationContext(), "Down Swipe", Toast.LENGTH_SHORT).show();
                    if(frag_state.equals("task"))
                    {

                            LandingActivity.getSupportFM().beginTransaction()
                                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                                    .remove(taskFragment)
                                    .commit();


                        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        mDrawerToggle.setDrawerIndicatorEnabled(true);
                        getSupportFM().popBackStack();
                        frag_state="home";
                    }

                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {

            return true;
        }


    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {

        public static View rootView;



        public PlaceholderFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //attaching FAB to the scrollview
            ObservableScrollView scrollView=(ObservableScrollView)rootView.findViewById(R.id.scrollview_mains);
            FloatingActionButton fab=(FloatingActionButton)rootView.findViewById(R.id.task_accept_main);
            fab.attachToScrollView(scrollView);
            this.rootView=rootView;


            Bundle bundle=this.getArguments();
            Log.i(TAG,"PlaceHolderFragment@OnCreateView:bundle received"+bundle.getBoolean("flag_header"));

            //dummy data


            ViewGroup layout=(ViewGroup)rootView.findViewById(R.id.article_parent);
            if(bundle.getBoolean("flag_article")) {

                new LoremIpsumAdapter(getActivity(), projectID,parsedData.getArticle(), layout);
            }
            else layout.setVisibility(View.GONE);

            LinearLayout lLayout=(LinearLayout)rootView.findViewById(R.id.header_parent);
            if(bundle.getBoolean("flag_header")) {


                new HeaderFooterAdapter(getActivity(),projectID,parsedData.getHeaders(), lLayout, R.id.header_links);
            }else lLayout.setVisibility(View.GONE);

            lLayout=(LinearLayout)rootView.findViewById(R.id.footer_parent);
            if(bundle.getBoolean("flag_footer")) {

                new HeaderFooterAdapter(getActivity(), projectID, parsedData.getFooters(),lLayout, R.id.footer_links);
            }else lLayout.setVisibility(View.GONE);

            return rootView;
        }

    }

}
