<<<<<<< HEAD
package com.himalikiran.nepshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
   // private ListView mDrawerList;
    private TabLayout tabLayout;
   // private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
   // private String mPhotoUrl;
    private int[] tabIcons = {
            R.drawable.ic_portfolio,
            R.drawable.ic_watchlist,
            R.drawable.ic_nepse
    };


    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        //navView.setNavigationItemSelectedListener(MenuItem );
        View header=navView.getHeaderView(0);
        final TextView userNameView = (TextView)header.findViewById(R.id.userNamePlaceHolder);
        final ImageView profilePicture = (ImageView)header.findViewById(R.id.profilePicture);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    //Toast.makeText(getApplicationContext(), "Sign in successful! " +user.getUid(),Toast.LENGTH_LONG).show();

                    //View header = navView.getHeaderView(0);
                    if (user.getDisplayName() != null) {
                        Snackbar.make(navView, "Welcome " + user.getDisplayName(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        userNameView.setText(user.getDisplayName().toString());
                        if (user.getPhotoUrl() != null) {

                            Glide.with(MainActivity.this).load(user.getPhotoUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(profilePicture) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    profilePicture.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        }
                    }
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    //Toast.makeText(getApplicationContext(), "Sign in unsuccessful! Please try again",Toast.LENGTH_LONG).show();
                }
                // ...
            }
        };

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (navView != null) {
            setupDrawerContent(navView);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        TabAdapter tabAdapter = new TabAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(tabAdapter);

        // Find the tab layout that shows the tabs
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabTextView = (TextView)findViewById(R.id.tabtext);
        tabLayout.setupWithViewPager(viewPager);
        //final FragmentManager addSharefm = getSupportFragmentManager();

       setupTabIcons();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddNewShareDialog addShareDialog = new AddNewShareDialog();
                //addShareDialog.
                addShareDialog.show(getSupportFragmentManager(), "Add New Share");

            }
        });


    }


    private void setupTabIcons() {

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);

            if (tab != null) {
                tab.setIcon(tabIcons[i]);
            }
        }

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        int id = menuItem.getItemId();

                        if (id == R.id.nav_profile) {
                            mDrawerLayout.openDrawer(GravityCompat.START);
                            return true;
                        } else if (id == R.id.nav_reports) {

                        } else if (id == R.id.logout) {
                            signOut();

                        }

                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void signOut(){
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }


}
=======
package com.himalikiran.nepshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
   // private ListView mDrawerList;
    private TabLayout tabLayout;
   // private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
   // private String mPhotoUrl;
    private int[] tabIcons = {
            R.drawable.ic_portfolio,
            R.drawable.ic_watchlist,
            R.drawable.ic_nepse
    };


    private static final String TAG = "EmailPassword";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        //navView.setNavigationItemSelectedListener(MenuItem );
        View header=navView.getHeaderView(0);
        final TextView userNameView = (TextView)header.findViewById(R.id.userNamePlaceHolder);
        final ImageView profilePicture = (ImageView)header.findViewById(R.id.profilePicture);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    //Toast.makeText(getApplicationContext(), "Sign in successful! " +user.getUid(),Toast.LENGTH_LONG).show();

                    //View header = navView.getHeaderView(0);
                    if (user.getDisplayName() != null) {
                        Snackbar.make(navView, "Welcome " + user.getDisplayName(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        userNameView.setText(user.getDisplayName().toString());
                        if (user.getPhotoUrl() != null) {
                            //Glide.with(MainActivity.this).load(user.getPhotoUrl()).into(profilePicture);
                            //Picasso.with(MainActivity.this).load(user.getPhotoUrl()).into(profilePicture);
                            Glide.with(MainActivity.this).load(user.getPhotoUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(profilePicture) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    profilePicture.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        }
                    }
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    //Toast.makeText(getApplicationContext(), "Sign in unsuccessful! Please try again",Toast.LENGTH_LONG).show();
                }
                // ...
            }
        };

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (navView != null) {
            setupDrawerContent(navView);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        TabAdapter tabAdapter = new TabAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(tabAdapter);

        // Find the tab layout that shows the tabs
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabTextView = (TextView)findViewById(R.id.tabtext);
        tabLayout.setupWithViewPager(viewPager);
        //final FragmentManager addSharefm = getSupportFragmentManager();

       setupTabIcons();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddNewShareDialog addShareDialog = new AddNewShareDialog();
                //addShareDialog.
                addShareDialog.show(getSupportFragmentManager(), "Add New Share");

            }
        });


    }


    private void setupTabIcons() {

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);

            if (tab != null) {
                tab.setIcon(tabIcons[i]);
            }
        }

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        int id = menuItem.getItemId();

                        if (id == R.id.nav_profile) {
                            mDrawerLayout.openDrawer(GravityCompat.START);
                            return true;
                        } else if (id == R.id.nav_reports) {

                        } else if (id == R.id.logout) {
                            signOut();

                        }

                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void signOut(){
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }


//    public void LoadSignInActivity(){
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }
}
>>>>>>> 3829f0e93f32b33448d7b54d2d137b76144d4926
