package com.example.translate.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.translate.Phrase;
import com.example.translate.R;

import java.util.ArrayList;

public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.PhraseViewHolder> {
    private ArrayList<Phrase> mPhraseList;

    public PhraseAdapter(ArrayList<Phrase> mPhraseList) {
        this.mPhraseList = mPhraseList;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    public static class PhraseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {        //inner class CoinViewHolder that contains the content of each row
        public TextView phraseEn, phraseCn, phrasePinyin;
        public ProgressBar progressBar;
        private View subItem;

        public PhraseViewHolder(View v) {                                     //constructor for the CoinViewHolder
            super(v);                                                                                           //the more TextViews required, the more stuff here
            v.setOnClickListener(this);
            phraseEn = v.findViewById(R.id.txtPhraseEn);
            phraseCn = v.findViewById(R.id.txtPhraseCn);
            phrasePinyin = v.findViewById(R.id.txtPhrasePinyin);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @Override
    public PhraseAdapter.PhraseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.phrase_row, parent, false);
        return new PhraseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhraseViewHolder holder, int position) {                                         //position determines the list item we are currently creating for the RecyclerView
        Phrase phrase = mPhraseList.get(position);
        holder.phraseEn.setText(phrase.getPhraseEn());
        holder.phraseCn.setText(phrase.getPhraseCn());
        holder.phrasePinyin.setText(phrase.getPinyin());
    }

    @Override
    public int getItemCount() {
        return mPhraseList.size();
    }
}
