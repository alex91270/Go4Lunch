package com.example.go4lunchAlx.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.service.RestApiService;

public class ListViewFragment extends Fragment {

    private Context mContext;
    private RestApiService service = DI.getRestApiService();
    private RecyclerView mRecyclerView;
    private ListRecyclerViewAdapter myAdapter;

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

        myAdapter = new ListRecyclerViewAdapter(service.getRestaurants());
        mRecyclerView.setAdapter(myAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("alex", "listview onresume");
        if (myAdapter != null) {
            mRecyclerView.setAdapter(myAdapter);
        }
    }
}