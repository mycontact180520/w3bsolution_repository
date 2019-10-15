package com.w3bsolution.entrega2s.Domain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.w3bsolution.entrega2s.Domain.Product;
import com.w3bsolution.entrega2s.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ale on 8/16/2019.
 */

public class DataAccess {

    private User logged_user;

    public DataAccess() {

    }

    public User getLogged_user() {
        return logged_user;
    }

    public String SendInfoPostMethod(String requestURL, HashMap<String, String> postDataParameter) {
        URL url;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParameter));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i("Error: ", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Error: ", e.getMessage());
        }
        return sb.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        }
        return result.toString();
    }

    public String DownloadURL(String myurl) throws IOException {
        Log.i("URL", myurl);
        myurl = myurl.replace(" ", "%20");
        InputStream is = null;

        int len = 99999;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Answer", "Response from server: " + response);
            is = conn.getInputStream();
            String contectAsString = readIt(is, len);
            return contectAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public Bitmap CompressImageBeforeSend(Bitmap bitmap, float new_with, float new_height) {
        int with = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (with > new_with || height > new_height) {
            float scale_with = new_with / with;
            float scale_height = new_height / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scale_with, scale_height);
            return Bitmap.createBitmap(bitmap, 0, 0, with, height, matrix, false);
        } else {
            return bitmap;
        }
    }

    public static final String ReturnImagesFilePath(String path) {
        final String file_path = path;
        File image_file = new File(file_path);
        if (!image_file.exists()) {
            image_file.mkdir();
            image_file.canRead();
            image_file.canWrite();
        }
        return file_path;
    }

    public List<Product> getAllProductsList(String s) {
        List<Product> productList = new ArrayList<>();
        JSONArray response = null;
        JSONObject productjson = null;
        try {
            response = new JSONArray(s);
            if (response != null) {
                for (int i = 0; i < response.length(); i++) {
                    productjson = response.getJSONObject(i);
                    String object = productjson.getString("product");
                    JSONArray final_array = new JSONArray(object);
                    for (int j = 0; j < final_array.length(); j++) {
                        JSONObject final_object = final_array.getJSONObject(j);
                        int product_id = final_object.getInt("product_id");
                        String product_token = final_object.getString("product_token");
                        String title = final_object.getString("title");
                        String description = final_object.getString("description");
                        int user_id = final_object.getInt("user_id");
                        double seller_price = final_object.getDouble("seller_price");
                        double price_to_show = final_object.getDouble("price_to_show");
                        String product_state = final_object.getString("product_state");
                        String warranty = final_object.getString("warranty");
                        String product_conditions = final_object.getString("product_conditions");
                        int product_category_id = final_object.getInt("product_category_id");
                        String product_photo_one = final_object.getString("product_photo_one");
                        String product_photo_two = final_object.getString("product_photo_two");
                        String created_date = final_object.getString("created_date").toString();
                        Product product = new Product(product_id, product_token, title, description, user_id, seller_price, price_to_show, product_state, warranty, product_conditions, product_category_id, product_photo_one, product_photo_two);
                        productList.add(product);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public List<Category> getCategoryList(String s) {
        List<Category> categories = new ArrayList<>();
        JSONArray response = null;
        JSONObject categoryJson = null;
        try {
            response = new JSONArray(s.trim());
            if (response != null) {
                for (int i = 0; i < response.length(); i++) {
                    categoryJson = response.getJSONObject(i);
                    int category_id = categoryJson.getInt("category_id");
                    String category = categoryJson.getString("category");
                    categories.add(new Category(category_id, category));
                }
            }
        } catch (JSONException e) {
            Log.i("Error: ", e.getMessage());
        }
        return categories;
    }

    /*public String[] ReadCategoriesInfo(String s) {
        String[] answer = null;
        JSONArray response = null;
        JSONObject categoryJson = null;
        try {
            response = new JSONArray(s.trim());
            answer = new String[response.length()];
            if (response != null) {
                for (int i = 0; i < response.length(); i++) {
                    categoryJson = response.getJSONObject(i);
                    int category_id = categoryJson.getInt("category_id");
                    String category = categoryJson.getString("category");
                    answer[i] = category;

                }
            }
        } catch (JSONException e) {
            Log.i("Error: ", e.getMessage());
        }
        return answer;
    }*/

    public JSONArray ReadUserInfo(String s) {
        JSONArray answer = null;
        JSONObject logged_user = null;
        JSONArray user = null;
        try {
            answer = new JSONArray(s);
            if (answer != null) {
                //all code here to get the user data
                for (int i = 0; i < answer.length(); i++) {
                    logged_user = answer.getJSONObject(i);
                    break;
                }
                String object = logged_user.getString("user");
                user = new JSONArray(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }


    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public StringBuilder inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                answer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    //encoding image to save into a database
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}
