package com.example.asaditasgourmet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.example.asaditasgourmet.fragment.Fragment1;
import com.example.asaditasgourmet.fragment.Fragment2;
import com.example.asaditasgourmet.fragment.Fragment4;
import com.example.asaditasgourmet.fragment.GeneralFragment;
import com.example.asaditasgourmet.fragment.PedidosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Domiciliario extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domiciliario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        GeneralFragment ffr1 = new GeneralFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, ffr1)
                .addToBackStack(null)
                .commit();
        mBottomNavigationView.getMenu().findItem(R.id.nav_general).setChecked(true);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_general) {
                    GeneralFragment ffr1 = new GeneralFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, ffr1)
                            .addToBackStack(null)
                            .commit();
                    mBottomNavigationView.getMenu().findItem(R.id.nav_general).setChecked(true);
                    return true;
                }
                else if (id == R.id.nav_pedidos) {
                    PedidosFragment ffr2 = new PedidosFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, ffr2)
                            .addToBackStack(null)
                            .commit();
                    mBottomNavigationView.getMenu().findItem(R.id.nav_pedidos).setChecked(true);
                    return true;
                } else if (id == R.id.nav_perfil) {
                    Fragment4 ffr4 = new Fragment4();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, ffr4)
                            .addToBackStack(null)
                            .commit();
                    mBottomNavigationView.getMenu().findItem(R.id.nav_perfil).setChecked(true);
                    return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.soporte: {
               String msj = "Hola necesito ayuda con App Mecanica";
               String numeroTel = "+573224477818";
               Intent intent = new Intent(Intent.ACTION_VIEW);
               String uri = "whatsapp://send?phone=" + numeroTel + "&text=" + msj;
               intent.setData(Uri.parse(uri));
               startActivity(intent);
              return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
    }
}
