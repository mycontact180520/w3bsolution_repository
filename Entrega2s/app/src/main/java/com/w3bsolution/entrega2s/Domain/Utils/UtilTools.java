package com.w3bsolution.entrega2s.Domain.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;
import com.w3bsolution.entrega2s.Domain.Category;
import com.w3bsolution.entrega2s.Domain.Product;
import com.w3bsolution.entrega2s.Domain.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;

public class UtilTools {


    public Bitmap get_imagen(String url) {
        Bitmap bm = null;
        URL imageURL = null;
        try {
            imageURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageURL.openConnection();
            conn.connect();
            bm = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (IOException e) {
            Log.i("Error: ", e.getMessage());
        }
        return bm;
    }


    public void LoadImageWithPicasso(Context context, String url, ImageView photo) {
        Picasso.with(context)
                .load(url)
                .resize(100, 100)
                .centerCrop()
                .into(photo);
    }


    public void MakeRoundedPhoto(ImageView imageView, Resources resources, String path) {
        RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory.create(resources, path);
        rounded.setCornerRadius(imageView.getMaxHeight());
        imageView.setImageDrawable(rounded);

    }


    public AlertDialog.Builder MakeAlertDialog(final Context context, String window_type, User user) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(linearLayout.VERTICAL);
        switch (window_type.toLowerCase()) {
            case "personal_info":
                alertDialog.setTitle("Datos personales");
                alertDialog.setMessage("Puede editar sus datos");
                final EditText name = new EditText(context);
                linearLayout.setPadding(10, 10, 10, 10);
                if (user.getName() == null || user.getName().equalsIgnoreCase("")) {
                    name.setHint("Nombre");
                } else {
                    name.setText(user.getName());
                }
                linearLayout.addView(name);
                final EditText lastname = new EditText(context);
                if (user.getLastname() == null || user.getLastname().equalsIgnoreCase("")) {
                    lastname.setHint("Apellidos");
                } else {
                    lastname.setText(user.getLastname());
                }
                linearLayout.addView(lastname);
                final EditText phone = new EditText(context);
                if (user.getTelefono() == null || user.getTelefono().equalsIgnoreCase("")) {
                    phone.setHint("Teléfono");
                } else {
                    phone.setText(user.getTelefono());
                }
                linearLayout.addView(phone);
                final EditText movil = new EditText(context);
                if (user.getMovil() == null || user.getMovil().equalsIgnoreCase("")) {
                    movil.setHint("Celular");
                } else {
                    movil.setText(user.getMovil());
                }
                linearLayout.addView(movil);
                final EditText address = new EditText(context);
                if (user.getDireccion() == null || user.getDireccion().equalsIgnoreCase("")) {
                    address.setHint("Dirección");
                } else {
                    address.setText(user.getDireccion());
                }
                linearLayout.addView(address);
                final EditText municipio = new EditText(context);
                if (user.getMunicipio() == null || user.getMunicipio().equalsIgnoreCase("")) {
                    municipio.setHint("Municipio");
                } else {
                    municipio.setText(user.getMunicipio());
                }
                linearLayout.addView(municipio);
                final EditText province = new EditText(context);
                if (user.getProvincia() == null || user.getProvincia().equalsIgnoreCase("")) {
                    province.setHint("Provincia");
                } else {
                    province.setText(user.getProvincia());
                }
                linearLayout.addView(province);
                break;
        }
        alertDialog.setView(linearLayout);
        return alertDialog;
    }

    public int DetermineImageRotation(String imagePath) {
        int rotation = 0;
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotation;
    }

    public float getImageRotation(Uri path, Context context) {
        float rotate = 0f;
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(path, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnindex = cursor.getColumnIndex(filePathColumn[0]);
            rotate = cursor.getInt(columnindex);
            cursor.close();
        } catch (Exception e) {
            return 0f;
        }
        return rotate;
    }

    public String TokenGenerator() {
        String token = "";
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        token = bytes.toString();
        return token;
    }


}
