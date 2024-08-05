package com.dabakarov10.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class Dynamic_item_Page extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView txt_eventName, txt_eventCountUsers, txt_eventDate, txt_eventCode, txt_eventDescription;
    private ListView listView;
    private ArrayList<String> list;
    private Adapter adapter;
    private EditText userInput, userInputCount;
    private Button button_SendItem;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefList;
    private ArrayList<DataModel> dataModels = new ArrayList<>();
    private String EventName, EventCountUsers, EventDate, EventCode, EventDescription;
    private ImageView img_CopyToClipboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_item_page);
        init();
    }

    public void init() {
        txt_eventName = findViewById(R.id.eventName);
        txt_eventCountUsers = findViewById(R.id.eventCountUsers);
        txt_eventDate = findViewById(R.id.eventDate);
        txt_eventCode = findViewById(R.id.eventCode);
        txt_eventDescription = findViewById(R.id.eventDescription);
        listView = findViewById(R.id.listView);
        userInput = findViewById(R.id.userInput);
        userInputCount = findViewById(R.id.userInputCount);
        button_SendItem = findViewById(R.id.button_SendItem);

        img_CopyToClipboard = findViewById(R.id.img_CopyToClipboard);

        Intent intent = getIntent();

        EventName = intent.getStringExtra("EventName");
        EventCountUsers = intent.getStringExtra("EventCountUsers");
        EventDate = intent.getStringExtra("EventDate");
        EventCode = intent.getStringExtra("EventCode");
        EventDescription = intent.getStringExtra("EventDescription");

        EventCode = intent.getStringExtra("EventCode");

        myRefList = database.getReference("EventsList").child(EventCode).child("itemsListDM");
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        getDataModelsList();
        button_SendItem.setOnClickListener(this);
        img_CopyToClipboard.setOnClickListener(this);

        listView.setAdapter((ListAdapter) adapter);
        listView.setOnItemClickListener(this);

        txt_eventName.setText(EventName);
        txt_eventCountUsers.setText(EventCountUsers + " משתתפים");
        txt_eventDate.setText("תאריך: " + EventDate);
        txt_eventCode.setText("קןד האירוע: " + EventCode);
        txt_eventDescription.setText("פרטי האירוע: " + EventDescription);

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
                        if (!name.equals("!@#Ignore!@#")) {
                            dataModels.add(new DataModel(name, checked));
                            list.add(name);
                        }
                    }
                    listView.setAdapter((ListAdapter) adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "*Error*", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == button_SendItem) {
            if (!userInput.getText().toString().trim().equals("")) {
                if (new Validators().check_number_presence(userInputCount.getText().toString().trim())) {
                    dataModels.add(new DataModel(User.name + ": \n" + userInputCount.getText().toString().trim() + " " + userInput.getText().toString(), false));
                    myRefList.setValue(dataModels);

                } else {
                    dataModels.add(new DataModel(User.name + ": \n" + 1 + " " + userInput.getText().toString(), false));
                    myRefList.setValue(dataModels);
                }
                userInput.setText("");
                userInputCount.setText("");
                list.add(dataModels.get(dataModels.size() - 1).name);
                listView.setAdapter((ListAdapter) adapter);
            }
        }
        if (view == img_CopyToClipboard) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", txt_eventCode.getText().toString().substring(txt_eventCode.getText().toString().indexOf(":")+2));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "קוד האירוע הועתק", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //  Toast.makeText(getApplicationContext(), "item: " + (i + 1) + "\n" + list.get(i), Toast.LENGTH_LONG).show();
        if (User.name.equals(list.get(i).substring(0, list.get(i).indexOf("\n") - 2))) {
            AlertDialog alertbox = new AlertDialog.Builder(Dynamic_item_Page.this)
                    .setMessage("למחוק את הפריט?")
                    .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            list.remove(i);
                            dataModels.remove(i);
                            myRefList.setValue(dataModels);
                            listView.setAdapter((ListAdapter) adapter);
                            Toast.makeText(getApplicationContext(), "הפריט נמחק בהצלחה", Toast.LENGTH_LONG).show();

                        }
                    })
                    .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })
                    .show();
        } else
            Toast.makeText(getApplicationContext(), "אין לך גישה לשנות פריט זה", Toast.LENGTH_LONG).show();
    }
}