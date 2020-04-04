package com.example.translate.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.translate.Phrase;
import com.example.translate.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.PhraseViewHolder> {
    private ArrayList<Phrase> mPhraseList;
    private OnItemClickListener mListener;

    public PhraseAdapter(ArrayList<Phrase> mPhraseList) {
        this.mPhraseList = mPhraseList;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class PhraseViewHolder extends RecyclerView.ViewHolder {        //inner class CoinViewHolder that contains the content of each row
        public TextView phraseEn, phraseCn, phrasePinyin;
        public ImageButton mBtnDelete;

        public PhraseViewHolder(View v, final OnItemClickListener listener) {                                     //constructor for the CoinViewHolder
            super(v);                                                                                           //the more TextViews required, the more stuff here

            phraseEn = v.findViewById(R.id.txtPhraseEn);
            phraseCn = v.findViewById(R.id.txtPhraseCn);
            phrasePinyin = v.findViewById(R.id.txtPhrasePinyin);
            mBtnDelete = v.findViewById(R.id.btnDelete);

            mBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }

    }

    @Override
    public PhraseAdapter.PhraseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.phrase_row, parent, false);
        return new PhraseViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(final PhraseViewHolder holder, int position) {                                         //position determines the list item we are currently creating for the RecyclerView
        final Phrase phrase = mPhraseList.get(position);
        holder.phraseEn.setText(phrase.getPhraseEn());
        holder.phraseCn.setText(phrase.getPhraseCn());
        holder.phrasePinyin.setText(phrase.getPinyin());
//

    }

    @Override
    public int getItemCount() {
        return mPhraseList.size();
    }

    public void deleteItem(int position) {
        mPhraseList.remove(position);
        notifyItemRemoved(position);
        //return mPhraseList.get(position).getPhraseEn();
    }

    public void addItem(String id, String phraseEn, String phraseCn, String phrasePinyin, String category, boolean learned, boolean saved) {

        mPhraseList.add(mPhraseList.size(), new Phrase("1", phraseEn, phraseCn, phrasePinyin, category, "false", "false"));
        notifyItemInserted(mPhraseList.size() - 1);
        notifyDataSetChanged();
    }

    public ArrayList<Phrase> getPhraseList() {
        return mPhraseList;
    }

    public void deleteAll() {
        mPhraseList.clear();
        notifyDataSetChanged();
    }

}

