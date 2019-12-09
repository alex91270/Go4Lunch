package com.example.go4lunchAlx.ui.mates;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.main.MainActivity;
import com.example.go4lunchAlx.service.RestApiService;

public class MatesViewFragment extends Fragment {

    private RestApiService service = DI.getRestApiService();
    private RecyclerView mRecyclerView;
    private MatesRecyclerViewAdapter myAdapter;
    private Context mContext;
    private SearchView searchView;

    public static MatesViewFragment newInstance() {
        return (new MatesViewFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mates_view, container, false);
        mContext = this.getActivity();
        mRecyclerView = root.findViewById(R.id.list_workmates);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        myAdapter = new MatesRecyclerViewAdapter(service.getFirebaseUsers());
        mRecyclerView.setAdapter(myAdapter);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.toolbar_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search workmate...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("alex", "text changed");
                return false;
            }
        });
    }
}