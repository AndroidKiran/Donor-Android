package com.iamdonor.fragment;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.iamadonor.splash.R;
import com.iamdonor.tablistners.GroupPageAdapter;

public class GroupFragment extends SherlockFragment{
	
	private View mGroupTabView;
	private ViewPager mViewPager;
	private Context mContext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getSherlockActivity();
		mGroupTabView = inflater.inflate(R.layout.viewpager_main,container, false);
		mViewPager = (ViewPager) mGroupTabView.findViewById(R.id.viewPager);
		mViewPager.setAdapter(new GroupPageAdapter(mContext,getChildFragmentManager()));
		return mGroupTabView;
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
