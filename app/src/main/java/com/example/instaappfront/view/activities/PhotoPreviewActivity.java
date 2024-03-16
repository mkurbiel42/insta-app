package com.example.instaappfront.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.instaappfront.R;
import com.example.instaappfront.databinding.ActivityPhotoPreviewBinding;
import com.example.instaappfront.databinding.TagChipBinding;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.model.abst.Tag;
import com.example.instaappfront.viewmodel.PhotoInfoViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoPreviewActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener {
    private ActivityPhotoPreviewBinding binding;
    private ExoPlayer player;
    private GoogleMap map;
    private SupportMapFragment supportMapFragment;
    private Photo photoData;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotoPreviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle dataBundle = getIntent().getExtras();
        String url = dataBundle.getString("url");

        photoData = (Photo) GlobalData.photoList.stream().filter(p -> p.getUrl().equals(url)).toArray()[0];

        Glide.with(PhotoPreviewActivity.this)
                .load("http://" + GlobalData.serverAddress + "/api/profile/pfp/" + photoData.getAlbum())
                .into(binding.pfp);

        Call<String> call = RetrofitST.getUserAPI().getUsername(photoData.getAlbum(), "Bearer: " + GlobalData.token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    binding.tvUsername.setText("@" + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        if(photoData.isVideo()){
            player = new ExoPlayer.Builder(PhotoPreviewActivity.this).build();
            binding.vidvPreview.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri("http://" + GlobalData.serverAddress + "/" + photoData.getUrl());
            player.setMediaItem(mediaItem);
            player.prepare();
        }else{
            Glide.with(PhotoPreviewActivity.this)
                    .load("http://" + GlobalData.serverAddress + "/" + photoData.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imgvPreview);
            binding.vidvPreview.setVisibility(View.GONE);
        }

        for(Tag tag : photoData.getTags()){
            TagChipBinding chipBinding = TagChipBinding.inflate(getLayoutInflater());
            Chip chip = (Chip) chipBinding.getRoot();

            chipBinding.chipItemRoot.setText(tag.getName());
            chip.setCloseIconVisible(false);

            binding.tagsWrapper.addView(chip);
        }

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frame_map);

        supportMapFragment.getMapAsync(this);

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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(PhotoPreviewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PhotoPreviewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setOnCameraMoveStartedListener(PhotoPreviewActivity.this);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setCompassEnabled(true);

        LatLng photoLatLng = new LatLng(photoData.getPlace().getLat(), photoData.getPlace().getLng());

        map.addMarker(new MarkerOptions().position(photoLatLng).title(photoData.getPlace().getName()));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(photoLatLng, 15);
        map.moveCamera(cameraUpdate);
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            binding.scrollte.requestDisallowInterceptTouchEvent(true);

        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            binding.scrollte.requestDisallowInterceptTouchEvent(true);

        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            binding.scrollte.requestDisallowInterceptTouchEvent(true);
        }
    }
}