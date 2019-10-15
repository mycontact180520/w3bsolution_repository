package com.w3bsolution.entrega2s;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.w3bsolution.entrega2s.Adapters.BasicProductsGridView;
import com.w3bsolution.entrega2s.Domain.Category;
import com.w3bsolution.entrega2s.Domain.DataAccess;
import com.w3bsolution.entrega2s.Domain.Product;
import com.w3bsolution.entrega2s.Domain.Utils.UtilTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FullProductListFragment extends Fragment {

    SharedPreferences sharedPref;
    private static int user_id;
    private static Context context;
    private boolean logged_user = false;
    //    private final static String ALL_PRODUCTS_URL = "http://192.168.43.124/entrega2s.cabalgatavinales.com/GetAllProductsOrByProductIdService.php";
//    private final static String ALL_PRODUCTS_BY_USERID_URL = "http://192.168.43.124/entrega2s.cabalgatavinales.com/GetAllProductsByUserIdService.php?";
    private final static String ALL_PRODUCTS_BY_ID_URL = "http://192.168.43.124/entrega2s.cabalgatavinales.com/GetAllProductsOrByProductIdService.php?";
    private final static String LOAD_ALL_CATEGORIES = "http://192.168.43.124/entrega2s.cabalgatavinales.com/GetAllCategoriesService.php?categorie_id=0";
    /*private final static String ALL_PRODUCTS_BY_ID_URL = "http://192.168.0.103/entrega2s.cabalgatavinales.com/GetAllProductsOrByProductIdService.php?";
    private final static String ALL_PRODUCTS_BY_USERID_URL = "http://192.168.0.103/entrega2s.cabalgatavinales.com/GetAllProductsByUserIdService.php?";
    private final static String LOAD_ALL_CATEGORIES = "http://192.168.0.103/entrega2s.cabalgatavinales.com/GetAllCategoriesService.php?categorie_id=0";*/
    private ProgressDialog dialog;
    private DataAccess access;
    private List<Category> categoryList;
    private List<Product> productList;
    private UtilTools tools;
    private GridView all_products_grid_view;
    private static View view;
    private static LinearLayout first_relative;
    private Button first_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unregistered_product_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        sharedPref = getContext().getSharedPreferences("logged_user_data", MODE_PRIVATE);
        dialog = new ProgressDialog(context);
        categoryList = new ArrayList<>();
        productList = new ArrayList<>();
        access = new DataAccess();
        tools = new UtilTools();
        this.view = view;
        all_products_grid_view = (GridView) view.findViewById(R.id.all_products_grid_view);
        first_relative = (LinearLayout) view.findViewById(R.id.first_relative);
        first_button = (Button) view.findViewById(R.id.first_button);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        compatActivity.setSupportActionBar(toolbar);
        toolbar.setTitle("Tienda");
        toolbar.setSubtitle("");
        setHasOptionsMenu(true);
        first_relative.setFocusable(true);

        //checking for logged user
        if (context.getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getInt("logged_user_id", 0) > 0) {
            user_id = getContext().getSharedPreferences("logged_user_data", MODE_PRIVATE).getInt("logged_user_id", 0);
            logged_user = true;
        }

        //loading categories
        new LoadProductCategory().execute(LOAD_ALL_CATEGORIES);
        //loading products
        new LoadAllProductsFromDB().execute(ALL_PRODUCTS_BY_ID_URL + "product_id=0");

        //changing actions according user status
        if (logged_user) {
            first_relative.setVisibility(view.GONE);
            PaintProductList();
        } else {
            first_relative.setVisibility(view.VISIBLE);
            first_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void PaintProductList() {
        if (!productList.isEmpty()) {
            List<Product> product_to_show = new ArrayList<>();
            for (Product p : productList) {
                if (p.getProduct_state().equalsIgnoreCase("publicado")) {
                    if (logged_user && p.getUser_id() != user_id) {
                        product_to_show.add(p);
                    } else {
                        product_to_show.add(p);
                    }
                }
            }
            BasicProductsGridView gridAdapter = new BasicProductsGridView(product_to_show, categoryList, context, logged_user);
            all_products_grid_view.setAdapter(gridAdapter);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        if (logged_user) {
            inflater.inflate(R.menu.logged_user_search_menu, menu);
        } else {
            inflater.inflate(R.menu.search_menu, menu);
        }
    }


    //calling collect methods
    private class LoadAllProductsFromDB extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Espere...");
            dialog.setMessage("Cargando los productos");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return access.DownloadURL(strings[0]);
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            productList = access.getAllProductsList(s);
            dialog.dismiss();
            PaintProductList();
        }
    }

    private class LoadProductCategory extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                return access.DownloadURL(strings[0]);
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            categoryList = access.getCategoryList(s);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        productList.clear();
        //loading products
        new LoadAllProductsFromDB().execute(ALL_PRODUCTS_BY_ID_URL + "product_id=0");
    }
}
