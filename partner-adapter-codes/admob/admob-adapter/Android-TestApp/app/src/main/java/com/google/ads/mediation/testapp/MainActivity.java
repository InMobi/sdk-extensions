// Copyright (C) 2016 Google, Inc.

package com.google.ads.mediation.testapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.inmobi.sdk.InMobiSdk;

/**
 *The {@link MainActivity} is the single activity for this app. It contains a {@link
 * NavigationView} which is used to navigate through the apps fragments.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container, new TestNativeAdFragment());
            transaction.commit();
            navigationView.setCheckedItem(R.id.nav_native);
        }
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_interstitial) {
            transaction.replace(R.id.container, new TestInterstitialAdFragment());
        } else if (id == R.id.nav_banner) {
            transaction.replace(R.id.container, new TestBannerAdFragment());
        } else if (id == R.id.nav_custom_size_banner) {
            transaction.replace(R.id.container, new TestCustomAdSizeFragment());
        } else if (id == R.id.nav_rewarded_video) {
            transaction.replace(R.id.container, new TestRewardedVideoAdFragment());
        } else if (id == R.id.nav_native) {
            transaction.replace(R.id.container, new TestNativeAdFragment());
        }

        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
