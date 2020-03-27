package com.example.translate.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import de.hdodenhof.circleimageview.CircleImageView;

import com.example.translate.R;
import com.example.translate.ui.profile.ProfileFragment;

public class HomeFragment extends Fragment {

	private Button mBtnStartNumbers;
	private Button mBtnStartGreetings;
	private Button mBtnStartFood;
	private Button mBtnStartHelp;
	private CircleImageView mBtnProfileImage;

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

//			    Fragment f = new LearningFragment();
//			    f.setEnterTransition(new Slide(Gravity.BOTTOM));
//			    f.setExitTransition(new Slide(Gravity.TOP));
//
//
//				final FragmentTransaction ft = getFragmentManager().beginTransaction();
//				ft.replace(R.id.nav_host_fragment, f, "NewFragmentTag");
//				ft.commit();
//				ft.addToBackStack(null);


                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                ft.replace(R.id.nav_host_fragment, new LearningFragment(), "NewFragmentTag");
                ft.commit();
                ft.addToBackStack(null);

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



	// TODO: add bookmark icon button
}
