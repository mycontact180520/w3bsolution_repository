package com.w3bsolution.entrega2s;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.w3bsolution.entrega2s.Domain.User;

import java.util.zip.Inflater;

public class StartActivity extends AppCompatActivity {

    Button autenticar_button, registrar_user, go_to_store;
    TextView link;
    private boolean online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        registrar_user = (Button) findViewById(R.id.register_button);
        go_to_store = (Button) findViewById(R.id.go_to_store);
        autenticar_button = (Button) findViewById(R.id.autenticar_button);

        online = myClickHandler(StartActivity.this.getCurrentFocus());

        if (online) {

            link = (TextView) findViewById(R.id.home_link);
            link.setMovementMethod(LinkMovementMethod.getInstance());

            //implementando la accion del boton registrar
            autenticar_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (online) {
                        Intent register_activity = new Intent("com.w3bsolution.entrega2s.LoginActivity");
                        startActivity(register_activity);
                    } else {
                        Toast.makeText(StartActivity.this, "Lo sentimos, no tiene conección a internet.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            registrar_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (online) {
                        Intent register_activity = new Intent("com.w3bsolution.entrega2s.RegisterActivity");
                        startActivity(register_activity);
                    } else {
                        Toast.makeText(StartActivity.this, "Lo sentimos, no tiene conección a internet.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            go_to_store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (online) {
                        Intent go_to_store_activity = new Intent("com.w3bsolution.entrega2s.HomeProductsListActivity");
                        startActivity(go_to_store_activity);
                    } else {
                        Toast.makeText(StartActivity.this, "Lo sentimos, no tiene conección a internet.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(StartActivity.this, "Lo sentimos, no tiene conección a internet.", Toast.LENGTH_LONG).show();
        }

    }


    public boolean myClickHandler(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
