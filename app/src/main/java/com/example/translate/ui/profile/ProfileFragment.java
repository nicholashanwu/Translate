package com.example.translate.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.translate.DatabaseHelper;
import com.example.translate.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

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


        RecyclerView mRecyclerView = view.findViewById(R.id.rvAchievement);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AchievementAdapter(getContext(), getAllAchievements());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setNestedScrollingEnabled(false);

        CardView mBtnStartSaved = view.findViewById(R.id.btnStartSaved);
        CardView mBtnStartLearned = view.findViewById(R.id.btnStartLearned);
        CardView mBtnStartMyList = view.findViewById(R.id.btnStartMyList);
        HorizontalScrollView mHsvCards = view.findViewById(R.id.hsvCards);
        CircleImageView mBtnProfileImageProfile = view.findViewById(R.id.btnProfileImageProfile);
        ImageView mIvSaved = view.findViewById(R.id.ivSaved);
        ImageView mIvMastered = view.findViewById(R.id.ivMastered);
        ImageView mIvMyList = view.findViewById(R.id.ivMyList);

        Glide.with(getContext()).load(R.drawable.envelope).into(mIvSaved);
        Glide.with(getContext()).load(R.drawable.tools).into(mIvMastered);
        Glide.with(getContext()).load(R.drawable.exam).into(mIvMyList);
        Glide.with(getContext()).load(R.drawable.tzuyu).into(mBtnProfileImageProfile);

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
                }
                res.close();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_alert_dialog, null);

        TextView txtTitle = view.findViewById(R.id.title);
        ImageButton imageButton = view.findViewById(R.id.image);
        TextView mTxtMessage = view.findViewById(R.id.message);
        TextView mTxtLearned = view.findViewById(R.id.txtLearned);
        TextView mTxtMastered = view.findViewById(R.id.txtMastered);
        TextView mTxtForgot = view.findViewById(R.id.txtForgot);

        mTxtMessage.setTextSize(14);
        imageButton.setImageResource(R.mipmap.over_40);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //closes dialog
            }
        });

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(view);
        builder.show();
    }

    private Cursor getAllAchievements() {
        return myDb.getAchievements();
    }


}
