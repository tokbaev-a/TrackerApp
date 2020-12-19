package knigh4ttk.application.trackerapp.Fragments;

import android.content.Context;

import java.util.List;

import knigh4ttk.application.trackerapp.Data.AppDatabase;
import knigh4ttk.application.trackerapp.Item.Item;

public class HistoryPresenter implements HistoryContract.Presenter {

    HistoryContract.View view;
    AppDatabase appDatabase;

    public HistoryPresenter(HistoryContract.View view, Context context) {
        this.view = view;
        appDatabase = AppDatabase.getInstance(context);
    }

    @Override
    public List<Item> getAllItems() {
        return appDatabase.itemDao().getAllItems();
    }

    public void deleteAll() {
        appDatabase.itemDao().deleteAll();
    }
}

