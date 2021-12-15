package com.example.a4basics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

public class ShipModel {
    public ArrayList<Groupable> ships;
    ArrayList<ShipModelSubscriber> subscribers;
    public selRect rect;

    public ShipModel() {
        subscribers = new ArrayList<>();
        ships = new ArrayList<>();
    }

    public Ship createShip(double x, double y) {
        Ship s = new Ship(x,y);
        ships.add(s);
        notifySubscribers();
        return s;
    }

    public Optional<Groupable> detectHit(double x, double y) {
        return ships.stream().filter(s -> s.contains(x, y)).reduce((first, second) -> second);
    }

    public ArrayList<Groupable> detectSelRectHit(double x, double y) {
        ArrayList<Groupable> sel = new ArrayList<>();
        ships.forEach(e -> {
            if (rect.contains(e.getLeft(), e.getTop(), e.getRight(), e.getBottom())) {
                sel.add(e);
            }
        });
        return sel;
    }

    public void moveShip(ArrayList<Groupable> b, double dX, double dY) {
        b.forEach(e -> e.moveShip(dX, dY));
        notifySubscribers();
    }

    public void resizeSelRect(selRect rect, double x, double y) {
        rect.resize(x, y);
        notifySubscribers();
    }

    public void addSubscriber (ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }

    public selRect createRectSelect(double x, double y) {
        rect = new selRect(x, y);
        notifySubscribers();
        return rect;
    }

    public void removePostGrouping(ArrayList<Groupable> newGroup) {
        newGroup.forEach(e -> ships.remove(e));
        notifySubscribers();
    }

    public void removeGroup(Groupable group) {
        ships.remove(group);
        notifySubscribers();
    }

    public void clearSelRect() {
        rect = null;
        notifySubscribers();
    }

    public ShipGroup createShipGroup(ArrayList<Groupable> selShips) {
        ShipGroup g = new ShipGroup(selShips);
        ships.add(g);
        notifySubscribers();
        System.out.println(g.getChildren());
        return g;
    }

    public ArrayList<Groupable> divideShipGroup(Groupable selGroup) {
        ArrayList<Groupable> dividedItems = new ArrayList<>();
        selGroup.getChildren().forEach(g -> {
            ships.add(g);
            dividedItems.add(g);
        });
        notifySubscribers();
        return dividedItems;
    }
}
