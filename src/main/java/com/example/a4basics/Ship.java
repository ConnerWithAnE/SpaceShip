package com.example.a4basics;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.DoubleStream;

public class Ship implements Groupable {
    double translateX, translateY;
    double[] xs = {0,20,0,-20,0};
    double[] ys = {24,-20,-12,-20,24};
    double shipWidth, shipHeight;
    double[] displayXs, displayYs;
    WritableImage buffer;
    PixelReader reader;
    double clickX, clickY;
    double selX, selY;


    public Ship(double newX, double newY) {
        Canvas shipCanvas;
        GraphicsContext gc;

        translateX = newX;
        translateY = newY;
        double minVal = DoubleStream.of(xs).min().getAsDouble();
        double maxVal = DoubleStream.of(xs).max().getAsDouble();
        shipWidth = maxVal - minVal;
        minVal = DoubleStream.of(ys).min().getAsDouble();
        maxVal = DoubleStream.of(ys).max().getAsDouble();
        shipHeight = maxVal - minVal;
        displayXs = new double[xs.length];
        displayYs = new double[ys.length];
        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] = xs[i] + shipWidth/2;
            displayYs[i] = ys[i] + shipHeight/2;
        }

        shipCanvas = new Canvas(shipWidth,shipHeight);
        gc = shipCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillPolygon(displayXs, displayYs, displayXs.length);
        buffer = shipCanvas.snapshot(null,null);
        reader = buffer.getPixelReader();

        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] = xs[i] + translateX;
            displayYs[i] = ys[i] + translateY;
        }
    }

    /*
     * Parameters: None
     * Function: Checks if there are children (Checks if it is or isnt a group)
     * Returns:
     *    - boolean if it has children
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

    /*
     * Parameters: None
     * Function: Gets the children - not needed here, this here, it is a child
     * Returns:
     *    - null, is an unneeded method
     */
    @Override
    public ArrayList<Groupable> getChildren() {
        return null;
    }

    /*
     * Parameters:
     *    - x: The x coordinate to check
     *    - y: The y coordinate to check
     * Function: Checks if the given coordinates are within the ship
     * Returns:
     *    - True or False if in or not
     */
    @Override
    public boolean contains(double x, double y) {
        clickX = x - translateX + shipWidth/2;
        clickY = y - translateY + shipHeight/2;
        // check bounding box first, then bitmap
        boolean inside = false;
        if (clickX >= 0 && clickX <= shipWidth && clickY >= 0 && clickY <= shipHeight) {
            if (reader.getColor((int) clickX, (int) clickY).equals(Color.BLACK)) inside = true;
        }
        return inside;
    }

    /*
     * Parameters: None
     * Function: Creates a copy of the current Object
     * Returns:
     *    - New Groupable Ship Object
     */
    @Override
    public Groupable duplicate() {
        Ship twin = new Ship(this.translateX, this.translateY);
        return twin;
    }

    /*
     * Parameters: None
     * Function: Gets the left x coordinate of the ship
     * Returns:
     *    - The left x coordinate
     */
    @Override
    public double getLeft() {
        return this.translateX - this.shipWidth/2;
    }

    /*
     * Parameters: None
     * Function: Gets the right x coordinate of the ship
     * Returns:
     *    - The right x coordinate
     */
    @Override
    public double getRight() {
        return this.translateX + this.shipWidth/2;
    }

    /*
     * Parameters: None
     * Function: Gets the top y coordinate of the ship
     * Returns:
     *    - The top y coordinate
     */
    @Override
    public double getTop() {
        return this.translateY - this.shipHeight/2;
    }

    /*
     * Parameters: None
     * Function: Gets the bottom y coordinate of the ship
     * Returns:
     *    - The bottom y coordinate
     */
    @Override
    public double getBottom() {
        return this.translateY + this.shipHeight/2;
    }

    /*
     * Parameters: None
     * Function: Gets the displayXs array
     * Returns:
     *    - An array of doubles
     */
    @Override
    public double[] getDisplayXs() {
        return this.displayXs;
    }

    /*
     * Parameters: None
     * Function: Gets the displayYs array
     * Returns:
     *    - An array of doubles
     */
    @Override
    public double[] getDisplayYs() {
        return this.displayYs;
    }

    /*
     * Parameters:
     *    - dx: The amount to move on the x plain
     *    - dy: The amount to move on the y plain
     * Function: Alters the ships x and y coordinates according to input
     * Returns: Void
     */
    @Override
    public void moveShip(double dx, double dy) {
        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] += dx;
            displayYs[i] += dy;
        }
        translateX += dx;
        translateY += dy;
    }

    /*
     * Parameters:
     *    - a: The amount to rotate by
     *    - x: A vararg ArrayList of type Double for rotating within a group
     * Function: If the optional parameter is empty, calls rotate with the rotation amount
     *           and the ships X and Y. If the parameter is not empty, grabs the given x
     *           and y then calls rotate with those instead
     * Returns: Void
     */
    public void rotate(double a, ArrayList<Double>... x) {
        Optional<ArrayList<Double>> temp = Arrays.stream(x).findAny();
        if (!temp.isEmpty()) {
            rotate(a, temp.get().get(0), temp.get().get(1));
        } else {
            rotate(a, translateX, translateY);
        }
    }

    /*
     * Parameters:
     *    - a: The amount to rotate by
     *    - cx: The x axis to rotate around
     *    - cy: The y axis to rotate around
     * Function: Alters the ships rotation
     * Returns: Void
     */
    public void rotate(double a, double cx, double cy) {
        double x, y;
        double radians = a * Math.PI / 180;
        for (int i = 0; i < displayXs.length; i++) {
            x = displayXs[i] - cx;
            y = displayYs[i] - cy;
            displayXs[i] = rotateX(x, y, radians) + cx;
            displayYs[i] = rotateY(x, y, radians) + cy;
        }
//        translateX = Arrays.stream(displayXs).min().getAsDouble();
//        translateY = Arrays.stream(displayYs).max().getAsDouble();
    }

    /*
     * Parameters:
     *    - x: The x display location
     *    - y: The y display location
     *    - thetaR: The rotation amount
     * Function: Alters the ships x and y coordinates according to input
     * Returns:
     *    - The rotated x position of the given values
     */
    private double rotateX(double x, double y, double thetaR) {
        return Math.cos(thetaR) * x - Math.sin(thetaR) * y;
    }

    /*
     * Parameters:
     *    - x: The x display location
     *    - y: The y display location
     *    - thetaR: The rotation amount
     * Function: Alters the ships x and y coordinates according to input
     * Returns:
     *    - The rotated y position of the given values
     */
    private double rotateY(double x, double y, double thetaR) {
        return Math.sin(thetaR) * x + Math.cos(thetaR) * y;
    }
}
