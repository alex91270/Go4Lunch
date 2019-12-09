package com.example.go4lunchAlx.ui.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.data.restaurant_search.OnSearchedRestaurantAdded;
import com.example.go4lunchAlx.detail.DetailActivity;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.ui.FragmentSearchViewAutocomplete;

import java.util.ArrayList;
import java.util.List;

public class ListViewFragment extends FragmentSearchViewAutocomplete {

    private Context mContext;
    private RestApiService service = DI.getRestApiService();
    private RecyclerView mRecyclerView;
    private ListRecyclerViewAdapter myAdapter;
    private OnSearchedRestaurantAdded onSearchedRestaurantAdded;
    private InputMethodManager imm;
    private List<Restaurant> listDisplayed;

    public static ListViewFragment newInstance() {
        return (new ListViewFragment());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_view, container, false);
        mContext = this.getActivity();
        mRecyclerView = root.findViewById(R.id.list_restaurants);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        listDisplayed = service.getAllRestaurants();

        myAdapter = new ListRecyclerViewAdapter(listDisplayed);
        mRecyclerView.setAdapter(myAdapter);

        onSearchedRestaurantAdded = new OnSearchedRestaurantAdded() {
            @Override
            public void onRestaurantAdded() {
                Log.i("alex", "listview onRestaurantAdded");
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Log.i("alex", "j'update mon recycler !!");
                listDisplayed = service.getAllRestaurants();
                myAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(myAdapter);

                String restoId = service.getAllRestaurants().get(service.getAllRestaurants().size() - 1).getId();
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("restoId", restoId);
                mContext.startActivity(intent);
            }
        };

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("alex", "mapview onviewcreated");
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.i("alex", "listview onresume");
        if (myAdapter != null) {
            myAdapter.notifyDataSetChanged();
            //mRecyclerView.setAdapter(myAdapter);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        super.setSuggestionsListener(onSearchedRestaurantAdded);

    }
}