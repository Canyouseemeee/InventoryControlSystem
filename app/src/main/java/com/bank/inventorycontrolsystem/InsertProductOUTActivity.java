package com.bank.inventorycontrolsystem;

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
import android.widget.EditText;
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
import com.bank.inventorycontrolsystem.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InsertProductOUTActivity extends AppCompatActivity {

    private EditText mOutProductNo;
    private EditText mOutProductPrice;
    private EditText mOutProductQty;
    private Button mOutCancle;
    private Button mOutOk;
    private Spinner mOutSpinner;
    private ArrayList<Product> items;
    private String mProductId;
    private static final String TAG = "InsertProductOUTActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product_out);

        mOutProductNo = (EditText) findViewById(R.id.out_product_no);
        mOutProductPrice = (EditText) findViewById(R.id.out_product_price);
        mOutProductQty = (EditText) findViewById(R.id.out_product_qty);
        mOutCancle = (Button) findViewById(R.id.out_bt_cancel);
        mOutOk = (Button) findViewById(R.id.out_bt_ok);
        mOutSpinner = (Spinner) findViewById(R.id.out_spinner);

        mOutCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mOutOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProductOut();
//                InsertProduct();
            }
        });

        mOutProductNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String sql = "SELECT * FROM productout WHERE ProductOutNo = '" + charSequence + "'";
                Dru.connection(ConnectDB.getConnection())
                        .execute(sql)
                        .commit(new ExecuteQuery() {
                            @Override
                            public void onComplete(ResultSet resultSet) {
                                try {
                                    if (resultSet.next()) {
                                        mOutOk.setEnabled(false);
                                        mOutProductNo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                        Toast.makeText(getBaseContext(), "รหัสเลขสินค้าออกซ้ำ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mOutOk.setEnabled(true);
                                        mOutProductNo.setTextColor(getResources().getColor(android.R.color.background_dark));
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

        mOutProductQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String Price = mOutProductPrice.getText().toString().trim();
                String ProductNo = mProductId;
                String sql = "SELECT * FROM product WHERE product_id = '" + ProductNo + "' AND qty < '" + charSequence + "'";
                Dru.connection(ConnectDB.getConnection())
                        .execute(sql)
                        .commit(new ExecuteQuery() {
                            @Override
                            public void onComplete(ResultSet resultSet) {
                                try {
                                    if (resultSet.next()) {
                                        mOutOk.setEnabled(false);
                                        mOutProductQty.setTextColor(getResources().getColor(android.R.color.holo_red_light));
//                                        Toast.makeText(getBaseContext(), "จำนวนสินค้าเกินจำนวนสินค้าที่อยู่ในคลัง", Toast.LENGTH_SHORT).show();
                                        mOutProductQty.setError("จำนวนสินค้าเกินจำนวนสินค้าที่อยู่ในคลัง");
                                        mOutProductQty.setFocusable(true);
                                    } else {
                                        mOutOk.setEnabled(true);
                                        mOutProductQty.setTextColor(getResources().getColor(android.R.color.background_dark));
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

    private void insertProductOut() {
        String ProductNo = mOutProductNo.getText().toString().trim();
        String Price = mOutProductPrice.getText().toString().trim();
        String qty = mOutProductQty.getText().toString().trim();
        int qty_2 = Integer.parseInt(mOutProductQty.getText().toString().trim());
        int Price_2 = Integer.parseInt(mOutProductPrice.getText().toString().trim());


        if (ProductNo.isEmpty()) {
            mOutProductNo.setError("กรุณาใส่ข้อมูลรหัสเลขสินค้า");
            mOutProductNo.setFocusable(true);
            return;
        } else if (Price.isEmpty()) {
            mOutProductPrice.setError("กรุณาใส่ข้อมูลราคาสินค้า");
            mOutProductPrice.setFocusable(true);
            return;
        } else if (qty.isEmpty()) {
            mOutProductQty.setError("กรุณาใส่ข้อมูลจำนวนสินค้า");
            mOutProductQty.setFocusable(true);
            return;
        } else if (Price_2 == 0) {
            mOutProductPrice.setError("กรุณาใส่ราคาสินค้า");
            mOutProductPrice.setFocusable(true);
            mOutOk.setEnabled(false);
            return;
        } else if (qty_2 == 0) {
            mOutProductQty.setError("กรุณาใส่ข้อมูลจำนวนสินค้า");
            mOutProductQty.setFocusable(true);
            mOutOk.setEnabled(false);
            return;
        } else{
            mOutOk.setEnabled(true);
        }

        String sql = "INSERT INTO productout VALUES ('" + ProductNo + "','" + mProductId + "',CURRENT_DATE()," + qty + "," + Price + ")";
        Log.d(TAG, "insertProductOut: " + sql);
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {
                        ProductOutinsert();
                    }
                });
    }

    private void ProductOutinsert() {
        String Price = mOutProductPrice.getText().toString().trim();
        String qty = mOutProductQty.getText().toString().trim();

        String sql = "UPDATE product SET qty = qty - " + qty + ", price = " + Price + " WHERE product_id = '" + mProductId + "' AND qty >= 0";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getBaseContext(), "บันทึกข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
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
                            items = new ArrayList<Product>();
                            while (resultSet.next()) {
                                Product product = new Product();
                                product.setProductId(resultSet.getString("product_id"));
                                product.setProductName(resultSet.getString("name"));
                                product.setImage(resultSet.getString("image"));

                                items.add(product);

                            }

                            mOutSpinner.setAdapter(new ProductAdapter(getBaseContext(), items));
                            mOutSpinner.setSelection(0);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });
        mOutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Product product = (Product) adapterView.getItemAtPosition(position);
                mProductId = product.getProductId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class ProductAdapter extends ArrayAdapter<Product> {
        public ProductAdapter(Context baseContext, ArrayList<Product> item) {
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


            Product product = getItem(position);
            Dru.loadImageCircle(ProductImage, ConnectDB.Base_IMAGE + product.getImage());
            ProductName.setText(product.getProductName());

            return convertView;
        }
    }
}
