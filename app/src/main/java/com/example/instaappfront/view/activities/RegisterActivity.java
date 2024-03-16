package com.example.instaappfront.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.instaappfront.R;
import com.example.instaappfront.databinding.ActivityRegisterBinding;
import com.example.instaappfront.helpers.AlertST;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.model.RegisterData;
import com.example.instaappfront.model.retrofit.ErrorResponse;
import com.example.instaappfront.model.retrofit.RegisterResponse;
import com.example.instaappfront.viewmodel.RegisterViewModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(RegisterActivity.this)
                .get(RegisterViewModel.class);
        binding.setRegisterViewModel(viewModel);

        viewModel.getObservedRegisterData().observe(RegisterActivity.this, loginData -> {
            binding.setRegisterViewModel(viewModel);
        });

        binding.btnRegister.setOnClickListener(v -> {
            RegisterData registerData = viewModel.getObservedRegisterData().getValue();

            if(!Objects.equals(registerData.getPassword(), registerData.getConfirmPassword())){
                AlertST.BuildErrorAlert(RegisterActivity.this, "Register unsuccessful", "Passwords must match").show();
                return;
            }

            Call<RegisterResponse> call = RetrofitST.getUserAPI().registerUser(registerData.getEmail(), registerData.getUsername(), registerData.getPassword(), registerData.getFirstName(), registerData.getLastName());
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        View dialogMessage = getLayoutInflater().inflate(R.layout.dialog_textview, null);
                        ((TextView) dialogMessage.findViewById(R.id.tv_cdialog)).setText(response.body().message);

                        AlertST.GetCustomBuilder(RegisterActivity.this, "Confirm register")
                                .setView(dialogMessage)
                                .setPositiveButton("OK", (d, i) -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("registerEmail", viewModel.getObservedRegisterData().getValue().getEmail());
                                    setResult(1000, intent);
                                    RegisterActivity.this.finish();
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    } else {
                        try {
                            ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                            AlertST.BuildErrorAlert(RegisterActivity.this, "Register unsuccessful", errorResponse.getError()).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    AlertST.BuildErrorAlert(RegisterActivity.this, "Error while registering", t.getMessage()).show();
                }
            });
        });


        setContentView(view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}