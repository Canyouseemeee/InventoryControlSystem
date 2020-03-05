package com.bank.inventorycontrolsystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.bank.inventorycontrolsystem.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportProductActivity extends AppCompatActivity implements OnAttachSearchListener {

    private ArrayList<Product> items;
    private RecyclerView mRecycleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_product);

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO search
                ReportProductDialog dialog = new ReportProductDialog();
                dialog.show(getSupportFragmentManager(), null);

//                new ReportProductDialog().show(getSupportFragmentManager(),null);

            }
        });

        mRecycleView = (RecyclerView) findViewById(R.id.InsertRecycleView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.InToolBar);
        toolbar.setTitle("รายงานตามรหัสสินค้า");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnAttach(String productIdBegin, String productIdEnd) {
        fetchProduct(productIdBegin, productIdEnd);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProduct("", "");
    }

    private void fetchProduct(String productIdBegin, String productIdEnd) {
        String sql = "SELECT p.*,t.ProductTypeName,u.Unit_name FROM product p " +
                "INNER JOIN producttype t ON  p.ProductTypeID = t.ProductTypeID " +
                "INNER JOIN unit u ON  p.Unit_id = u.Unit_id";
        if (!productIdBegin.equals("") && !productIdEnd.equals("")) {
            sql += " WHERE product_id BETWEEN '" + productIdBegin + "' AND '" + productIdEnd + "'";
        }

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_by_id, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            Product product = items.get(position);
            holder.mg_ProductId.setText(product.getProductId());
            holder.mg_ProductName.setText(product.getProductName());
            holder.mg_Price.setText(String.format("%,.2f", product.getPrice()) + "  บาท");
            holder.mg_Qty.setText(String.format("%,.0f", (double) product.getQty()) + "  " + product.getUnit_name());
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

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            mg_image = (ImageView) itemView.findViewById(R.id.mg_image);
            mg_ProductId = (TextView) itemView.findViewById(R.id.mg_ProductId);
            mg_ProductName = (TextView) itemView.findViewById(R.id.mg_ProductName);
            mg_Price = (TextView) itemView.findViewById(R.id.mg_Price);
            mg_Qty = (TextView) itemView.findViewById(R.id.mg_Qty);
            mg_ProductTypeId = (TextView) itemView.findViewById(R.id.mg_ProductTypeId);
            mg_ProductTypeName = (TextView) itemView.findViewById(R.id.mg_ProductTypeName);
        }
    }
}
