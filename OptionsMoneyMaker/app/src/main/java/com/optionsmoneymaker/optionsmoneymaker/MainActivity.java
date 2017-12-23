package com.optionsmoneymaker.optionsmoneymaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.optionsmoneymaker.optionsmoneymaker.fragment.FragmentDrawer;
import com.optionsmoneymaker.optionsmoneymaker.fragment.HelpFragment;
import com.optionsmoneymaker.optionsmoneymaker.fragment.SettingFragment;
import com.optionsmoneymaker.optionsmoneymaker.fragment.HomeFragment;
import com.optionsmoneymaker.optionsmoneymaker.fragment.AboutFragment;

public class MainActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        Log.e("MainActivity", "position==" + position);
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new SettingFragment();
                title = getString(R.string.title_setting);
                break;
            case 2:
                fragment = new AboutFragment();
                title = getString(R.string.title_about);
                break;
            case 3:
                fragment = new HelpFragment();
                title = getString(R.string.title_help);
                break;
            case 4:
                title = getString(R.string.title_logout);
                if (cd.isConnectingToInternet()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Are you sure you want to logout?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //session.logoutUser();
                                    logout();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                } else {
                    toast(getResources().getString(R.string.no_internet));
                }
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}