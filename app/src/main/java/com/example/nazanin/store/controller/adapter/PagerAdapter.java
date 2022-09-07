package com.example.nazanin.store.controller.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nazanin.store.controller.fragment.viewPagerFragments.ProductViewpagerFragment;
import com.example.nazanin.store.model.DTO.Customer;

public class PagerAdapter extends FragmentPagerAdapter {
    private int tabCount;
    private Customer customer;
    public PagerAdapter(FragmentManager fm, int tabCount, Customer customer) {
        super(fm);
        this.tabCount=tabCount;
        this.customer=customer;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            //send pos to frag
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                Bundle bundle=new Bundle();
                bundle.putInt("position",position+1);
                bundle.putParcelable("customer",customer);
                ProductViewpagerFragment viewpagerFragment=new ProductViewpagerFragment();
                viewpagerFragment.setArguments(bundle);
                return viewpagerFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
