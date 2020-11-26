package com.example.chofem.store.Ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.chofem.store.Fragment.AddProductFragment;
import com.example.chofem.store.Fragment.MyStockFragment;
import com.example.chofem.store.Fragment.My_Orders_Fragment;
import com.example.chofem.store.Fragment.Profile_Fragment;
import com.example.chofem.store.Fragment.SelectLanguageFragment;
import com.example.chofem.store.R;
import com.example.chofem.store.utils.SharedPref;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class DefaultDrawerActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FrameLayout containerFrame;
    private Toolbar toolbar;
    private LinearLayout linearMapTab, linearEmptyLegTab, linearCustomTabs;
    private View linearEmptyLegView, mapView;
    private TextView txtEmptyLg, txtMap;
    public NavigationView navigationView;
    private DrawerLayout drawer;
    private Context mContext;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mContext = DefaultDrawerActivity.this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        checkLocationPermission();
        navDrawerSetup();


//        viewPager = findViewById(R.id.viewPager);
//        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(viewPagerAdapter);
//        tabLayout = findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);

        replaceFragment(new MyStockFragment(),true);
    }
    @Override
    protected void onResume() {
        super.onResume();
//        linearMapTab.performClick();
    }
    private void navDrawerSetup() {
        navigationView = findViewById(R.id.nav_view);
        containerFrame = findViewById(R.id.containerFrame);
        //navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // View headerView = navigationView.inflateHeaderView(R.layout.menu_layout);
    }
    public void navMenuClick(View view) {
        switch (view.getId()) {
            case R.id.nav_profile:
                Handler os = new Handler();
                os.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        replaceFragment(new Profile_Fragment(),false);
                    }
                }, 500);
                drawer.closeDrawers();

                // replaceFragment(new Profile_Fragment());
                break;
            case R.id.nav_my_order:
                Handler os1 = new Handler();
                os1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        replaceFragment(new My_Orders_Fragment(),false);
                    }
                }, 500);
                drawer.closeDrawers();
                break;
            case R.id.nav_language:
/*                PopupMenu popup = new PopupMenu(mContext, view);
                popup.setOnMenuItemClickListener(this);
                popup.inflate(R.menu.language_popup_menu);
                popup.show();*/
                Handler os2 = new Handler();
                os2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        replaceFragment(new SelectLanguageFragment(),false);
                    }
                }, 500);
                drawer.closeDrawers();
                break;
            case R.id.nav_add_item: {
                Handler os3 = new Handler();
                os3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        replaceFragment(new AddProductFragment(),false);
                    }
                }, 500);
                drawer.closeDrawers();

                break;
            }
            case R.id.nav_Setting: {
                Handler os3 = new Handler();
                os3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(mContext,ChangePasswordActivity.class));
                    }
                }, 500);
                drawer.closeDrawers();
                //replaceFragment(new AddProductFragment());
                break;
            }
            case R.id.btnLoggOut: {
                Handler os3 = new Handler();
                os3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exitDialog();
                    }
                }, 500);
                drawer.closeDrawers();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Fragment fragment;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
        count = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            count--;
            // only show dialog while there's back stack entry
            //   dialog.show(getSupportFragmentManager(), "ConfirmDialogFragment");

        } else if (count == 0) {
            // or just go back to main activity
            super.onBackPressed();
        }
    }

    public void replaceFragment(final Fragment fragment,boolean backStack) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //remove previous fragment
/*                Fragment fragmentOld = getSupportFragmentManager().findFragmentById(R.id.containerFrame);

    *//*            if (fragmentOld != null)
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();*//*

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                        //.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
                        .replace(R.id.containerFrame, fragment);
                        if(backStack)
                       *//* ft.addToBackStack(fragment.getTag());
                        else
                            ft.addToBackStack(null);*//*
                        ft.commit();*/

/*                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerFrame, fragment);
                //fragmentTransaction.addToBackStack(fragment.toString());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();*/


/*                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.containerFrame, fragment);
               // transaction.addToBackStack(null);
                transaction.commit();*/

                if(backStack)
                {
                    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.containerFrame, fragment);
                     transaction.addToBackStack(fragment.getTag());
                    transaction.commit();
                }
                else {
       /*             // it won't add to stack
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.containerFrame, fragment)
                            .commit();*/
                    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.containerFrame, fragment);
                    transaction.commit();
                }
            }
        });



//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.containerFrame, fragment);
//        fragmentTransaction.addToBackStack(fragment.toString());
//        //fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        fragmentTransaction.commit();


    }

    public void exitDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DefaultDrawerActivity.this);
        alertDialogBuilder
                .setMessage("Do you want to Log out Account?")
                .setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logOut();

                    }
                });
        alertDialogBuilder.show();
    }

    private void logOut() {
        SharedPref.getInstance(DefaultDrawerActivity.this).clearStoredData();
        Intent obj = new Intent(DefaultDrawerActivity.this, Login_Activity.class);
        finish();
        startActivity(obj);
    }
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Permision Needed")
                        .setMessage("Need Location Permission")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(DefaultDrawerActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                                ActivityCompat.requestPermissions(DefaultDrawerActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        20);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        drawer.closeDrawers();
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.copy_item:
                // do your code
                return true;
            case R.id.print_item:
//                Locale locale = new Locale("ur_IN");
//                Locale.setDefault(locale);
//                Configuration config = new Configuration();
//                config.locale = locale;
//                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//                Toast.makeText(mContext, getResources().getString(R.string.language), Toast.LENGTH_SHORT).show();


//                Locale locale = new Locale("ur_IN");//Set Selected Locale
//                Locale.setDefault(locale);//set new locale as default
//                Configuration config = new Configuration();//get Configuration
//                config.locale = locale;//set config locale as selected locale
//                this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
//                invalidateOptionsMenu();
//                setTitle(R.string.app_name);
//                super.onStart();

                Locale myLocale = new Locale("ur_IN");
                Locale.setDefault(myLocale);
                android.content.res.Configuration config = new android.content.res.Configuration();
                config.locale = myLocale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                recreate();

                // do your code
                return true;
            case R.id.share_item:
            default:
                return false;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // if (myLocale != null){
        Locale locale = new Locale("ur_IN");//Set Selected Locale
        newConfig.locale = locale;
        Locale.setDefault(locale);
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
    }

}