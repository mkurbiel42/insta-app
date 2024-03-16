package com.example.instaappfront.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instaappfront.databinding.FragmentFiltersBinding;
import com.example.instaappfront.helpers.AlertST;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.model.abst.FilterData;
import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.model.retrofit.FilterCall;
import com.example.instaappfront.view.activities.SavePostActivity;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiltersFragment extends Fragment {
    private SavePostActivity parentActivity;
    private void filterPhoto(long id, String filterType, FilterData filterData){
        FilterCall filterCall = new FilterCall(filterData, filterType, id);
        Call<Photo> call = RetrofitST.getFilesAPI().filterPhoto("Bearer: " + GlobalData.token, filterCall);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if(response.isSuccessful()) {
                    parentActivity.url = response.body().getUrl();
                    Log.d("?? filter", response.body().toString());
                    parentActivity.setImage(parentActivity.url);
                    parentActivity.closeUpperDrawer();
                }else{
                    Toast.makeText(getContext(), "Could not filter", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {

            }
        });
    }
    private FragmentFiltersBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFiltersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        parentActivity = (SavePostActivity) requireActivity();
        long id = parentActivity.id;

        binding.flip.setOnClickListener(v -> {
            filterPhoto(id, "flip", null);
        });

        binding.flop.setOnClickListener(v -> {
            filterPhoto(id, "flop", null);
        });

        binding.negate.setOnClickListener(v -> {
            filterPhoto(id, "negate", null);
        });

        binding.grayscale.setOnClickListener(v -> {
            filterPhoto(id, "grayscale", null);
        });

        binding.rotateLeft.setOnClickListener(v -> {
            FilterData filterData = new FilterData();
            filterData.setRotation(90);
            filterPhoto(id, "rotate", filterData);
        });

        binding.rotateRight.setOnClickListener(v -> {
            FilterData filterData = new FilterData();
            filterData.setRotation(270);
            filterPhoto(id, "rotate", filterData);
        });

        binding.rotate180.setOnClickListener(v -> {
            FilterData filterData = new FilterData();
            filterData.setRotation(180);
            filterPhoto(id, "rotate", filterData);
        });

        return view;
    }
}