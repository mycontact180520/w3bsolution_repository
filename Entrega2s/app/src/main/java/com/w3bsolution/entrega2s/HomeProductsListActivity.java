package com.w3bsolution.entrega2s;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class HomeProductsListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPref;
    NavigationView navigationView;
    private FragmentManager manager;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_products_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(" ");
        toolbar.setSubtitle("");

        context = getApplicationContext();
        manager = getSupportFragmentManager();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        SetUpLogOutNavegationView(navigationView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sharedPref = getSharedPreferences("logged_user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("logged_user_id");
        editor.remove("logged_username");
        editor.remove("logged_role");
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    public void SetUpLogOutNavegationView(NavigationView navigationView) {
        navigationView.getMenu().findItem(R.id.publish_new_product_menu).setVisible(false);
        navigationView.getMenu().findItem(R.id.logout_user).setVisible(false);
        navigationView.getMenu().findItem(R.id.registered_user_profile).setVisible(false);
        navigationView.getMenu().findItem(R.id.unregister_login_user).setVisible(true);
        navigationView.setNavigationItemSelectedListener(this);
        manager.beginTransaction().replace(R.id.content_home_products_list, new StartFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SharedPreferences.Editor editor = sharedPref.edit();

        if (id == R.id.go_home) {
            manager.beginTransaction().replace(R.id.content_home_products_list, new StartFragment()).commit();
        }
        if (id == R.id.publish_new_product_menu) {
            manager.beginTransaction().replace(R.id.content_home_products_list, new PublishNewProductFragment()).commit();
        }

        if (id == R.id.go_out) {
            editor.putBoolean("out_from_application", true);
            Intent go_out = new Intent(Intent.ACTION_MAIN);
            startActivity(go_out);
            finish();
        }

        if (id == R.id.unregister_login_user) {
            manager.beginTransaction().replace(R.id.content_home_products_list, new StartFragment()).commit();
        }

        if (id == R.id.registered_user_profile) {
            manager.beginTransaction().replace(R.id.content_home_products_list, new StandardUserProfileFragment()).commit();
        }

        if (id == R.id.logout_user) {
            SetUpLogOutNavegationView(navigationView);
            editor.remove("logged_user_id");
            editor.remove("logged_username");
            editor.remove("logged_role");
            editor.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
