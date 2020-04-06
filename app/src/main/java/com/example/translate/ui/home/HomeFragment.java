package com.example.translate.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.translate.R;

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
        ImageView mIvHome = view.findViewById(R.id.ivHome);
        ImageView mIvNumbers = view.findViewById(R.id.ivNumbers);
        ImageView mIvGreetings = view.findViewById(R.id.ivGreetings);
        ImageView mIvFood = view.findViewById(R.id.ivFood);
        ImageView mIvHelp = view.findViewById(R.id.ivHelp);

        CircleImageView mBtnProfileImage = view.findViewById(R.id.btnProfileImageHome);

        Glide.with(getContext()).load(R.drawable.tzuyu).apply(new RequestOptions().override(100, 100)).into(mBtnProfileImage);
        Glide.with(getContext()).load(R.drawable.forbidden_city_small).apply(new RequestOptions().override(800, 800)).into(mIvHome);

        Glide.with(getContext()).load(R.drawable.undraw_visual_data).apply(new RequestOptions().override(800, 600).centerCrop()).into(mIvNumbers);
        Glide.with(getContext()).load(R.drawable.undraw_conversation).apply(new RequestOptions().override(800, 600).centerCrop()).into(mIvGreetings);
        Glide.with(getContext()).load(R.drawable.undraw_hamburger).apply(new RequestOptions().override(800, 600).centerCrop()).into(mIvFood);
        Glide.with(getContext()).load(R.drawable.undraw_fatherhood).apply(new RequestOptions().override(800, 600).centerCrop()).into(mIvHelp);


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
