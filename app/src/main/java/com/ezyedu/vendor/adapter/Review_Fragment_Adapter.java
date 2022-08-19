package com.ezyedu.vendor.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ezyedu.vendor.fragment.Course_Review_Fragment;
import com.ezyedu.vendor.fragment.Institution_Review_Fragment;
import com.ezyedu.vendor.fragment.canceled_sales_fragment;
import com.ezyedu.vendor.fragment.completed_sales_fragment;
import com.ezyedu.vendor.fragment.pendeing_sales_fragment;
import com.ezyedu.vendor.fragment.processing_sales_fragment;

public class Review_Fragment_Adapter extends FragmentStateAdapter {
    public Review_Fragment_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1)
        {
            return new Course_Review_Fragment();
        }
        return new Institution_Review_Fragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
