package com.example.asaditasgourmet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrarActivity extends AppCompatActivity   {

    private EditText nombre, apellido, telefono,  usuario, password, repassword;
    private Spinner sexo;
    private ImageView imagen;
    private Button registrar;
    private TextView texto;
    private ProgressDialog pDialog;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String currentPhotoPath;
    Uri photoURI;

    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";
    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;
    private static final int MY_PERMISSIONS_REQUEST_CAMARA = 0;
    String pathx;
    String path = GlobalInfo.PATH_IP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
/*
        AndPermission
                .with(this)
                .permission(Permission.READ_EXTERNAL_STORAGE,Permission.WRITE_EXTERNAL_STORAGE)
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //ÃƒÂ¦Ã¢â‚¬Â¹Ã¢â‚¬â„¢ÃƒÂ§Ã‚Â»Ã‚ÂÃƒÂ¦Ã‚ÂÃ†â€™ÃƒÂ©Ã¢â€žÂ¢Ã‚Â
                        //   DialogUtil.showPermissionDialog(MainActivity.this,Permission.transformText(MainActivity.this, permissions).get(0));
                    }
                })
                .start();*/



        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        nombre = findViewById(R.id.Nombre);
        apellido = findViewById(R.id.Apellido);
        telefono = findViewById(R.id.Telefono);
        sexo = findViewById(R.id.Sexo);
        usuario = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        repassword = findViewById(R.id.Recontrasema);

        imagen = findViewById(R.id.image_flag);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        texto = (TextView) findViewById(R.id.btn_login);
        texto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        registrar = (Button) findViewById(R.id.btn_new_register);
        registrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(nombre.getText().toString().isEmpty() || apellido.getText().toString().isEmpty() || telefono.getText().toString().isEmpty() || usuario.getText().toString().isEmpty()){
                    nombre.setError("Campo es requerido");
                    apellido.setError("Campo es requerido");
                    telefono.setError("Campo es requerido");
                    usuario.setError("Campo es requerido");
                    password.setError("Campo es requerido");
                }else{
                  if(password.getText().toString().equals(repassword.getText().toString())) {
                    registrar(nombre.getText().toString(),
                              apellido.getText().toString(),telefono.getText().toString(),
                              sexo.getSelectedItem().toString() ,usuario.getText().toString(),
                              password.getText().toString(),repassword.getText().toString()
                      );
                      Toast.makeText(getApplicationContext(), "Registrado con exito", Toast.LENGTH_LONG).show();
                  }else{
                    new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Campos contraseña no coinciden!")
                        .show();
                  }
                }
            }
        });


        imagen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cargarImagen();
             }
        });
    }



    private void cargarImagen() {
        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    abriCamara();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la AplicaciÃƒÂ³n"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
    }


    private void abriCamara() {
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1) {// Marshmallow+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                } else {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMARA );
                }
            }else{ //have permissions
                tomarFotografia();
            }
        }else{ // Pre-Marshmallow
            tomarFotografia();
        }
    }



    private void tomarFotografia() {
        try {
            File fileImagen = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
            boolean isCreada = fileImagen.exists();
            String nombreImagen = "";
            if (isCreada == false) {
                isCreada = fileImagen.mkdirs();
            }
            if (isCreada == true) {
                nombreImagen = (System.currentTimeMillis() / 1000) + ".jpg";
            }
            pathx = Environment.getExternalStorageDirectory() +
                    File.separator + RUTA_IMAGEN + File.separator + nombreImagen;
            File imagen = new File(pathx);
            Intent intent = null;
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ////
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authorities = getApplicationContext().getPackageName() + ".fileprovider";
                Uri imageUri = FileProvider.getUriForFile(this, authorities, imagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
            }
            startActivityForResult(intent, COD_FOTO);////
        }catch(Exception e){
            // Toast.makeText(getApplicationContext(), " Error "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_SELECCIONA:
                    Uri miPath=data.getData();
                    imagen.setImageURI(miPath);
                    break;

                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{pathx}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String paths, Uri uri) {
                                    // Log.i("Ruta de almacenamiento","Path: "+path);
                                    Toast.makeText(getApplicationContext(), "ruta "+paths, Toast.LENGTH_LONG).show();
                                }
                            });

                    Toast.makeText(getApplicationContext(), "ruta "+pathx, Toast.LENGTH_LONG).show();

                    Bitmap bitmap= BitmapFactory.decodeFile(pathx);
                    imagen.setImageBitmap(bitmap);
                    break;
            }
        }
    }





    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile(){
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error  "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "MyPicture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
        photoURI = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


/*
    private void Sexo(){
        View addDialog = this.getLayoutInflater().inflate(R.layout.sexo_dialog, null);
        final Dialog dia =  new LovelyCustomDialog(this)
                .setView(addDialog)
                .setTopColorRes(R.color.colorPrimary)
                .setTopTitle("Seleccione un sexo")
                .show();
        TextView ok = (TextView) addDialog.findViewById(R.id.masculino3);
        TextView close = (TextView) addDialog.findViewById(R.id.femenino);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1  Toast.makeText(getApplicationContext(), "click en ok "+nombrex.getText().toString() , Toast.LENGTH_SHORT).show();
                edit5.setText("Masculino");
                dia.hide();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(), "click en close" , Toast.LENGTH_SHORT).show();
                edit5.setText("Femenino");
                dia.hide();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date =  month + "/" + dayOfMonth + "/" + year;
       // dateText.setText(date);
         edit3.setText(date);
      //  Toast.makeText(getApplicationContext(), "Volver atrFas "+date, Toast.LENGTH_SHORT).show();
    }*/


    @Override
    public void onBackPressed() {
        // Toast.makeText(getApplicationContext(), "Volver atrFas", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
        startActivity(intent);
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }






    private void registrar(final String xnombre, final String  xapellido, final String xtelefono, final String xsexo, final String xemail, final String  xpassword , final String xrepassword ) {
        String tag_string_req = "req_login";
        pDialog.setMessage("Registrando ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                path+"Api/crearUsuario.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                nombre.setText("");
                apellido.setText("");
                telefono.setText("");
                usuario.setText("");
                password.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext()," Error "+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nombre", xnombre);
                params.put("apellido", xapellido);
                params.put("telefono", xtelefono);
                params.put("sexo", xsexo);
                params.put("usuario", xemail);
                params.put("password", xpassword);
                params.put("repassword", xrepassword);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
