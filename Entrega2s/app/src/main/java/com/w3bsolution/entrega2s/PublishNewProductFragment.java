package com.w3bsolution.entrega2s;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.w3bsolution.entrega2s.Domain.Category;
import com.w3bsolution.entrega2s.Domain.DataAccess;
import com.w3bsolution.entrega2s.Domain.Product;
import com.w3bsolution.entrega2s.Domain.Utils.UtilTools;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublishNewProductFragment extends Fragment {

    Spinner product_status, product_conditions, product_categories;
    DataAccess access;
    Button cancel_new_product_button, save_new_product_button, image_one_button, image_two_button;
    TextInputEditText product_title, product_price, product_warranty, product_description;
    ImageView image_one, image_two;
    //statics parameters
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    //    private static final String SAVE_PRODUCT_URL = "http://192.168.0.103/entrega2s.cabalgatavinales.com/ProductRegisterService.php";
    private static final String SAVE_PRODUCT_URL = "http://192.168.43.124/entrega2s.cabalgatavinales.com/ProductRegisterService.php";
    Uri filePathOne, filePathTwo;
    View v;
    Context context;
    Bitmap image_one_bit;
    Bitmap image_two_bit;
    boolean from_button_one = false, from_button_two = false;
    ProgressDialog dialog;
    private int user_id;
    private Product product;
    private List<Category> categoryList;
    private UtilTools tools;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publish_new_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = view;
        product = new Product();
        context = getContext();
        access = new DataAccess();
        tools = new UtilTools();
        dialog = new ProgressDialog(getContext());
        categoryList = new ArrayList<>();
        //getting selectors
        product_title = (TextInputEditText) view.findViewById(R.id.product_title);
        product_price = (TextInputEditText) view.findViewById(R.id.product_price);
        product_warranty = (TextInputEditText) view.findViewById(R.id.product_warranty);
        product_description = (TextInputEditText) view.findViewById(R.id.product_description);
        product_status = (Spinner) view.findViewById(R.id.product_status);
        product_conditions = (Spinner) view.findViewById(R.id.product_conditions);
        product_categories = (Spinner) view.findViewById(R.id.product_categories);
        image_one = (ImageView) view.findViewById(R.id.product_image_one);
        image_two = (ImageView) view.findViewById(R.id.product_image_two);
        /*image_one_button = (Button) view.findViewById(R.id.product_image_one_button);
        image_two_button = (Button) view.findViewById(R.id.product_image_two_button);*/
        cancel_new_product_button = (Button) view.findViewById(R.id.cancel_new_product_button);
        save_new_product_button = (Button) view.findViewById(R.id.save_new_product_button);


        //setting entries to spinners
        String[] states_array = new String[3];
        states_array[0] = "Desea publicar el producto?";
        states_array[1] = "Guardar y publicar";
        states_array[2] = "Solo guardar";
        PaintBookingStates(product_status, states_array);

        String[] conditions_array = new String[7];
        conditions_array[0] = "Seleccione el estado";
        conditions_array[1] = "Nuevo";
        conditions_array[2] = "1 mes de uso";
        conditions_array[3] = "3 mes de uso";
        conditions_array[4] = "6 mes de uso";
        conditions_array[5] = "1 año de uso";
        conditions_array[6] = "Más de 1 año de uso";
        PaintBookingStates(product_conditions, conditions_array);


        //getting values  from service
        new LoadProductCategory().execute("http://192.168.43.124/entrega2s.cabalgatavinales.com/GetAllCategoriesService.php?categorie_id=0");
//        new LoadProductCategory().execute("http://192.168.0.103/entrega2s.cabalgatavinales.com/GetAllCategoriesService.php?categorie_id=0");


        //setting buttons actions
        image_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from_button_one = true;
                from_button_two = false;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent, "Seleccionar foto"), COD_SELECCIONA);
            }
        });
        image_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from_button_one = false;
                from_button_two = true;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent, "Seleccionar foto"), COD_SELECCIONA);
            }
        });

        cancel_new_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartFragment startFragment = new StartFragment();
                OpenFragmentCalled(startFragment);
            }
        });

        save_new_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting shared information from LoginActivity
                user_id = getContext().getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getInt("logged_user_id", 0);
                product.setUser_id(user_id);
                if (ChekForProductReadyToBeSave()) {
                    String title = product_title.getText().toString();
                    product.setTitle(title);
                    double seller_price = Double.parseDouble(product_price.getText().toString());
                    product.setSeller_price(seller_price);
                    double price_to_show = ((seller_price * 10) / 100) + seller_price;
                    product.setPrice_to_show(price_to_show);
                    String description = product_description.getText().toString();
                    product.setDescription(description);
                    String category = product_categories.getSelectedItem().toString();
                    int category_id = 0;
                    for (Category c : categoryList) {
                        if (c.getCategory().equalsIgnoreCase(category)) {
                            category_id = c.getCategory_id();
                            break;
                        }
                    }
                    product.setCategory_id(category_id);
                    String product_state = product_status.getSelectedItem().toString();
                    product.setProduct_state(product_state);
                    String warranty = product_warranty.getText().toString();
                    product.setWarraty(warranty);
                    String condition = product_conditions.getSelectedItem().toString();
                    product.setProduct_condition(condition);
                    String image_one_string = "";
                    if (image_one_bit != null) {
                        Bitmap image_to_save = access.CompressImageBeforeSend(image_one_bit, 500, 500);
                        image_one_string = ConvertImageToString(image_to_save);
                        product.setProduct_image_one(image_one_string);
                    }
                    String image_two_string = "";
                    if (image_two_bit != null) {
                        Bitmap image_to_save = access.CompressImageBeforeSend(image_two_bit, 500, 500);
                        image_two_string = ConvertImageToString(image_to_save);
                        product.setProduct_image_two(image_two_string);
                    }
                    //sending url
                    new SaveNewProductService().execute(SAVE_PRODUCT_URL);
                    StartFragment startFragment = new StartFragment();
                    OpenFragmentCalled(startFragment);
                } else {
                    Toast.makeText(context, "Revise todos los campos del producto.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean ChekForProductReadyToBeSave() {
        boolean answer = true;
        if (TextUtils.isEmpty(product_title.getText().toString()) || TextUtils.isEmpty(product_warranty.getText().toString())
                || TextUtils.isEmpty(product_description.getText().toString())
                || (image_one_bit == null && image_two_bit == null)
                || TextUtils.isEmpty(product_conditions.getSelectedItem().toString())
                || TextUtils.isEmpty(product_status.getSelectedItem().toString()) || TextUtils.isEmpty(product_price.getText().toString())
        ) {
            answer = false;
        }
        return answer;
    }

    public void OpenFragmentCalled(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_home_products_list, fragment).commit();
    }


    private String ConvertImageToString(Bitmap image_bit) {
        dialog.setTitle("Espere...");
        dialog.setMessage("Estamos procesando las imágenes");
        dialog.show();
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        image_bit.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imageByte = array.toByteArray();
        String imageString = Base64.encodeToString(imageByte, Base64.DEFAULT);
        dialog.dismiss();
        return imageString;
    }

    public void PaintBookingStates(Spinner spinner, String[] states_array) {
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, states_array);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);
        spinner.setSelection(0);
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
            String[] answer = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                answer[i] = categoryList.get(i).getCategory();
            }
            if (answer == null) {
                Toast.makeText(getActivity(), "Ha ocurrido un error interno.", Toast.LENGTH_LONG).show();
            } else {
                PaintBookingStates(product_categories, answer);
            }

        }
    }


    private class SaveNewProductService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            HashMap<String, String> data = new HashMap<>();
            String token = tools.TokenGenerator();
            data.put("token", token);
            data.put("title", product.getTitle());
            data.put("description", product.getDescription());
            data.put("user_id", String.valueOf(product.getUser_id()));
            data.put("price", new DecimalFormat("0.00").format(product.getSeller_price()));
            data.put("price_to_show", new DecimalFormat("0.00").format(product.getPrice_to_show()));
            String state = "";
            if (product.getProduct_state().equalsIgnoreCase("guardar y publicar")) {
                state = "Publicado";
            } else {
                state = "Guardado";
            }
            data.put("product_state", state);
            data.put("warranty", product.getWarraty());
            data.put("product_conditions", product.getProduct_condition());
            data.put("product_category_id", String.valueOf(product.getCategory_id()));
            if (product.getProduct_image_one() != null) {
                data.put("product_photo_one", product.getProduct_image_one());
            } else {
                data.put("product_photo_one", "");
            }
            if (product.getProduct_image_two() != null) {
                data.put("product_photo_two", product.getProduct_image_two());
            } else {
                data.put("product_photo_two", "");
            }

            String res = access.SendInfoPostMethod(url, data);
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Procesando...");
            dialog.setMessage("Creando y publicando el nuevo producto");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String response = s.trim();
            Log.i("Response from server: ", response);
            dialog.dismiss();
            if (response.toLowerCase().contains("created")) {
                Toast.makeText(context, "Producto guardado correctamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "No se ha podido crear el producto", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case COD_SELECCIONA:
                Uri miPath = data.getData();
                if (from_button_one) {
                    from_button_one = false;
                    image_one.setBackgroundColor(Color.TRANSPARENT);
                    try {
                        image_one_bit = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), miPath);
                        image_one.setImageBitmap(image_one_bit);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (from_button_two) {
                    from_button_two = false;
                    image_two.setBackgroundColor(Color.TRANSPARENT);
                    try {
                        image_two_bit = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), miPath);
                        image_two.setImageBitmap(image_two_bit);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
