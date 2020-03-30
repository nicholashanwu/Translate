package com.example.translate.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.translate.R;
import com.example.translate.ui.profile.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

	private Button mBtnStartNumbers;
	private Button mBtnStartGreetings;
	private Button mBtnStartFood;
	private Button mBtnStartHelp;
	private CircleImageView mBtnProfileImage;
    private NavController navController;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_home, container, false);

		return root;
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mBtnStartNumbers = (Button) view.findViewById(R.id.btnStartNumbers);
		mBtnStartGreetings = (Button) view.findViewById(R.id.btnStartGreetings);
		mBtnStartFood = (Button) view.findViewById(R.id.btnStartFood);
		mBtnStartHelp = (Button) view.findViewById(R.id.btnStartHelp);
		mBtnProfileImage = (CircleImageView) view.findViewById(R.id.btnProfileImage);

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

				final FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
				ft.replace(R.id.nav_host_fragment, new ProfileFragment(), "NewFragmentTag");
				ft.commit();
				ft.addToBackStack(null);
			}
		});

	}

}
