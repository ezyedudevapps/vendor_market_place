package com.ezyedu.vendor.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ezyedu.vendor.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private BottomSheetListner mlistner;
    SharedPreferences sharedPreferences;
    String status_code = null;

    Button proceed_order,cancel_btn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("order_status", Context.MODE_PRIVATE);
        status_code = sharedPreferences.getString("status_code","");
        Log.i("Session_bottom_activity",status_code);

        if (status_code.equals("1"))
        {
            proceed_order.setText("Proceed to Processing");
        }
        else if (status_code.equals("2"))
        {
            proceed_order.setVisibility(View.GONE);
        }
        else if (status_code.equals("3"))
        {
            proceed_order.setText("Complete Your Order");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet,container,false);

         proceed_order = view.findViewById(R.id.order_sts);
         cancel_btn = view.findViewById(R.id.cancel_btm);

        proceed_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mlistner.onButtonClicked("proceed");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mlistner.onButtonClicked("cancel");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
        return view;
    }

    public interface BottomSheetListner{
        void onButtonClicked(String text) throws JSONException;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mlistner = (BottomSheetListner) context;
    }
}
