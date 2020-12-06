package com.ariefzuhri.amigo19.pageradapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.model.EducationDetail;
import java.util.ArrayList;

public class EducationDetailPagerAdapter extends PagerAdapter {
    private ArrayList<EducationDetail> educationDetails;
    private Context context;

    public EducationDetailPagerAdapter(ArrayList<EducationDetail> educationDetails, Context context){
        this.educationDetails = educationDetails;
        this.context = context;
    }

    @Override
    public int getCount() {
        return educationDetails.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_education_detail, container, false);

        ImageView imgBackground = view.findViewById(R.id.img_background_education_detail);
        imgBackground.setImageResource(educationDetails.get(position).getImage());

        TextView tvContent = view.findViewById(R.id.tv_content_education_detail);
        tvContent.setText(educationDetails.get(position).getContent());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
