package com.example.translate.ui.profile;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.example.translate.DatabaseHelper;
import com.example.translate.MainActivity;
import com.example.translate.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CardView mBtnStartSaved;
    private CardView mBtnStartLearned;
    private CardView mBtnStartMyList;


    private HorizontalScrollView mHsvCards;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mRecyclerView = view.findViewById(R.id.rvAchievement);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mAdapter = new AchievementAdapter(MainActivity.achievementList);
        mRecyclerView.setAdapter(mAdapter);

        mBtnStartSaved = (CardView) view.findViewById(R.id.btnStartSaved);
        mBtnStartLearned = (CardView) view.findViewById(R.id.btnStartLearned);
        mBtnStartMyList = (CardView) view.findViewById(R.id.btnStartMyList);
        mHsvCards = (HorizontalScrollView) view.findViewById(R.id.hsvCards);

        final DatabaseHelper myDb = new DatabaseHelper(getContext());

        mHsvCards.setHorizontalScrollBarEnabled(false);

        mBtnStartSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getSaved();
                if (res.getCount() == 0) {
                    showMessage("No Data", "No saved!");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("learningType", "saved");
                    Navigation.findNavController(getView()).navigate(R.id.action_navigation_profile_to_navigation_learning, bundle);
                }
            }
        });

        mBtnStartLearned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getLearned();
                if (res.getCount() == 0) {
                    showMessage("No Data", "No learned!");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("learningType", "learned");
                    Navigation.findNavController(getView()).navigate(R.id.action_navigation_profile_to_navigation_learning, bundle);
                }
            }
        });

        mBtnStartMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_profile_to_navigation_my_list_fragment);


            }
        });

    }


    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}
