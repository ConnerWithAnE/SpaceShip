package com.example.a4basics;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ShipGroup implements Groupable{

    ArrayList<Groupable> groupChildren;

    double translateX, translateY;

    double left = 0, top = 0, right = 0, bottom = 0;


    double[] xs = {0,20,0,-20,0};
    double[] ys = {24,-20,-12,-20,24};
    double shipWidth, shipHeight;
    double[] displayXs, displayYs;
    WritableImage buffer;
    PixelReader reader;
    double clickX, clickY;
    double selX, selY;


    public ShipGroup(ArrayList<Groupable> ships) {
        groupChildren = new ArrayList<>(ships);

        groupChildren.forEach(s -> {
            if (this.left == 0 || s.getLeft() < this.left) this.left = s.getLeft();
            if (this.top == 0 || s.getTop() < this.top) this.top = s.getTop();
            if (this.right == 0 || s.getRight() > this.right) this.right = s.getRight();
            if (this.bottom == 0 || s.getBottom() > this.bottom) this.bottom = s.getBottom();
        });

    }

    @Override
    public boolean hasChildren() {
        return !groupChildren.isEmpty();
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
        AtomicBoolean inside = new AtomicBoolean(false);
        groupChildren.forEach(s -> {
            if (!inside.get() && s.contains(x, y)) {
                inside.set(true);
            }
        });
        System.out.println(inside.get());
        return inside.get();
    }

    @Override
    public void moveShip(double dx, double dy) {
        groupChildren.forEach(c -> c.moveShip(dx, dy));
        AtomicReference<Double> cLeft = new AtomicReference<>((double) 0);
        AtomicReference<Double> cTop = new AtomicReference<>((double) 0);
        AtomicReference<Double> cRight = new AtomicReference<>((double) 0);
        AtomicReference<Double> cBottom = new AtomicReference<>((double) 0);

        groupChildren.forEach(s -> {
            if (cLeft.get() == 0 || s.getLeft() < cLeft.get()) cLeft.set(s.getLeft());
            if (cTop.get() == 0 || s.getTop() < cTop.get()) cTop.set(s.getTop());
            if (cRight.get() == 0 || s.getRight() > cRight.get()) cRight.set(s.getRight());
            if (cBottom.get() == 0 || s.getBottom() > cBottom.get()) cBottom.set(s.getBottom());
        });
        this.left = cLeft.get();
        this.top = cTop.get();
        this.right = cRight.get();
        this.bottom = cBottom.get();
    }

    @Override
    public double getLeft() {
        return this.left;
    }

    @Override
    public double getRight() {
        return this.right;
    }

    @Override
    public double getTop() {
        return this.top;
    }

    @Override
    public double getBottom() {
        return this.bottom;
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
