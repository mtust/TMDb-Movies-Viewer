package com.example.tust.tmdbmovieviewer.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tust.tmdbmovieviewer.FragmentActivity.FragmentMovie;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final int PAGES = 3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return  FragmentMovie.getFm(position);
    }

    @Override
    public int getCount() {
        return PAGES;
    }
}