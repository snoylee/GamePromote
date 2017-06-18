package com.xygame.sg.adapter.main;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter<T> extends FragmentPagerAdapter {

	private ArrayList<T> list;
	public FragmentAdapter(FragmentManager fm, ArrayList<T> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return (Fragment) list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
