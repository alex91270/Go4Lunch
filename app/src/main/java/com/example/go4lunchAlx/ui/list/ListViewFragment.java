package com.example.go4lunchAlx.ui.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.comparators.SortByAttendants;
import com.example.go4lunchAlx.comparators.SortByDistance;
import com.example.go4lunchAlx.comparators.SortByRate;
import com.example.go4lunchAlx.data.restaurant_search.OnSearchedRestaurantAdded;
import com.example.go4lunchAlx.ui.detail.DetailActivity;
import com.example.go4lunchAlx.service.di.DI;
import com.example.go4lunchAlx.models.Restaurant;
import com.example.go4lunchAlx.service.RestApiService;
import com.example.go4lunchAlx.ui.FragmentSearchViewAutocomplete;
import java.util.ArrayList;
import java.util.Collections;

public class ListViewFragment extends FragmentSearchViewAutocomplete {

    private Context mContext;
    private RestApiService service = DI.getRestApiService();
    private RecyclerView mRecyclerView;
    private ListRecyclerViewAdapter myAdapter;
    private OnSearchedRestaurantAdded onSearchedRestaurantAdded;
    private InputMethodManager imm;
    private ArrayList<Restaurant> listDisplayed;
    private SharedPreferences sharedPreferences;

    public static ListViewFragment newInstance() {
        return (new ListViewFragment());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_view, container, false);
        listDisplayed = new ArrayList<>();
        mContext = this.getActivity();
        mRecyclerView = root.findViewById(R.id.list_restaurants);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        sharedPreferences = mContext.getSharedPreferences("com.example.go4lunchAlx", Context.MODE_PRIVATE);
        myAdapter = new ListRecyclerViewAdapter(mContext, listDisplayed);
        mRecyclerView.setAdapter(myAdapter);
        setAdapter();

        onSearchedRestaurantAdded = new OnSearchedRestaurantAdded() {
            @Override
            public void onRestaurantAdded() {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                setAdapter();

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
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myAdapter != null) {
            setAdapter();
            }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        super.setSuggestionsListener(onSearchedRestaurantAdded);

    }

    private void setAdapter() {
        listDisplayed.clear();
        listDisplayed.addAll(service.getAllRestaurants());

       switch (sharedPreferences.getString("sortPrefs", "")) {
            case "0" :
                Collections.sort(listDisplayed, new SortByDistance());
                break;
            case "1":
                Collections.sort(listDisplayed, new SortByRate());
                break;
            case "2":
                Collections.sort(listDisplayed, new SortByAttendants());
                break;
        }

        myAdapter.updateRestos(listDisplayed);
    }
}