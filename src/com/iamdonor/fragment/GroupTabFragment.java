package com.iamdonor.fragment;

import java.lang.reflect.Field;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.iamadonar.utils.RobotCondensedTextView;
import com.iamadonor.splash.R;
import com.iamdonor.child.frgament.GroupsFragment;
import com.iamdonor.child.frgament.MyGroupFragment;
import com.robo.userinterface.TabInterface;

public class GroupTabFragment extends SherlockFragment{
	
	private static FragmentTabHost tabHost;
	private static SherlockFragmentActivity mContext;
	private ActionBar mActionBar;
	private Resources mResource;
	private RobotCondensedTextView tagTV;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tab_host_layout, container, false);
        mContext = getSherlockActivity();
        mActionBar = mContext.getSupportActionBar();
        mResource = getResources();
        tabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);
        
        tabHost.setup(getSherlockActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("GroupsTag").setIndicator(buildTabLayout("Groups")),
                GroupsFragment.class, null);

        
        tabHost.addTab(tabHost.newTabSpec("MyGroupsTag").setIndicator(buildTabLayout("My Groups")),
                MyGroupFragment.class, null);

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
    

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		final String currentTab = tabHost.getCurrentTabTag();
		if("MyGroupsTag".equals(currentTab)){
			inflater.inflate(R.menu.add_group_menu, menu);
	    } 
		super.onCreateOptionsMenu(menu, inflater);
		
	}	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment currentTab = getChildFragmentManager().findFragmentById(android.R.id.tabcontent);
		switch (item.getItemId()) {
		case R.id.action_create_group:
			if (currentTab instanceof TabInterface) {
				((TabInterface)currentTab).createGroup();
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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
