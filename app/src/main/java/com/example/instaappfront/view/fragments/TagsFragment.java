package com.example.instaappfront.view.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instaappfront.R;
import com.example.instaappfront.api.TagsAPI;
import com.example.instaappfront.databinding.FragmentTagsBinding;
import com.example.instaappfront.databinding.TagChipBinding;
import com.example.instaappfront.helpers.AlertST;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.helpers.Util;
import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.model.abst.Tag;
import com.example.instaappfront.model.retrofit.ErrorResponse;
import com.example.instaappfront.view.activities.LoginActivity;
import com.example.instaappfront.view.activities.SavePostActivity;
import com.example.instaappfront.viewmodel.PhotoInfoViewModel;
import com.example.instaappfront.viewmodel.PhotoListViewModel;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagsFragment extends Fragment {
    private FragmentTagsBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTagsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        Log.d("?? token ig", GlobalData.token);
        PhotoInfoViewModel viewModel = ((SavePostActivity) getActivity()).viewModel;

        viewModel.getObservedTagsList().observe(getViewLifecycleOwner(), d -> {
            Log.d("?? viewmodel tags", d.toString());

            binding.tagsWrapper.removeAllViews();

            for(String tagName : viewModel.getObservedTagsList().getValue()){
                TagChipBinding chipBinding = TagChipBinding.inflate(getLayoutInflater());
                Chip chip = (Chip) chipBinding.getRoot();

                chipBinding.chipItemRoot.setText(tagName);

                binding.tagsWrapper.addView(chip);

                chipBinding.chipItemRoot.setOnCloseIconClickListener(v -> {
                    viewModel.removeFromTagsList(tagName);
                });
            }
        });

        binding.btnAddTag.setOnClickListener(v -> {
            if(viewModel.getObservedTagsList().getValue().contains(binding.etTag.getText().toString())) return;

            Call<Tag> call = RetrofitST.getTagsAPI().postNewTag("Bearer " + GlobalData.token, binding.etTag.getText().toString(), Util.random.nextInt(550) + 50);

            call.enqueue(new Callback<Tag>() {
                @Override
                public void onResponse(Call<Tag> call, Response<Tag> response) {
                    if(response.isSuccessful()){
                        viewModel.addToTagsList(response.body().getName());
                    }else {
                        try {
                            ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                            Toast.makeText(getContext(), errorResponse.getError(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Tag> call, Throwable t) {
                    Log.d("?? add tag", t.getMessage());
                }
            });
        });


        return view;
    }
}