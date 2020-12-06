package com.ariefzuhri.amigo19.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.localpreference.ShoppingModePreference;
import com.ariefzuhri.amigo19.pageradapter.SectionsPagerAdapter;

import com.fxn.BubbleTabBar;
import com.fxn.OnBubbleClickListener;

import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;
import static com.ariefzuhri.amigo19.utils.AppUtils.subscribeForUpdate;

public class MainActivity extends AppCompatActivity {
    SectionsPagerAdapter pagerAdapter;
    ViewPager viewPager;
    BubbleTabBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subscribeForUpdate(); // Jika versi sudah terbaru, subscribe ke fcm. Sebaliknya, unsubscribe.

        ShoppingModePreference shoppingModePreference = new ShoppingModePreference(this);
        if (shoppingModePreference.isShoppingMode()){
            Intent intent = new Intent(this, ShoppingModeActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) getSupportActionBar().hide();
        getTransparentNotifBar(this);

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);  // min 1 -> Jika pagerAdapter.getCount() = 1, jangan dikurang 1

        bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setupBubbleTabBar(viewPager);
        bottomBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int id) {
                switch (id){
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        break;

                    case R.id.navigation_personal:
                        viewPager.setCurrentItem(1);
                        break;

                    case R.id.navigation_statistic:
                        viewPager.setCurrentItem(2);
                        break;

                    case R.id.navigation_education:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });
    }
}