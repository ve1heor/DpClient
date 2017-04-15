package com.example.absurd.dpclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.absurd.dpclient.application.DBHelper;
import com.example.absurd.dpclient.R;
import com.example.absurd.dpclient.activity.ElementActivity;
import com.example.absurd.dpclient.adapters.ActivAdapter;
import com.example.absurd.dpclient.containers.ActivContainer;

import java.util.ArrayList;

public class ActivFragment extends Fragment {
    Intent intent;
    ArrayList<ActivContainer> arrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DBHelper dbHelper = new DBHelper(getActivity());
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        ListView listView = (ListView)view.findViewById(R.id.listView);

        arrayList = new ArrayList<ActivContainer>();
        arrayList = dbHelper.getAllActiv();
        listView.setAdapter(new ActivAdapter(getActivity(),arrayList));
        listView.setDividerHeight(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView codeActiv = (TextView) view.findViewById(R.id.textCodeActiv);
                TextView nameActiv = (TextView) view.findViewById(R.id.textNameActiv);
                TextView shtrihCode = (TextView) view.findViewById(R.id.textShtrihCodeActiv);
                //Bitmap bitmap =(Bitmap) imageView.get;
                intent = new Intent(getActivity(),ElementActivity.class);
                intent.putExtra("flag",false);
                intent.putExtra("code",codeActiv.getText());
                intent.putExtra("nameActiv",nameActiv.getText());
                intent.putExtra("shtrihCode",shtrihCode.getText());
                for(ActivContainer a: arrayList){
                    if(a.getCode()==codeActiv.getText())
                        intent.putExtra("photo",a.getPhoto());
                }
                startActivity(intent);
            }
        });
        return view;//inflater.inflate(R.layout.fragment_second, container, false);
    }
}