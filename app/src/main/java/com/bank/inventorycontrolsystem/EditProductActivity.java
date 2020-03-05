package com.bank.inventorycontrolsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.bank.inventorycontrolsystem.model.Product;
import com.bank.inventorycontrolsystem.model.Type;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class EditProductActivity extends AppCompatActivity {

    private TextView mEdProductId;
    private TextView mEdProductName;
    private TextView mEdProductPrice;
    private TextView mEdProductQty;
    private Button mEd_Bt_cancle;
    private Button mEd_Bt_ok;
    private ImageView mEd_add;
    private Spinner mEd_spinner;
    private ImageView mEdProductImage;
    private ArrayList<Type> item;
    private String mTypeId;
    private Bitmap mBitmap;
    private Product product;
    private static final String TAG = "EditProductActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        product = getIntent().getParcelableExtra("product");

//        String product_id = getIntent().getStringExtra("product_id");
//        String product_name = getIntent().getStringExtra("product_name");
//        double price = getIntent().getDoubleExtra("price",0.00);
//        int qty = getIntent().getIntExtra("qty",0);
//        String image = getIntent().getStringExtra("image");
//        String type_id = getIntent().getStringExtra("type_id");
//        String type_name = getIntent().getStringExtra("type_name");

        mEdProductId = (TextView) findViewById(R.id.ed_product_id);
        mEdProductName = (TextView) findViewById(R.id.ed_product_name);
        mEdProductPrice = (TextView) findViewById(R.id.ed_product_price);
        mEdProductQty = (TextView) findViewById(R.id.ed_product_qty);
        mEdProductImage = (ImageView) findViewById(R.id.ed_product_image);
        mEd_Bt_cancle = (Button) findViewById(R.id.ed_bt_cancel);
        mEd_Bt_ok = (Button) findViewById(R.id.ed_bt_ok);
        mEd_add = (ImageView) findViewById(R.id.ed_add);
        mEd_spinner = (Spinner) findViewById(R.id.ed_spinner);

        mEdProductId.setText(product.getProductId());
        mEdProductName.setText(product.getProductName());
        mEdProductPrice.setText(String.format("%,.2f", product.getPrice()).replace(",", ""));
        mEdProductQty.setText(String.format("%,.0f", (double) product.getQty()).replace(",", ""));

        Dru.loadImageCircle(mEdProductImage, ConnectDB.Base_IMAGE + product.getImage());
        mEd_Bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEdProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dru.selectImage(EditProductActivity.this, 2345);
            }
        });

        mEd_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), AddTypeActivity.class));
            }
        });


        mEd_Bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProduct();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2345 && resultCode == RESULT_OK && data != null) {
            try {
                Uri path = data.getData();
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                mEdProductImage.setImageBitmap(mBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void EditProduct() {
        String ProductId = mEdProductId.getText().toString().trim();
        String ProductName = mEdProductName.getText().toString().trim();
        String Price = mEdProductPrice.getText().toString().trim();
        String qty = mEdProductQty.getText().toString().trim();

        if (ProductName.isEmpty()) {
            mEdProductName.setError("ว่างเปล่า");
            mEdProductName.setFocusable(true);
            return;
        } else if (Price.isEmpty()) {
            mEdProductPrice.setError("ว่างเปล่า");
            mEdProductPrice.setFocusable(true);
            return;
        } else if (qty.isEmpty()) {
            mEdProductQty.setError("ว่างเปล่า");
            mEdProductQty.setFocusable(true);
            return;
        }

        String name = "empty";
        if (mBitmap != null) {
            name = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            Dru.uploadImage(ConnectDB.Base_IMAGE, name, mBitmap);
        }

        String sql = "UPDATE product SET name='" + ProductName + "',price=" + Price + ",qty=" + qty + ",image='" + name + "',ProductTypeID='" + mTypeId + "' WHERE product_id = '" + ProductId + "' AND " + qty + " > 0";
        Log.d(TAG, "EditProduct: " + sql);
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

                            mEd_spinner.setAdapter(new TypeAdapter(getBaseContext(), item));
                            for (int i = 0; i < item.size(); i++) {
                                if (product.getTypeId().equals(item.get(i).getTypeID())) {
                                    mEd_spinner.setSelection(i);
                                    mTypeId = item.get(i).getTypeID();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });
        mEd_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

     

        