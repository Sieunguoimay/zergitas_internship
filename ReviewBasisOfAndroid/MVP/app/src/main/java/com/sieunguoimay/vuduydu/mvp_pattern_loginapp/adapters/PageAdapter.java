package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.fragments.BrowserFragment;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.fragments.CanvasFragment;
import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.fragments.EditorFragment;

import java.util.AbstractList;
import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    private EditorFragment editorFragment;
    public CanvasFragment canvasFragment;
    private BrowserFragment browserFragment;
    public PageAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs= numOfTabs;
        editorFragment = new EditorFragment();
        canvasFragment =  new CanvasFragment();
        browserFragment = new BrowserFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: return editorFragment;
            case 1:  return canvasFragment;
            case 2: return browserFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() { return numOfTabs; }
}
