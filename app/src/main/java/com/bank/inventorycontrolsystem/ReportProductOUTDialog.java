package com.bank.inventorycontrolsystem;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class ReportProductOUTDialog extends DialogFragment {


    private OnAttachSearchListener listener;
    private EditText mOut_report_Begin;
    private EditText mOut_report_End;
    private Button mOutBtSearch;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    private Button mBtDateBegin;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_out_report_product, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("รายงานตามเลขสินค้ารับเข้า");
        mOut_report_Begin = (EditText) view.findViewById(R.id.out_report_begin);
        mOut_report_End = (EditText) view.findViewById(R.id.out_report_end);
        mOutBtSearch = (Button) view.findViewById(R.id.out_bt_search);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        mOut_report_Begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportProductOUTActivity.sActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "/" + month + "/" + day;
                        mOut_report_Begin.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

        mOut_report_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportProductOUTActivity.sActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "/" + month + "/" + day;
                        mOut_report_End.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

        mOutBtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String begin = mOut_report_Begin.getText().toString().trim();
                String end = mOut_report_End.getText().toString().trim();
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
