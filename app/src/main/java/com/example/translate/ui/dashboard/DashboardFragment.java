package com.example.translate.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardFragment extends Fragment {

    private DatabaseHelper myDb;
    private TextView mTxtAchievements;
    private TextView mTxtTestsTaken;
    private TextView mTxtWordsMastered;
    private TextView mTxtLevel;
    private TextView mTxtWordsAdded;
    private TextView mTxtToNextLevel;
    private RoundCornerProgressBar mPbExp;
    private RoundCornerProgressBar mPbHd;
    private RoundCornerProgressBar mPbD;
    private RoundCornerProgressBar mPbC;
    private RoundCornerProgressBar mPbP;
    private RoundCornerProgressBar mPbF;
    private RoundCornerProgressBar mPbAchievement;
    private CircleImageView mBtnProfileImage;


    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        mTxtToNextLevel = view.findViewById(R.id.txtToNextLevel);
        mPbExp = view.findViewById(R.id.pbExp);
        mPbHd = view.findViewById(R.id.pbHd);
        mPbD = view.findViewById(R.id.pbD);
        mPbC = view.findViewById(R.id.pbC);
        mPbP = view.findViewById(R.id.pbP);
        mPbF = view.findViewById(R.id.pbF);
        mPbAchievement = view.findViewById(R.id.pbAchievement);
        mBtnProfileImage = view.findViewById(R.id.btnProfileImageDashboard);

        Glide.with(getContext()).load(R.drawable.tzuyu).apply(new RequestOptions().override(100, 100)).into(mBtnProfileImage);

        mBtnProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_dashboard_to_navigation_profile);
            }
        });

        mBtnAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_dashboard_to_navigation_achievement);
            }
        });

        myDb = new DatabaseHelper(getActivity());

        getValues();

        getMyListWords();

        getMastered();

        int achievementCount = getAchievements();

        int level = getExperience(achievementCount);

        mTxtLevel.setText(String.valueOf(level));

        myDb.close();
    }

    public void getValues() {

        try (Cursor res = myDb.getScores()) {

            res.moveToFirst();
            mTxtAchievements.setText(String.valueOf(res.getInt(2)));
            res.move(1);
            mTxtTestsTaken.setText(String.valueOf(res.getInt(2)));
            if (res.getInt(2) > 30) {
                if (myDb.progressAchievement("Tenacious Tester")) {
                    showAchievement("Tenacious Tester");
                }
            } else if (res.getInt(2) > 20) {
                if (myDb.progressAchievement("Talented Tester")) {
                    showAchievement("Talented Tester");
                }
            } else if (res.getInt(2) > 10) {
                if (myDb.progressAchievement("Tenacious Tester")) {
                    showAchievement("Tenacious Tester");
                }
            }

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

        }
    }

    public int getExperience(int achievements) {

        int level;
        if (achievements >= 32) {
            level = 5;
            mPbExp.setProgress(0);
            mPbExp.setMax(1);
            mTxtToNextLevel.setText("MAX LEVEL");
        } else if (achievements >= 24) {
            level = 4;
            mPbExp.setMax(8);
            mPbExp.setProgress(achievements - 24);
            mTxtToNextLevel.setText(32 - achievements + " ACHIEVEMENTS TO GO");
        } else if (achievements >= 17) {
            level = 3;
            mPbExp.setMax(7);
            mPbExp.setProgress(achievements - 17);
            mTxtToNextLevel.setText(24 - achievements + " ACHIEVEMENTS TO GO");
        } else if (achievements >= 11) {
            level = 2;
            mPbExp.setMax(6);
            mPbExp.setProgress(achievements - 11);
            mTxtToNextLevel.setText(17 - achievements + " ACHIEVEMENTS TO GO");
        } else if (achievements >= 6) {
            level = 1;
            mPbExp.setMax(5);
            mPbExp.setProgress(achievements - 6);
            mTxtToNextLevel.setText(11 - achievements + " ACHIEVEMENTS TO GO");
        } else {
            level = 0;
            mPbExp.setMax(5);
            mPbExp.setProgress(achievements);
            mTxtToNextLevel.setText(6 - achievements + " ACHIEVEMENTS TO GO");
        }
        return level;
    }

    public int getAchievements() {

        Cursor cur = null;
        try {
            cur = myDb.getAchieved();
            mPbAchievement.setMax(32);
            mPbAchievement.setProgress(cur.getCount());
            mTxtAchievements.setText(cur.getCount() + "/32");

        } catch (Exception e) {
            // exception handling
        } finally {
            if (cur != null) {
                cur.close();
            }
        }

        return cur.getCount();
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

    private void showAchievement(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Yellow));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert_dialog_achievement, null);
        TextView txtTitle = view.findViewById(R.id.title);
        ImageButton imageButton = view.findViewById(R.id.image);

        imageButton.setImageResource(R.mipmap.over_95);

        builder.setPositiveButton("AWESOME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });

        txtTitle.setText(title);
        builder.setView(view);
        builder.show();
    }

}
