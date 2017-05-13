package com.example.d27sa.tripmatetry1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

public class listadd extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    AutoCompleteTextView textIn,Dest;
    Button buttonAdd;
    Button destbtn;
    LinearLayout container;
    private EditText nameList;
    ArrayList<String> values;
    /*TextView reList, info;*/

/*    private static final String[] NUMBER = new String[] {
            "One", "Two", "Three", "Four", "Five",
            "Six", "Seven", "Eight", "Nine", "Ten"
    };*/
    ArrayAdapter<String> adapter;

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final String LOG_TAG = "listadd";
    private AutoCompleteTextView mAutocompleteTextView,mAutocompleteTextViewdestination;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private dbhelper db;
    Button buttonRemove;
    String id;
    public ArrayList <String> itemnew1=new ArrayList<>();
    private PlaceArrayAdapter.PlaceAutocomplete itemnew;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listadd);
        mGoogleApiClient = new GoogleApiClient.Builder(listadd.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        mAutocompleteTextViewdestination = (AutoCompleteTextView) findViewById(R.id
                .dest);
        mAutocompleteTextViewdestination.setThreshold(5);

        mAutocompleteTextViewdestination.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextViewdestination.setAdapter(mPlaceArrayAdapter);




        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .textin);
        mAutocompleteTextView.setThreshold(5);

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        Dest = (AutoCompleteTextView)findViewById(R.id.dest);
        Dest.setAdapter(mPlaceArrayAdapter);

        nameList = (EditText) findViewById(R.id.editText);


        /*adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, NUMBER);*/

        textIn = (AutoCompleteTextView)findViewById(R.id.textin);
        textIn.setAdapter(mPlaceArrayAdapter);
        //destbtn=(Button)findViewById(R.id.destbtn);
        buttonAdd = (Button)findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.container1);
        /*reList = (TextView)findViewById(R.id.relist);
        reList.setMovementMethod(new ScrollingMovementMethod());
        info = (TextView)findViewById(R.id.info);
        info.setMovementMethod(new ScrollingMovementMethod());*/

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final ScrollView addView = (ScrollView)layoutInflater.inflate(R.layout.row, null);
                final AutoCompleteTextView textOut = (AutoCompleteTextView)addView.findViewById(R.id.textout);
                textOut.setAdapter(mPlaceArrayAdapter);
                textOut.setText(textIn.getText().toString());
                saveHalt();
                textIn.setText("");
                buttonRemove = (Button)addView.findViewById(R.id.remove);

                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        /*info.append("thisListener called:\t" + this + "\n");
                        info.append("Remove addView: " + addView + "\n\n");*/
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        String result = textOut.getText()+"";
                        String newId=result.substring(0,result.indexOf(","));
                        removelist(newId);
                        /*listAllAddView();*/
                    }
                };

                buttonRemove.setOnClickListener(thisListener);
                container.addView(addView);

                /*destbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveHalt();
                    }
                });*/
                /*info.append(
                        "thisListener:\t" + thisListener + "\n"
                                + "addView:\t" + addView + "\n\n"
                );*/

                /*listAllAddView();*/
            }
        });
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println("inside onCreate "+mGoogleApiClient);
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            itemnew=item;
            //itemnew1.add((String) itemnew.placeId);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            // System.out.println("placeId"+item.placeId);
            System.out.println("place description"+item.description);
            // placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);

        }
    };

    public void saveDest(View view){
        db = new dbhelper(this);
        String mylist=nameList.getText().toString();
        id = (String)itemnew.description;
        db.addlist(id.substring(0,id.indexOf(",")),id,mylist);
    }

    public void saveHalt(){
        db = new dbhelper(this);
        String mylist=nameList.getText().toString();
        id = (String)itemnew.description;
        db.addlist(id.substring(0,id.indexOf(",")),id,mylist);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
        System.out.println("inside onCreate "+mGoogleApiClient);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }


    public void removelist(String id){
        db=new dbhelper(this);
        String mylist=nameList.getText().toString();
        System.out.println("inside removelist"+ id);
        //db.addlist(id.substring(0,id.indexOf(",")),id);
        db.deleteHalt(id,mylist);
    }
    /*private void listAllAddView(){
        *//*reList.setText("");*//*

        int childCount = container.getChildCount();
        for(int i=0; i<childCount; i++){
            View thisChild = container.getChildAt(i);
            *//*reList.append(thisChild + "\n");*//*

            AutoCompleteTextView childTextView = (AutoCompleteTextView) thisChild.findViewById(R.id.textout);
            String childTextViewValue = childTextView.getText().toString();
            reList.append("= " + childTextViewValue + "\n");
        }
    }*/

    public void addmylist(View view){
        //nameList = (EditText) findViewById(R.id.editText);
        String mylist=nameList.getText().toString();
        System.out.println(mylist);
        db = new dbhelper(this);
        //db.getWritableDatabase();
        db.createlist(mylist);
    }

    public void displayList(View view){
        System.out.println("hello in displayList");
        String mylist1=nameList.getText().toString();
        db = new dbhelper(this);
        Cursor mylist=db.getList(nameList.getText().toString());
        int j=0;
        mylist.moveToFirst();
        values=new ArrayList<>();
        //System.out.println(mylist.);
        // String [] from={"placeId","description"};
        // System.out.println(mylist.getString(0));
        ListView newList=(ListView)findViewById(R.id.List);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        System.out.println("outside while "+values);

        // Assign adapter to ListView
        newList.setAdapter(adapter);
/*        System.out.println("inside display");
        int[] to = { android.R.id.text1, android.R.id.text2};
        ListAdapter adapter= new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,mylist,from,to);


        newList.setAdapter(adapter);*/
    }

    public  void movetosave(View view){
        Intent myintent= new Intent(this,active.class);
        myintent.putExtra("list",nameList.getText().toString());
        myintent.putExtra("halts",values);
        setResult(RESULT_OK, myintent);
        finish();

    }


}
