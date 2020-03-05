package com.bank.inventorycontrolsystem;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class ReportProductDialog extends DialogFragment {


    private OnAttachSearchListener listener;
    private EditText mEt_report_Begin;
    private EditText mEt_report_End;
    private Button mBtSearch;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_report_product, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("รายงานตามรหัสสินค้า");
        mEt_report_Begin = (EditText) view.findViewById(R.id.et_report_begin);
        mEt_report_End = (EditText) view.findViewById(R.id.et_report_end);
        mBtSearch = (Button) view.findViewById(R.id.bt_search);

        mBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String begin = mEt_report_Begin.getText().toString().trim();
                String end = mEt_report_End.getText().toString().trim();
                listener.OnAttach(begin, end);
                getDialog().dismiss();
            }
        });

        return builder.setView(view).create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnAttachSearchListener) context;
    }
}
