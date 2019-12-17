package com.example.go4lunchAlx.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.data.restaurant_search.OnSearchedRestaurantAdded;
import com.example.go4lunchAlx.data.restaurant_search.RestaurantSearchedAdding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.HashMap;
import java.util.Map;

public abstract class FragmentSearchViewAutocomplete extends Fragment {

    private SimpleCursorAdapter mAdapter;
    private SearchView searchView;
    private HashMap<String, String> suggestions;
    private Context mContext;
    private PlacesClient mPlacesClient;
    private static String apiKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        suggestions = new HashMap<>();
        mContext = getActivity();
        apiKey = this.getString(R.string.google_maps_key);

        if (!Places.isInitialized()) {
            Places.initialize(mContext, apiKey);
        }

        mPlacesClient = Places.createClient(mContext);

        final String[] from = new String[] {"Restaurant Name"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(mContext,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu, menu);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        ((AutoCompleteTextView) searchView.findViewById(R.id.search_src_text)).setThreshold(1);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setQueryHint(getString(R.string.hint_search_rest));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i("alex", "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                findSuggestions(s);
                return false;
            }
        });
    }

    private void populateAdapter() {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "Restaurant Name" });
        int i = 0;
        for(Map.Entry<String, String> entry : suggestions.entrySet()){
            c.addRow(new Object[] {i, entry.getKey()});
            i++;
        }
        mAdapter.changeCursor(c);
    }

    private void findSuggestions(String query) {

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest request =
                FindAutocompletePredictionsRequest.builder()
                        .setCountry("FR")
                        .setTypeFilter(TypeFilter.ESTABLISHMENT)
                        .setSessionToken(token)
                        .setQuery(query)
                        .build();

        mPlacesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(
                        (response) -> {
                            StringBuilder sb = new StringBuilder();
                            suggestions.clear();
                            for (AutocompletePrediction prediction :
                                    response.getAutocompletePredictions()) {
                                suggestions.put(prediction.getPrimaryText(null).toString(), prediction.getPlaceId());
                                sb.append(prediction.getPrimaryText(null).toString());
                                sb.append("\n");
                            }
                            populateAdapter();
                        })
                .addOnFailureListener((exception) -> {
                    exception.printStackTrace();
                });
    }



    public void setSuggestionsListener(OnSearchedRestaurantAdded onSearchedRestaurantAdded) {
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                Log.i("alex", "suggestion click");
                Cursor cursor = (Cursor) mAdapter.getItem(position);
                //String txt = cursor.getString(cursor.getColumnIndex("Restaurant Name"));

                RestaurantSearchedAdding restaurantSearchedAdding = new RestaurantSearchedAdding(onSearchedRestaurantAdded);
                restaurantSearchedAdding.addSearchedRestaurantToList(mContext, suggestions.get(cursor.getString(cursor.getColumnIndex("Restaurant Name"))));

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                Log.i("alex", "onsuggestionselect");
                return true;
            }
        });
    }

}
