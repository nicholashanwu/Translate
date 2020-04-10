package com.example.translate.ui.dashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class DashboardFragment extends Fragment {

    DatabaseHelper myDb;
    private TextView mTxtAchievements;
    private TextView mTxtTestsTaken;
    private TextView mTxtWordsMastered;
    private TextView mTxtLevel;
    private TextView mTxtWordsAdded;
    private RoundCornerProgressBar mPbExp;
    private RoundCornerProgressBar mPbHd;
    private RoundCornerProgressBar mPbD;
    private RoundCornerProgressBar mPbC;
    private RoundCornerProgressBar mPbP;
    private RoundCornerProgressBar mPbF;
    private RoundCornerProgressBar mPbAchievement;

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExtendedFloatingActionButton mBtnAchievements = view.findViewById(R.id.btnAchievements);
        mTxtAchievements = view.findViewById(R.id.txtAchievements);
        mTxtTestsTaken = view.findViewById(R.id.txtTestsTaken);
        mTxtWordsMastered = view.findViewById(R.id.txtWordsMastered);
        mTxtLevel = view.findViewById(R.id.txtExperienceLevel);
        mTxtWordsAdded = view.findViewById(R.id.txtWordsAdded);
        mPbExp = view.findViewById(R.id.pbExp);
        mPbHd = view.findViewById(R.id.pbHd);
        mPbD = view.findViewById(R.id.pbD);
        mPbC = view.findViewById(R.id.pbC);
        mPbP = view.findViewById(R.id.pbP);
        mPbF = view.findViewById(R.id.pbF);
        mPbAchievement = view.findViewById(R.id.pbAchievement);


        mBtnAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_dashboard_to_achievementFragment);
            }
        });

        myDb = new DatabaseHelper(getActivity());

        Cursor res = myDb.getScores();
        res.moveToFirst();
        int achievements = res.getInt(2);
        mTxtAchievements.setText(String.valueOf(res.getInt(2)));
        res.move(1);
        mTxtTestsTaken.setText(String.valueOf(res.getInt(2)));
        res.move(1);
        mTxtWordsMastered.setText(String.valueOf(res.getInt(2)));
        res.move(1);
        mTxtLevel.setText(String.valueOf(res.getInt(2)));
        res.move(1);
        mTxtWordsAdded.setText(String.valueOf(res.getInt(2)));
        res.move(1);
        mPbExp.setProgress(res.getInt(2));
        res.move(1);
        int totalTests = 0;

        int hd = res.getInt(2);
        totalTests += hd;
        res.move(1);
        int d = res.getInt(2);
        totalTests += d;
        res.move(1);
        int c = res.getInt(2);
        totalTests += c;
        res.move(1);
        int p = res.getInt(2);
        totalTests += p;
        res.move(1);
        int f = res.getInt(2);
        totalTests += f;
        res.move(1);
        mPbHd.setMax(totalTests);
        mPbD.setMax(totalTests);
        mPbC.setMax(totalTests);
        mPbP.setMax(totalTests);
        mPbF.setMax(totalTests);

        mPbHd.setProgress(hd);
        mPbD.setProgress(d);
        mPbC.setProgress(c);
        mPbP.setProgress(p);
        mPbF.setProgress(f);

        res.close();

        getAchievements();

        getMyListWords();

        getMastered();

        myDb.close();

        int level = getExperience(achievements);

        mTxtLevel.setText(String.valueOf(level));
    }

    public int getExperience(int achievements) {
        int level;
        if (achievements == 32) {
            level = 5;
        } else if (achievements > 24) {
            level = 4;
        } else if (achievements > 17) {
            level = 3;
        } else if (achievements > 11) {
            level = 2;
        } else if (achievements > 6) {
            level = 1;
        } else {
            level = 0;
        }
        return level;
    }

    public void getAchievements() {
        Cursor cur = myDb.getAchieved();

        try {
            mPbAchievement.setMax(32);
            mPbAchievement.setProgress(cur.getCount());
            mTxtAchievements.setText(String.valueOf(cur.getCount()) + "/32");
            myDb.updateScore("Achievements");
        } catch (Exception e) {
            // exception handling
        } finally {
            if (cur != null) {
                cur.close();
            }
        }

    }

    public void getMyListWords() {

        Cursor cur = myDb.getCategory("custom");

        try {
            mTxtWordsAdded.setText(String.valueOf(cur.getCount()));
        } catch (Exception e) {
            // exception handling
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
    }

    public void getMastered() {

        Cursor cur = myDb.getCategory("learned");

        try {
            mTxtWordsMastered.setText(String.valueOf(cur.getCount()));
        } catch (Exception e) {
            // exception handling
        } finally {
            if (cur != null) {
                cur.close();
            }
        }

    }


}
