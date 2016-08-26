package com.iamdonor.fragment;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.iamadonar.utils.RobotCondensedTextView;
import com.iamadonor.splash.R;
import com.iamdonor.child.frgament.AddEventFragment;
import com.iamdonor.child.frgament.EmergencyFragment;
import com.iamdonor.child.frgament.EventsFragment;
import com.iamdonor.child.frgament.PostEmergencyFragment;

public class EmergencyTabFragments extends SherlockFragment{
	private FragmentTabHost tabHost;
	private TextView tagTV;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab_host_layout, container, false);

        tabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);
        
        tabHost.setup(getSherlockActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("AskDonationTag").setIndicator(buildTabLayout("Emergency")),
                EmergencyFragment.class, null);
        
        tabHost.addTab(tabHost.newTabSpec("PostDonationTag").setIndicator(buildTabLayout("Ask Donation")),
                PostEmergencyFragment.class, null);
        
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
}
