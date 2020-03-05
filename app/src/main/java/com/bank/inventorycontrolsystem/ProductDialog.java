package com.bank.inventorycontrolsystem;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.adedom.library.Dru;
import com.bank.inventorycontrolsystem.model.Product;

public class ProductDialog extends DialogFragment {


    private ImageView dTvimage;
    private TextView dTvname;
    private TextView dTvprice;
    private TextView dTvqty;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Product product = getArguments().getParcelable("product");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_product, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("สินค้า");

        dTvimage = (ImageView) view.findViewById(R.id.tv_image);
        dTvname = (TextView) view.findViewById(R.id.tv_name);
        dTvprice = (TextView) view.findViewById(R.id.tv_price);
        dTvqty = (TextView) view.findViewById(R.id.tv_qty);

        dTvname.setText(product.getProductName());
        dTvprice.setText(String.format("%,.2f",product.getPrice()) + "  บาท");
        dTvqty.setText(String.format("%,.0f",(double)product.getQty()) + " "+product.getUnit_name());
        Dru.loadImageCircle(dTvimage, ConnectDB.Base_IMAGE + product.getImage());

        return builder.setView(view).create();

    }
}
