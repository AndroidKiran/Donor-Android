package com.iamdonor.tablistners;

import com.iamdonor.child.frgament.EventsFragment;
import com.iamdonor.child.frgament.AddEventFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPageAdapter extends FragmentPagerAdapter {


	// Declare the number of ViewPager pages
	  final int PAGE_COUNT = 2;
	  private String titles[] = new String[] { "Events", "Add Event"};
	 

	  public ViewPageAdapter(FragmentManager fm) {
		super(fm);
	  }
	  
	  @Override
	  public Fragment getItem(int position) {
	    switch (position) {

	      // Open FragmentTab1.java
	    case 0:
		      EventsFragment fragmenttab1 = new EventsFragment();
		      
		   /*   b = new Bundle();
		      b.putString("phoneNum",phoneNum);
		      b.putString("fragmentValue",fragmentValue);
		      fragmenttab1.setArguments(b);*/
		      
		      return fragmenttab1;

	      // Open FragmentTab2.java
	    case 1:
		      AddEventFragment fragmenttab2 = new AddEventFragment();
		    /*  b = new Bundle();
		      b.putString("phoneNum",phoneNum);
		      b.putString("fragmentValue",fragmentValue);
		      fragmenttab2.setArguments(b);*/
		      
		      return fragmenttab2;
	    }
	    return null;
	  }

	  public CharSequence getPageTitle(int position) {
	    return titles[position];
	  }

	  @Override
	  public int getCount() {
	    return PAGE_COUNT;
	  }
}
