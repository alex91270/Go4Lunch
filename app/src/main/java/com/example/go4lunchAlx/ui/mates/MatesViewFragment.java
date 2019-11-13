package com.example.go4lunchAlx.ui.mates;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunchAlx.R;
import com.example.go4lunchAlx.di.DI;
import com.example.go4lunchAlx.service.RestApiService;

public class MatesViewFragment extends Fragment {

    private RestApiService service = DI.getRestApiService();
    private RecyclerView mRecyclerView;
    private MatesRecyclerViewAdapter myAdapter;
    private Context mContext;

    public static MatesViewFragment newInstance() {
        return (new MatesViewFragment());
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
}