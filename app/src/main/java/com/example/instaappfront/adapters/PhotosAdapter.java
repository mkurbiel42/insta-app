package com.example.instaappfront.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.instaappfront.R;
import com.example.instaappfront.helpers.GlobalData;
import com.example.instaappfront.helpers.Util;
import com.example.instaappfront.model.abst.Photo;
import com.example.instaappfront.view.activities.PhotoPreviewActivity;
import com.google.android.material.internal.ViewUtils;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private List<Photo> list;
    private Context context;
    private int spanCount;

    private boolean withPfp;

    public PhotosAdapter(List<Photo> list, Context context, int spanCount, boolean withPfp) {
        this.list = list;
        this.context = context;
        this.spanCount = spanCount;
        this.withPfp = withPfp;
    }

    @NonNull
    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.ViewHolder holder, int position) {
        Photo data = list.get(position);

        Glide.with(holder.imageView.getContext())
                .load("http://" + GlobalData.serverAddress + "/" + data.getUrl())
                .into(holder.imageView);

        if(data.isVideo()){
            holder.videoIcon.setVisibility(View.VISIBLE);
        }

        if(this.withPfp){
            holder.pfpWrapper.setVisibility(View.VISIBLE);
            Glide.with(holder.pfp.getContext())
                    .load("http://" + GlobalData.serverAddress + "/api/profile/pfp/" + data.getAlbum())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.pfp);
        }

        holder.root.setOnClickListener(v -> {
            Intent intent = new Intent(context, PhotoPreviewActivity.class);
            intent.putExtra("url", data.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private CardView root;
        private ImageView videoIcon;
        private ImageView pfp;
        private CardView pfpWrapper;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgv_photo);
            root = itemView.findViewById(R.id.photo_root);
            videoIcon = itemView.findViewById(R.id.video_icon);
            pfp = itemView.findViewById(R.id.pfp);
            pfpWrapper = itemView.findViewById(R.id.pfpWrapper);
        }
    }
}
