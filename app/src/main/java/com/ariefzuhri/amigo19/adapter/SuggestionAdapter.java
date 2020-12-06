package com.ariefzuhri.amigo19.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ariefzuhri.amigo19.R;
import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {
    private Activity activity;
    private AlertDialog.Builder dialog;
    private List<Integer> indexSuggestion;
    private ArrayList<String> listSuggestions = new ArrayList<>();

    public SuggestionAdapter(Activity activity){
        this.activity = activity;
        dialog = new AlertDialog.Builder(activity);
    }
    
    public void setData(List<Integer> indexSuggestion){
        String[] suggestions = activity.getResources().getStringArray(R.array.personal_suggestion);
        for (int i = 7; i < suggestions.length; i++) indexSuggestion.add(i); // General sug mulai dari index 7

        this.indexSuggestion = indexSuggestion;
        for (int index : indexSuggestion) listSuggestions.add(suggestions[index]);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion_personal, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, final int position) {
        String suggestion = listSuggestions.get(position);
        holder.bind(suggestion);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage(activity.getResources().getStringArray(R.array.personal_suggestion_detail)[indexSuggestion.get(position)]);
                dialog.setNeutralButton(R.string.dialog_ok, null);
                dialog.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSuggestions.size();
    }

    static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSuggestion;

        SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSuggestion = itemView.findViewById(R.id.tv_title_suggestion_personal);
        }

        void bind(String suggestion) {
            tvSuggestion.setText(suggestion);
        }
    }
}
