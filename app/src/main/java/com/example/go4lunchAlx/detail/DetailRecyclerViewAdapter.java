package com.example.go4lunchAlx.detail;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Rating;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ViewHolder> {

    private final List<User> mWorkmates;
    private Context mContext;
    //private static View.OnClickListener clickListener;
    private RestApiService service = DI.getRestApiService();

    public DetailRecyclerViewAdapter(List<User> items) {
        mWorkmates = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workmate, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = mWorkmates.get(position);

        if (user.getUrlPicture() == null) {
            holder.mWorkmatePicture.setImageResource(R.drawable.avatar);
        } else {
            Glide.with(holder.mWorkmatePicture.getContext())
                    .load(user.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mWorkmatePicture);
        }

        holder.mWorkmateName.setText(user.getUsername() + " is joining!");
    }

    @Override
    public int getItemCount() {
        return mWorkmates.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_mate_photo)
        public ImageView mWorkmatePicture;
        @BindView(R.id.item_mate_name)
        public TextView mWorkmateName;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
