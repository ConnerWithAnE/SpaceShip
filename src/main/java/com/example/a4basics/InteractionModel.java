package com.example.a4basics;

import java.util.ArrayList;

public class InteractionModel {
    ArrayList<ShipModelSubscriber> subscribers;
    ArrayList<Groupable> selectedShip;
    selRect rect;
    ShipClipboard clipboard;

    public InteractionModel() {
        subscribers = new ArrayList<>();
        selectedShip = new ArrayList<>();
        clipboard = new ShipClipboard();
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

    public void copyToClipboard() {
        clipboard.emptyClip();
        selectedShip.forEach(e -> {
            clipboard.addItem(e.duplicate());
        });
    }

    public ArrayList<Groupable> getFromClipboard() {
        ArrayList<Groupable> newItems = new ArrayList<>();
        clipboard.getClipBoard().forEach(e -> {
            newItems.add(e.duplicate());
        });
        return newItems;
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
