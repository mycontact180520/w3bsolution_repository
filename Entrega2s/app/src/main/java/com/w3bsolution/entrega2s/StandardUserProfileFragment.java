package com.w3bsolution.entrega2s;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.w3bsolution.entrega2s.Domain.CreditCard;
import com.w3bsolution.entrega2s.Domain.DataAccess;
import com.w3bsolution.entrega2s.Domain.User;

import com.w3bsolution.entrega2s.Domain.Utils.UtilTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class StandardUserProfileFragment extends Fragment {

    int user_id;
    TextView fa_camera_view, logged_user_username, logged_user_points;
    TextView logged_user_personal_details_fa_one, logged_user_personal_details_fa_two, logged_user_personal_details_fa_three;
    TextView fa_angle_one, fa_angle_two, fa_angle_three;
    TextView logged_user_personal_details_fa_section_one, logged_user_personal_details_fa_section_two;
    TextView fa_angle_section_one, fa_angle_section_two;
    UtilTools tools;

    LinearLayout personal_data_linearLayout, card_details_LinearLayout, user_products_LinearLayout, user_password_LinearLayout, user_log_out_LinearLayout;

    DataAccess access;
    Context context;
    User user;
    CreditCard user_credit_card;
    View v;
    ImageView user_photo;
    ProgressDialog dialog;
    Button save_user_profile_update, cancel_user_profile_update;
    //for user image
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

//    private final static String UPDATE_USER_URL = "http://192.168.0.103/entrega2s.cabalgatavinales.com/UpdateUserProfilePostMethod.php";
    private final static String UPDATE_USER_URL = "http://192.168.43.124/entrega2s.cabalgatavinales.com/UpdateUserProfilePostMethod.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_standar_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = new User();
        access = new DataAccess();
        context = getContext();
        dialog = new ProgressDialog(context);
        v = view;
        tools = new UtilTools();

        logged_user_username = (TextView) view.findViewById(R.id.logged_user_username);
        logged_user_points = (TextView) view.findViewById(R.id.logged_user_points);
        user_photo = (ImageView) view.findViewById(R.id.user_profile_image);
        fa_camera_view = (TextView) view.findViewById(R.id.fa_camera_user_profile);
        logged_user_personal_details_fa_one = (TextView) view.findViewById(R.id.logged_user_personal_details_fa_one);
        logged_user_personal_details_fa_two = (TextView) view.findViewById(R.id.logged_user_personal_details_fa_two);
        logged_user_personal_details_fa_three = (TextView) view.findViewById(R.id.logged_user_personal_details_fa_three);
        logged_user_personal_details_fa_section_one = (TextView) view.findViewById(R.id.logged_user_personal_details_fa_section_one);
        logged_user_personal_details_fa_section_two = (TextView) view.findViewById(R.id.logged_user_personal_details_fa_section_two);
        fa_angle_one = (TextView) view.findViewById(R.id.fa_angle_one);
        fa_angle_two = (TextView) view.findViewById(R.id.fa_angle_two);
        fa_angle_three = (TextView) view.findViewById(R.id.fa_angle_three);
        fa_angle_section_one = (TextView) view.findViewById(R.id.fa_angle_section_one);
        fa_angle_section_two = (TextView) view.findViewById(R.id.fa_angle_section_two);
        save_user_profile_update = (Button) view.findViewById(R.id.save_user_profile_update);
        cancel_user_profile_update = (Button) view.findViewById(R.id.cancel_user_profile_update);
        personal_data_linearLayout = (LinearLayout) view.findViewById(R.id.personal_data_linearLayout);
        user_password_LinearLayout = (LinearLayout) view.findViewById(R.id.user_password_LinearLayout);
        card_details_LinearLayout = (LinearLayout) view.findViewById(R.id.card_details_LinearLayout);


        //setting fonts to add icons
        Typeface fonts = Typeface.createFromAsset(getActivity().getAssets(), "fonts/fontawesome-webfont.ttf");
        //setting icons
        fa_camera_view.setTypeface(fonts);
        logged_user_personal_details_fa_one.setTypeface(fonts);
        logged_user_personal_details_fa_two.setTypeface(fonts);
        logged_user_personal_details_fa_three.setTypeface(fonts);
        logged_user_personal_details_fa_section_one.setTypeface(fonts);
        logged_user_personal_details_fa_section_two.setTypeface(fonts);
        fa_angle_one.setTypeface(fonts);
        fa_angle_two.setTypeface(fonts);
        fa_angle_three.setTypeface(fonts);
        fa_angle_section_one.setTypeface(fonts);
        fa_angle_section_two.setTypeface(fonts);

        //getting shared information from LoginActivity
        user_id = getContext().getSharedPreferences("logged_user_data", context.MODE_PRIVATE).getInt("logged_user_id", 0);

        //calling get logged user information service
//        String get_user_info_query = "http://192.168.0.103/entrega2s.cabalgatavinales.com/BasicUserGetInfoByUserIdService.php?user_id=" + user_id;
        String get_user_info_query = "http://192.168.43.124/entrega2s.cabalgatavinales.com/BasicUserGetInfoByUserIdService.php?user_id=" + user_id;
        new LoadLoggedUser().execute(get_user_info_query.trim());
        //getting user credit card if hi has
        /*if(user.getCard_id() > 0){
            String get_user_credit_card_query = "http://192.168.43.124/entrega2s.cabalgatavinales.com/GetUserCreditCardService.php?id=" + user.getCard_id();
        }*/


        //setting onclick funtion for a camera
        fa_camera_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFileChooser();
            }
        });

        //setting onclick cancel button
        cancel_user_profile_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartFragment startFragment = new StartFragment();
                OpenFragmentCalled(startFragment);
            }
        });

        //setting onclick save button
        save_user_profile_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    Bitmap image_to_save = access.CompressImageBeforeSend(bitmap, 100, 100);
                    String image_string = access.getStringImage(image_to_save);
                    user.setImage_url(image_string);
                }
                new UpdateUserProfile().execute(UPDATE_USER_URL);
                StartFragment startFragment = new StartFragment();
                OpenFragmentCalled(startFragment);
            }
        });


        //setting personal data click
        personal_data_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Datos personales");
                alertDialog.setMessage("Puede editar sus datos");
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(linearLayout.VERTICAL);
                linearLayout.setPadding(10, 10, 10, 10);
                final EditText name = new EditText(getContext());
                if (user.getName() == null || user.getName().equalsIgnoreCase("")) {
                    name.setHint("Nombre");
                } else {
                    name.setText(user.getName());
                }
                linearLayout.addView(name);
                final EditText lastname = new EditText(getContext());
                if (user.getLastname() == null || user.getLastname().equalsIgnoreCase("")) {
                    lastname.setHint("Apellidos");
                } else {
                    lastname.setText(user.getLastname());
                }
                linearLayout.addView(lastname);
                final EditText phone = new EditText(getContext());
                if (user.getTelefono() == null || user.getTelefono().equalsIgnoreCase("")) {
                    phone.setHint("Teléfono");
                } else {
                    phone.setText(user.getTelefono());
                }
                linearLayout.addView(phone);
                final EditText movil = new EditText(getContext());
                if (user.getMovil() == null || user.getMovil().equalsIgnoreCase("")) {
                    movil.setHint("Celular");
                } else {
                    movil.setText(user.getMovil());
                }
                linearLayout.addView(movil);
                final EditText address = new EditText(getContext());
                if (user.getDireccion() == null || user.getDireccion().equalsIgnoreCase("")) {
                    address.setHint("Dirección");
                } else {
                    address.setText(user.getDireccion());
                }
                linearLayout.addView(address);
                final EditText municipio = new EditText(getContext());
                if (user.getMunicipio() == null || user.getMunicipio().equalsIgnoreCase("")) {
                    municipio.setHint("Municipio");
                } else {
                    municipio.setText(user.getMunicipio());
                }
                linearLayout.addView(municipio);
                final EditText province = new EditText(getContext());
                if (user.getProvincia() == null || user.getProvincia().equalsIgnoreCase("")) {
                    province.setHint("Provincia");
                } else {
                    province.setText(user.getProvincia());
                }
                linearLayout.addView(province);

                alertDialog.setView(linearLayout);

                //changing buttons action
                alertDialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String _name = name.getText().toString();
                        String _lastname = lastname.getText().toString();
                        String _phone = phone.getText().toString();
                        String _movil = movil.getText().toString();
                        String _address = address.getText().toString();
                        String _municipio = municipio.getText().toString();
                        String _province = province.getText().toString();
                        //comparing to set user values
                        if (!TextUtils.isEmpty(_name)) {
                            user.setName(_name);
                        }
                        if (!TextUtils.isEmpty(_lastname)) {
                            user.setLastname(_lastname);
                        }
                        if (!TextUtils.isEmpty(_phone)) {
                            user.setTelefono(_phone);
                        }
                        if (!TextUtils.isEmpty(_movil)) {
                            user.setMovil(_movil);
                        }
                        if (!TextUtils.isEmpty(_address)) {
                            user.setDireccion(_address);
                        }
                        if (!TextUtils.isEmpty(_province)) {
                            user.setProvincia(_province);
                        }
                        if (!TextUtils.isEmpty(_municipio)) {
                            user.setMunicipio(_municipio);
                        }
                        Toast.makeText(getContext(), "Información almacenada para actualizar", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(getContext(), "La información no ha sido almacenada.", Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();
            }
        });

        //changing action tu user_password_LinearLayout
        user_password_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setTitle("Contraseña");
                alertBuilder.setMessage("Cambiar contraseña");
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(10, 10, 10, 10);

                final TextView current_pass = new EditText(getContext());
                current_pass.setHint("Contraseña actual");
                current_pass.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                layout.addView(current_pass);
                final TextView new_pass = new EditText(getContext());
                new_pass.setHint("Nueva contraseña");
                new_pass.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                layout.addView(new_pass);
                final TextView repeat_pass = new EditText(getContext());
                repeat_pass.setHint("Repetir contraseña");
                repeat_pass.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                layout.addView(repeat_pass);
                alertBuilder.setView(layout);

                //changing buttons action
                alertBuilder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //make sure three inputs are not empty
                        if (TextUtils.isEmpty(current_pass.getText().toString()) && TextUtils.isEmpty(new_pass.getText().toString()) && TextUtils.isEmpty(repeat_pass.getText().toString())) {
                            Toast.makeText(getContext(), "Debe llenar los tres campos.", Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(current_pass.getText().toString())) {
                            Toast.makeText(getContext(), "La contraseña actual no puede estar en blanco.", Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(new_pass.getText().toString()) || TextUtils.isEmpty(repeat_pass.getText().toString())) {
                            Toast.makeText(getContext(), "Los campos de nueva contraseña no pueden estar en blanco.", Toast.LENGTH_LONG).show();
                        } else if (current_pass.getText().toString().equals(user.getPassword())) {
                            if (new_pass.getText().toString().equals(repeat_pass.getText().toString())) {
                                user.setPassword(new_pass.getText().toString());
                                Toast.makeText(getContext(), "Nueva contraseña lista para ser almacenada.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "La nueva contraseña no coincide.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Los contraseña actual es incorrecta.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertBuilder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "La información no ha sido almacenada.", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
                alertBuilder.show();
            }
        });

        //setting card details LinearLayout click
        card_details_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int card_id = user.getCard_id();

                if (card_id <= 0) {
                    Toast.makeText(context, "Usted no tiene tarjeta asociada a su cuenta.", Toast.LENGTH_LONG).show();
                }else{
                    final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                    alertBuilder.setTitle("Tarjeta de compra");
                    alertBuilder.setMessage("Detalles de su tarjeta");
                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(10, 10, 10, 10);


                    final EditText card_number = new EditText(context);
//                    if()
                }
            }
        });


    }


    public void OpenFragmentCalled(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_home_products_list, fragment).commit();
    }

    public void ShowFileChooser() {
        Intent chooser = new Intent();
        chooser.setType("image/*");
        chooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(chooser.createChooser(chooser, "Seleccione su foto de perfil"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                user_photo.setVisibility(v.VISIBLE);
                Picasso.with(context)
                        .load(filePath)
                        .resize(100, 100)
                        .centerCrop()
                        .into(user_photo);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Ha ocurrido un error mientras se actualizaba el usuario.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //update user profile calling service
    private class UpdateUserProfile extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Procesando...");
            dialog.setMessage("Actualizando información del usuario");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Log.i("Answer: ", s);
            System.out.println("Answer: " + s);
            if (!s.trim().equalsIgnoreCase("Error User updated")) {
                Toast.makeText(context, "Datos actualizados correctamente.", Toast.LENGTH_LONG).show();
                Picasso.with(context)
                        .load(user.getImage_url())
                        .resize(100, 100)
                        .centerCrop()
                        .into(user_photo);
            } else {
                Toast.makeText(context, "No se pudo actualizar el usuario.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            //calling update user profile service
            String url = strings[0];
            HashMap<String, String> data = new HashMap<>();
            data.put("user_id", String.valueOf(user.getId()));
            data.put("username", user.getUsername());
            data.put("email", user.getEmail().trim());
            data.put("password", user.getPassword());
            data.put("user_image", user.getImage_url());
            data.put("user_role", user.getRole());
            data.put("name", user.getName());
            data.put("lastname", user.getLastname());
            data.put("phone", user.getTelefono());
            data.put("celular", user.getMovil());
            data.put("address", user.getDireccion());
            data.put("municipio", user.getMunicipio());
            data.put("provincia", user.getProvincia());
            data.put("store_name", user.getStore_name());
            data.put("store_detail", user.getStore_detail());
            data.put("store_raiting", String.valueOf(user.getStore_rating()));
            data.put("card_id", String.valueOf(user.getCard_id()));

            String res = access.SendInfoPostMethod(url, data);

            return res;
        }
    }

    private class LoadLoggedUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setTitle("Procesando...");
            dialog.setMessage("Obteniendo información del usuario");
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
            JSONArray answer = access.ReadUserInfo(s);
            if (answer == null) {
                Toast.makeText(context, "No se ha podido obtener la información de usuario.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject jsonObject = answer.getJSONObject(0);
                    user_id = jsonObject.getInt("user_id");
                    String username = jsonObject.getString("username");
                    String email = jsonObject.getString("email");
                    String password = jsonObject.getString("password");
                    String role = jsonObject.getString("role");
                    String name = jsonObject.getString("name");
                    String lastname = jsonObject.getString("lastname");
                    String phone = jsonObject.getString("phone");
                    String movil = jsonObject.getString("movil");
                    String address = jsonObject.getString("address");
                    String province = jsonObject.getString("province");
                    String municipio = jsonObject.getString("municipio");
                    String store_name = jsonObject.getString("store_name");
                    String store_detail = jsonObject.getString("store_detail");
                    double store_rating = jsonObject.getDouble("store_rating");
                    int card_id = jsonObject.getInt("card_id");
                    String image_url = jsonObject.getString("user_image");

                    //setting up values
                    logged_user_username.setText(username);
                    user = new User(user_id, name, username, role, lastname, email, password, phone, movil, address, province, municipio, store_name, store_detail, store_rating, card_id, image_url);
                    setUser(user);

                    //setting values on the view
                    SetUserValuesToTheView(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dialog.dismiss();
        }
    }


    public void SetUserValuesToTheView(User u) throws IOException {
        logged_user_points.setText(String.valueOf(u.getStore_rating()) + " pts");
        //revisando el perfil del usuario para saber si tiene foto.
        if (u.getImage_url() == null || u.getImage_url().equalsIgnoreCase("")) {
            user_photo.setVisibility(v.GONE);
        } else {
            /*Uri uri = Uri.parse(u.getImage_url());
            Uri path = uri.buildUpon().build();*/
            //remove this rotation line to publish
            int rotation = tools.DetermineImageRotation(u.getImage_url());
            Picasso.with(context)
                    .load(u.getImage_url())
                    .fit()
                    .centerCrop()
                    .rotate(rotation)
                    .into(user_photo);
            user_photo.setVisibility(v.VISIBLE);
        }
    }
}
