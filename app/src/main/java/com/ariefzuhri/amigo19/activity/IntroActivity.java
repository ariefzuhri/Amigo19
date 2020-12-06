package com.ariefzuhri.amigo19.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.localpreference.FirstTimeLaunchPreference;
import static com.ariefzuhri.amigo19.utils.AppUtils.getTransparentNotifBar;

public class IntroActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] slides;
    private Button btnSkip, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Atur status bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        getTransparentNotifBar(this);

        // Initialize
        viewPager = findViewById(R.id.view_pager_intro);
        dotsLayout = findViewById(R.id.layout_dots_intro);
        btnSkip = findViewById(R.id.btn_skip_intro);
        btnNext = findViewById(R.id.btn_next_intro);

        // Isi slide, ada empat
        slides = new int[]{
                R.layout.intro_slide_1,
                R.layout.intro_slide_2,
                R.layout.intro_slide_3,
                R.layout.intro_slide_4
        };

        // Tambah dot-dot
        addBottomDots(0);

        // Atur view pager agar bisa geser antar-slide
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // Atur respon tombol
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLoginActivity();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage = viewPager.getCurrentItem() + 1;
                if (currentPage < slides.length) viewPager.setCurrentItem(currentPage);
                else launchLoginActivity();
            }
        });
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[slides.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(36);
            dots[i].setTextColor(getResources().getColor(R.color.gray));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.white));
    }

    private void launchLoginActivity() {
        FirstTimeLaunchPreference preference = new FirstTimeLaunchPreference(this);
        preference.setIsFirstTimeLaunch(false);
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position == slides.length - 1){
                btnNext.setText(getString(R.string.end_intro));
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText(getString(R.string.next_intro));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    public class ViewPagerAdapter extends PagerAdapter{
        ViewPagerAdapter(){}

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position){
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(slides[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return slides.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object){
            container.removeView((View) object);
        }
    }
}
