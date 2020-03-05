package com.bank.inventorycontrolsystem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bank.inventorycontrolsystem.model.Maps;

import java.util.List;

public class BaseViewModel extends ViewModel {

    private BaseRepository repository = new BaseRepository();

    public LiveData<List<Maps>> getMapDB(String user_id){
        return repository.getMapDB(user_id);
    }

    public void updateLocation(String mapId,double lat,double lng){
        repository.updateLocationDB(mapId,lat,lng);
    }
}
