package com.ariefzuhri.amigo19.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.activity.EducationDetailActivity;
import com.ariefzuhri.amigo19.model.Education;
import java.util.ArrayList;
import static com.ariefzuhri.amigo19.activity.EducationDetailActivity.EXTRA_INDEX_EDUCATION;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationViewHolder> {
    private ArrayList<Education> listEducations = new ArrayList<>();
    private Activity activity;

    public EducationAdapter(Activity activity){
        this.activity = activity;
    }

    public void setData(String[] topic, String[] desc){
        for (int i = 0; i < topic.length; i++) listEducations.add(new Education(topic[i], desc[i]));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EducationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_education, parent, false);
        return new EducationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationViewHolder holder, final int position) {
        Education education = listEducations.get(position);
        holder.bind(education);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EducationDetailActivity.class);
                intent.putExtra(EXTRA_INDEX_EDUCATION, position);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listEducations.size();
    }

    static class EducationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopic, tvDesc;

        EducationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopic = itemView.findViewById(R.id.tv_topic_education);
            tvDesc = itemView.findViewById(R.id.tv_desc_education);
        }

        void bind(Education education) {
            tvTopic.setText(education.getTopic());
            tvDesc.setText(education.getDesc());
        }
    }
}
