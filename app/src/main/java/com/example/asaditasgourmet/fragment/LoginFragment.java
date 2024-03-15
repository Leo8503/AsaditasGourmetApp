package com.example.asaditasgourmet.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.asaditasgourmet.fragment.Fragment4.MY_PREFS_NAME;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.animation.ValueAnimator;
import android.view.animation.*;
import android.widget.Button;
import android.util.*;
import android.media.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.asaditasgourmet.AppController;
import com.example.asaditasgourmet.Domiciliario;
import com.example.asaditasgourmet.GlobalInfo;
import com.example.asaditasgourmet.MainActivity;
import com.example.asaditasgourmet.R;
import com.example.asaditasgourmet.RegistrarActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment
{
	public TextView registrar;
	Button button_login;
	View form_login, imglogo, label_signup, darkoverlay;
	private DisplayMetrics dm;
	private EditText eusuario, epassword;
	private ProgressDialog pDialog;
	private RelativeLayout boton;
	public static final String usuario = "nameKey";
	public static final String clave = "claveKey";
	public static final String MY_PREFS_NAME = "MySession";
	SharedPreferences.Editor editor;
	SharedPreferences openeditor;
	private ImageView logo;
	private KenBurnsView fondo;
	String path = GlobalInfo.PATH_IP;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.fragment_login, container, false);
		logo=v.findViewById(R.id.fragmentloginLogo);
		fondo= (KenBurnsView)  v.findViewById(R.id.imgLogin);

		LoadConfiguracion();

		dm=getResources().getDisplayMetrics();
		form_login=v.findViewById(R.id.form_login);
		imglogo=v.findViewById(R.id.fragmentloginLogo);
		darkoverlay=v.findViewById(R.id.fragmentloginView1);
		label_signup=v.findViewById(R.id.label_signup);
		pDialog = new ProgressDialog(getActivity());
		pDialog.setCancelable(false);

		boton = getActivity().findViewById(R.id.button_login);
		eusuario = v.findViewById(R.id.mainEditText1);
		epassword = v.findViewById(R.id.mainEditText2);
		boton.setTag(0);

		final ValueAnimator va = new ValueAnimator();
		va.setDuration(1500);
		va.setInterpolator(new DecelerateInterpolator());
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		  @Override
		  public void onAnimationUpdate(ValueAnimator p1) {
		      RelativeLayout.LayoutParams button_login_lp = (RelativeLayout.LayoutParams) boton.getLayoutParams();
		      button_login_lp.width = Math.round((Float) p1.getAnimatedValue());
		      boton.setLayoutParams(button_login_lp);
		  }
		});

		boton.animate().translationX(dm.widthPixels + boton.getMeasuredWidth()).setDuration(0).setStartDelay(0).start();
		boton.animate().translationX(0).setStartDelay(6500).setDuration(1500).setInterpolator(new OvershootInterpolator()).start();
		registrar=v.findViewById(R.id.registrar);
		registrar.setOnClickListener(new View.OnClickListener(){
		@Override
		public void onClick(View p1) {
			Intent intent = new Intent(getActivity(), RegistrarActivity.class);
			startActivity(intent);
			getActivity().finish();
		}
	});

		boton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View p1) {
  	     	LoginUser(eusuario.getText().toString(), epassword.getText().toString());
			}
		});
		return v;
	}


	private void LoginUser(final String suser, final String scontrasena) {
		String tag_string_req = "req_register";
		pDialog.setMessage("Validando usuario ...");
		showDialog();
		StringRequest strReq = new StringRequest(Request.Method.POST,
						path+"Api/login.php", new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					hideDialog();
					 try {
							 JSONArray respObj = new JSONArray(response);
							 for(int i = 0; i < respObj.length(); i++){
									 JSONObject c = respObj.getJSONObject(i);
									 String resultado = c.getString("resultado");
									 String id = c.getString("ID");
         					 String rol = c.getString("rol");
	           		   String foto = c.getString("foto");
									 String nombre = c.getString("nombre");
									 String apellido = c.getString("apellido");
									 String correo = c.getString("correo");
									 String password = c.getString("password");
									 String telefono = c.getString("telefono");
									 String estado = c.getString("estado");

									 if(resultado.equals("1")){
										 if(rol.equals("Cliente")){
											 editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
											 editor.putString("id", id);
											 editor.putString("correo", correo);
											 editor.putString("nombre", nombre);
											 editor.putString("apellido", apellido);
											 editor.putString("telefono", telefono);
											 editor.commit();

											 Intent intent = new Intent(getActivity(), MainActivity.class);
 	   				 				   startActivity(intent);
										 }
										 if(rol.equals("Domiciliario")){
											 editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
											 editor.putString("id", id);
											 editor.putString("correo", correo);
											 editor.putString("nombre", nombre);
											 editor.putString("apellido", apellido);
											 editor.putString("telefono", telefono);
											 editor.commit();

											 Intent intent = new Intent(getActivity(), Domiciliario.class);
 										   startActivity(intent);
										 }
									 }
									 if(resultado.equals("0")){
										 Toast.makeText(getActivity(), "El usuario no existe", Toast.LENGTH_LONG).show();
									 }
									 if(resultado.equals("2")){
										 Toast.makeText(getActivity(), "Campos no pueden estar vacios", Toast.LENGTH_LONG).show();
									 }
							 }
					 } catch (Exception e){
							 e.printStackTrace();
							 Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
					 }
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					hideDialog();
					Toast.makeText(getActivity(), "Error: " +error.getMessage(), Toast.LENGTH_LONG).show();
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
						Map<String, String> params = new HashMap<String, String>();
						params.put("correo", suser);
						params.put("password",scontrasena);
						return params;
				}
		};
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	 }

	private void showDialog() {
			if (!pDialog.isShowing())
					pDialog.show();
	}

	private void hideDialog() {
			if (pDialog.isShowing())
					pDialog.dismiss();
	}




  private void LoadConfiguracion() {
      String url = path+"Api/Configuracion.php";
      StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              try {
					JSONArray myJsonArray = new JSONArray(response);
					editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putString("logo", myJsonArray.getJSONObject(0).getString("logo"));
					editor.putString("imgApp", myJsonArray.getJSONObject(0).getString("imgApp"));
					editor.putString("tipo", myJsonArray.getJSONObject(0).getString("tipo"));
					editor.putString("soporte", myJsonArray.getJSONObject(0).getString("soporte"));
					editor.putString("color", myJsonArray.getJSONObject(0).getString("color"));
					editor.putString("banner1", myJsonArray.getJSONObject(0).getString("banner1"));
					editor.putString("banner2", myJsonArray.getJSONObject(0).getString("banner2"));
					editor.putString("banner3", myJsonArray.getJSONObject(0).getString("banner3"));
					editor.putString("negocio", myJsonArray.getJSONObject(0).getString("negocio"));
					editor.putString("direccion", myJsonArray.getJSONObject(0).getString("direccion"));
					editor.putString("correo", myJsonArray.getJSONObject(0).getString("correo"));
					editor.putString("telefono", myJsonArray.getJSONObject(0).getString("telefono"));
					editor.putString("latitud", myJsonArray.getJSONObject(0).getString("latitud"));
					editor.putString("longitud", myJsonArray.getJSONObject(0).getString("longitud"));
					editor.commit();

					Glide.with(getActivity()).load(path+"pages/configuracion/upload/"+myJsonArray.getJSONObject(0).getString("logo")).into(logo);
					Glide.with(getActivity()).load(path+"pages/configuracion/upload/"+myJsonArray.getJSONObject(0).getString("imgApp")).into(fondo);
		      } catch (JSONException e) {
                  e.printStackTrace();
                  hideDialog();
                  Toast.makeText(getActivity(), "e "+e.getMessage(), Toast.LENGTH_LONG).show();
              }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              hideDialog();
              Toast.makeText(getActivity(), "error "+error.getMessage(), Toast.LENGTH_LONG).show();
          }
      });
      RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
      requestQueue.add(stringRequest);
  }



	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new AccelerateDecelerateInterpolator());
		fondo.setTransitionGenerator(generator);
		imglogo.animate().setStartDelay(5000).setDuration(2000).alpha(1).start();
		darkoverlay.animate().setStartDelay(5000).setDuration(2000).alpha(0.6f).start();
		label_signup.animate().setStartDelay(6000).setDuration(2000).alpha(1).start();
		form_login.animate().translationY(dm.heightPixels).setStartDelay(0).setDuration(0).start();
		form_login.animate().translationY(0).setDuration(1500).alpha(1).setStartDelay(6000).start();
	}
}
// auto typing with adb (for demo purpose)
// sleep 6;input text "agusibrahim@mail.com";input keyevent 61;input text "thisispasswd"
