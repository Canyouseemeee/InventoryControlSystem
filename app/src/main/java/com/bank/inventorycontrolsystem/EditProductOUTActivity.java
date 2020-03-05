package com.bank.inventorycontrolsystem;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.adedom.library.ExecuteUpdate;
import com.bank.inventorycontrolsystem.model.ProductOUT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditProductOUTActivity extends AppCompatActivity {

    private static final String TAG = "EditProductOUTActivity";
    private ProductOUT productOUT;
    private TextView mEdProductOUT_no;
    private TextView mEdProductOUT_price;
    private TextView mEdProductOUT_qty;
    private Button mEdOUTBtn_cancle;
    private Button mEdOUTBtn_ok;
    private Spinner medOUTSpinner;
    private ArrayList<ProductOUT> items;
    private String mEdProductId;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_out);

        productOUT = getIntent().getParcelableExtra("productOUT");

        mEdProductOUT_no = (TextView) findViewById(R.id.ed_out_product_no);
        mEdProductOUT_price = (TextView) findViewById(R.id.ed_out_product_price);
        mEdProductOUT_qty = (TextView) findViewById(R.id.ed_out_product_qty);
        mEdOUTBtn_cancle = (Button) findViewById(R.id.ed_out_bt_cancel);
        mEdOUTBtn_ok = (Button) findViewById(R.id.ed_out_bt_ok);
        medOUTSpinner = (Spinner) findViewById(R.id.ed_out_spinner);

        mEdProductOUT_no.setText(productOUT.getProductOutNo());
        mEdProductOUT_price.setText(String.format("%,.2f", productOUT.getPrice()).replace(",", ""));
        mEdProductOUT_qty.setText(String.format("%,.0f", (double) productOUT.getQuantity()).replace(",", ""));

        mEdOUTBtn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEdOUTBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProductOUT();
            }
        });

        mEdProductOUT_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String ProductNo = mEdProductId;
                String sql = "SELECT * FROM product WHERE product_id = '" + ProductNo + "' AND qty +" + productOUT.getQuantity() + " < '" + charSequence + "'";
                Dru.connection(ConnectDB.getConnection())
                        .execute(sql)
                        .commit(new ExecuteQuery() {
                            @Override
                            public void onComplete(ResultSet resultSet) {
                                try {
                                    if (resultSet.next()) {
                                        mEdOUTBtn_ok.setEnabled(false);
                                        mEdProductOUT_qty.setTextColor(getResources().getColor(android.R.color.holo_red_light));
//                                        Toast.makeText(getBaseContext(), "จำนวนสินค้าเกินจำนวนสินค้าที่อยู่ในคลัง", Toast.LENGTH_SHORT).show();
                                        mEdProductOUT_qty.setError("จำนวนสินค้าเกินจำนวนสินค้าที่อยู่ในคลัง");
                                        mEdProductOUT_qty.setFocusable(true);
                                    } else {
                                        mEdOUTBtn_ok.setEnabled(true);
                                        mEdProductOUT_qty.setTextColor(getResources().getColor(android.R.color.background_dark));
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void EditProductOUT() {
        final String ProductNo = mEdProductOUT_no.getText().toString().trim();
        final String ProductPrice = mEdProductOUT_price.getText().toString().trim();
        final String Productqty = mEdProductOUT_qty.getText().toString().trim();

        if (ProductNo.isEmpty()) {
            mEdProductOUT_no.setError("ว่างเปล่า");
            mEdProductOUT_no.setFocusable(true);
            return;
        } else if (ProductPrice.isEmpty()) {
            mEdProductOUT_price.setError("ว่างเปล่า");
            mEdProductOUT_price.setFocusable(true);
            return;
        } else if (Productqty.isEmpty()) {
            mEdProductOUT_qty.setError("ว่างเปล่า");
            mEdProductOUT_qty.setFocusable(true);
            return;
        } else if (productOUT.getQuantity() <= 0) {
            mEdProductOUT_qty.setError("ว่างเปล่า");
            mEdProductOUT_qty.setFocusable(true);
            mEdOUTBtn_ok.setEnabled(false);

        }


        String sql = "UPDATE productout SET ProductOutNo='" + ProductNo + "',product_id='" + mEdProductId + "',DateOut=CURRENT_DATE(),Quantity=" + Productqty + ",Price=" + ProductPrice + " WHERE ProductOutNo = '" + ProductNo + "' AND " + Productqty + " > 0";
        Log.d(TAG, "EditProductOUT: " + sql);
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {
                        updateProduct(ProductNo, Productqty, ProductPrice);
                    }
                });
    }

    private void updateProduct(String productNo, String productqty, String productPrice) {
//        String sql = "UPDATE product p ,productout o  SET p.price = " + productPrice + ",p.qty = (p.qty + o.Quantity)  - " + productqty + " WHERE p.product_id = '" + mEdProductId + "' AND o.ProductOutNo = '" + productNo + "'";

        String sql = "UPDATE product SET price=" + productPrice + ",qty =( qty + " + productOUT.getQuantity() + ") - " + productqty + " WHERE product_id = '" + productOUT.getProductId() + "'";
        Log.d(TAG, "updateProduct: " + sql);
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getBaseContext(), "อัพเดทข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpinner();
    }

    private void setSpinner() {
        String sql = "SELECT * FROM product ORDER BY product_id DESC";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        try {
                            items = new ArrayList<ProductOUT>();
                            while (resultSet.next()) {
                                ProductOUT productOUT = new ProductOUT();
                                productOUT.setProductId(resultSet.getString("product_id"));
                                productOUT.setName(resultSet.getString("name"));
                                productOUT.setImage(resultSet.getString("image"));
                                items.add(productOUT);

                            }

                            medOUTSpinner.setAdapter(new ProductAdapter(getBaseContext(), items));
                            for (int i = 0; i < items.size(); i++) {
                                if (productOUT.getProductId().equals(items.get(i).getProductId())) {
                                    medOUTSpinner.setSelection(i);
                                    mEdProductId = items.get(i).getProductId();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });
        medOUTSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ProductOUT productOUT = (ProductOUT) adapterView.getItemAtPosition(position);
                mEdProductId = productOUT.getProductId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class ProductAdapter extends ArrayAdapter<ProductOUT> {
        public ProductAdapter(Context baseContext, ArrayList<ProductOUT> item) {
            super(baseContext, 0, item);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position, convertView, parent);
        }

        private View initView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_product, parent, false);
            }

            ImageView ProductImage = (ImageView) convertView.findViewById(R.id.sp_image);
            TextView ProductName = (TextView) convertView.findViewById(R.id.sp_name);


            ProductOUT productOUT = getItem(position);
            Dru.loadImageCircle(ProductImage, ConnectDB.Base_IMAGE + productOUT.getImage());
            ProductName.setText(productOUT.getName());

            return convertView;
        }
    }
}
