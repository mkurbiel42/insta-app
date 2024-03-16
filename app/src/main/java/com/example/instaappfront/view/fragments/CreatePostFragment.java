package com.example.instaappfront.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instaappfront.R;
import com.example.instaappfront.databinding.FragmentCreatePostBinding;
import com.example.instaappfront.helpers.AlertST;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.PermissionsST;
import com.example.instaappfront.helpers.RetrofitST;
import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.view.activities.SavePostActivity;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostFragment extends Fragment {
    private FragmentCreatePostBinding binding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;
    private boolean isRecording = false;

    @SuppressLint("RestrictedApi")
    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider, PreviewView previewView) {
        if(previewView == null) return;
        if(previewView.getDisplay() == null) return;
        Preview preview = new Preview.Builder().build();

        imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .build();

        videoCapture =
                new VideoCapture.Builder()
                    .setTargetRotation(previewView.getDisplay().getRotation())
                    .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, videoCapture, preview);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreatePostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        if (!PermissionsST.checkIfPermissionsGranted(getContext())) {
            AlertST.BuildErrorAlert(getContext(), "Error", "Permissions for this action has not been granted");
        } else {
            cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider, binding.camera);
                } catch (InterruptedException | ExecutionException e) {
                    Log.d("?? camera future error", "guh? how did we get here?");
                }
            }, ContextCompat.getMainExecutor(getContext()));
        }

        binding.btnPhoto.setOnClickListener(v -> {
            takeAPhoto();
        });

        binding.btnRecord.setOnClickListener(v -> {
            if(!isRecording){
                startRecording();
            }else{
                saveRecording();
            }
        });


        return view;
    }

    private void takeAPhoto(){
        File dir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photos_kurbiel");
        String name = String.valueOf((int) System.currentTimeMillis());
        boolean isDirectoryCreated = dir.exists() || dir.mkdirs();

        if (isDirectoryCreated) {
            File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/photos_kurbiel/", "" + name + ".jpg");
            ImageCapture.OutputFileOptions outputFileOptions =
                    new ImageCapture.OutputFileOptions.Builder(file).build();

            imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(getContext()),
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            RequestBody fileRequest = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fileRequest);

                            Call<Photo> call = RetrofitST.getFilesAPI().uploadFile("Bearer " + GlobalData.token, body);
                            call.enqueue(new Callback<Photo>() {
                                @Override
                                public void onResponse(Call<Photo> call, Response<Photo> response) {
                                    Log.d("?? upload", response.body().toString());
                                    Intent intent = new Intent(getContext(), SavePostActivity.class);
                                    intent.putExtra("url", response.body().getOriginalUrl());
                                    intent.putExtra("id", response.body().getId());
                                    getContext().startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<Photo> call, Throwable t) {
                                    AlertST.BuildErrorAlert(getContext(), "Error uploading file", t.getMessage()).show();
                                }
                            });
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception) {

                        }
                    });
        }
    }

    @SuppressLint("RestrictedApi")
    private void startRecording(){
        String name = String.valueOf((int) System.currentTimeMillis());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            isRecording = true;
            binding.btnRecord.setBackgroundResource(R.drawable.outline_red_fill);
            Toast.makeText(getContext(), "recording started", Toast.LENGTH_SHORT).show();

            File file = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/photos_kurbiel/", "" + name + ".mp4");
            VideoCapture.OutputFileOptions outputFileOptions =
                    new VideoCapture.OutputFileOptions.Builder(file).build();

            videoCapture.startRecording(
                    outputFileOptions,
                    ContextCompat.getMainExecutor(getContext()),
                    new VideoCapture.OnVideoSavedCallback() {
                        @Override
                        public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                            RequestBody fileRequest = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fileRequest);

                            Call<Photo> call = RetrofitST.getFilesAPI().uploadFile("Bearer " + GlobalData.token, body);
                            call.enqueue(new Callback<Photo>() {
                                @Override
                                public void onResponse(Call<Photo> call, Response<Photo> response) {
                                    Log.d("?? upload", response.body().toString());
                                    Intent intent = new Intent(getContext(), SavePostActivity.class);
                                    intent.putExtra("video_url", response.body().getOriginalUrl());
                                    intent.putExtra("id", response.body().getId());
                                    getContext().startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<Photo> call, Throwable t) {
                                    AlertST.BuildErrorAlert(getContext(), "Error uploading file", t.getMessage()).show();
                                }
                            });
                        }

                        @Override
                        public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                            // error
                        }
                    });
        }
    }

    @SuppressLint("RestrictedApi")
    private void saveRecording(){
        isRecording = false;
        videoCapture.stopRecording();
        binding.btnRecord.setBackgroundResource(R.drawable.outline);
    }
}