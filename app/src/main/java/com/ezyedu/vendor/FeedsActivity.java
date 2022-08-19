package com.ezyedu.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezyedu.vendor.model.Globals;
import com.ezyedu.vendor.model.ImageGlobals;

public class FeedsActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    TextView textView;

    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        drawerLayout = findViewById(R.id.drawer_layout);

        textView = findViewById(R.id.tittle_page);

        textView.setText("Feeds");


        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


    }

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
        MainActivity.redirectActivity(this,MessageActivity.class);
    }

    public void Clickfeeds(View view)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
        //recreate();
    }

    public void Clickpayment(View view)
    {
        MainActivity.redirectActivity(this,Payment_activity.class);
        //  redirectActivity(this,);
    }
    public void Clickusers(View view)
    {
        //  redirectActivity(this,);
        Toast.makeText(this, "Users", Toast.LENGTH_SHORT).show();
    }
    public void Clickreviews(View view)
    {
        Toast.makeText(this, "Reviews", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clickanalytics(View view)
    {
        Toast.makeText(this, "analytics", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clicksales(View view)
    {
        Toast.makeText(this, "Sales", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clickinstitute(View view)
    {
        Toast.makeText(this, "Institute", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clicksettings(View view)
    {
        Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
    }
    public void Clickproducts(View view)
    {
        Toast.makeText(this, "Products", Toast.LENGTH_SHORT).show();
        // redirectActivity(this,);
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