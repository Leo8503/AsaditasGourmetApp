package com.example.asaditasgourmet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.example.asaditasgourmet.database.DatabaseHelper;
import com.example.asaditasgourmet.fragment.Fragment1;
import com.example.asaditasgourmet.fragment.Fragment2;
import com.example.asaditasgourmet.fragment.Fragment3;
import com.example.asaditasgourmet.fragment.Fragment4;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    DatabaseHelper db;

    @Override
    public void onResume(){
        super.onResume();
        mCartItemCount = db.getCountCart();
        setupBadge();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DatabaseHelper(getApplicationContext());

        CheckMapPermission();

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Fragment1 ffr1 = new Fragment1();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, ffr1)
                .addToBackStack(null)
                .commit();
        mBottomNavigationView.getMenu().findItem(R.id.nav_restaurants).setChecked(true);
       // getSupportActionBar().setTitle("Restaurants");
        mCartItemCount = db.getCountCart();

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_restaurants) {
                    Fragment1 ffr1 = new Fragment1();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, ffr1)
                            .addToBackStack(null)
                            .commit();
                    mBottomNavigationView.getMenu().findItem(R.id.nav_restaurants).setChecked(true);
                    //getSupportActionBar().setTitle("Restaurants");
                    return true;
                }
                else if (id == R.id.nav_search) {
                    Fragment2 ffr2 = new Fragment2();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, ffr2)
                            .addToBackStack(null)
                            .commit();
                    mBottomNavigationView.getMenu().findItem(R.id.nav_search).setChecked(true);
                    //getSupportActionBar().setTitle("Search");
                    return true;
                }else if (id == R.id.nav_favourites) {
                    Fragment3 ffr3 = new Fragment3();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, ffr3)
                            .addToBackStack(null)
                            .commit();
                    mBottomNavigationView.getMenu().findItem(R.id.nav_favourites).setChecked(true);
                    //getSupportActionBar().setTitle("Favorites");
                    return true;
                } else if (id == R.id.nav_profile) {
                    Fragment4 ffr4 = new Fragment4();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, ffr4)
                            .addToBackStack(null)
                            .commit();
                    mBottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                    //getSupportActionBar().setTitle("Profile");
                    return true;
                }
                return true;
            }
        });
    }







    private void CheckMapPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission( MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission( MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1002 );
            } else {
            }
        } else {
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        switch (requestCode) {
            case 1002: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
                            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this,
                            Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                }
            }
            break;
        }

    }


    @Override
    public void onBackPressed(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart: {
                Intent intent = new Intent(MainActivity.this, CardItemActivity.class );
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
