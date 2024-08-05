package com.dabakarov10.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserProfilePage extends AppCompatActivity {
    private TextView user_Username, user_Password, user_email, user_phone, caption;
    private User user = new User();
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);
        init();
        setUserInfo();
    }

    public void init() {
        user_Username = findViewById(R.id.TextSighUp_Username);
        user_Password = findViewById(R.id.TextSighUp_Password);
        user_email = findViewById(R.id.TextSighUp_email);
        user_phone = findViewById(R.id.TextSighUp_phone);
        caption = findViewById(R.id.caption);
        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setUserInfo() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userUID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = new User(dataSnapshot.getValue(User.class));
                    user_Username.setText(user_Username.getText().toString() + user.getName());
                    user_Password.setText(user_Password.getText().toString() + user.getPassword());
                    user_email.setText(user_email.getText().toString() + user.getEmail());
                    user_phone.setText(user_phone.getText().toString() + user.getPhone());
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


}