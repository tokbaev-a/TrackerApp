package knigh4ttk.application.trackerapp.Data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import knigh4ttk.application.trackerapp.Item.Item;

@Dao
public interface ItemDao {

    @Insert
    void insert(Item item);

    @Query("SELECT * FROM items_table ")
    List<Item> getAllItems();

    @Query("DELETE FROM items_table")
    void deleteAll();
}

