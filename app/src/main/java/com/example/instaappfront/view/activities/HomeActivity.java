package com.example.instaappfront.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.instaappfront.R;
import com.example.instaappfront.databinding.ActivityHomeBinding;
import com.example.instaappfront.helpers.FragmentsST;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.model.abst.Profile;
import com.example.instaappfront.view.fragments.AllPostsFragment;
import com.example.instaappfront.view.fragments.CreatePostFragment;
import com.example.instaappfront.view.fragments.UserPostsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private AllPostsFragment allPosts;
    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        allPosts = new AllPostsFragment();
        CreatePostFragment createPost = new CreatePostFragment();
        UserPostsFragment userPosts = new UserPostsFragment();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if(GlobalData.profile == null){
            Call<Profile> call = RetrofitST.getUserAPI().getProfile("Bearer: " + GlobalData.token);
            call.enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    if(response.isSuccessful()){
                        Log.d("?? profile", response.body().toString());

                        GlobalData.profile = response.body();
                        binding.tvUsername.setText("@" + GlobalData.profile.getUsername());
                        if(GlobalData.profile.getPfpUrl() != null){
                            Glide.with(HomeActivity.this)
                                    .load("http://" + GlobalData.serverAddress + "/" + GlobalData.profile.getPfpUrl())
                                    .into(binding.pfp);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    Log.d("?? profile failure", t.getMessage());
                }
            });
        }else if(GlobalData.profile.getPfpUrl() != null){
            Glide.with(HomeActivity.this)
                    .load("http://" + GlobalData.serverAddress + "/" + GlobalData.profile.getPfpUrl())
                    .into(binding.pfp);
        }


        binding.pfpWrapper.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivityForResult(intent, 123);
        });

        binding.menuBottom.setSelectedItemId(R.id.menu_all);
        FragmentsST.replaceFragment(getSupportFragmentManager(), binding.frame.getId(), allPosts);

        binding.menuBottom.setOnItemSelectedListener(v -> {
            switch(v.getItemId()){
                case R.id.menu_posts:
                    FragmentsST.replaceFragment(getSupportFragmentManager(), binding.frame.getId(), userPosts);
                    break;

                case R.id.menu_all:
                    FragmentsST.replaceFragment(getSupportFragmentManager(), binding.frame.getId(), allPosts);
                    break;

                case R.id.menu_create:
                    FragmentsST.replaceFragment(getSupportFragmentManager(), binding.frame.getId(), createPost);
                    break;
                default:
                    break;
            }

            return true;
        });

        setContentView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 123){
            if(GlobalData.profile.getPfpUrl() != null){
                Glide.with(HomeActivity.this)
                        .load("http://" + GlobalData.serverAddress + "/" + GlobalData.profile.getPfpUrl())
                        .into(binding.pfp);
            }

            allPosts.getPhotos();
            binding.tvUsername.setText("@" + GlobalData.profile.getUsername());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}