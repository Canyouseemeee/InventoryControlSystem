package com.bank.inventorycontrolsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteUpdate;

public class RegisterActivity extends AppCompatActivity {

    private EditText mReUsername;
    private EditText mRePassword;
    private Button mCancle;
    private Button mConfirm;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mReUsername = (EditText) findViewById(R.id.mEdtusername);
        mRePassword = (EditText) findViewById(R.id.mEdtpassword);
        mCancle = (Button) findViewById(R.id.btr_cancle);
        mConfirm = (Button) findViewById(R.id.btr_confirm);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProduct();
            }
        });
    }

    private void insertProduct() {
        String Username = mReUsername.getText().toString().trim();
        String Password = mRePassword.getText().toString().trim();
        String id = "0";

        if (Username.isEmpty()) {
            mReUsername.setError("กรุณาใส่ข้อมูลรหัสสินค้า");
            mReUsername.setFocusable(true);
            return;
        } else if (Password.isEmpty()) {
            mRePassword.setError("กรุณาใส่ข้อมูลชื่อสินค้า");
            mRePassword.setFocusable(true);
            return;
        }


        String sql = "INSERT INTO user VALUES ('"+id+"','" + Username + "','" + Password + "')";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteUpdate() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getBaseContext(), "บันทึกข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getBaseContext(), LoginActivity.class));
                        finish();
                    }
                });

    }
}

