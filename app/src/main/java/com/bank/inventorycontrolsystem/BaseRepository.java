package com.bank.inventorycontrolsystem;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adedom.library.Dru;
import com.adedom.library.ExecuteQuery;
import com.bank.inventorycontrolsystem.model.Maps;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BaseRepository extends AppCompatActivity {
    private static final String TAG = "BaseRepository";

    public LiveData<List<Maps>> getMapDB(String user_id) {
        final MutableLiveData<List<Maps>> mutableLiveData = new MutableLiveData<>();
        String sql = "SELECT * FROM maps WHERE user_id = '" + user_id + "'";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit(new ExecuteQuery() {
                    @Override
                    public void onComplete(ResultSet resultSet) {
                        try {
                            ArrayList<Maps> list = new ArrayList<>();
                            while (resultSet.next()) {
                                Maps map = new Maps(
                                        resultSet.getString(1),
                                        resultSet.getString(2),
                                        resultSet.getDouble(3),
                                        resultSet.getDouble(4)
                                );
                                list.add(map);
                            }
                            mutableLiveData.setValue(list);

                            for (Maps item : list) {
                                Log.d(TAG, "onComplete: " + item.getLatitude() + "," + item.getLongitude());
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return mutableLiveData;
    }

    public void updateLocationDB(String mapId, double lat, double lng){
        String sql = "UPDATE `maps` SET latitude="+lat+",longitude="+lng+" WHERE map_id = "+mapId+"";
        Dru.connection(ConnectDB.getConnection())
                .execute(sql)
                .commit();
    }
}
