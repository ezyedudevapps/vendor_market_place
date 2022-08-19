package com.ezyedu.vendor.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ezyedu.vendor.fragment.Product_preview_fragment;
import com.ezyedu.vendor.fragment.Profile_preview_fragment;
import com.ezyedu.vendor.fragment.canceled_sales_fragment;
import com.ezyedu.vendor.fragment.completed_sales_fragment;
import com.ezyedu.vendor.fragment.feed_preview_fragment;
import com.ezyedu.vendor.fragment.pendeing_sales_fragment;
import com.ezyedu.vendor.fragment.processing_sales_fragment;

public class preview_fragment_adapter extends FragmentStateAdapter {
    public preview_fragment_adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new Product_preview_fragment();
            case 2:
                return new feed_preview_fragment();
        }
        return new Profile_preview_fragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
