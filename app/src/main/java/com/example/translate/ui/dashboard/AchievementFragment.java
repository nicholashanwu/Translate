package com.example.translate.ui.dashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AchievementFragment extends Fragment {

    private AchievementAdapter mAdapter;
    private DatabaseHelper myDb;
    private ExtendedFloatingActionButton mBtnBackAchievements;

    public AchievementFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_achievement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mBtnBackAchievements = view.findViewById(R.id.btnBackAchievements);

        myDb = new DatabaseHelper(getActivity());
        final Cursor res = getAllAchievements();
        setAdapter(view, res);

        mBtnBackAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_achievement_to_navigation_dashboard);
                res.close();
            }
        });

    }

    private Cursor getAllAchievements() {
        return myDb.getAchievements();
    }

    private void setAdapter(View view, Cursor res) {
        RecyclerView mRecyclerView = view.findViewById(R.id.rvAchievement);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AchievementAdapter(getContext(), res);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
    }
}
