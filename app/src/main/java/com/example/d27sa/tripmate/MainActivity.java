package com.example.d27sa.tripmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    Intent i;
    String list;
    Bundle bundle;
    int count;
    active tab1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setTheme(R.style.splashScreenTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        System.out.println("bundle"+bundle);
        System.out.println("bundle"+getIntent());

        if(getIntent().getStringExtra("list")!=null) {
            list = (String)getIntent().getStringExtra("list");
            count++;
            System.out.println("bundleinside "+list);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (this,addlist.class);
                startActivity(i);
                *//*Snackbar.make(view, "Mihir bhadva ahe", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
            }
        });*/

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void gotoaddlist (View view)
    {
        i = new Intent (this,listadd.class);
        startActivityForResult(i,1);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        Log.v("active", "save instance");
        savedInstanceState.putAll(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Returning current tabs
            switch (position) {
                case 0:active tab1 = new active();
                    if(count==0) {
                    System.out.println("inside active of main");
                    count++;
                    return tab1;
                     }else {
                    if (getIntent() != null) {
                        System.out.println("inside active of if of active");
                        bundle = new Bundle();
                        bundle.putString("list", list);
                        tab1.setArguments(bundle);
                        count++;
                        return tab1;
                    }
                }
                case 1:
                    saved tab2 = new saved();
                    return tab2;
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Lists";
                case 1:
                    return "Details";
            }
            return null;
        }
    }
}
