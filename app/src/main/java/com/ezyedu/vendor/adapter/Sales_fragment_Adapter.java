package com.ezyedu.vendor.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ezyedu.vendor.fragment.All_sales_fragment;
import com.ezyedu.vendor.fragment.canceled_sales_fragment;
import com.ezyedu.vendor.fragment.completed_sales_fragment;
import com.ezyedu.vendor.fragment.pendeing_sales_fragment;
import com.ezyedu.vendor.fragment.processing_sales_fragment;

public class Sales_fragment_Adapter extends FragmentStateAdapter {

    public Sales_fragment_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new processing_sales_fragment();
            case 2:
                return new completed_sales_fragment();
            case 3:
                return new canceled_sales_fragment();
        }
        return new pendeing_sales_fragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
