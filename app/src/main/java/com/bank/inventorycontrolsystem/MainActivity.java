package com.bank.inventorycontrolsystem;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.bank.inventorycontrolsystem.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button mBtnin;
    private ArrayList<Product> item;
    private RecyclerView mRecycleView;
    private SwipeRefreshLayout mswipeRefreshLayout;
    private Button mBtnout;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnin = (Button) findViewById(R.id.btn_in);
        mBtnout = (Button) findViewById(R.id.btn_out);
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);
        mswipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mBtnin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ProductINActivity.class));
            }
        });

        mBtnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ProductOUTActivity.class));
            }
        });

        mswipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_light
        );

        mswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchProduct();
            }
        });

        // TODO: 05/02/2563 5 test connect db
        if (ConnectDB.getConnection() == null) {
            Dru.failed(getBaseContext());
        } else {
            Dru.completed(getBaseContext());
        }

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("รายการสินค้า");
        setSupportActionBar(toolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_main){
            startActivity(new Intent(getBaseContext(),ProductActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        fetchProduct();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void fetchProduct() {
        mswipeRefreshLayout.setRefreshing(true);
        String sql = "SELECT p.*,t.ProductTypeName,u.Unit_name FROM product p " +
                "INNER JOIN producttype t ON  p.ProductTypeID = t.ProductTypeID " +
                "INNER JOIN unit u ON  p.Unit_id = u.Unit_id";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        mswipeRefreshLayout.setRefreshing(false);
                        try {
                            item = new ArrayList<Product>();
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
                                item.add(product);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            Product product = item.get(position);
            holder.it_name.setText(product.getProductName());
            holder.it_price.setText(String.format("%,.2f",product.getPrice()) +"  บาท");
            holder.it_qty.setText(String.format("%,.0f",(double)product.getQty()) + "  "+ product.getUnit_name());
            Dru.loadImageCircle(holder.it_image, ConnectDB.Base_IMAGE + product.getImage());
        }

        @Override
        public int getItemCount() {
            return item.size();
        }
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        private final ImageView it_image;
        private final TextView it_name;
        private final TextView it_price;
        private final TextView it_qty;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            it_image = (ImageView) itemView.findViewById(R.id.it_image);
            it_name = (TextView) itemView.findViewById(R.id.it_name);
            it_price = (TextView) itemView.findViewById(R.id.it_price);
            it_qty = (TextView) itemView.findViewById(R.id.it_qty);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = item.get(getAdapterPosition());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("product",product);

                    ProductDialog dialog = new ProductDialog();
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(),null);
                }
            });
        }
    }
}
