package com.colman.fit_me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.colman.fit_me.ui.user_profile.UserProfileViewModel;


public class SignUpActivity extends AppCompatActivity {

    private EditText emailId;
    private EditText password;
    private Button signUpButton;
    private TextView tvSignIn;
    private ProgressBar pgsBar;
    private UserProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

        emailId = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        signUpButton = findViewById(R.id.signUpButton);
        tvSignIn = findViewById(R.id.tvSignIn);
        pgsBar = findViewById(R.id.progress_circular);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgsBar.setVisibility(v.VISIBLE);
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    viewModel.signUp(email, pwd, data -> {
                        if(data)
                        {
                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            finish();
                            pgsBar.setVisibility(v.INVISIBLE);
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            pgsBar.setVisibility(v.INVISIBLE);
                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                    pgsBar.setVisibility(v.INVISIBLE);
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
