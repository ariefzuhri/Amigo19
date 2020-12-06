package com.ariefzuhri.amigo19.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ariefzuhri.amigo19.R;
import com.ariefzuhri.amigo19.activity.HospitalActivity;
import com.ariefzuhri.amigo19.adapter.EducationAdapter;
import com.ornach.nobobutton.NoboButton;

public class EducationFragment extends Fragment {

    public EducationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_education, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState){
        RecyclerView rvEducation = view.findViewById(R.id.rv_education);
        rvEducation.setLayoutManager(new LinearLayoutManager(getActivity()));
        EducationAdapter adapter = new EducationAdapter(getActivity());
        adapter.notifyDataSetChanged();
        rvEducation.setAdapter(adapter);

        String[] topic = getActivity().getResources().getStringArray(R.array.topic_education);
        String[] desc = getActivity().getResources().getStringArray(R.array.desc_education);
        adapter.setData(topic, desc);

        NoboButton btnHospital = view.findViewById(R.id.btn_hospital_education);
        btnHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HospitalActivity.class);
                startActivity(intent);
            }
        });
    }
}
