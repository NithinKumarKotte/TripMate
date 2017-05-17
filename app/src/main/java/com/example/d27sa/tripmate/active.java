package com.example.d27sa.tripmate;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.SYSTEM_HEALTH_SERVICE;

/**
 * Created by d27sa on 02-04-2017.
 */
public class active extends Fragment {
    String value;
    static int count=0;
    ArrayList<CheckBox>mycheck=new ArrayList<CheckBox>();
    ListView listView;
    ArrayList<String> mylist;
    View rootView;
    EditText deletelistname;
    EditText startlistname;
    EditText acttosavelist;
    Button startlist;
    Button removebutton;
    Button acttosave;

    public active(){
        count=count+1;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activetab, container, false);

        /*Bundle bundle=getArguments();
        if(bundle!=null){
            String str= bundle.getString("list");
            //listView=(ListView)rootView.findViewById(R.id.activelist1);
            System.out.println("inside oncreate "+str);
            mylistname(bundle.getString("list"));
        }*/

            alllists();

        deletelistname=(EditText)rootView.findViewById(R.id.editText2);
        System.out.println("inside active deletelist"+deletelistname);
        removebutton=(Button)rootView.findViewById(R.id.button);
        startlistname=(EditText)rootView.findViewById(R.id.editText3);
        startlist=(Button)rootView.findViewById(R.id.startbutton);
        acttosavelist=(EditText)rootView.findViewById(R.id.editText4);
        acttosave=(Button)rootView.findViewById(R.id.actosav);
        removebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               deletetable();
                deletelistname.setText("");
            }
        });

        startlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartListFrmActive();
                //.setText("");
            }
        });

        acttosave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // StartListFrmActive();
                //.setText("");
            }
        });


        return rootView;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listView=(ListView)getView().findViewById(R.id.activelist1);
            if(resultCode == RESULT_OK && requestCode == 1) {
                value = data.getStringExtra("list");
                System.out.println("inside active "+value);
               // halts = data.getStringArrayListExtra("halts");
                CheckBox mycheckBox=new CheckBox(this.getContext());
                mycheckBox.setText(value);
                mycheckBox.setId(count);
                System.out.println("inside on resume"+count);
                mycheckBox.setClickable(true);
                RelativeLayout ll = (RelativeLayout)getView().findViewById(R.id.active);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if(count!=0){
                    lp.addRule(RelativeLayout.BELOW,mycheckBox.getId());
                    //ll.addView(mycheckBox, lp);
                }
                else{
                    lp.addRule(RelativeLayout.BELOW,R.id.section_label_active );
                   // ll.addView(mycheckBox, lp);
                }
               // count++;
               mycheck.add(mycheckBox);
               ArrayAdapter<CheckBox> adapter = new ArrayAdapter<CheckBox>(getContext(), android.R.layout.simple_selectable_list_item,android.R.id.checkbox, mycheck);
                listView.setAdapter(adapter);

            }

    }
*/
   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(outState);
        super.onSaveInstanceState(outState);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle=getArguments();
        if(bundle!=null){


                alllists();



            removebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletetable();
                    deletelistname.setText("");

                }
            });

            startlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartListFrmActive();
                    //.setText("");
                }
            });


        }
            /*
            if(bundle.containsKey("list")){
                String strClickedItem=bundle.getString("list");
                System.out.println("my list inside active "+strClickedItem);
                listView=(ListView)getView().findViewById(R.id.activelist);
                CheckBox mycheckBox=new CheckBox(this.getContext());
                mycheckBox.setText(strClickedItem);
                mycheckBox.setId(count);
                System.out.println(mycheckBox.getId());
                System.out.println("inside on resume"+count);
                mycheckBox.setClickable(true);
                RelativeLayout ll = (RelativeLayout)getView().findViewById(R.id.active);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if(count!=0){
                    lp.addRule(RelativeLayout.BELOW,mycheckBox.getId());
                    //ll.addView(mycheckBox, lp);
                }
                else{
                    lp.addRule(RelativeLayout.BELOW,R.id.section_label );
                    //ll.addView(mycheckBox, lp);
                }
                mycheck.add(mycheckBox);
                ArrayAdapter<CheckBox> adapter = new ArrayAdapter<CheckBox>(getContext(), android.R.layout.simple_selectable_list_item,android.R.id.checkbox, mycheck);
                listView.setAdapter(adapter);
            }
        }*/
    }


 /*   @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        System.out.println(count);
    }*/

   public void mylistname(String list){
        dbhelper db=new dbhelper(this.getContext());
        int j=0;
        mylist=new ArrayList<>();
        Cursor cursor = db.getList(list);
       RelativeLayout ll = (RelativeLayout)rootView.findViewById(R.id.active);
       listView=(ListView)ll.findViewById(R.id.activelist1);

        cursor.moveToFirst();

        if (cursor != null) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    mylist.add(j,cursor.getString(i));

                    System.out.println("inside while "+mylist);
                    Log.e( "","" + cursor.getString(i));
                }
                j++;
            }while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,mylist);

        System.out.println("outside while "+mylist);

        // Assign adapter to ListView
        listView.setAdapter(adapter);


    }

    public void alllists(){
        dbhelper db=new dbhelper(this.getContext());
        int j=0;
        mylist=new ArrayList<>();
        Cursor cursor = db.getListname();
        RelativeLayout ll = (RelativeLayout)rootView.findViewById(R.id.active);
        listView=(ListView)ll.findViewById(R.id.activelist1);

        cursor.moveToFirst();

        if (cursor != null) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    mylist.add(j,cursor.getString(i));

                    System.out.println("inside while "+mylist);
                    Log.e( "","" + cursor.getString(i));
                }
                j++;
            }while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,mylist);

        System.out.println("outside while "+mylist);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

    }


    public void deletetable(){
        dbhelper db=new dbhelper(this.getContext());
        db.deletelist(deletelistname.getText().toString());
        alllists();

    }

    public ArrayList initiallist(){
        dbhelper db=new dbhelper(this.getContext());
        int j=0;
        ArrayList mylist=new ArrayList<>();
        Cursor cursor = db.getListname();
        RelativeLayout ll = (RelativeLayout)rootView.findViewById(R.id.active);
        listView=(ListView)ll.findViewById(R.id.activelist1);

        cursor.moveToFirst();

        if (cursor != null) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    mylist.add(j,cursor.getString(i));

                    System.out.println("inside while "+mylist);
                    Log.e( "","" + cursor.getString(i));
                }
                j++;
            }while (cursor.moveToNext());
        }else{mylist=null;}
     return  mylist;

    }

    public void StartListFrmActive(){
       // System.out.println("hello in displayList");
        String mylist1=startlistname.getText().toString();
        dbhelper db = new dbhelper(this.getContext());
        Cursor mylist=db.getList(mylist1);
        int j=0;
        mylist.moveToFirst();
        ArrayList values=new ArrayList<>();
        //System.out.println(mylist.);
        // String [] from={"placeId","description"};
        // System.out.println(mylist.getString(0));
        //ListView newList=(ListView)rootView.findViewById(R.id.List);
        if (mylist != null) {
            do {
                for (int i = 0; i < mylist.getColumnCount(); i++) {

                    values.add(j,mylist.getString(i));

                    System.out.println("inside while "+values);
                    Log.e( "","" + mylist.getString(i));
                }
                j++;
            }while (mylist.moveToNext());
        }

        //System.out.println("outside while "+values);
        Intent myintent= new Intent(this.getContext(),MapsActivity.class);
        myintent.putExtra("note",values);
        startActivity(myintent);
    }

    public void movtosave(){

        String mylist1=acttosavelist.getText().toString();
        dbhelper db = new dbhelper(this.getContext());
        Cursor mylist=db.getList(mylist1);
        int j=0;
        mylist.moveToFirst();
        ArrayList values=new ArrayList<>();
        //System.out.println(mylist.);
        // String [] from={"placeId","description"};
        // System.out.println(mylist.getString(0));
        //ListView newList=(ListView)rootView.findViewById(R.id.List);
        if (mylist != null) {
            do {
                for (int i = 0; i < mylist.getColumnCount(); i++) {

                    values.add(j,mylist.getString(i));

                    System.out.println("inside while "+values);
                    Log.e( "","" + mylist.getString(i));
                }
                j++;
            }while (mylist.moveToNext());
        }
        Intent intent = new Intent(getActivity(), saved.class);
        intent.putStringArrayListExtra("list", values);
        startActivity(intent);
    }
}
