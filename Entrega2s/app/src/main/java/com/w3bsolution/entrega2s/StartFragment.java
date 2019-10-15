package com.w3bsolution.entrega2s;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.w3bsolution.entrega2s.Domain.Roles;

import java.util.Date;

public class StartFragment extends Fragment {

    Button autenticar_button, registrar_user, go_to_store;
    //    TextView link;
    private boolean online;
    TextView welcome_signature, welcome_status_time;
    private String logged_username = "", role = "";
    private int user_id;
    private static SharedPreferences sharedPref;
    private static Context context;
    private View v;
    NavigationView navigationView;
    private FragmentActivity activity;
    private FragmentManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        manager = getActivity().getSupportFragmentManager();
        activity = getActivity();
        v = view;
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        sharedPref = context.getSharedPreferences("logged_user_data", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        compatActivity.setSupportActionBar(toolbar);
        toolbar.setTitle(" ");
        toolbar.setSubtitle("");

        welcome_signature = (TextView) view.findViewById(R.id.welcome_signature);
        welcome_status_time = (TextView) view.findViewById(R.id.welcome_status_time);

        //cambiando en tiempo de ejecucion el cartel de bienvenida
        Date date = new Date();
        String date_string = date.toString();
        String[] date_array = date_string.split(" ");
        String[] time_array = date_array[3].split(":");
        int hour = Integer.parseInt(time_array[0]);
        String welcome_signature_string = "Buenos días!";
        if (hour >= 0 && hour < 12) {
            welcome_signature_string = "Buenos días!";
        } else if (hour >= 12 && hour < 20) {
            welcome_signature_string = "Buenas tardes!";
        } else if (hour >= 20) {
            welcome_signature_string = "Buenas noches!";
        }
        welcome_status_time.setText(welcome_signature_string);

        welcome_signature.setVisibility(View.INVISIBLE);
        welcome_signature.setText("");
        logged_username = "";

        registrar_user = (Button) view.findViewById(R.id.register_button);
        go_to_store = (Button) view.findViewById(R.id.go_to_store);
        autenticar_button = (Button) view.findViewById(R.id.autenticar_button);

        //checking user logged
        checkForLoggedUser();

        online = true;
//        online = myClickHandler(view);

        autenticar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (online) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 200);
                } else {
                    Toast.makeText(getContext(), "Lo sentimos, no tiene conexión a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });
        registrar_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (online) {
                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Lo sentimos, no tiene conexión a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });
        go_to_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (online) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_home_products_list, new FullProductListFragment()).commit();
                } else {
                    Toast.makeText(getContext(), "Lo sentimos, no tiene conexión a internet.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //llenando el menu en caliente
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (logged_username.length() <= 0) {
            if (role.equalsIgnoreCase(Roles.CLIENT.toString())) {
                if (menu != null) {
                    menu.findItem(R.id.publish_new_product_menu).setVisible(false);
                }
            } else if (role.equalsIgnoreCase(Roles.ADMIN.toString())) {

            } else if (role.equalsIgnoreCase(Roles.SUPERADMIN.toString())) {

            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void checkForLoggedUser() {
        if (context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getInt("logged_user_id", 0) > 0) {
            user_id = context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getInt("logged_user_id", 0);
            logged_username = context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getString("logged_username", "");
            role = context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getString("logged_role", "");
            welcome_signature.setVisibility(View.VISIBLE);
            welcome_signature.setText("Hola " + logged_username + ".");
            FixItemsMenu();
            HideUnhideFrontButtons(true);
        }
    }

    public void HideUnhideFrontButtons(boolean hide) {
        if (hide) {
            autenticar_button.setVisibility(v.GONE);
            registrar_user.setVisibility(v.GONE);
        } else {
            autenticar_button.setVisibility(v.VISIBLE);
            registrar_user.setVisibility(v.VISIBLE);
        }
    }

    public void FixItemsMenu() {
        if (role.equalsIgnoreCase(Roles.CLIENT.toString())) {
            //editando los items del drawable menu de acorde al usuario conectado
            navigationView.getMenu().findItem(R.id.unregister_login_user).setVisible(false);
            navigationView.getMenu().findItem(R.id.publish_new_product_menu).setVisible(true);
            navigationView.getMenu().findItem(R.id.logout_user).setVisible(true);
            navigationView.getMenu().findItem(R.id.registered_user_profile).setVisible(true);

        } else if (role.equalsIgnoreCase(Roles.ADMIN.toString())) {

        } else if (role.equalsIgnoreCase(Roles.SUPERADMIN.toString())) {

        }

        //updating menu
        setHasOptionsMenu(true);

    }

    //revisando la conectividad
    public boolean myClickHandler(View view) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 || resultCode == 200) {
            if (context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getBoolean("get_back", true)) {
                welcome_signature.setVisibility(View.INVISIBLE);
                setHasOptionsMenu(true);
            } else if (context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getInt("logged_user_id", 0) <= 0) {
                welcome_signature.setVisibility(View.INVISIBLE);
                setHasOptionsMenu(true);
            } else {
                user_id = context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getInt("logged_user_id", 0);
                logged_username = context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getString("logged_username", "");
                role = context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getString("logged_role", "");
                welcome_signature.setVisibility(View.VISIBLE);
                welcome_signature.setText("Hola " + logged_username + ".");
                //fixing menu
                FixItemsMenu();
                //fixing front page buttons
                HideUnhideFrontButtons(true);
            }
        }
    }
}

