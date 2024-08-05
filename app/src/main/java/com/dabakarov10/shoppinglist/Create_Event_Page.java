package com.dabakarov10.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Create_Event_Page extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {
    private ListView listView_items;
    private DatePickerDialog picker;
    private TextView alert;
    private EditText txt_EventDate, txt_EventName, txt_eventDescription, userInputItems;
    private Button btn_send, button_SendItem;
    private int day, month, year;
    private Switch aSwitch;
    private Event event;
    private Adapter adapter;
    private ArrayList<String> list;
    private ArrayList<DataModel> dataModels = new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefList;
    private String eventCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_page);
        init();
    }

    public void init() {
        txt_EventDate = (EditText) findViewById(R.id.txt_EventDate);
        txt_EventName = (EditText) findViewById(R.id.txt_EventName);
        txt_eventDescription = (EditText) findViewById(R.id.txt_eventDescription);
        alert = findViewById(R.id.alertTextBox);
        listView_items = findViewById(R.id.listView_items);
        userInputItems = findViewById(R.id.userInputItems);
        button_SendItem = findViewById(R.id.button_SendItem);
        aSwitch = findViewById(R.id.switch1);
        btn_send = (Button) findViewById(R.id.btn_send);

        eventCode = new Utilities().getAlphaNumericString();
        myRefList = database.getReference("EventsList").child(eventCode);

        button_SendItem.setVisibility(View.GONE);
        userInputItems.setVisibility(View.GONE);
        listView_items.setVisibility(View.GONE);

        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        listView_items.setAdapter((ListAdapter) adapter);

        listView_items.setOnItemClickListener(this);
        button_SendItem.setOnClickListener(this);
        txt_EventDate.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        aSwitch.setOnClickListener(this);
        listView_items.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        if (txt_EventDate == v) {
            final Calendar cldr = Calendar.getInstance();
            day = cldr.get(Calendar.DAY_OF_MONTH);
            month = cldr.get(Calendar.MONTH);
            year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(Create_Event_Page.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    txt_EventDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }, year, month, day);
            picker.show();
        }
        if (aSwitch == v) {
            if (aSwitch.isChecked()) {
                listView_items.setVisibility(View.VISIBLE);
                button_SendItem.setVisibility(View.VISIBLE);
                userInputItems.setVisibility(View.VISIBLE);
            }
            if (!aSwitch.isChecked()) {
                listView_items.setVisibility(View.GONE);
                button_SendItem.setVisibility(View.GONE);
                userInputItems.setVisibility(View.GONE);
            }
        }
        if (btn_send == v) {
            String OutPutAlert = new Validators().check_allValidators(txt_EventDate.getText().toString(), txt_EventName.getText().toString(), txt_eventDescription.getText().toString());
            if (OutPutAlert.equals("")) {
                alert.setVisibility(View.GONE);
                if (aSwitch.isChecked()) {
                    event = new Event(txt_EventName.getText().toString(), "1", txt_EventDate.getText().toString(), eventCode, txt_eventDescription.getText().toString(), "EXISTS_ITEM", dataModels);
                } else {
                    dataModels.add(new DataModel("!@#Ignore!@#", false));
                    event = new Event(txt_EventName.getText().toString(), "1", txt_EventDate.getText().toString(), eventCode, txt_eventDescription.getText().toString(), "DYNAMIC_ITEM", dataModels);
                }
                addUserEventsList();
                startActivity(new Intent(Create_Event_Page.this, HomePage.class));
                myRefList.setValue(event);
                finish();
            } else {
                alert.setVisibility(View.VISIBLE);
                alert.setText(OutPutAlert);
            }
        }

        if (button_SendItem == v) {
            if (!userInputItems.getText().toString().trim().equals("")) {
                dataModels.add(new DataModel(userInputItems.getText().toString(), false));

                userInputItems.setText("");
                list.add(dataModels.get(dataModels.size() - 1).name);
                listView_items.setAdapter((ListAdapter) adapter);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //  Toast.makeText(getApplicationContext(), "item: " + (i + 1) + "\n" + list.get(i), Toast.LENGTH_LONG).show();
        AlertDialog alertbox = new AlertDialog.Builder(Create_Event_Page.this)
                .setMessage("למחוק את הפריט?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        list.remove(i);
                        dataModels.remove(i);
                        listView_items.setAdapter((ListAdapter) adapter);
                        Toast.makeText(getApplicationContext(), "הפריט נמחק בהצלחה", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    public void addUserEventsList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = new User(dataSnapshot.getValue(User.class));
                    user.userEvents.add(eventCode);
                    databaseReference.setValue(user);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            finish();
            //moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Disallow ScrollView to intercept touch events.
                v.getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_UP:
                // Allow ScrollView to intercept touch events.
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        // Handle ListView touch events.
        v.onTouchEvent(event);
        return true;

    }
}