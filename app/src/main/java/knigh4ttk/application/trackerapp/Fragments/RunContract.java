package knigh4ttk.application.trackerapp.Fragments;

import knigh4ttk.application.trackerapp.Item.Item;

public interface RunContract {

    interface View {
    }

    interface Presenter {
        void insert(Item item);
    }
}
