package com.dabakarov10.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Exists_item_Page extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private TextView txt_eventName, txt_eventCountUsers, txt_eventDate, txt_eventCode, txt_eventDescription;
    private String EventName, EventCountUsers, EventDate, EventCode, EventDescription;
    private ArrayList<DataModel> dataModels;
    private CustomAdapter adapter;
    private ListView listView;
    private Bundle extras;
    private DatabaseReference myRefList;
    private ImageView img_CopyToClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exists_item_page);
        init();
    }

    public void init() {
        txt_eventName = findViewById(R.id.eventName);
        txt_eventCountUsers = findViewById(R.id.eventCountUsers);
        txt_eventDate = findViewById(R.id.eventDate);
        txt_eventCode = findViewById(R.id.eventCode);
        txt_eventDescription = findViewById(R.id.eventDescription);
        listView = findViewById(R.id.listView);

        img_CopyToClipboard = findViewById(R.id.img_CopyToClipboard);

        Intent intent = getIntent();
        EventName = intent.getStringExtra("EventName");
        EventCountUsers = intent.getStringExtra("EventCountUsers");
        EventDate = intent.getStringExtra("EventDate");
        EventCode = intent.getStringExtra("EventCode");
        EventDescription = intent.getStringExtra("EventDescription");
        adapter = new CustomAdapter(dataModels, getApplicationContext());

        myRefList = FirebaseDatabase.getInstance().getReference("EventsList").child(EventCode).child("itemsListDM");
        dataModels = new ArrayList<>();
        extras = getIntent().getExtras();

        txt_eventName.setText(EventName);
        txt_eventCountUsers.setText(EventCountUsers + " משתתפים");
        txt_eventDate.setText("תאריך: " + EventDate);
        txt_eventCode.setText("קןד האירוע: " + EventCode);
        txt_eventDescription.setText("פרטי האירוע: " + EventDescription);
        img_CopyToClipboard.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        getDataModelsList();
    }

    public void getDataModelsList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("EventsList").child(EventCode).child("itemsListDM");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Boolean checked = postSnapshot.child("checked").getValue(Boolean.class);
                        String name = postSnapshot.child("name").getValue(String.class);
                        dataModels.add(new DataModel(name, checked));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                makeAList(dataModels);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "*Error*", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void makeAList(ArrayList<DataModel> dataModels) {
        if (dataModels.size() == 0) {
            Toast.makeText(getApplicationContext(), "*empty*", Toast.LENGTH_LONG).show();
        }
        adapter = new CustomAdapter(dataModels, getApplicationContext());
        listView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataModel dataModel = dataModels.get(position);
        if (dataModel.checked != true) {
            dataModel.checked = !dataModel.checked;
            dataModel.name = User.name + ": \n" + dataModel.name;
            myRefList.setValue(dataModels);
            adapter.notifyDataSetChanged();
        } else if (User.name.equals(dataModel.name.substring(0, dataModel.name.indexOf("\n") - 2))) {
            dataModel.checked = !dataModel.checked;
            dataModel.name = dataModel.name.substring(dataModel.name.indexOf("\n") + 1);
            myRefList.setValue(dataModels);
            adapter.notifyDataSetChanged();
        } else
            Toast.makeText(getApplicationContext(), "אין לך גישה לשנות פריט זה", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view == img_CopyToClipboard) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", txt_eventCode.getText().toString().substring(txt_eventCode.getText().toString().indexOf(":")+2));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "קוד האירוע הועתק", Toast.LENGTH_LONG).show();
        }
    }
}