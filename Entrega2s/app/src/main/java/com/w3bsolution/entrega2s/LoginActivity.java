package com.w3bsolution.entrega2s;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.w3bsolution.entrega2s.Domain.DataAccess;
import com.w3bsolution.entrega2s.Domain.Roles;
import com.w3bsolution.entrega2s.Domain.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText login_email, login_password;
    Button login_action;

    private ProgressDialog progressBar;
    DataAccess access;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = getSharedPreferences("logged_user_data", Context.MODE_PRIVATE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Entrega2s");
        toolbar.setSubtitle("Iniciar sesión");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));


        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);
        login_action = (Button) findViewById(R.id.login_action);

        //remove this two lines
        login_email.setText("alew3bslution@gmail.com");
        login_password.setText("aledeulo");

        progressBar = new ProgressDialog(this);
        access = new DataAccess();
        login_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_email.getText().toString();
                String password = login_password.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(view, "Debe introducir una dirección de correo.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (TextUtils.isEmpty(password)) {
                    Snackbar.make(view, "Introduzca la contraseña.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Snackbar.make(view, "Debe introducir una dirección de correo.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
//                    new LoadRegisterDataFunction().execute("http://entrega2s.cabalgatavinales.com/UserLoginService.php?email=" + email + "&password=" + password);
//                        new LoadRegisterDataFunction().execute("http://192.168.0.103/entrega2s.cabalgatavinales.com/UserLoginService.php?email=" + email + "&password=" + password);
                        new LoadRegisterDataFunction().execute("http://192.168.43.124/entrega2s.cabalgatavinales.com/UserLoginService.php?email=" + email + "&password=" + password);

                    }
                    progressBar.dismiss();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("get_back", true);
        editor.commit();
        Intent intent = new Intent();
        setResult(200, intent);
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    private class LoadRegisterDataFunction extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return access.DownloadURL(urls[0]);
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            progressBar.setTitle("Espere unos segundos.");
            progressBar.setMessage("Autenticando usuario...");
            progressBar.setCancelable(false);
            progressBar.show();

        }

        @Override
        protected void onPostExecute(String s) {
            JSONArray answer = access.ReadUserInfo(s);
            try {
                if (answer == null) {
                    Toast.makeText(getBaseContext(), "El usuario no se pudo autenticar", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jsonObject = answer.getJSONObject(0);
                    int user_id = jsonObject.getInt("user_id");
                    String username = jsonObject.getString("username");
                    String role = jsonObject.getString("role");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("logged_user_id", user_id);
                    editor.putString("logged_username", username);
                    editor.putString("logged_role", role);
                    editor.putBoolean("get_back", false);
                    editor.commit();
                    Intent intent = new Intent();
                    setResult(200, intent);
                    progressBar.dismiss();
                    finish();
                }
            } catch (JSONException e) {
                Toast.makeText(getBaseContext(), "El usuario no se pudo autenticar. Revise sus credenciales.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

}
