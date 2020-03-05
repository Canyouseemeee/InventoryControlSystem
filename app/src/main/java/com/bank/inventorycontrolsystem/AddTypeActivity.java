package com.bank.inventorycontrolsystem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.adedom.library.ExecuteUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AddTypeActivity extends AppCompatActivity {

    private EditText mTyProductId;
    private EditText mTyProductName;
    private Button mTyCancle;
    private Button mTyOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);

        mTyProductId = (EditText) findViewById(R.id.ty_product_id);
        mTyProductName = (EditText) findViewById(R.id.ty_product_name);
        mTyCancle = (Button) findViewById(R.id.bt_ty_cancel);
        mTyOk = (Button) findViewById(R.id.bt_ty_ok);

        mTyCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTyOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertType();

            }
        });

        mTyProductId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String sql = "SELECT * FROM producttype WHERE ProductTypeID = '" + charSequence + "'";
                Dru.connection(ConnectDB.getConnection())
                        .execute(sql)
                        .commit(new ExecuteQuery() {
                            @Override
                            public void onComplete(ResultSet resultSet) {
                                try {
                                    if (resultSet.next()) {
                                        mTyOk.setEnabled(false);
                                        mTyProductId.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                        Toast.makeText(getBaseContext(), "รหัสประเภทซ้ำ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mTyOk.setEnabled(true);
                                        mTyProductId.setTextColor(getResources().getColor(android.R.color.background_dark));
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void InsertType() {
        String ProductTypeId = mTyProductId.getText().toString().trim();
        String ProductTypeName = mTyProductName.getText().toString().trim();


        if (ProductTypeId.isEmpty()) {
            mTyProductId.setError("กรุณาใส่ข้อมูลรหัสสินค้า");
            mTyProductId.setFocusable(true);
            return;
        } else if (ProductTypeName.isEmpty()) {
            mTyProductName.setError("กรุณาใส่ข้อมูลชื่อสินค้า");
            mTyProductName.setFocusable(true);
            return;
        }

        String sql = "INSERT INTO producttype(ProductTypeID, ProductTypeName) VALUES ('" + ProductTypeId + "','" + ProductTypeName + "')";
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
}
