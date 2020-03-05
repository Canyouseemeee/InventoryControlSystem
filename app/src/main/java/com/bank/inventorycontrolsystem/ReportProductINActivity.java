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
import com.bank.inventorycontrolsystem.model.ProductIN;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportProductINActivity extends AppCompatActivity implements OnAttachSearchListener {

    private RecyclerView mPdRecycleView;
    private ArrayList<ProductIN> items;
    private static final String TAG = "ReportProductINActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_product_in);

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportProductINDialog dialog = new ReportProductINDialog();
                dialog.show(getSupportFragmentManager(), null);
            }
        });

        mPdRecycleView = (RecyclerView) findViewById(R.id.InsertRecycleView);
        mPdRecycleView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.InToolBar);
        toolbar.setTitle("รายงานสินค้ารับเข้า");
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
    protected void onResume() {
        super.onResume();
        fetchProduct("","");
    }

    private void fetchProduct(String productIdBegin, String productIdEnd) {
        String sql = "SELECT p.product_id,p.name,p.image,i.ProductInNo,i.DateIn,i.Quantity,i.Price," +
                "t.ProductTypeName FROM product p INNER JOIN productin i ON  p.product_id = i.product_id " +
                "INNER JOIN producttype t ON p.ProductTypeID = t.ProductTypeID";
        if (!productIdBegin.equals("") && !productIdEnd.equals("")) {
            sql += " WHERE i.ProductInNo BETWEEN '" + productIdBegin + "' AND '" + productIdEnd + "'";
        }
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

    @Override
    public void OnAttach(String productIdBegin, String productIdEnd) {
        fetchProduct(productIdBegin, productIdEnd);
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_in, parent, false);
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

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            pdin_image = (ImageView) itemView.findViewById(R.id.pdin_image);
            pdin_ProductIdNo = (TextView) itemView.findViewById(R.id.pdin_ProductIdNo);
            pdin_ProductName = (TextView) itemView.findViewById(R.id.pdin_ProductName);
            pdin_Price = (TextView) itemView.findViewById(R.id.pdin_Price);
            pdin_Qty = (TextView) itemView.findViewById(R.id.pdin_Qty);
            pdin_ProductDateIn = (TextView) itemView.findViewById(R.id.pdin_ProductDateIn);
            pdin_ProductTypeName = (TextView) itemView.findViewById(R.id.pdin_ProductTypeName);

        }
    }

}
