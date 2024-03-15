package com.example.asaditasgourmet.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.asaditasgourmet.LoginActivity;
import com.example.asaditasgourmet.LoginNumberActivity;
import com.example.asaditasgourmet.MainActivity;
import com.example.asaditasgourmet.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

//import cn.pedant.SweetAlert.SweetAlertDialog;

public class Fragment4 extends Fragment {

      private RelativeLayout cerrar;

      public static final String MY_PREFS_NAME = "MySession";
      SharedPreferences.Editor editor;
      SharedPreferences openeditor;

      TextView nombre;
      public static final String tusuario = "nameKey";
      public static final String trol = "rolKey";
      public static final String tnombre = "nombreKey";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_4, container, false);
        openeditor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        //nombre.setText(openeditor.getString(tnombre, null));
        cerrar = view.findViewById(R.id.bottomSheet5);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Cerrar sesion")
                            .setContentText("Deseas realmente cerrar sesion?")
                            .setConfirmText("Aceptar!")
                            .setCancelText("Cancelar")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    editor.clear().apply();
                                    Intent int_main = new Intent(getActivity(), LoginActivity.class);
                                    int_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(int_main);
                                }
                            }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

            }
        });

        return view;
    }
}
