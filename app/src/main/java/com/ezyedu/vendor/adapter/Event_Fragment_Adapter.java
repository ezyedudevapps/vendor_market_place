package com.ezyedu.vendor.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ezyedu.vendor.fragment.Approved_Event_Fragment;
import com.ezyedu.vendor.fragment.Cancelled_Event_Fragment;
import com.ezyedu.vendor.fragment.Pending_Event_Fragment;

public class Event_Fragment_Adapter extends FragmentStateAdapter {

    public Event_Fragment_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new Pending_Event_Fragment();
            case 2:
                return new Cancelled_Event_Fragment();
        }
        return new Approved_Event_Fragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
