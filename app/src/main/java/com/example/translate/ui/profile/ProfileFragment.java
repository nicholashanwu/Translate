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

import com.example.translate.DatabaseHelper;
import com.example.translate.MainActivity;
import com.example.translate.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CardView mBtnStartSaved;
    private CardView mBtnStartLearned;
    private CardView mBtnStartMyList;
    private CircleImageView mBtnProfileImageProfile;
    private ImageView mIvSaved;
    private ImageView mIvMastered;
    private ImageView mIvMyList;

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

        mBtnStartSaved = view.findViewById(R.id.btnStartSaved);
        mBtnStartLearned = view.findViewById(R.id.btnStartLearned);
        mBtnStartMyList = view.findViewById(R.id.btnStartMyList);
        mHsvCards = view.findViewById(R.id.hsvCards);
        mBtnProfileImageProfile = view.findViewById(R.id.btnProfileImageProfile);
        mIvSaved = view.findViewById(R.id.ivSaved);
        mIvMastered = view.findViewById(R.id.ivMastered);
        mIvMyList = view.findViewById(R.id.ivMyList);

        Picasso.get().load(R.drawable.envelope).resize(360, 360).into(mIvSaved);
        Picasso.get().load(R.drawable.tools).resize(360, 360).into(mIvMastered);
        Picasso.get().load(R.drawable.exam).resize(360, 360).into(mIvMyList);
        Picasso.get().load(R.mipmap.tzuyu).resize(144, 144).into(mBtnProfileImageProfile);

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


}
