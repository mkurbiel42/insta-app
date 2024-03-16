package com.example.instaappfront.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.instaappfront.databinding.ActivityLoginBinding;
import com.example.instaappfront.helpers.AlertST;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.PermissionsST;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.model.LoginData;
import com.example.instaappfront.model.retrofit.ErrorResponse;
import com.example.instaappfront.model.retrofit.LoginResponse;
import com.example.instaappfront.viewmodel.LoginViewModel;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private String prefToken;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        sharedPreferences = getSharedPreferences("instaapp_mkurbiel", Context.MODE_PRIVATE);
        prefToken = sharedPreferences.getString("token", null);

        if (!PermissionsST.checkIfPermissionsGranted(this)) {
            requestPermissions(PermissionsST.REQUIRED_PERMISSIONS, 100);
        }else{
            if(prefToken != null){
                GlobalData.token = prefToken;

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        viewModel = new ViewModelProvider(LoginActivity.this)
                .get(LoginViewModel.class);
        binding.setLoginViewModel(viewModel);

        viewModel.getObservedLoginData().observe(LoginActivity.this, loginData -> {
            binding.setLoginViewModel(viewModel);
        });

        binding.btnLogin.setOnClickListener(v -> {
            LoginData loginData = viewModel.getObservedLoginData().getValue();
            binding.btnLogin.setEnabled(false);

            Call<LoginResponse> call = RetrofitST.getUserAPI().loginUser(loginData.getEmail(), loginData.getPassword());
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.isSuccessful()){
                        GlobalData.token = response.body().token;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", GlobalData.token);
                        editor.apply();

                        Log.d("?? global token", GlobalData.token);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        try {
                            ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                            AlertST.BuildErrorAlert(LoginActivity.this, "Login unsuccessful", errorResponse.getError()).show();
                            binding.btnLogin.setEnabled(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    AlertST.BuildErrorAlert(LoginActivity.this, "Error while logging in", t.getMessage()).show();
                }
            });

        });

        binding.tvSwitch.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, 1000);
        });

        setContentView(view);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            String email = data != null ? data.getStringExtra("registerEmail") : "";
            viewModel.getObservedLoginData().getValue().setEmail(email);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(prefToken != null){
                        GlobalData.token = prefToken;

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Permissions not granted", Toast.LENGTH_SHORT).show();
                    if(prefToken != null){
                        GlobalData.token = prefToken;

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                break;
        }
    }
}