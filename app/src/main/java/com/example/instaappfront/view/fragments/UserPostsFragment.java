package com.example.instaappfront.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.instaappfront.R;
import com.example.instaappfront.adapters.PhotosAdapter;
import com.example.instaappfront.databinding.FragmentUserPostsBinding;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.model.abst.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPostsFragment extends Fragment {
    private FragmentUserPostsBinding binding;

    public void listPhotos(){
        int spanCount = 3;
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy (StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        binding.recycler.setLayoutManager(staggeredGridLayoutManager);
        PhotosAdapter adapter = new PhotosAdapter(GlobalData.userPhotoList, getContext(), spanCount, false);
        binding.recycler.setAdapter(adapter);
    }

    public void getPhotos(){
        Call<List<Photo>> call = RetrofitST.getFilesAPI().getUserPhotos("Bearer " + GlobalData.token);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                GlobalData.userPhotoList = response.body();
                listPhotos();
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserPostsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.recycler.setHasFixedSize(true);

        if(GlobalData.userPhotoList == null){
            getPhotos();
        }else{
            listPhotos();
        }

        return view;
    }
}