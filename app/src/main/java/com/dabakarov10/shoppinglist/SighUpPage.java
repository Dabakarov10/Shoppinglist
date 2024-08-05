package com.dabakarov10.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SighUpPage extends AppCompatActivity implements View.OnClickListener {
    private TextView caption, alert;
    private Button button_send;
    private EditText InputTextSighUp_Username, InputTextSighUp_Password, InputTextSighUp_email, InputTextSighUp_phone;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigh_up_page);
        init();
        onStart();
    }

    private void init() {
        caption = findViewById(R.id.caption);
        alert = findViewById(R.id.alertTextBox);
        button_send = findViewById(R.id.button_send);
        InputTextSighUp_Username = findViewById(R.id.InputTextSighUp_Username);
        InputTextSighUp_Password = findViewById(R.id.InputTextSighUp_Password);
        InputTextSighUp_email = findViewById(R.id.InputTextSighUp_email);
        InputTextSighUp_phone = findViewById(R.id.InputTextSighUp_phone);

        mAuth = FirebaseAuth.getInstance();// חיבור משתמשים

        mDatabase = FirebaseDatabase.getInstance().getReference("users"); // חיבור למסד נתונים

        button_send.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == button_send) {
            String OutPutAlert = new Validators().check_allValidators(InputTextSighUp_Password.getText().toString(), InputTextSighUp_Username.getText().toString().trim(), InputTextSighUp_email.getText().toString().trim(), InputTextSighUp_phone.getText().toString());
            if (OutPutAlert.equals("")) {
                alert.setVisibility(View.GONE);

                mAuth.createUserWithEmailAndPassword(InputTextSighUp_email.getText().toString().trim(), InputTextSighUp_Password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);

                                    /***!!!***/ mDatabase = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                     ArrayList<String> arrayList = new ArrayList<>();
                                     /*****/ arrayList.add("Events codes");
                                    mDatabase.setValue(new User(InputTextSighUp_Username.getText().toString().trim(),
                                                                InputTextSighUp_email.getText().toString().trim(),
                                                                InputTextSighUp_Password.getText().toString(),
                                                                InputTextSighUp_phone.getText().toString(), user.getUid(),arrayList ));

                                    SharedPreferences sp_logIn = getSharedPreferences("sharedPreferences_logIn", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor_logIn = sp_logIn.edit();
                                    editor_logIn.putString("email", InputTextSighUp_email.getText().toString());
                                    editor_logIn.putString("password", InputTextSighUp_Password.getText().toString());
                                    editor_logIn.apply();
                                    Toast.makeText(SighUpPage.this, "היי " + InputTextSighUp_Username.getText() + ", נרשמת בהצלחה!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SighUpPage.this, LogInPage.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SighUpPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });

            } else {
                alert.setVisibility(View.VISIBLE);
                alert.setText(OutPutAlert);
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, "You Signed up successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, HomePage.class));

        } else {
            Toast.makeText(this, "You Didnt signed up", Toast.LENGTH_LONG).show();
        }
    }
}