package com.iamdonor.fragment;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.iamadonar.utils.RobotCondensedTextView;
import com.iamadonor.splash.R;
import com.iamdonor.child.frgament.AddEventFragment;
import com.iamdonor.child.frgament.EventsFragment;
import com.octo.android.robospice.persistence.DurationInMillis;

public class EventTabFragment extends SherlockFragment {
		private FragmentTabHost tabHost;
		private SherlockFragmentActivity mContext;
		private RobotCondensedTextView tagTV;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mContext = getSherlockActivity();
		}
		
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View root = inflater.inflate(R.layout.fragment_tab_host_layout, container, false);

	        tabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);
	        
	        tabHost.setup(getSherlockActivity(), getChildFragmentManager(), android.R.id.tabcontent);

	        tabHost.addTab(tabHost.newTabSpec("EventTag").setIndicator(buildTabLayout("Events")),
	                EventsFragment.class, null);

	        
	        tabHost.addTab(tabHost.newTabSpec("PostEventTag").setIndicator(buildTabLayout("Post Event")),
	                AddEventFragment.class, null);
	        
	        return root;
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
	    private View buildTabLayout(String tag) {
	        View tab = LayoutInflater.from(getSherlockActivity()).inflate(R.layout.tab_layout, null); 
	        tagTV = (RobotCondensedTextView)tab.findViewById(R.id.tabTag);
	        tagTV.setText(tag);
	        return tab;
	    }
	    
	    @Override
	    public void onDestroyView() {
	    	super.onDestroyView();
	    	tabHost = null;
	    	
	    }

		/*@Override
		public void executeService() {
			System.out.println("inside tab frgament");
			Fragment currentTab = getChildFragmentManager().findFragmentById(android.R.id.tabcontent);
			if (currentTab instanceof TaskScheduler) {
				((TaskScheduler)currentTab).executeService();
			}
		}*/
		
		
}
