package com.example.go4lunchAlx.ui.mates;

import android.content.Context;
import android.content.Intent;
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
import com.example.go4lunchAlx.detail.DetailActivity;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatesRecyclerViewAdapter extends RecyclerView.Adapter<MatesRecyclerViewAdapter.ViewHolder> {

    private final List<User> mUsers;
    private Context mContext;
    private static View.OnClickListener clickListener;

    public MatesRecyclerViewAdapter(List<User> items) {
        mUsers = items;
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

        User user =mUsers.get(position);

        holder.mMateName.setText(user.getUsername());

        if (user.getSelectedRestaurant() == null) {
            holder.mWhereEats.setText("Has not yet decided");
        } else {
            holder.mWhereEats.setText("eats at " + user.getSelectedRestaurant());
        }

        if (user.getUrlPicture() == "no_pic" || user.getUrlPicture() == null) {
            holder.mMatePhoto.setImageResource(R.drawable.avatar);
        } else {
            mContext = holder.mMatePhoto.getContext();
            String apiKey = mContext.getString(R.string.google_maps_key);

            String photoURL = user.getUrlPicture();

            Glide.with(holder.mMatePhoto.getContext())
                    .load(photoURL)
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.mMatePhoto);
        }

        holder.mLayoutMate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                //intent.putExtra("id", restaurant.getId());
                //intent.putExtra("pictureRef", restaurant.getPhoto());
                //mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_mate_name)
        public TextView mMateName;
        @BindView(R.id.item_where_eats)
        public TextView mWhereEats;
        @BindView(R.id.item_mate_photo)
        public ImageView mMatePhoto;
        @BindView(R.id.layout_workmate)
        public ConstraintLayout mLayoutMate;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }
}
