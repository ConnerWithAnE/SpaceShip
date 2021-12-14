package com.example.a4basics;

import java.util.ArrayList;

public class ShipGroup implements Groupable{

    ArrayList<Groupable> groupChildren;


    public ShipGroup(ArrayList<Groupable> ships) {
        groupChildren = new ArrayList<>(ships);
    }

    @Override
    public boolean hasChildren() {
        return groupChildren.isEmpty();
    }

    public Groupable splitGroup() {
        groupChildren.forEach(e -> {
            System.out.println(e);
        });
        return null;
    }

    @Override
    public ArrayList<Groupable> getChildren() {
        return groupChildren;
    }

    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    @Override
    public void moveShip(double dx, double dy) {
        groupChildren.forEach(c -> c.moveShip(dx, dy));
    }

    @Override
    public double getLeft() {
        return 0;
    }

    @Override
    public double getRight() {
        return 0;
    }

    @Override
    public double getTop() {
        return 0;
    }

    @Override
    public double getBottom() {
        return 0;
    }

    @Override
    public double[] getDisplayXs() {
        return new double[0];
    }

    @Override
    public double[] getDisplayYs() {
        return new double[0];
    }
}
