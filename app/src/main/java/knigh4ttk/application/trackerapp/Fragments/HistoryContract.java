package knigh4ttk.application.trackerapp.Fragments;

import java.util.List;

import knigh4ttk.application.trackerapp.Item.Item;

public interface HistoryContract {

    interface View {

    }

    interface Presenter {
        List<Item> getAllItems();
    }
}
