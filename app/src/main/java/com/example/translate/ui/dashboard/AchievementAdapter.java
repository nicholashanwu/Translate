package com.example.translate.ui.dashboard;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.translate.R;

import androidx.recyclerview.widget.RecyclerView;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public AchievementAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mIsAchieved, mDescription;
        public ProgressBar mProgressBar;

        public AchievementViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.txtAchievementName);
            mProgressBar = itemView.findViewById(R.id.pbAchievement);
            mDescription = itemView.findViewById(R.id.txtAchievementDescription);
            mIsAchieved = itemView.findViewById(R.id.txtIsAchieved);
        }
    }

    @Override
    public AchievementAdapter.AchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.row, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AchievementViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(1);
        String description = mCursor.getString(2);
        String currentProgress = mCursor.getString(3);
        String totalProgress = mCursor.getString(4);
        String complete = mCursor.getString(5);

        double progressDouble = 100 * (Double.valueOf(currentProgress) / Double.valueOf(totalProgress));
        int progressInt = (int) progressDouble;

        holder.mProgressBar.setProgress(progressInt, true);

        holder.mName.setText(name);
        holder.mDescription.setText(description);

        if (complete.equals("1")) {
            holder.mIsAchieved.setText("yes");
        } else {
            holder.mIsAchieved.setText("no");
        }

        holder.mIsAchieved.setText(Integer.valueOf(currentProgress) + "/" + Integer.valueOf(totalProgress));
        if (progressInt == 100) {
            holder.mIsAchieved.setTypeface(Typeface.DEFAULT_BOLD);
            holder.mIsAchieved.setTextColor(Color.parseColor("#29B6F6"));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
