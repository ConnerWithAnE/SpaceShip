package com.example.a4basics;

import java.util.ArrayList;

public class InteractionModel {
    ArrayList<ShipModelSubscriber> subscribers;
    ArrayList<Groupable> selectedShip;
    selRect rect;

    public InteractionModel() {
        subscribers = new ArrayList<>();
        selectedShip = new ArrayList<>();
    }

    public void clearSelection() {
        selectedShip.clear();
        notifySubscribers();
    }

    public void setSelected(Groupable newSelection, Boolean ctrl, Boolean isRectSelect) {
        if (!ctrl && !isRectSelect) {
            selectedShip.clear();
            selectedShip.add(newSelection);
        } else {
            if (!selectedShip.contains(newSelection)) {
                selectedShip.add(newSelection);
            }
        }
        notifySubscribers();
    }

    public void removeSelected(Groupable newSelection) {
        selectedShip.remove(newSelection);
        notifySubscribers();
    }

    public void removeAllSelected() {
        selectedShip.clear();
        notifySubscribers();
    }

    public void setRect(selRect newRect) {
        rect = newRect;
        notifySubscribers();
    }

    public void addSubscriber(ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }
}
