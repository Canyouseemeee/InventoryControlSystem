package com.bank.inventorycontrolsystem;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class InsertProductINActivity extends AppCompatActivity {

    private EditText mInProductNo;
    private EditText mInProductQty;
    private EditText mInProductPrice;
    private Button mInCancle;
    private Button mInOk;
    private ArrayList<Product> items;
    private Spinner mInSpinner;
    private String mProductId;
    private static final String TAG = "InsertProductINActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product_in);

        mInProductNo = (EditText) findViewById(R.id.in_in_product_no);
        mInProductPrice = (EditText) findViewById(R.id.in_in_product_price);
        mInProductQty = (EditText) findViewById(R.id.in_in_product_qty);
        mInCancle = (Button) findViewById(R.id.bt_cancel);
        mInOk = (Button) findViewById(R.id.bt_ok);
        mInSpinner = (Spinner) findViewById(R.id.in_in_spinner);
        mInCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mInOk.setEnabled(true);
        mInOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProductIn();
//                InsertProduct();
            }
        });

        mInProductNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String sql = "SELECT * FROM productin WHERE ProductInNo = '" + charSequence + "'";
                Dru.connection(ConnectDB.getConnection())
                        .execute(sql)
                        .commit(new ExecuteQuery() {
                            @Override
                            public void onComplete(ResultSet resultSet) {
                                try {
                                    if (resultSet.next()) {
                                        mInOk.setEnabled(false);
                                        mInProductNo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                        Toast.makeText(getBaseContext(), "รหัสเลขสินค้าเข้าซ้ำ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mInOk.setEnabled(true);
                                        mInProductNo.setTextColor(getResources().getColor(android.R.color.background_dark));
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
    // TODO:Stork
//    private void InsertProduct() {
//        Dru.with(ConnectDB.getConnection()).call("insertproduct").parameter(mInProductNo.getText().toString().trim())
//                .parameter(mProductId)
//                .parameter(mInProductQty.getText().toString().trim())
//                .parameter(mInProductPrice.getText().toString().trim())
//                .commit(new ExecuteUpdate() {
//                    @Override
//                    public void onComplete() {
//                        finish();
//                        Toast.makeText(getBaseContext(), "บันทึกข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void insertProductIn() {
        String ProductNo = mInProductNo.getText().toString().trim();
        String Price = mInProductPrice.getText().toString().trim();
        String qty = mInProductQty.getText().toString().trim();
        int qty_2 = Integer.parseInt(mInProductQty.getText().toString().trim());
        int Price_2 = Integer.parseInt(mInProductPrice.getText().toString().trim());

        if (ProductNo.isEmpty()) {
            mInProductNo.setError("กรุณาใส่ข้อมูลรหัสเลขสินค้า");
            mInProductNo.setFocusable(true);
            return;
        } else if (Price.isEmpty()) {
            mInProductPrice.setError("กรุณาใส่ข้อมูลราคาสินค้า");
            mInProductPrice.setFocusable(true);
            return;
        } else if (qty.isEmpty()) {
            mInProductQty.setError("กรุณาใส่ข้อมูลจำนวนสินค้า");
            mInProductQty.setFocusable(true);
            return;
        } else if (Price_2 == 0) {
            mInProductPrice.setError("กรุณาใส่ราคาสินค้า");
            mInProductPrice.setFocusable(true);
            return;
        } else if (qty_2 == 0) {
            mInProductQty.setError("กรุณาใส่ข้อมูลจำนวนสินค้า");
            mInProductQty.setFocusable(true);
            return;
        }


        String sql = "INSERT INTO productin VALUES ('" + ProductNo + "','" + mProductId + "',CURRENT_DATE()," + qty + "," + Price + ")";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getBaseContext(), "บันทึกข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
                        Productinsert();
                        finish();
                    }
                });

    }

    private void Productinsert() {
        String Price = mInProductPrice.getText().toString().trim();
        String qty = mInProductQty.getText().toString().trim();

        String sql = "UPDATE product SET qty = qty + " + qty + ", price = " + Price + " WHERE product_id = '" + mProductId + "'";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {

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
//                                type.setTypeID(resultSet.getString("ProductTypeID"));
//                                type.setTypeName(resultSet.getString("ProductTypeName"));
//                                items.add(type);
                                items.add(product);

                            }

                            mInSpinner.setAdapter(new ProductAdapter(getBaseContext(), items));
                            mInSpinner.setSelection(0);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });
        mInSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
