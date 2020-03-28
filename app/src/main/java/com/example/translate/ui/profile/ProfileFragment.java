package com.example.translate.ui.profile;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.example.translate.ui.home.LearningFragment;

public class ProfileFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mBtnStartSaved;
    private Button mBtnStartLearned;
    private DatabaseHelper myDb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mRecyclerView = view.findViewById(R.id.rvAchievement);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        AchievementAdapter.RecyclerViewClickListener listener = new AchievementAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                //launchDetailActivity(position);
            }
        };

        mBtnStartSaved = (Button) view.findViewById(R.id.btnStartSaved);
        mBtnStartLearned = (Button) view.findViewById(R.id.btnStartLearned);

        mBtnStartSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDb = new DatabaseHelper(getContext());

                Cursor res = myDb.getSaved();
                if(res.getCount() == 0){
                    showMessage("No Data", "No saved!");
                } else {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                    Fragment fragment = new LearningFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("learningType", "saved");
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.nav_host_fragment, fragment);
                    transaction.commit();
                    transaction.addToBackStack(null);
                }
                //launch learning_fragmenet with saved
            }
        });

        mBtnStartLearned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDb = new DatabaseHelper(getContext());

                Cursor res = myDb.getLearned();
                if(res.getCount() == 0){
                    showMessage("No Data", "No learned!");
                } else {
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                    Fragment fragment = new LearningFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("learningType", "learned");
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.nav_host_fragment, fragment);
                    transaction.commit();
                    transaction.addToBackStack(null);
                }
                //launch learning_fragmenet with learned
            }
        });



        mAdapter = new AchievementAdapter(Achievement.getAchievements(), listener);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
