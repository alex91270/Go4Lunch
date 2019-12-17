package com.example.go4lunchAlx.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.detail.DetailActivity;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.signin.SigninActivity;
import com.example.go4lunchAlx.ui.list.ListViewFragment;
import com.example.go4lunchAlx.ui.map.MapViewFragment;
import com.example.go4lunchAlx.ui.mates.MatesViewFragment;
import com.example.go4lunchAlx.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private RestApiService service = DI.getRestApiService();
    SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yy");

    //Declare fragment handled by Navigation Drawer
    private Fragment fragmentSettings;
    private Fragment fragmentMapview;
    private Fragment fragmentListview;
    private Fragment fragmentMatesview;

    //Identify each fragment with a number
    private static final int FRAGMENT_YOURLUNCH = 0;
    private static final int FRAGMENT_SETTINGS = 1;
    private static final int FRAGMENT_MAPVIEW = 2;
    private static final int FRAGMENT_LISTVIEW = 3;
    private static final int FRAGMENT_MATESVIEW = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        configureBottomView();
        showFirstFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.activity_main_drawer_yourlunch :
                showYourlunchFragment();
                break;
            case R.id.activity_main_drawer_settings:
                showSettingsFragment();
                break;
            case R.id.activity_main_drawer_logout:
                Toast.makeText(this, "LOGOUT", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SigninActivity.class));
                break;
            case R.id.action_mapview:
                toolbar.setTitle(R.string.toolbar_title_hungry);
                showMapViewFragment();
                break;
            case R.id.action_listview:
                toolbar.setTitle(R.string.toolbar_title_hungry);
                showListViewFragment();
                break;
            case R.id.action_workmates:
                toolbar.setTitle(R.string.toolbar_title_mates);
                showMatesViewFragment();
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Show fragment according an Identifier
    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_YOURLUNCH :
                showYourlunchFragment();
                break;
            case FRAGMENT_SETTINGS:
                showSettingsFragment();
                break;
            case FRAGMENT_MAPVIEW:
                showMapViewFragment();
                break;
            case FRAGMENT_LISTVIEW:
                showListViewFragment();
                break;
            case FRAGMENT_MATESVIEW:
                showMatesViewFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //Handle back click to close menu
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void configureToolBar(){
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.toolbar_title_hungry);
    }

    private void configureDrawerLayout(){
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView(){
        navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textViewName = headerView.findViewById(R.id.header_user_name);
        textViewName.setText(service.getCurrentUser().getUsername());
        TextView textViewEmail = headerView.findViewById(R.id.header_user_email);
        textViewEmail.setText(service.getMyEmailAddress());
        ImageView imageViewAvatar = headerView.findViewById(R.id.header_avatar);
        Glide.with(this)
                .load(service.getCurrentUser().getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(imageViewAvatar);

        navigationView.setNavigationItemSelectedListener(this);

    }

    private void configureBottomView(){
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void showYourlunchFragment(){
        User currentUser = service.getUserById(service.getCurrentUserId());
        if (currentUser.getDateSelection()!= null && ft.format(new Date())
                .equals(ft.format(new Date(currentUser.getDateSelection())))) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("restoId", currentUser
                    .getSelectedRestaurant());
            this.startActivity(intent);
        } else {
            Toast.makeText(this, "No restaurant selected yet for today !", Toast.LENGTH_LONG).show();
        }

    }

    private void showSettingsFragment(){
        if (fragmentSettings == null) fragmentSettings = SettingsFragment.newInstance();
        startTransactionFragment(fragmentSettings);
    }

    private void showMapViewFragment(){

        if (fragmentMapview == null) fragmentMapview = MapViewFragment.newInstance();
        startTransactionFragment(fragmentMapview);
    }

    private void showListViewFragment(){
        if (fragmentListview == null) fragmentListview = ListViewFragment.newInstance();
        startTransactionFragment(fragmentListview);
    }

    private void showMatesViewFragment(){
        if (fragmentMatesview == null) fragmentMatesview = MatesViewFragment.newInstance();
        startTransactionFragment(fragmentMatesview);
    }

    //Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }

    //Show first fragment when activity is created
    private void showFirstFragment(){
        toolbar.setTitle(R.string.toolbar_title_hungry);
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null){
            showFragment(FRAGMENT_MAPVIEW);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }
}
