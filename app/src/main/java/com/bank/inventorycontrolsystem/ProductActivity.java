package com.bank.inventorycontrolsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.bank.inventorycontrolsystem.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    private ArrayList<Product> items;
    private RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), InsertProductActivity.class));
            }
        });

        mRecycleView = (RecyclerView) findViewById(R.id.InsertRecycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.InToolBar);
        toolbar.setTitle("จัดการข้อมูลสินค้า");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }else if (item.getItemId() == R.id.menu_report_by_id) {
            startActivity(new Intent(getBaseContext(), ReportProductActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProduct();
    }

    private void fetchProduct() {
        String sql = "SELECT p.*,t.ProductTypeName,u.Unit_name FROM product p " +
                "INNER JOIN producttype t ON  p.ProductTypeID = t.ProductTypeID " +
                "INNER JOIN unit u ON  p.Unit_id = u.Unit_id";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        try {
                            items = new ArrayList<Product>();
                            while (resultSet.next()) {
                                Product product = new Product(
                                        resultSet.getString(1),
                                        resultSet.getString(2),
                                        resultSet.getDouble(3),
                                        resultSet.getInt(4),
                                        resultSet.getString(5),
                                        resultSet.getString(6),
                                        resultSet.getString(7),
                                        resultSet.getString(8),
                                        resultSet.getString(9)


                                );
                                items.add(product);
                            }
                            mRecycleView.setAdapter(new ProductAdapter());

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_management_product, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            Product product = items.get(position);
            holder.mg_ProductId.setText(product.getProductId());
            holder.mg_ProductName.setText(product.getProductName());
            holder.mg_Price.setText(String.format("%,.2f", product.getPrice()) + "  บาท");
            holder.mg_Qty.setText(String.format("%,.0f", (double) product.getQty()) + " "+ product.getUnit_name());
            holder.mg_ProductTypeId.setText(product.getTypeId());
            holder.mg_ProductTypeName.setText(product.getTypeName());
            Dru.loadImageCircle(holder.mg_image, ConnectDB.Base_IMAGE + product.getImage());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class ProductHolder extends RecyclerView.ViewHolder {


        private final ImageView mg_image;
        private final TextView mg_ProductId;
        private final TextView mg_ProductName;
        private final TextView mg_Price;
        private final TextView mg_Qty;
        private final TextView mg_ProductTypeId;
        private final TextView mg_ProductTypeName;
        private final Button btEdit;
        private final Button btDelete;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            mg_image = (ImageView) itemView.findViewById(R.id.mg_image);
            mg_ProductId = (TextView) itemView.findViewById(R.id.mg_ProductId);
            mg_ProductName = (TextView) itemView.findViewById(R.id.mg_ProductName);
            mg_Price = (TextView) itemView.findViewById(R.id.mg_Price);
            mg_Qty = (TextView) itemView.findViewById(R.id.mg_Qty);
            mg_ProductTypeId = (TextView) itemView.findViewById(R.id.mg_ProductTypeId);
            mg_ProductTypeName = (TextView) itemView.findViewById(R.id.mg_ProductTypeName);
            btEdit = (Button) itemView.findViewById(R.id.bt_edit);
            btDelete = (Button) itemView.findViewById(R.id.bt_delete);

            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = items.get(getAdapterPosition());
                    Intent intent = new Intent(getBaseContext(), EditProductActivity.class);
//                    intent.putExtra("product_id",product.getProductId());
//                    intent.putExtra("product_name",product.getProductName());
//                    intent.putExtra("price",product.getPrice());
//                    intent.putExtra("qty",product.getQty());
//                    intent.putExtra("image",product.getImage());
//                    intent.putExtra("type_id",product.getTypeId());
//                    intent.putExtra("type_name",product.getTypeName());
                    intent.putExtra("product", product);
                    startActivity(intent);
                }
            });

            btDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Product product = items.get(getAdapterPosition());
                    dialogMessagesuct(product);
                    return false;
                }
            });
        }

        private void dialogMessagesuct(final Product product) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setTitle("ลบข้อมูลสินค้า");
            builder.setMessage("คุณต้องการลบข้อมูลสินค้า หรือไม่ " + product.getProductName() + " ??");
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteProduct(product.getProductId());
                }
            });
            builder.show();
        }

        private void deleteProduct(String productId) {
            String sql = "DELETE FROM product WHERE product_id = '" + productId + "'";
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
