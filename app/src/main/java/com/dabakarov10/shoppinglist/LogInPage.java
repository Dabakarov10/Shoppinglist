package com.dabakarov10.shoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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

public class LogInPage extends AppCompatActivity implements View.OnClickListener {
    private Button button_SighUp, button_SighIn;
    private TextView alertTextBox;
    private EditText InputTextLogIn_email, InputTextLogIn_Password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        button_SighUp = findViewById(R.id.button_SighUp);
        button_SighIn = findViewById(R.id.button_SighIn);
        InputTextLogIn_email = findViewById(R.id.InputTextLogIn_email);
        InputTextLogIn_Password = findViewById(R.id.InputTextLogIn_Password);
        alertTextBox = findViewById(R.id.alertTextBox);
        mAuth = FirebaseAuth.getInstance();

        button_SighIn.setOnClickListener(this);
        button_SighUp.setOnClickListener(this);
        SharedPreferences prfs = getSharedPreferences("sharedPreferences_logIn", Context.MODE_PRIVATE);
        InputTextLogIn_email.setText(prfs.getString("email", ""));
        InputTextLogIn_Password.setText(prfs.getString("password", ""));
        onStart();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Toast.makeText(this, "You Signed In successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, HomePage.class));
            finish();

        } else {
            Toast.makeText(this, "You Didnt signed in", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == button_SighIn) {
            String OutPutalertTextBox = new Validators().check_allValidators(InputTextLogIn_Password.getText().toString(), InputTextLogIn_email.getText().toString().trim());
            if (OutPutalertTextBox.equals("")) {
                alertTextBox.setVisibility(View.GONE);
                mAuth.signInWithEmailAndPassword(InputTextLogIn_email.getText().toString().trim(), InputTextLogIn_Password.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LogInPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            } else {
                alertTextBox.setVisibility(View.VISIBLE);
                alertTextBox.setText(OutPutalertTextBox);
            }
        }
        if (view == button_SighUp) {
            startActivity(new Intent(getApplicationContext(), SighUpPage.class));
        }

    }
}