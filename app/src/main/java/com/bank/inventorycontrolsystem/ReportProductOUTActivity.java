package com.bank.inventorycontrolsystem;

import android.content.Context;
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
import com.bank.inventorycontrolsystem.model.ProductOUT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReportProductOUTActivity extends AppCompatActivity implements OnAttachSearchListener {

    private RecyclerView mPdRecycleView;
    private ArrayList<ProductOUT> items;
    public static Context sActivity;
    private static final String TAG = "ReportProductOUTActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_product_out);

        sActivity = this;

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportProductOUTDialog dialog = new ReportProductOUTDialog();
                dialog.show(getSupportFragmentManager(), null);
            }
        });

        mPdRecycleView = (RecyclerView) findViewById(R.id.InsertRecycleView);
        mPdRecycleView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        Toolbar toolbar = (Toolbar) findViewById(R.id.InToolBar);
        toolbar.setTitle("รายงานข้อมูลสินค้าออก");
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
        fetchProduct("", "");
    }

    private void fetchProduct(String productIdBegin, String productIdEnd) {
        String sql = "SELECT p.product_id,p.name,p.image,o.ProductOutNo,o.DateOut,o.Quantity,o.Price," +
                "t.ProductTypeName FROM product p INNER JOIN productout o ON  p.product_id = o.product_id " +
                "INNER JOIN producttype t ON p.ProductTypeID = t.ProductTypeID";
        if (!productIdBegin.equals("") && !productIdEnd.equals("")) {
            sql += " WHERE o.DateOut BETWEEN '" + productIdBegin + "' AND '" + productIdEnd + "'";
        }
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

    @Override
    public void OnAttach(String productIdBegin, String productIdEnd) {
        fetchProduct(productIdBegin, productIdEnd);
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_out, parent, false);
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

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            pdout_image = (ImageView) itemView.findViewById(R.id.pdout_image);
            pdout_ProductOutNo = (TextView) itemView.findViewById(R.id.pdout_ProductOutNo);
            pdout_ProductName = (TextView) itemView.findViewById(R.id.pdout_ProductName);
            pdout_Price = (TextView) itemView.findViewById(R.id.pdout_Price);
            pdout_Qty = (TextView) itemView.findViewById(R.id.pdout_Qty);
            pdout_ProductDateOut = (TextView) itemView.findViewById(R.id.pdout_ProductDateOut);
            pdout_ProductTypeName = (TextView) itemView.findViewById(R.id.pdout_ProductTypeName);

        }
    }

}
