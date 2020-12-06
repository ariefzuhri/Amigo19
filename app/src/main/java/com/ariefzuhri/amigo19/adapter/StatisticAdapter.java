package com.ariefzuhri.amigo19.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.model.Statistic;
import com.ariefzuhri.amigo19.response.AttributesGlobal;
import com.ariefzuhri.amigo19.response.AttributesProvince;

import java.util.ArrayList;
import static com.ariefzuhri.amigo19.utils.AppUtils.getDecimalFormat;
import static com.ariefzuhri.amigo19.utils.AppUtils.getFlagIndonesia;
import static com.ariefzuhri.amigo19.utils.AppUtils.getFlagWorld;
import static com.ariefzuhri.amigo19.utils.AppUtils.loadImage;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.StatisticViewHolder> {
    private Activity activity;
    private ArrayList<Statistic> statistic = new ArrayList<>();

    public StatisticAdapter(Activity activity){
        this.activity = activity;
    }

    // Statistik di StatisticDetailActivity
    public void setDataIndonesia(ArrayList<AttributesProvince> indonesiaProvinces){
        statistic.clear();

        for (AttributesProvince province : indonesiaProvinces){
            statistic.add(new Statistic(province.getProvince().getId(),
                    province.getProvince().getName(),
                    province.getProvince().getInfected(),
                    province.getProvince().getRecovered(),
                    province.getProvince().getDeath()));
        }

        notifyDataSetChanged();
    }

    // Statistik di StatisticDetailActivity
    public void setDataGlobal(ArrayList<AttributesGlobal> global){
        statistic.clear();

        for (AttributesGlobal country : global){
            statistic.add(new Statistic(
                    country.getGlobal().getName(),
                    country.getGlobal().getInfected(),
                    country.getGlobal().getRecovered(),
                    country.getGlobal().getDeath()));
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatisticAdapter.StatisticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistic, parent, false);
        return new StatisticViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticAdapter.StatisticViewHolder holder, int position) {
        Statistic statistic = this.statistic.get(position);
        holder.bind(statistic);
    }

    @Override
    public int getItemCount() {
        return statistic.size();
    }

    class StatisticViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFlag;
        TextView tvName, tvInfected, tvDeath, tvRecovered, tvPatient;

        StatisticViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFlag = itemView.findViewById(R.id.img_flag_statistic);
            tvName = itemView.findViewById(R.id.tv_name_statistic);
            tvInfected = itemView.findViewById(R.id.tv_infected_statistic);
            tvDeath = itemView.findViewById(R.id.tv_death_statistic);
            tvRecovered = itemView.findViewById(R.id.tv_recovered_statistic);
            tvPatient = itemView.findViewById(R.id.tv_patient_statistic);
        }

        @SuppressLint("SetTextI18n")
        void bind(Statistic statistic) {
            if (statistic.getId() != 0) loadImage(activity, imgFlag, getFlagIndonesia(activity, statistic.getId()));
            else imgFlag.setImageResource(getFlagWorld(activity, statistic.getName()));

            tvName.setText(getAdapterPosition()+1 + ". " + statistic.getName());
            tvInfected.setText(Html.fromHtml((itemView.getResources().getString(R.string.stats_infected, getDecimalFormat(statistic.getInfected())))));
            tvDeath.setText(Html.fromHtml((itemView.getResources().getString(R.string.stats_death, getDecimalFormat(statistic.getDeath())))));
            tvRecovered.setText(Html.fromHtml((itemView.getResources().getString(R.string.stats_recovered, getDecimalFormat(statistic.getRecovered())))));
            tvPatient.setText(Html.fromHtml((itemView.getResources().getString(R.string.stats_patient, getDecimalFormat(statistic.getPatient())))));
        }
    }
}
