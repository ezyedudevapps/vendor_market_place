package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyedu.vendor.adapter.Review_Fragment_Adapter;
import com.ezyedu.vendor.adapter.Sales_fragment_Adapter;
import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;
import com.google.android.material.tabs.TabLayout;

public class Reviews_Page extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView textView;

    TabLayout tableLayout;
    ViewPager2 pager2;
    String session_id = null;
    Review_Fragment_Adapter review_fragment_adapter;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews__page);

        drawerLayout = findViewById(R.id.drawer_layout);

        textView = findViewById(R.id.tittle_page);

        textView.setText("Reviews");


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
        session_id = sharedPreferences.getString("session_val","");
        Log.i("Session_HPactivity",session_id);

        tableLayout = findViewById(R.id.tab1);
        pager2 = findViewById(R.id.view_p);
        FragmentManager fragmentManager = getSupportFragmentManager();
        review_fragment_adapter = new Review_Fragment_Adapter(fragmentManager,getLifecycle());
        pager2.setAdapter(review_fragment_adapter);

        tableLayout.addTab(tableLayout.newTab().setText("Institution"));
        tableLayout.addTab(tableLayout.newTab().setText("Courses"));

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                tableLayout.selectTab(tableLayout.getTabAt(position));
            }
        });
    }

    //message navigation menu....
    public void ClickMenu(View view)
    {
        MainActivity.openDrawer(drawerLayout);
    }
    public void Clickhome(View view)
    {
        MainActivity.redirectActivity(this,MainActivity.class);
    }
    public void Clicmessages(View view)
    {
        //recreate();
        MainActivity.redirectActivity(this,MessageActivity.class);
    }


    public void Clickfeeds(View view)
    {
        MainActivity.redirectActivity(this,Bank_Account_Activity.class);
    }

    public void ClickEvents(View view) {
        // redirectActivity(this,);
        MainActivity.redirectActivity(this, Events_Activity.class);
    }

    public void Clickpayment(View view)
    {
        MainActivity.redirectActivity(this,Payment_activity.class);
        //  redirectActivity(this,);
    }
    public void Clickusers(View view)
    {
        //  redirectActivity(this,);
        MainActivity.redirectActivity(this,Courses_Activity.class);
    }
    public void Clickreviews(View view)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
        // redirectActivity(this,);
    }
    public void ClickFeeds(View view)
    {
        MainActivity. redirectActivity(this,My_Feeds_Activity.class);
    }

    public void Clickanalytics(View view)
    {
        MainActivity.redirectActivity(this,Analytics_Activity.class);
    }
    public void Clicksales(View view)
    {
        MainActivity.redirectActivity(this,Sales_activity.class);
    }
    public void Clickinstitute(View view)
    {
        MainActivity.redirectActivity(this,Edit_profile_activity.class);
        // redirectActivity(this,);
    }
    public void Clicksettings(View view)
    {
        MainActivity.redirectActivity(this,Settings_Activity.class);

    }
    public void Clickproducts(View view)
    {
        MainActivity.redirectActivity(this,Connection_Actiivity.class);
    }
    public void Clickfaq(View view)
    {
        Toast.makeText(this, "Faq", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            Intent intent1 = new Intent(this,MainActivity.class);
            startActivity(intent1);
        }
    }
}