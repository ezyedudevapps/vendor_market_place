package com.ezyedu.vendor.model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ezyedu.vendor.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditBottomSheetDialog extends BottomSheetDialogFragment
{
    private EditSheetListner mlistner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.middle_sheet_layout,container,false);

        Button button1 = v.findViewById(R.id.edit_my_pf);
        Button button2 = v.findViewById(R.id.edit_insti_pf);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistner.onButtonClicked("EditMyProfile");
                dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistner.onButtonClicked("EditInstitutionProfile");
                dismiss();
            }
        });
        return v;
    }
    public interface EditSheetListner{
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mlistner = (EditSheetListner) context;
        }catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " Must Implement BottomSheetListner");
        }

    }
}
