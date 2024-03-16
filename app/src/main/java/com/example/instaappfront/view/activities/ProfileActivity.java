package com.example.instaappfront.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instaappfront.databinding.ActivityProfileBinding;
import com.example.instaappfront.databinding.ChangePasswordBinding;
import com.example.instaappfront.databinding.EditProfileBinding;
import com.example.instaappfront.helpers.AlertST;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.model.abst.Profile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void setProfileData(){
        binding.tvUsername.setText("@" + GlobalData.profile.getUsername());
        binding.tvEmail.setText(GlobalData.profile.getEmail());
        binding.tvPersonName.setText(GlobalData.profile.getFirstName() + " " + GlobalData.profile.getLastName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        if(GlobalData.profile != null){
            if(GlobalData.profile.getPfpUrl() != null) {
                Glide.with(ProfileActivity.this)
                        .load("http://" + GlobalData.serverAddress + "/" + GlobalData.profile.getPfpUrl())
                        .into(binding.pfp);
            }
            setProfileData();
        }

        binding.btnSignOut.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("instaapp_mkurbiel", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("token");
            editor.apply();
            GlobalData.profile = null;
            GlobalData.userPhotoList = null;
            GlobalData.photoList = null;

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.btnChangePfp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        binding.btnEditProfile.setOnClickListener(v -> {
            EditProfileBinding dialogBinding = EditProfileBinding.inflate(getLayoutInflater());
            View dialogView = dialogBinding.getRoot();
            dialogBinding.etFirstName.setText(GlobalData.profile.getFirstName());
            dialogBinding.etLastName.setText(GlobalData.profile.getLastName());
            dialogBinding.etUsername.setText(GlobalData.profile.getUsername());

            AlertST.GetCustomBuilder(ProfileActivity.this, "Update profile")
                    .setView(dialogView)
                    .setNegativeButton("Cancel", (d, i) -> {
                    })
                    .setPositiveButton("Save", (d, i) -> {
                        String firstName = dialogBinding.etFirstName.getText().toString();
                        String userName = dialogBinding.etUsername.getText().toString();
                        String lastName = dialogBinding.etLastName.getText().toString();
                        Call<Profile> call = RetrofitST.getUserAPI().editProfile(userName, firstName, lastName, "Bearer: " + GlobalData.token);
                        call.enqueue(new Callback<Profile>() {
                            @Override
                            public void onResponse(Call<Profile> call, Response<Profile> response) {
                                if(response.isSuccessful()){
                                    GlobalData.profile = response.body();
                                    setProfileData();
                                }else{
                                    Toast.makeText(ProfileActivity.this, "Username can not be empty", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Profile> call, Throwable t) {

                            }
                        });
                    })
                    .show();
        });

        binding.btnChangePassword.setOnClickListener(v -> {
            ChangePasswordBinding dialogBinding = ChangePasswordBinding.inflate(getLayoutInflater());
            View dialogView = dialogBinding.getRoot();

            AlertST.GetCustomBuilder(ProfileActivity.this, "Update profile")
                    .setView(dialogView)
                    .setNegativeButton("Cancel", (d, i) -> {
                    })
                    .setPositiveButton("Save", (d, i) -> {
                        String oldPass = dialogBinding.etOldPass.getText().toString();
                        String newPass = dialogBinding.etNewPass.getText().toString();
                        String confNewPass = dialogBinding.etConfNewPass.getText().toString();
                        if(newPass.equals(confNewPass)) {
                            Call<Profile> call = RetrofitST.getUserAPI().changePassword(oldPass, newPass, "Bearer: " + GlobalData.token);
                            call.enqueue(new Callback<Profile>() {
                                @Override
                                public void onResponse(Call<Profile> call, Response<Profile> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Password changed successfully", Toast.LENGTH_LONG).show();
                                        SharedPreferences sharedPreferences = getSharedPreferences("instaapp_mkurbiel", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove("token");
                                        editor.apply();

                                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }else{
                                        AlertST.BuildErrorAlert(ProfileActivity.this, "Error changing password", "Old password invalid").show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Profile> call, Throwable t) {

                                }
                            });
                        }else{
                            AlertST.BuildErrorAlert(ProfileActivity.this, "Error changing password", "Passwords must match").show();
                        }
                    })
                    .show();
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            GlobalData.photoList = null;
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode == 100 && resultCode == RESULT_OK){
            Uri imgData = data.getData();
            String imgPath = getRealPathFromURI(imgData);
            Log.d("?? imgdata", imgPath);
            File file = new File(imgPath);

            RequestBody fileRequest = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fileRequest);

            Call<Profile> call = RetrofitST.getUserAPI().uploadPfp("Bearer " + GlobalData.token, body);

            call.enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    if(response.isSuccessful()){
                        GlobalData.profile.setPfpUrl(response.body().getPfpUrl());
                        Glide.with(ProfileActivity.this)
                                .load("http://" + GlobalData.serverAddress + "/" + response.body().getPfpUrl())
                                .into(binding.pfp);
                    }
                }

                @Override
                public void onFailure(Call<Profile> call, Throwable t) {
                    AlertST.BuildErrorAlert(ProfileActivity.this, "Error uploading file", t.getMessage()).show();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}