package com.example.a4basics;

import java.util.ArrayList;

public interface Groupable {

    boolean hasChildren();

    ArrayList<Groupable> getChildren();

    boolean contains(double x, double y);

    void moveShip(double dx, double dy);

    void rotate(double amount, ArrayList<Double>... x);

    Groupable duplicate();

    double getLeft();

    double getRight();

    double getTop();

    double getBottom();

    double[] getDisplayXs();

    double[] getDisplayYs();

}
