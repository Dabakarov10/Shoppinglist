package com.dabakarov10.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class HomePage extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private Button btn_createEvent, btn_addEventToList;
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<String> EventsCode = new ArrayList<String>();
    private TextView txtV_caption;
    private EventCustomAdapter adapter;
    private ListView listView;
    private EditText txt_addEvent;
    public User user = new User();
    private String userUID;
    private boolean flag = false;
    private Event event = new Event();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRefList, myRefListCountUsers;
    private TextView battery;
    private ImageView img_to_AlarmPage;
    private int countUsers;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            if (level >= 50)
                battery.setTextColor(Color.GREEN);
            else if (level < 50 && level > 15)
                battery.setTextColor(Color.rgb(255, 193, 5));
            else
                battery.setTextColor(Color.RED);

            battery.setText("אחוזי סוללה:  " + String.valueOf(level) + "%");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        init();
        setUserInfo();
        getEventsListCodes();
    }

    public void init() {
        btn_addEventToList = (Button) findViewById(R.id.btn_addEventToList);
        btn_createEvent = (Button) findViewById(R.id.btn_createEvent);
        txt_addEvent = (EditText) findViewById(R.id.txt_addEvent);
        listView = (ListView) findViewById(R.id.eventsList);
        txtV_caption = findViewById(R.id.caption);
        img_to_AlarmPage = findViewById(R.id.img_to_AlarmPage);
        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRefList = database.getReference("users").child(userUID).child("userEvents");
        myRefListCountUsers = database.getReference("EventsList").child(txt_addEvent.getText().toString());

        battery = this.findViewById(R.id.text1);
        this.registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        btn_addEventToList.setOnClickListener(this);
        btn_createEvent.setOnClickListener(this);
        img_to_AlarmPage.setOnClickListener(this);

        adapter = new EventCustomAdapter(events, getApplicationContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

        listView.setOnItemLongClickListener(this);
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if (btn_addEventToList == view) {
            if (!txt_addEvent.getText().toString().equals("")) {
                eventIsRealyExists();

            } else
                Toast.makeText(getApplicationContext(), "יש להכניס קוד אירוע", Toast.LENGTH_LONG).show();
        }
        if (btn_createEvent == view) {
            startActivity(new Intent(HomePage.this, Create_Event_Page.class));
            finish();
        }
        if (view == img_to_AlarmPage) {
            startActivity(new Intent(getApplicationContext(), Alarm_Notification_Page.class));
        }
    }

    public void addCountUsers() {
        String codee = txt_addEvent.getText().toString();
        countUsers = -1;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("EventsList").child(codee).child("eventCountUsers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String str_countUsers = dataSnapshot.getValue(String.class);
                    countUsers = Integer.valueOf(str_countUsers);
                    countUsers++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FirebaseDatabase.getInstance().getReference("EventsList").child(codee)
                        .child("eventCountUsers").setValue(String.valueOf(countUsers));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "*Error*", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void SubtractCountUsers(String codee) {
        countUsers = -1;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("EventsList").child(codee).child("eventCountUsers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String str_countUsers = dataSnapshot.getValue(String.class);
                    countUsers = Integer.valueOf(str_countUsers);
                    countUsers--;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FirebaseDatabase.getInstance().getReference("EventsList").child(codee)
                        .child("eventCountUsers").setValue(String.valueOf(countUsers));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "*Error*", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setEventsListCodeView() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("EventsList");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (int i = 0; i < EventsCode.size(); i++) {
                        event.setEventCode(dataSnapshot.child(EventsCode.get(i)).child("eventCode").getValue(String.class));
                        event.setEventName(dataSnapshot.child(EventsCode.get(i)).child("eventName").getValue(String.class));
                        event.setEventDate(dataSnapshot.child(EventsCode.get(i)).child("eventDate").getValue(String.class));
                        event.setEventCountUsers(dataSnapshot.child(EventsCode.get(i)).child("eventCountUsers").getValue(String.class));
                        event.setEventDescription(dataSnapshot.child(EventsCode.get(i)).child("eventDescription").getValue(String.class));
                        event.setEvent_type(dataSnapshot.child(EventsCode.get(i)).child("event_type").getValue(String.class));

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("EventsList").child(EventsCode.get(i)).child("itemsListDM");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        Boolean checked = postSnapshot.child("checked").getValue(Boolean.class);
                                        String name = postSnapshot.child("name").getValue(String.class);
                                        event.itemsListDM.add(new DataModel(name, checked));
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
                        events.add(new Event(event));
                        listView.setAdapter((ListAdapter) adapter);

                    }
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

    public void getEventsListCodes() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUID).child("userEvents");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String Code = postSnapshot.getValue(String.class);
                        if (!Code.equals("Events codes"))
                            EventsCode.add(Code);
                    }
                    setEventsListCodeView();
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

    public void addUserEventsList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    addCountUsers();
                    User user = new User(dataSnapshot.getValue(User.class));
                    user.userEvents.add(txt_addEvent.getText().toString());
                    databaseReference.setValue(user);
                    EventsCode.add(txt_addEvent.getText().toString());
                    txt_addEvent.setText("");
                    Toast.makeText(HomePage.this, "האירוע התווסף בהצלחה", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(HomePage.this, HomePage.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "*Error*", Toast.LENGTH_LONG).show();
            }
        });
        flag = true;
    }

    public void eventIsRealyExists() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("EventsList").child(txt_addEvent.getText().toString());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.child("eventCode").getValue(String.class).equals(txt_addEvent.getText().toString())) {
                        addUserEventsList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (flag == false) {
                    Toast.makeText(HomePage.this, "האירוע אינו קיים", Toast.LENGTH_LONG).show();
                } else flag = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "*Error*", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        //close();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomePage.this, LogInPage.class));
                finish();
                return true;
            case R.id.userProfile:
                startActivity(new Intent(HomePage.this, UserProfilePage.class));
                return true;
            case R.id.refResh:
                startActivity(new Intent(HomePage.this, HomePage.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void delEventFromfirebase(String str) {
        for (int i = 0; i < EventsCode.size(); i++) {
            if (str.equals(EventsCode.get(i)))
                EventsCode.remove(i);
        }
        myRefList.setValue(EventsCode);
    }

    public void setUserInfo() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = new User(dataSnapshot.getValue(User.class));
                    txtV_caption.setText("שלום "+User.name+"!");
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

    /*                  click to extends item                   */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (events.get(position).getEvent_type().equals("EXISTS_ITEM")) {

            Intent myIntent = new Intent(HomePage.this, Exists_item_Page.class);
            myIntent.putExtra("EventName", events.get(position).getEventName());
            myIntent.putExtra("EventCountUsers", events.get(position).getEventCountUsers());
            myIntent.putExtra("EventDate", events.get(position).getEventDate());
            myIntent.putExtra("EventCode", events.get(position).getEventCode());
            myIntent.putExtra("EventDescription", events.get(position).getEventDescription());
            myIntent.putExtra("EventDescription", events.get(position).getEventDescription());
            startActivity(myIntent);
        } else {
            Intent myIntent = new Intent(HomePage.this, Dynamic_item_Page.class);
            myIntent.putExtra("EventName", events.get(position).getEventName());
            myIntent.putExtra("EventCountUsers", events.get(position).getEventCountUsers());
            myIntent.putExtra("EventDate", events.get(position).getEventDate());
            myIntent.putExtra("EventCode", events.get(position).getEventCode());
            myIntent.putExtra("EventDescription", events.get(position).getEventDescription());
            myIntent.putExtra("EventDescription", events.get(position).getEventDescription());
            startActivity(myIntent);
        }
    }

    /*                 long click to del item                      */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //  Toast.makeText(getApplicationContext(), "item: " + (i + 1) + "\n" + list.get(i), Toast.LENGTH_LONG).show();
        AlertDialog alertbox = new AlertDialog.Builder(HomePage.this)
                .setMessage("למחוק את הפריט?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SubtractCountUsers(events.get(position).getEventCode());
                        delEventFromfirebase(events.get(position).getEventCode());
                        events.remove(position);
                        //myRefList.setValue(dataModels);// not in this page.
                        listView.setAdapter((ListAdapter) adapter);
                        Toast.makeText(getApplicationContext(), "הפריט נמחק בהצלחה", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
        return true;
    }

}