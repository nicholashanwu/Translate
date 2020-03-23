package com.example.translate.ui.home;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.translate.DatabaseHelper;
import com.example.translate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class LearningFragment extends Fragment {

    private FloatingActionButton mFabDone;
    private FloatingActionButton mFabSave;
    private FloatingActionButton mFabAnswer;

    private TextView mTxtId;
    private TextView mTxtPhrase;
    private TextView mTxtCategory;
    private TextView mTxtLearned;

    private DatabaseHelper myDb;


    public LearningFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LearningFragment newInstance(String param1, String param2) {
        LearningFragment fragment = new LearningFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning, container, false);

        myDb = new DatabaseHelper(getContext());
        mTxtId = view.findViewById(R.id.txtId);
        mTxtPhrase = view.findViewById(R.id.txtPhrase);
        mTxtCategory = view.findViewById(R.id.txtCategory);
        mTxtLearned = view.findViewById(R.id.txtLearned);
        mFabAnswer = view.findViewById(R.id.fabAnswer);
        mFabSave = view.findViewById(R.id.fabSave);
        mFabDone = view.findViewById(R.id.fabDone);

        mFabDone.setOnClickListener(new View.OnClickListener() {        //adds sample data if the row exists
            @Override
            public void onClick(View view) {
                myDb.insertData("phrase", "category", true);
            }
        });

        mFabAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing found");

                } else {
                    StringBuffer buffer = new StringBuffer();

                    while (res.moveToNext()) {
                        buffer.append("Id: " + res.getString(0));
                        buffer.append("\nPhrase: " + res.getString(1));
                        buffer.append("\nCategory: " + res.getString(2));
                        buffer.append("\nLearned: " + res.getString(3) + "\n");

                    }
                    showMessage("Data", buffer.toString());
                }
            }
        });

        mFabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //myDb.onUpgrade(, 1, 1);
                myDb.dropTable();

            }
        });


        return view;
    }
}
