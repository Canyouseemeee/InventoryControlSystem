package com.bank.inventorycontrolsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.adedom.library.ExecuteUpdate;
import com.bank.inventorycontrolsystem.model.ProductOUT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductOUTActivity extends AppCompatActivity {

    private RecyclerView mPdRecycleView;
    private ArrayList<ProductOUT> items;
    private static final String TAG = "ProductOUTActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_out);

        findViewById(R.id.Pd_Out_floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), InsertProductOUTActivity.class));
            }
        });

        mPdRecycleView = (RecyclerView) findViewById(R.id.Pd_Out_RecycleView);
        mPdRecycleView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.Pd_OutToolBar);
        toolbar.setTitle("จัดการข้อมูลสินค้าออก");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_productout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_report_out) {
            startActivity(new Intent(getBaseContext(), ReportProductOUTActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProduct();
    }

    private void fetchProduct() {
        String sql = "SELECT p.product_id,p.name,p.image,o.ProductOutNo,o.DateOut,o.Quantity,o.Price," +
                "t.ProductTypeName FROM product p INNER JOIN productout o ON  p.product_id = o.product_id " +
                "INNER JOIN producttype t ON p.ProductTypeID = t.ProductTypeID ORDER BY o.ProductOutNo DESC";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        try {
                            items = new ArrayList<ProductOUT>();
                            while (resultSet.next()) {
                                ProductOUT productOUT = new ProductOUT(
                                        resultSet.getString("name"),
                                        resultSet.getString("image"),
                                        resultSet.getString("ProductTypeName"),
                                        resultSet.getString("ProductOutNo"),
                                        resultSet.getString("product_id"),
                                        resultSet.getString("DateOut"),
                                        resultSet.getInt("Quantity"),
                                        resultSet.getDouble("Price")

                                );
                                items.add(productOUT);
                            }

                            mPdRecycleView.setAdapter(new ProductAdapter());

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_out, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            ProductOUT productOUT = items.get(position);
            holder.pdout_ProductOutNo.setText(productOUT.getProductOutNo());
            holder.pdout_ProductName.setText(productOUT.getName());
            holder.pdout_ProductDateOut.setText(productOUT.getDateIn());
            holder.pdout_Price.setText(String.format("%,.2f", productOUT.getPrice()) + "  บาท");
            holder.pdout_Qty.setText(String.format("%,.0f", (double) productOUT.getQuantity()) + "  หน่วย");
            holder.pdout_ProductTypeName.setText(productOUT.getTypename());
            Dru.loadImageCircle(holder.pdout_image, ConnectDB.Base_IMAGE + productOUT.getImage());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder {

        private final ImageView pdout_image;
        private final TextView pdout_ProductOutNo;
        private final TextView pdout_ProductName;
        private final TextView pdout_Price;
        private final TextView pdout_Qty;
        private final TextView pdout_ProductDateOut;
        private final TextView pdout_ProductTypeName;
        private final Button pdout_bt_edit;
        private final Button pdout_bt_delete;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            pdout_image = (ImageView) itemView.findViewById(R.id.pdout_image);
            pdout_ProductOutNo = (TextView) itemView.findViewById(R.id.pdout_ProductOutNo);
            pdout_ProductName = (TextView) itemView.findViewById(R.id.pdout_ProductName);
            pdout_Price = (TextView) itemView.findViewById(R.id.pdout_Price);
            pdout_Qty = (TextView) itemView.findViewById(R.id.pdout_Qty);
            pdout_ProductDateOut = (TextView) itemView.findViewById(R.id.pdout_ProductDateOut);
            pdout_ProductTypeName = (TextView) itemView.findViewById(R.id.pdout_ProductTypeName);
            pdout_bt_edit = (Button) itemView.findViewById(R.id.pdout_bt_edit);
            pdout_bt_delete = (Button) itemView.findViewById(R.id.pdout_bt_delete);

            pdout_bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductOUT productOUT = items.get(getAdapterPosition());
                    Intent intent = new Intent(getBaseContext(), EditProductOUTActivity.class);
                    intent.putExtra("productOUT", productOUT);
                    startActivity(intent);
                }
            });

            pdout_bt_delete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ProductOUT productOUT = items.get(getAdapterPosition());
                    dialogMessagesuct(productOUT);
                    return false;
                }
            });
        }

        private void dialogMessagesuct(final ProductOUT productOUT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductOUTActivity.this);
            builder.setTitle("ลบข้อมูลสินค้า");
            builder.setMessage("คุณต้องการลบข้อมูลสินค้ารับเข้า หรือไม่ " + productOUT.getName() + " ??");
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteProduct(productOUT.getProductOutNo(),productOUT.getProductId(), String.valueOf(productOUT.getQuantity()));
                }
            });
            builder.show();
        }

        private void deleteProduct(String productOutNo, final String productid, final String quantity) {
            String sql = "DELETE FROM productout WHERE ProductOutNo = '" + productOutNo + "'";
            Dru.connection(ConnectDB.getConnection())
                    .execute(sql)
                    .commit(new ExecuteUpdate() {
                        @Override
                        public void onComplete() {
                            updateProduct(productid, quantity);
                        }
                    });
        }

        private void updateProduct(String productid, String quantity) {
            Log.d(TAG, "updateProduct: ");
            String sql = "UPDATE product SET qty = qty + " + quantity + " WHERE product_id = '" + productid + "'";
            Dru.connection(ConnectDB.getConnection())
                    .execute(sql)
                    .commit(new ExecuteUpdate() {
                        @Override
                        public void onComplete() {
                            Toast.makeText(getBaseContext(), "ลบข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
                            onResume();
                        }
                    });
        }
    }
}
