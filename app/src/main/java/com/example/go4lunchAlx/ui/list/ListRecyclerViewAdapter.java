package com.example.go4lunchAlx.ui.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.go4lunchAlx.ui.detail.DetailActivity;
import com.example.go4lunchAlx.service.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

    private List<Restaurant> mRestaurants;
    private Context mContext;
    private RestApiService service = DI.getRestApiService();

    public ListRecyclerViewAdapter(Context context, List<Restaurant> items) {
        mRestaurants = items;
        mContext = context;
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
        if (restaurant.getOpening().equals(mContext.getString(R.string.closing_soon))) {
            holder.mRestaurantOpening.setTextColor(Color.RED);
        } else {
            holder.mRestaurantOpening.setTextColor(Color.BLACK);
        }

        holder.yellowBackground.getLayoutParams().width = ((int)(Math.round(service.getRate(restaurant)*0.2*holder.mStars.getLayoutParams().width))) ;

        if (restaurant.getPhoto() == null) {
            holder.mRestaurantPhoto.setImageResource(R.drawable.resto_sign150);
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

        holder.mLayoutRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("restoId", restaurant.getId());
                mContext.startActivity(intent);
            }
        });
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
        @BindView(R.id.layout_restaurant)
        public ConstraintLayout mLayoutRestaurant;

        @BindView(R.id.stars)
        public ImageView mStars;
        @BindView(R.id.yellowBackground)
        public ImageView yellowBackground;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    void updateRestos(@NonNull final List<Restaurant> restos) {
        this.mRestaurants = restos;
        notifyDataSetChanged();
    }
}
