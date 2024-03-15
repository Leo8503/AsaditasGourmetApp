package com.example.asaditasgourmet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asaditasgourmet.Adapter.CountriesListAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.example.asaditasgourmet.GlobalInfo;



public class LoginNumberActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button boton;
    Spinner spinner;
    private CountriesListAdapter adapter;
    private EditText loginInput;
    private Spinner countrySelector;
    private ProgressDialog pDialog;
    private TextView link;
    String path = GlobalInfo.PATH_IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_login);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        setupSpinner();

        link =  (TextView) findViewById(R.id.terms);
        countrySelector = (Spinner) findViewById(R.id.countrySelector);
        loginInput = (EditText) findViewById(R.id.loginInput);
        boton = (Button) findViewById(R.id.sendOtpBtn);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              RegistroUser(countrySelector.getSelectedItem().toString(), loginInput);
            }
        });


        String text = "Al Continuar. usted acepta nuestros términos de Servicio Política de privacidad y Política de contenido";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
             @Override
             public void onClick(View widget) {
                 Uri uri =  Uri.parse("http://www.example.com");
                 Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                 startActivity(intent);
             }
             @SuppressLint("MissingSuperCall")
             @Override
             public void updateDrawState(TextPaint ds) {
                 super.updateDrawState(ds);
             }
         };
        ss.setSpan(clickableSpan1, 36, 103, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //ss.setSpan(clickableSpan2, 70, 93, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        link .setText(ss);
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void RegistroUser(String indicativo, EditText numero) {
        String xnumero = numero.getText().toString();
        if (!xnumero.isEmpty()) {
            registerNewUser(indicativo, xnumero);
        } else {
            if(numero.getText().toString().equals("")){
                numero.setError("Ingrese el numero de su movil");
            }

        }
    }

    private void registerNewUser( final String indicativo, final String numero) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Cargando ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                    path+"Api/registrar.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                   JSONArray respObj = new JSONArray(response);
                   for(int i = 0; i < respObj.length(); i++){
                       JSONObject c = respObj.getJSONObject(i);
                       String resultado = c.getString("insertado");
                       if(resultado.equals("1")){
                         Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                         startActivity(intent);
                       }else{
                         Toast.makeText(getApplicationContext(), "No se pudo crear el usuario", Toast.LENGTH_LONG).show();
                       }
                   }
                 }catch (Exception e){
                     e.printStackTrace();
                     Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), "error: " +error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("indicativo", indicativo);
                params.put("numero",numero);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    private void setupSpinner(){
        spinner = (Spinner) findViewById(R.id.countrySelector);
        spinner.setOnItemSelectedListener(LoginNumberActivity.this);
        String[] recourseList = this.getResources().getStringArray(R.array.CountryCodes);
        adapter = new CountriesListAdapter(this, recourseList);
        spinner.setAdapter(adapter);
        int selectedCountry = adapter.getPositionForDeviceCountry();
        if (selectedCountry != -1) {
            spinner.setSelection(selectedCountry);
        }
    }
}
