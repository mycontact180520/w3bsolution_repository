package com.w3bsolution.entrega2s;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.w3bsolution.entrega2s.Domain.DataAccess;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, email, password;
    private Button register_user_button;
    private Spinner roles;
    String _username;
    String _email;
    String _password;

    private ProgressDialog progressDialog;
    private DataAccess access;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Entrega2s");
        toolbar.setSubtitle("Registrar usuario");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));

        progressDialog = new ProgressDialog(this);
        access = new DataAccess();

        //getting data fields;
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);


        //getting button to asign actions
        register_user_button = (Button) findViewById(R.id.register_user_button);

        register_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(RegisterActivity.this, "Complete los campos del formulario.", Toast.LENGTH_SHORT).show();
                } else if (password.getText().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                } else {
                    _username = username.getText().toString();
                    _email = email.getText().toString();
                    _password = password.getText().toString();
                    UserRegister(_username, _email, _password);
                }
            }
        });
    }

    public void UserRegister(String username, String email, String password) {
        //all registration code goes here
//        new SaveRegisterDataFunction().execute("http://entrega2s.cabalgatavinales.com/BasicUserRegisterService.php?username=" + username + "&email=" + email + "&password=" + password + "&role=client");
//        new SaveRegisterDataFunction().execute("http://192.168.0.103/entrega2s.cabalgatavinales.com/BasicUserRegisterService.php?username=" + username + "&email=" + email + "&password=" + password + "&role=client");
        new SaveRegisterDataFunction().execute("http://192.168.43.124/entrega2s.cabalgatavinales.com/BasicUserRegisterService.php?username=" + username + "&email=" + email + "&password=" + password + "&role=client");
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private class SaveRegisterDataFunction extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return access.DownloadURL(urls[0]);
            } catch (IOException e) {
                Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
