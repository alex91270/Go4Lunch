package com.example.go4lunchAlx.ui.list;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.models.Restaurant;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

    private final List<Restaurant> mRestaurants;
    private Context mContext;

    public ListRecyclerViewAdapter(List<Restaurant> items) {
        mRestaurants = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Restaurant restaurant = mRestaurants.get(position);

        if (restaurant.getName().length() > 22) {
            holder.mRestaurantName.setText(restaurant.getName().substring(0, 19) + "...");
        } else {
            holder.mRestaurantName.setText(restaurant.getName());
        }

        if (restaurant.getVicinity().length() > 30) {
            holder.mRestaurantAddress.setText(restaurant.getVicinity().substring(0, 27) + "...");
        } else {
            holder.mRestaurantAddress.setText(restaurant.getVicinity());
        }

        holder.mRestaurantAttendants.setText("(" + restaurant.getAttendants().size() + ")");
        holder.mRestaurantDistance.setText(restaurant.getDistance() + "m");
        holder.mRestaurantOpening.setText(restaurant.getOpening());
        if(restaurant.getRating()>=1) holder.mStar1.setVisibility(View.VISIBLE);
        if(restaurant.getRating()>=2) holder.mStar2.setVisibility(View.VISIBLE);
        if(restaurant.getRating()>=3) holder.mStar3.setVisibility(View.VISIBLE);
        if(restaurant.getRating()>=4) holder.mStar4.setVisibility(View.VISIBLE);
        if(restaurant.getRating()==5) holder.mStar5.setVisibility(View.VISIBLE);

        if (restaurant.getPhoto() == "no_pic") {
            holder.mRestaurantPhoto.setImageResource(R.drawable.resto_sign);
        } else {
            mContext = holder.mRestaurantPhoto.getContext();
            String apiKey = mContext.getString(R.string.google_maps_key);
            String photoRef = restaurant.getPhoto();

            String photoURL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + photoRef + "&sensor=false&maxheight=150&maxwidth=150&key=" + apiKey;

            Glide.with(holder.mRestaurantPhoto.getContext())
                    .load(photoURL)
                    .apply(RequestOptions.centerCropTransform())
                    .into(holder.mRestaurantPhoto);
        }

    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_name)
        public TextView mRestaurantName;
        @BindView(R.id.item_address)
        public TextView mRestaurantAddress;
        @BindView(R.id.item_attendants)
        public TextView mRestaurantAttendants;
        @BindView(R.id.item_distance)
        public TextView mRestaurantDistance;
        @BindView(R.id.item_opening)
        public TextView mRestaurantOpening;
        @BindView(R.id.item_photo)
        public ImageView mRestaurantPhoto;

        @BindView(R.id.star1)
        public ImageView mStar1;
        @BindView(R.id.star2)
        public ImageView mStar2;
        @BindView(R.id.star3)
        public ImageView mStar3;
        @BindView(R.id.star4)
        public ImageView mStar4;
        @BindView(R.id.star5)
        public ImageView mStar5;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
