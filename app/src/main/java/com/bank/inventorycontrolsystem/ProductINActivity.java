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
import com.bank.inventorycontrolsystem.model.ProductIN;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductINActivity extends AppCompatActivity {

    private RecyclerView mPdRecycleView;
    private ArrayList<ProductIN> items;
    private static final String TAG = "ProductINActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_in);

        findViewById(R.id.Pd_In_floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), InsertProductINActivity.class));
            }
        });

        mPdRecycleView = (RecyclerView) findViewById(R.id.Pd_In_RecycleView);
        mPdRecycleView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.Pd_InToolBar);
        toolbar.setTitle("จัดการข้อมูลสินค้ารับเข้า");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_productin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_report_in) {
            startActivity(new Intent(getBaseContext(), ReportProductINActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProduct();
    }

    private void fetchProduct() {
        String sql = "SELECT p.product_id,p.name,p.image,i.ProductInNo,i.DateIn,i.Quantity,i.Price," +
                "t.ProductTypeName FROM product p INNER JOIN productin i ON  p.product_id = i.product_id " +
                "INNER JOIN producttype t ON p.ProductTypeID = t.ProductTypeID ORDER BY i.ProductInNo DESC";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        try {
                            items = new ArrayList<ProductIN>();
                            while (resultSet.next()) {
                                ProductIN product = new ProductIN(
                                        resultSet.getString("name"),
                                        resultSet.getString("image"),
                                        resultSet.getString("ProductTypeName"),
                                        resultSet.getString("ProductInNo"),
                                        resultSet.getString("product_id"),
                                        resultSet.getString("DateIn"),
                                        resultSet.getInt("Quantity"),
                                        resultSet.getDouble("Price")

                                );
                                items.add(product);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_in, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            ProductIN product = items.get(position);
            holder.pdin_ProductIdNo.setText(product.getProductIdNo());
            holder.pdin_ProductName.setText(product.getName());
            holder.pdin_ProductDateIn.setText(product.getDateIn());
            holder.pdin_Price.setText(String.format("%,.2f", product.getPrice()) + "  บาท");
            holder.pdin_Qty.setText(String.format("%,.0f", (double) product.getQuantity()) + "  หน่วย");
            holder.pdin_ProductTypeName.setText(product.getTypename());
            Dru.loadImageCircle(holder.pdin_image, ConnectDB.Base_IMAGE + product.getImage());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder {


        private final ImageView pdin_image;
        private final TextView pdin_ProductIdNo;
        private final TextView pdin_ProductName;
        private final TextView pdin_Price;
        private final TextView pdin_Qty;
        private final TextView pdin_ProductDateIn;
        private final TextView pdin_ProductTypeName;
        private final Button pdin_bt_edit;
        private final Button pdin_bt_delete;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            pdin_image = (ImageView) itemView.findViewById(R.id.pdin_image);
            pdin_ProductIdNo = (TextView) itemView.findViewById(R.id.pdin_ProductIdNo);
            pdin_ProductName = (TextView) itemView.findViewById(R.id.pdin_ProductName);
            pdin_Price = (TextView) itemView.findViewById(R.id.pdin_Price);
            pdin_Qty = (TextView) itemView.findViewById(R.id.pdin_Qty);
            pdin_ProductDateIn = (TextView) itemView.findViewById(R.id.pdin_ProductDateIn);
            pdin_ProductTypeName = (TextView) itemView.findViewById(R.id.pdin_ProductTypeName);
            pdin_bt_edit = (Button) itemView.findViewById(R.id.pdin_bt_edit);
            pdin_bt_delete = (Button) itemView.findViewById(R.id.pdin_bt_delete);

            pdin_bt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductIN product = items.get(getAdapterPosition());
                    Intent intent = new Intent(getBaseContext(), EditProductINActivity.class);
                    intent.putExtra("productin", product);
                    startActivity(intent);
                }
            });

            pdin_bt_delete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ProductIN productIN = items.get(getAdapterPosition());
                    dialogMessagesuct(productIN);
                    return false;
                }
            });
        }

        private void dialogMessagesuct(final ProductIN productIN) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ProductINActivity.this);
            builder.setTitle("ลบข้อมูลสินค้า");
            builder.setMessage("คุณต้องการลบข้อมูลสินค้ารับเข้า หรือไม่ " + productIN.getName() + " ??");
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteProduct(productIN.getProductId(), productIN.getProductIdNo(), String.valueOf(productIN.getQuantity()));
                }
            });
            builder.show();
        }

        private void deleteProduct(final String product_id, String productIdNo, final String quantity) {
            String sql = "DELETE FROM productin WHERE ProductInNo = '" + productIdNo + "'";
            Dru.connection(ConnectDB.getConnection())
                    .execute(sql)
                    .commit(new ExecuteUpdate() {
                        @Override
                        public void onComplete() {
                            updateProduct(product_id, quantity);

                        }
                    });
        }

        private void updateProduct(String product_id, String quantity) {
            Log.d(TAG, "updateProduct: ");
            String sql = "UPDATE product SET qty = qty - " + quantity + " WHERE product_id = '" + product_id + "'";
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
