package com.iamdonor.fragment;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.iamadonor.splash.R;
import com.iamdonor.tablistners.ViewPageAdapter;

public class DonationEventFragment extends SherlockFragment {

	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.viewpager_main, container, false);
	    //get the phone num
	   /* bundle = this.getArguments(); 
        if(bundle != null)
        {
            setPhoneNum(bundle.getString("phoneNum"));
            setFragmentValue(bundle.getString("myFragmentValue"));
            
        }*/
	    
	    // Locate the ViewPager in viewpager_main.xml
	    ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
	    // Set the ViewPagerAdapter into ViewPager
	    mViewPager.setAdapter(new ViewPageAdapter(getChildFragmentManager()));
	    return view;
	  }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		setMenuVisibility(true);
	}

	  @Override
	  public void onDetach() {
	    super.onDetach();
	    try {
	      Field childFragmentManager = Fragment.class
	          .getDeclaredField("mChildFragmentManager");
	      childFragmentManager.setAccessible(true);
	      childFragmentManager.set(this, null);
	    } catch (NoSuchFieldException e) {
	      throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	      throw new RuntimeException(e);
	    }
	  }
}
