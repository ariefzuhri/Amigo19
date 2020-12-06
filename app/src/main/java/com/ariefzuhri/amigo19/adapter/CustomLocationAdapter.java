package com.ariefzuhri.amigo19.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.activity.SettingLocationActivity;
import com.ariefzuhri.amigo19.model.Region;
import java.util.ArrayList;
import static com.ariefzuhri.amigo19.activity.CustomLocationActivity.EXTRA_CUSTOM_LOCATION;
import static com.ariefzuhri.amigo19.activity.CustomLocationActivity.RC_CUSTOM_LOCATION;

public class CustomLocationAdapter extends RecyclerView.Adapter<CustomLocationAdapter.CustomLocationViewHolder> implements Filterable {
    private Activity activity;
    private ArrayList<Region> listRegions = new ArrayList<>();
    private ArrayList<Region> listRegionsFiltered = new ArrayList<>();

    public CustomLocationAdapter(Activity activity){
        this.activity = activity;
    }

    public void setData(ArrayList<Region> listRegions){
        this.listRegions = listRegions;
        this.listRegionsFiltered = listRegions;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter(){
        return filter;
    }

    private final Filter filter = new Filter() {
        // Run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Region> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(listRegions);
            } else {
                for (Region region : listRegions) {
                    if (region.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(region);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        // Run on a UI thread
        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listRegionsFiltered = (ArrayList<Region>) results.values;
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public CustomLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_location, parent, false);
        return new CustomLocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomLocationViewHolder holder, final int position) {
        Region region = listRegionsFiltered.get(position);
        holder.bind(region);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, SettingLocationActivity.class);
                intent.putExtra(EXTRA_CUSTOM_LOCATION, listRegionsFiltered.get(position).getName());
                activity.setResult(RC_CUSTOM_LOCATION, intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRegionsFiltered.size();
    }

    static class CustomLocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        CustomLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_cla);
        }

        void bind(Region region) {
            tvName.setText(region.getName());
        }
    }
}
