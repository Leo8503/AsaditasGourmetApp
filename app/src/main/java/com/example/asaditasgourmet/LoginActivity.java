package com.example.asaditasgourmet;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.asaditasgourmet.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity
{
    Fragment frag_login;
    ProgressBar pbar;
    View button_login, button_label,button_icon,ic_menu1,ic_menu2;
    private DisplayMetrics dm;
    private EditText texto1, texto2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pbar = (ProgressBar) findViewById(R.id.mainProgressBar1);
        button_icon = findViewById(R.id.button_icon);
        button_label = findViewById(R.id.button_label);

        dm = getResources().getDisplayMetrics();
        pbar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        StatusBarUtil.immersive(this);

        frag_login = new LoginFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, frag_login).commit();

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_login, null);

        texto1 = dialogView.findViewById(R.id.mainEditText1);
        texto2 = dialogView.findViewById(R.id.mainEditText1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
