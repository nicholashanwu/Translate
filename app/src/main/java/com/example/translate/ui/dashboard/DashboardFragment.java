package com.example.translate.ui.dashboard;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.translate.DatabaseHelper;
import com.example.translate.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class DashboardFragment extends Fragment {

	DatabaseHelper myDb;

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

		Button mBtnAchievements = view.findViewById(R.id.btnAchievements);


		mBtnAchievements.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Navigation.findNavController(getView()).navigate(R.id.action_navigation_dashboard_to_achievementFragment);

			}
		});

		try {
			Cursor res = new getScores(getActivity()).execute(myDb).get();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}


	public static class getScores extends AsyncTask<DatabaseHelper, Void, Cursor> {

		WeakReference<Activity> activityWeakReference;

		getScores(Activity activity) {
			activityWeakReference = new WeakReference<Activity>(activity);
		}

		@Override
		protected void onPreExecute() {
			final Activity activity = activityWeakReference.get();
			if (activity != null) {

			}
		}

		@Override
		protected Cursor doInBackground(DatabaseHelper... myDb) {
			myDb[0] = new DatabaseHelper(activityWeakReference.get());

			Cursor res = myDb[0].getScores();
			return res;
		}

		@Override
		protected void onPostExecute(Cursor cursor) {

			super.onPostExecute(cursor);
		}
	}

	public void displayValues() {


	}


}
