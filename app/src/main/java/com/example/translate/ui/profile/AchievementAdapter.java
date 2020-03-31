package com.example.translate.ui.profile;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.translate.MainActivity;
import com.example.translate.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    private ArrayList<Achievement> mAchievementsList;


    public AchievementAdapter(ArrayList<Achievement> mAchievementsList) {
        this.mAchievementsList = mAchievementsList;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {        //inner class CoinViewHolder that contains the content of each row
        public TextView name, isAchieved, description;
        public ProgressBar progressBar;
        private View subItem;

        public AchievementViewHolder(View v) {                                     //constructor for the CoinViewHolder
            super(v);                                                                                           //the more TextViews required, the more stuff here
            v.setOnClickListener(this);
            name = v.findViewById(R.id.txtAchievementName);
            progressBar = v.findViewById(R.id.pbAchievement);
            description = v.findViewById(R.id.txtAchievementDescription);
            isAchieved = v.findViewById(R.id.txtIsAchieved);


        }

        @Override
        public void onClick(View view) {
            //mListener.onClick(view, getAdapterPosition());

        }
    }

    @Override
    public AchievementAdapter.AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new AchievementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AchievementViewHolder holder, int position) {                                         //position determines the list item we are currently creating for the RecyclerView
        Achievement achievement = mAchievementsList.get(position);

        holder.name.setText(MainActivity.achievementList.get(position).getName());
        holder.description.setText(MainActivity.achievementList.get(position).getDescription());
        if (MainActivity.achievementList.get(position).getComplete().equals("true")) {
            holder.isAchieved.setText("yes");
        } else {
            holder.isAchieved.setText("no");
        }

        double progressDouble = (double) 100 * (MainActivity.achievementList.get(position).getCurrentProgress()) / MainActivity.achievementList.get(position).getProgressTotal();
        int progressInt = (int) progressDouble;
        holder.progressBar.setProgress(progressInt, true);
        holder.isAchieved.setText(achievement.getCurrentProgress() + "/" + MainActivity.achievementList.get(position).getProgressTotal());
        if(progressInt == 100){
            holder.isAchieved.setTypeface(Typeface.DEFAULT_BOLD);
            holder.isAchieved.setTextColor(Color.parseColor("#D4E157"));
        }

    }

    @Override
    public int getItemCount() {
        return mAchievementsList.size();
    }
}
