package com.bank.inventorycontrolsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.bank.inventorycontrolsystem.model.Type;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class InsertProductActivity extends AppCompatActivity {

    private EditText mInProductId;
    private EditText mInProductName;
    private EditText mInProductPrice;
    private EditText mInProductQty;
    private ImageView mInImage;
    private Button mInCancle;
    private Button mInOk;
    private Bitmap mBitmap;
    private Spinner mInSpinner;
    private ImageView mInAddType;
    private ArrayList<Type> item;
    private String mTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        mInProductId = (EditText) findViewById(R.id.in_product_id);
        mInProductName = (EditText) findViewById(R.id.in_product_name);
        mInProductPrice = (EditText) findViewById(R.id.in_product_price);
        mInProductQty = (EditText) findViewById(R.id.in_product_qty);
        mInImage = (ImageView) findViewById(R.id.in_product_image);
        mInCancle = (Button) findViewById(R.id.bt_cancel);
        mInOk = (Button) findViewById(R.id.bt_ok);
        mInSpinner = (Spinner) findViewById(R.id.spinner);
        mInAddType = (ImageView) findViewById(R.id.in_add);

        mInCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dru.selectImage(InsertProductActivity.this, 1234);
            }
        });

        mInAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), AddTypeActivity.class));
            }
        });


        mInOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProduct();
            }
        });

        mInProductId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String sql = "SELECT * FROM product WHERE product_id = '" + charSequence + "'";
                Dru.connection(ConnectDB.getConnection())
                        .execute(sql)
                        .commit(new ExecuteQuery() {
                            @Override
                            public void onComplete(ResultSet resultSet) {
                                try {
                                    if (resultSet.next()) {
                                        mInOk.setEnabled(false);
                                        mInProductId.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                        Toast.makeText(getBaseContext(), "รหัสสินค้าซ้ำ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mInOk.setEnabled(true);
                                        mInProductId.setTextColor(getResources().getColor(android.R.color.background_dark));
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

    @Override
    protected void onResume() {
        super.onResume();
        setSpinner();
    }

    private void setSpinner() {
        String sql = "SELECT * FROM producttype ORDER BY ProductTypeID DESC";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        try {
                            item = new ArrayList<Type>();
                            while (resultSet.next()) {
                                Type type = new Type();
                                type.setTypeID(resultSet.getString("ProductTypeID"));
                                type.setTypeName(resultSet.getString("ProductTypeName"));
                                item.add(type);

                            }

                            mInSpinner.setAdapter(new TypeAdapter(getBaseContext(), item));
                            mInSpinner.setSelection(0);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });
        mInSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Type type = (Type) adapterView.getItemAtPosition(position);
                mTypeId = type.getTypeID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void insertProduct() {
        String ProductId = mInProductId.getText().toString().trim();
        String ProductName = mInProductName.getText().toString().trim();
        String Price = mInProductPrice.getText().toString().trim();
        String qty = mInProductQty.getText().toString().trim();
        int qty_2 = Integer.parseInt(mInProductQty.getText().toString().trim());
        int Price_2 = Integer.parseInt(mInProductPrice.getText().toString().trim());

        if (ProductId.isEmpty()) {
            mInProductId.setError("กรุณาใส่ข้อมูลรหัสสินค้า");
            mInProductId.setFocusable(true);
            return;
        } else if (ProductName.isEmpty()) {
            mInProductName.setError("กรุณาใส่ข้อมูลชื่อสินค้า");
            mInProductName.setFocusable(true);
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

        String name = "empty";
        if (mBitmap != null) {
            name = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            Dru.uploadImage(ConnectDB.Base_IMAGE, name, mBitmap);
        }

        String sql = "INSERT INTO product VALUES ('" + ProductId + "','" + ProductName + "'," + Price + "," + qty + ",'" + name + "','" + mTypeId + "')";
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            try {
                Uri path = data.getData();
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                mInImage.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private class TypeAdapter extends ArrayAdapter<Type> {
        public TypeAdapter(Context baseContext, ArrayList<Type> item) {
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
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            TextView TypeId = (TextView) convertView.findViewById(android.R.id.text1);
            TextView TypeName = (TextView) convertView.findViewById(android.R.id.text2);

            Type type = getItem(position);
            TypeId.setText(type.getTypeID());
            TypeName.setText(type.getTypeName());

            return convertView;
        }
    }


}
