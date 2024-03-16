package com.example.instaappfront.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import android.content.Context;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instaappfront.R;
import com.example.instaappfront.databinding.ActivitySavePostBinding;
import com.example.instaappfront.helpers.AlertST;
import com.example.instaappfront.helpers.FragmentsST;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.helpers.Util;
import com.example.instaappfront.model.abst.FilterData;
import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.model.retrofit.FilterCall;
import com.example.instaappfront.model.retrofit.PhotoInfo;
import com.example.instaappfront.view.fragments.FiltersFragment;
import com.example.instaappfront.view.fragments.MapFragment;
import com.example.instaappfront.view.fragments.TagsFragment;
import com.example.instaappfront.viewmodel.LoginViewModel;
import com.example.instaappfront.viewmodel.PhotoInfoViewModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavePostActivity extends AppCompatActivity {
    private ActivitySavePostBinding binding;
    private Transition upperPosition;
    private static int transitionLength = 250;
    public PhotoInfoViewModel viewModel;
    private ExoPlayer player;
    public String url;
    public String videoUrl;
    public long id;

    public void setImage(String url){
        Glide.with(SavePostActivity.this)
                .load("http://" + GlobalData.serverAddress + "/" + url)
                .into(binding.imgvPostPhoto);
    }
    public void openUpperDrawer(){
        TransitionManager.beginDelayedTransition((ViewGroup) binding.upper, upperPosition);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.upper.getLayoutParams();
        params.topMargin = (int) Util.pxFromDp(SavePostActivity.this, -50);
        binding.upper.setLayoutParams(params);
        binding.backdrop.animate().alpha(1.0f).setDuration(transitionLength);
    }

    public void closeUpperDrawer(){
        TransitionManager.beginDelayedTransition((ViewGroup) binding.upper, upperPosition);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.upper.getLayoutParams();
        params.topMargin = (int) Util.pxFromDp(SavePostActivity.this, -600);
        binding.upper.setLayoutParams(params);
        binding.backdrop.animate().alpha(0.0f).setDuration(transitionLength);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavePostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle dataBundle = getIntent().getExtras();
        url = dataBundle.getString("url");
        videoUrl = dataBundle.getString("video_url");
        id = dataBundle.getLong("id");

        Log.d("?? id", ""+id);
        upperPosition = new ChangeBounds();
        upperPosition.setDuration(transitionLength);

        if(url != null){
            Log.d("?? image", url);
            setImage(url);
            binding.vidvPostVideo.setVisibility(View.GONE);
        }else{
            Log.d("?? video", videoUrl);
            player = new ExoPlayer.Builder(SavePostActivity.this).build();
            binding.vidvPostVideo.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri("http://" + GlobalData.serverAddress + "/" + videoUrl);
            player.setMediaItem(mediaItem);
            player.prepare();
            binding.imgvPostPhoto.setVisibility(View.GONE);
            binding.btnFilters.setVisibility(View.GONE);
        }

        viewModel = new ViewModelProvider(SavePostActivity.this)
                .get(PhotoInfoViewModel.class);

        Call<List<String>> call = RetrofitST.getTagsAPI().getTags("Bearer " + GlobalData.token);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    viewModel.setObservedTagsList(response.body());
                }else{
                    Log.d("?? tags call", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d("?? tags call", t.getMessage());
            }
        });

        SettingsClient settingsClient = LocationServices.getSettingsClient(SavePostActivity.this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(SavePostActivity.this, 150);
                } catch (IntentSender.SendIntentException sendEx) {
                }
            }
        });

        TagsFragment tagsFragment = new TagsFragment();
        MapFragment mapFragment = new MapFragment();
        FiltersFragment filtersFragment = new FiltersFragment();

        binding.btnTags.setOnClickListener(v -> {
            FragmentsST.replaceFragment(getSupportFragmentManager(), binding.frameUpper.getId(), tagsFragment);
            openUpperDrawer();
        });

        binding.btnLocation.setOnClickListener(v -> {
            FragmentsST.replaceFragment(getSupportFragmentManager(), binding.frameUpper.getId(), mapFragment);
            openUpperDrawer();
        });

        binding.btnFilters.setOnClickListener(v -> {
            FragmentsST.replaceFragment(getSupportFragmentManager(), binding.frameUpper.getId(), filtersFragment);
            openUpperDrawer();
        });

        binding.btnSaveSettings.setOnClickListener(v -> {
            closeUpperDrawer();
        });

        binding.btnSend.setOnClickListener(v -> {
            if(viewModel.getObservedPlace().getValue() == null || viewModel.getObservedTagsList().getValue() == null){
                AlertST.BuildErrorAlert(SavePostActivity.this, "Information missing", "Place or tags are missing").show();
                return;
            }

            Call<Photo> patchCall = RetrofitST.getFilesAPI().patchPhoto("Bearer " + GlobalData.token, new PhotoInfo(id, viewModel.getObservedPlace().getValue(), viewModel.getObservedTagsList().getValue()));
            patchCall.enqueue(new Callback<Photo>() {
                @Override
                public void onResponse(Call<Photo> call, Response<Photo> response) {
                    GlobalData.photoList = null;
                    GlobalData.userPhotoList = null;
                    if(player != null){
                        player.release();
                    }
                    SavePostActivity.this.finish();
                }

                @Override
                public void onFailure(Call<Photo> call, Throwable t) {
                    Toast.makeText(SavePostActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        setContentView(view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            if(player != null){
                player.release();
            }
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}