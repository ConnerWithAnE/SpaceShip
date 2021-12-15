package com.example.a4basics;

public class selRect {
    double left, top, right, bottom;
    double startX, startY;

    public selRect(double x, double y) {
        startX = x;
        startY = y;
        left = x;
        top = y;
        right = x;
        bottom = y;
    }

    public void resize(double x, double y) {
        left = Math.min(x, startX);
        right = Math.max(x, startX);
        top = Math.min(y, startY);
        bottom = Math.max(y, startY);
    }

    public boolean contains(double x, double y, double width, double height) {
        return x >= left && width <= right && y >= top && height <= bottom;
    }

}
