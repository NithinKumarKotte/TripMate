package com.example.d27sa.tripmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by d27sa on 02-04-2017.
 */
public class active extends Fragment {
    String value;
    static int count;
    ArrayList<CheckBox>mycheck=new ArrayList<CheckBox>();
    ListView listView;
    static{
        count=1;
    }
    ArrayList<String> halts=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activetab, container, false);

        return rootView;


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listView=(ListView)getView().findViewById(R.id.activelist);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                value = data.getStringExtra("list");
                halts = data.getStringArrayListExtra("halts");
                CheckBox mycheckBox=new CheckBox(this.getContext());
                mycheckBox.setText(value);
                mycheckBox.setId(count);
                mycheckBox.setClickable(true);
                RelativeLayout ll = (RelativeLayout)getView().findViewById(R.id.active);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if(count!=0){
                    lp.addRule(RelativeLayout.BELOW,mycheckBox.getId());
                    ll.addView(mycheckBox, lp);
                }
                else{
                    lp.addRule(RelativeLayout.BELOW,R.id.section_label );
                    ll.addView(mycheckBox, lp);
                }
                count++;
                mycheck.add(mycheckBox);
                ArrayAdapter<CheckBox> adapter = new ArrayAdapter<CheckBox>(getContext(), android.R.layout.simple_selectable_list_item);
                listView.setAdapter(adapter);

            }
        }
    }
}
