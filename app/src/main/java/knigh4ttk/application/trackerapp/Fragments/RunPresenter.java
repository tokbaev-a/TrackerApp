package knigh4ttk.application.trackerapp.Fragments;

import android.content.Context;

import knigh4ttk.application.trackerapp.Data.AppDatabase;
import knigh4ttk.application.trackerapp.Item.Item;

public class RunPresenter implements RunContract.Presenter {

    RunContract.View view;
    AppDatabase appDatabase;

    public RunPresenter(RunContract.View view, Context context) {
        this.view = view;
        appDatabase = AppDatabase.getInstance(context);
    }

    @Override
    public void insert(Item item) {
        appDatabase.itemDao().insert(item);
    }
}
