package com.iamdonor.tablistners;

import com.iamdonor.child.frgament.GroupsFragment;
import com.iamdonor.child.frgament.MyGroupFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GroupPageAdapter extends FragmentPagerAdapter{
	final int PAGECOUNT = 2;
	private String titles[] = new String[] { "Groups", "My Groups"};
	private Fragment fragment;
	private Context mContext;
	
	public GroupPageAdapter(Context mContext,FragmentManager fm) {
		super(fm);
		this.mContext = mContext;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		switch (position) {
		case 0:
			fragment = new GroupsFragment();
			//f = GroupsFragment.newInstance(mContext);
			return fragment;
			
		case 1:
			fragment = new MyGroupFragment();
			//f = MyGroupFragment.newInstance(mContext);
			return fragment;

		default:
			break;
		}
		
		return null;
	}
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}	

	@Override
	public int getCount() {
		
		return PAGECOUNT;
	}

}
