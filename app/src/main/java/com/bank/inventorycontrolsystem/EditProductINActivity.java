package com.bank.inventorycontrolsystem;

import android.content.Context;
import android.os.Bundle;
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
import com.bank.inventorycontrolsystem.model.ProductIN;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditProductINActivity extends AppCompatActivity {

    private ProductIN productin;
    private TextView mEdProductIN_no;
    private TextView mEdProductIN_price;
    private TextView mEdProductIN_qty;
    private Button mEdBtn_cancle;
    private Button mEdBtn_ok;
    private Spinner medSpinner;
    private ArrayList<ProductIN> items;
    private String mEdProductId;
    private static final String TAG = "EditProductINActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_in);

        productin = getIntent().getParcelableExtra("productin");

        mEdProductIN_no = (TextView) findViewById(R.id.ed_in_product_no);
        mEdProductIN_price = (TextView) findViewById(R.id.ed_in_product_price);
        mEdProductIN_qty = (TextView) findViewById(R.id.ed_in_product_qty);
        mEdBtn_cancle = (Button) findViewById(R.id.ed_bt_cancel);
        mEdBtn_ok = (Button) findViewById(R.id.ed_bt_ok);
        medSpinner = (Spinner) findViewById(R.id.ed_in_spinner);

        mEdProductIN_no.setText(productin.getProductIdNo());
        mEdProductIN_price.setText(String.format("%,.2f", productin.getPrice()).replace(",", ""));
        mEdProductIN_qty.setText(String.format("%,.0f", (double) productin.getQuantity()).replace(",", ""));

        mEdBtn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEdBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProductIN();
            }
        });

    }

    private void EditProductIN() {
        String ProductNo = mEdProductIN_no.getText().toString().trim();
        final String ProductPrice = mEdProductIN_price.getText().toString().trim();
        final String Productqty = mEdProductIN_qty.getText().toString().trim();

        if (ProductNo.isEmpty()) {
            mEdProductIN_no.setError("ว่างเปล่า");
            mEdProductIN_no.setFocusable(true);
            return;
        } else if (ProductPrice.isEmpty()) {
            mEdProductIN_price.setError("ว่างเปล่า");
            mEdProductIN_price.setFocusable(true);
            return;
        } else if (Productqty.isEmpty()) {
            mEdProductIN_qty.setError("ว่างเปล่า");
            mEdProductIN_qty.setFocusable(true);
            return;
        }


        String sql = "UPDATE productin SET ProductInNo='" + ProductNo + "',product_id='" + mEdProductId + "',DateIn=CURRENT_DATE(),Quantity=" + Productqty + ",Price=" + ProductPrice + " WHERE ProductInNo = '" + ProductNo + "' AND " + Productqty + " > 0";
        Log.d(TAG, "EditProductIN: ");
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {
                        updateProduct(Productqty, ProductPrice);

                    }
                });
    }

    private void updateProduct(String Productqty, String ProductPrice) {
        String sql = "UPDATE product SET price=" + ProductPrice + ",qty = (qty - " + productin.getQuantity() + ")+" + Productqty + " WHERE product_id = '" + mEdProductId + "'";
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
                            items = new ArrayList<ProductIN>();
                            while (resultSet.next()) {
                                ProductIN productIN = new ProductIN();
                                productIN.setProductId(resultSet.getString("product_id"));
                                productIN.setName(resultSet.getString("name"));
                                productIN.setImage(resultSet.getString("image"));
                                items.add(productIN);

                            }

                            medSpinner.setAdapter(new ProductAdapter(getBaseContext(), items));
                            for (int i = 0; i < items.size(); i++) {
                                if (productin.getProductId().equals(items.get(i).getProductId())) {
                                    medSpinner.setSelection(i);
                                    mEdProductId = items.get(i).getProductId();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });
        medSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ProductIN productIN = (ProductIN) adapterView.getItemAtPosition(position);
                mEdProductId = productIN.getProductId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class ProductAdapter extends ArrayAdapter<ProductIN> {
        public ProductAdapter(Context baseContext, ArrayList<ProductIN> item) {
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


            ProductIN productIN = getItem(position);
            Dru.loadImageCircle(ProductImage, ConnectDB.Base_IMAGE + productIN.getImage());
            ProductName.setText(productIN.getName());

            return convertView;
        }
    }
}
