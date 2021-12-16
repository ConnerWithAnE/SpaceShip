package com.example.a4basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ShipGroup implements Groupable{

    ArrayList<Groupable> groupChildren;


    double left = 0, top = 0, right = 0, bottom = 0;


    public ShipGroup(ArrayList<Groupable> ships) {
        groupChildren = new ArrayList<>(ships);

        groupChildren.forEach(s -> {
            if (this.left == 0 || s.getLeft() < this.left) this.left = s.getLeft();
            if (this.top == 0 || s.getTop() < this.top) this.top = s.getTop();
            if (this.right == 0 || s.getRight() > this.right) this.right = s.getRight();
            if (this.bottom == 0 || s.getBottom() > this.bottom) this.bottom = s.getBottom();
        });

    }

    /*
     * Parameters: None
     * Function: Checks if the group has children ( if it is a group )
     * Returns:
     *    - True or False if groupchildren is empty
     */
    @Override
    public boolean hasChildren() {
        return !groupChildren.isEmpty();
    }

    /*
     * Parameters: None
     * Function: gets the groupChildren ArrayList
     * Returns:
     *    - The ArrayList groupChildren
     */
    @Override
    public ArrayList<Groupable> getChildren() {
        return groupChildren;
    }

    /*
     * Parameters:
     *    - x: The x coordinate to check
     *    - y: The y coordinate to check
     * Function: Checks each child of the groups and returns true if the given x,y click is
     *           contained within the ship
     * Returns:
     *    - True or False if click within
     */
    @Override
    public boolean contains(double x, double y) {
        AtomicBoolean inside = new AtomicBoolean(false);
        groupChildren.forEach(s -> {
            if (!inside.get() && s.contains(x, y)) {
                inside.set(true);
            }
        });
        return inside.get();
    }

    /*
     * Parameters:
     *    - dx: The amount to move on the x plain
     *    - dy: The amount to move on the y plain
     * Function: Alters the x and y position for each groupable within the group, then resets the edges
     * Returns: Void
     */
    @Override
    public void moveShip(double dx, double dy) {
        groupChildren.forEach(c -> c.moveShip(dx, dy));
        setEdges();
    }

    /*
     * Parameters: None
     * Function: Sets the left, top, right and bottom most coordinates of the group
     * Returns: Void
     */
    public void setEdges() {
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


    /*
     * Parameters: None
     * Function: Creates a copy of the current Object
     * Returns:
     *    - New Groupable ShipGroup Object
     */
    @Override
    public Groupable duplicate() {
        ArrayList<Groupable> dupes = new ArrayList<>();
        groupChildren.forEach(e -> dupes.add(e.duplicate()));
        ShipGroup gTwin = new ShipGroup(dupes);
        return gTwin;
    }

    /*
     * Parameters: None
     * Function: Gets the left x coordinate of the group
     * Returns:
     *    - The left x coordinate
     */
    @Override
    public double getLeft() {
        return this.left;
    }

    /*
     * Parameters: None
     * Function: Gets the right x coordinate of the group
     * Returns:
     *    - The right x coordinate
     */
    @Override
    public double getRight() {
        return this.right;
    }

    /*
     * Parameters: None
     * Function: Gets the top y coordinate of the group
     * Returns:
     *    - The top y coordinate
     */
    @Override
    public double getTop() {
        return this.top;
    }

    /*
     * Parameters: None
     * Function: Gets the bottom y coordinate of the group
     * Returns:
     *    - The bottom y coordinate
     */
    @Override
    public double getBottom() {
        return this.bottom;
    }

    /*
     * Parameters: None
     * Function: Unused for groups, added for interface
     * Returns:
     *    - An empty double array
     */
    @Override
    public double[] getDisplayXs() {
        return new double[0];
    }

    /*
     * Parameters: None
     * Function: Unused for groups, added for interface
     * Returns:
     *    - An empty double array
     */
    @Override
    public double[] getDisplayYs() {
        return new double[0];
    }

    /*
     * Parameters:
     *    - a: The amount to rotate by
     *    - x: A vararg ArrayList of type Double for rotating within a group
     * Function: Calls rotate with the amount and center of the group
     * Returns: Void
     */
    public void rotate(double a, ArrayList<Double>... x) {
        Optional<ArrayList<Double>> temp = Arrays.stream(x).findAny();
        if (!temp.isEmpty()) {
            rotate(a, temp.get().get(0), temp.get().get(1));
        } else {
            rotate(a,this.right - ((this.right - this.left)/2),this.bottom - ((this.bottom - this.top)/2));
        }
    }

    /*
     * Parameters:
     *    - a: The amount to rotate by
     *    - cx: The x axis to rotate around
     *    - cy: The y axis to rotate around
     * Function: Creates the ArrayList<Double> to send the groups center, adds the two values
     *           then calls rotate on each child
     * Returns: Void
     */
    public void rotate(double a, double cx, double cy) {
        ArrayList<Double> temp = new ArrayList<>();
        temp.add(cx);
        temp.add(cy);
        groupChildren.forEach((s -> {
            s.rotate(a, temp);
        }));
        //setEdges();
    }

}
