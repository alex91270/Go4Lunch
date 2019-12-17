package com.example.go4lunchAlx.ui.mates;

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
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.models.User;
import com.example.go4lunchAlx.service.RestApiService;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MatesRecyclerViewAdapter extends RecyclerView.Adapter<MatesRecyclerViewAdapter.ViewHolder> {

    private List<User> mUsers;
    private String mWhereEats;
    private RestApiService service = DI.getRestApiService();

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

        mWhereEats = " has not yet decided";

        for (Restaurant restaurant: service.getRestaurants()) {
            if(restaurant.getAttendants().contains(user.getUid())) {
                mWhereEats = " eats at " + restaurant.getName();
            }
        }

        holder.mMateName.setText(user.getUsername() + mWhereEats);

        if (user.getUrlPicture() == "no_pic" || user.getUrlPicture() == null) {
            holder.mMatePhoto.setImageResource(R.drawable.avatar);
        } else {
            Glide.with(holder.mMatePhoto.getContext())
                    .load(user.getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mMatePhoto);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_mate_photo)
        public ImageView mMatePhoto;
        @BindView(R.id.item_mate_name)
        public TextView mMateName;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    void updateMates(@NonNull final List<User> mates) {
        this.mUsers = mates;
        notifyDataSetChanged();
    }
}
