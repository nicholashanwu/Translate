package com.example.translate.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.translate.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_home, container, false);
		return root;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        Button mBtnStartNumbers = view.findViewById(R.id.btnStartNumbers);
        Button mBtnStartGreetings = view.findViewById(R.id.btnStartGreetings);
        Button mBtnStartFood = view.findViewById(R.id.btnStartFood);
        Button mBtnStartHelp = view.findViewById(R.id.btnStartHelp);
        CircleImageView mBtnProfileImage = view.findViewById(R.id.btnProfileImageHome);

        Picasso.get().load(R.mipmap.tzuyu).resize(144, 144).into(mBtnProfileImage);

		mBtnStartNumbers.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putString("learningType", "numbers");
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_navigation_learning, bundle);
			}
		});

		mBtnStartGreetings.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putString("learningType", "essentials");
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_navigation_learning, bundle);
			}
		});

		mBtnStartFood.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putString("learningType", "food");
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_navigation_learning, bundle);
			}
		});

		mBtnStartHelp.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putString("learningType", "help");
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_navigation_learning, bundle);
			}
		});

		mBtnProfileImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_navigation_profile);
			}
		});

	}


}
