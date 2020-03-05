package com.bank.inventorycontrolsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    private EditText mEdtusername;
    private EditText mEdtpassword;
    private Button mBtnLogin;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEdtusername = (EditText) findViewById(R.id.mEdtusername);
        mEdtpassword = (EditText) findViewById(R.id.mEdtpassword);
        mBtnLogin = (Button) findViewById(R.id.mBtnLogin);
        mBtnRegister = (Button) findViewById(R.id.mBtnRegister);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "SELECT * FROM user WHERE username='"+mEdtusername.getText().toString().trim()+"'AND password='"+mEdtpassword.getText().toString().trim()+"'";
                Dru.connection(ConnectDB.getConnection())
                        .execute(sql)
                        .commit(new ExecuteQuery() {
                            @Override
                            public void onComplete(ResultSet resultSet) {
                                try {
                                    if (resultSet.next()) {
                                        // TODO: 11/6/2019
                                        String id = resultSet.getString(1);
                                        Toast.makeText(getBaseContext(), "รหัสถูกต้อง", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplication(), MainActivity.class)
                                                .putExtra("id", id)
                                        );
                                    } else {
                                        Toast.makeText(getBaseContext(), "รหัสไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();

                                }
                            }
                        });
            }
        });
    }
}
