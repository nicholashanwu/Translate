package com.example.translate.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.example.translate.ui.dashboard.AchievementAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class ProfileFragment extends Fragment {

    private AchievementAdapter mAdapter;
    private DatabaseHelper myDb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myDb = new DatabaseHelper(getActivity());

        CardView mBtnStartSaved = view.findViewById(R.id.btnStartSaved);
        CardView mBtnStartLearned = view.findViewById(R.id.btnStartLearned);
        CardView mBtnStartMyList = view.findViewById(R.id.btnStartMyList);
        HorizontalScrollView mHsvCards = view.findViewById(R.id.hsvCards);
        RoundedImageView mBtnProfileImageProfile = view.findViewById(R.id.btnProfileImageProfile);
        ImageView mIvSaved = view.findViewById(R.id.ivSaved);
        ImageView mIvMastered = view.findViewById(R.id.ivMastered);
        ImageView mIvMyList = view.findViewById(R.id.ivMyList);


        Glide.with(getContext()).load(R.drawable.envelope).apply(new RequestOptions().override(600, 600)).into(mIvSaved);
        Glide.with(getContext()).load(R.drawable.tools).apply(new RequestOptions().override(600, 600)).into(mIvMastered);
        Glide.with(getContext()).load(R.drawable.exam).apply(new RequestOptions().override(600, 600)).into(mIvMyList);
        Glide.with(getContext()).load(R.drawable.tzuyu).apply(new RequestOptions().override(100, 100)).into(mBtnProfileImageProfile);

        mBtnStartSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getSaved();
                if (res.getCount() == 0) {
                    showMessage("No Saved Words...", "No saved!");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("learningType", "saved");
                    Navigation.findNavController(getView()).navigate(R.id.action_navigation_profile_to_navigation_learning, bundle);

                    if (myDb.progressAchievement("Dedicated")) {
                        showAchievement("Dedicated");
                        if (myDb.progressAchievement("Self-Improver")) {
                            showAchievement("Self-Improver");
                        }
                    }


                    for (int i = 0; i < res.getCount(); i++) {
                        if (myDb.progressAchievement("Smart Saver")) {
                            showAchievement("Smart Saver");
                        }

                        if (myDb.progressAchievement("Sophisticated Saver")) {
                            showAchievement("Sophisticated Saver");
                        }
                    }


                }
                res.close();
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
                    if (myDb.progressAchievement("Pursuing Perfection")) {
                        showAchievement("Pursuing Perfection");
                        if (myDb.progressAchievement("Self-Improver")) {
                            showAchievement("Self-Improver");
                        }
                    }
                }
                res.close();
            }
        });

        mBtnStartMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_profile_to_navigation_my_list_fragment);

                if (myDb.progressAchievement("Average Addition")) {
                    showAchievement("Average Addition");
                    if (myDb.progressAchievement("Self-Improver")) {
                        showAchievement("Self-Improver");
                    }
                }
            }
        });

    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert_dialog_profile, null);

        TextView txtTitle = view.findViewById(R.id.title);
        ImageButton imageButton = view.findViewById(R.id.image);

        imageButton.setImageResource(R.mipmap.over_40);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //closes dialog
            }
        });

        txtTitle.setText(title);
        builder.setView(view);
        builder.show();
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

        txtTitle.setText("You got the " + title + " achievement!");
        builder.setView(view);
        builder.show();
    }





}
