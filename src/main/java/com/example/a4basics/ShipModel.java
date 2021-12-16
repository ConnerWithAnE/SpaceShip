package com.example.a4basics;

import java.util.ArrayList;
import java.util.Optional;

public class ShipModel {
    private ArrayList<Groupable> ships;
    ArrayList<ShipModelSubscriber> subscribers;
    private selRect rect;

    public ShipModel() {
        subscribers = new ArrayList<>();
        ships = new ArrayList<>();
    }

    /*
     * Parameters:
     *    - x: The x position to create a new ship
     *    - y: The y position to create a new ship
     * Function: Creates a new ship then adds it to the list ships
     * Returns:
     *    - s: A ship Object
     */
    public Ship createShip(double x, double y) {
        Ship s = new Ship(x,y);
        ships.add(s);
        notifySubscribers();
        return s;
    }

    /*
     * Parameters:
     *    - b: The ArrayList of ships to move
     *    - dX: The amount to move on the x plain
     *    - dY: The amount to move on the y plain
     * Function: moves each ship then notifies subscribers
     * Returns: Void
     */
    public void moveShip(ArrayList<Groupable> b, double dX, double dY) {
        b.forEach(e -> e.moveShip(dX, dY));
        notifySubscribers();
    }

    /*
     * Parameters: None
     * Function: Gets the ArrayList ships
     * Returns:
     *    - ArrayList ships
     */
    public ArrayList<Groupable> getShips() {
        return ships;
    }

    /*
     * Parameters:
     *    - g: The Groupable object to rotate
     *    - amount: the amount to rotate by
     * Function: calls rotate on the group and by a certain amount
     * Returns: Void
     */
    public void rotate(Groupable g, double amount) {
        g.rotate(amount);
        notifySubscribers();
    }

    /*
     * Parameters:
     *    - x: The x coordinate of the hit
     *    - y: The y coordinate of the hit
     * Function: Calls contains on each Groupable in ships
     * Returns:
     *    - An Optional<Groupable> of the ships hit
     */
    public Optional<Groupable> detectHit(double x, double y) {
        return ships.stream().filter(s -> s.contains(x, y)).reduce((first, second) -> second);
    }

    /*
     * Parameters:
     *    - x: The x coordinate of the rectangle hit
     *    - y: The y coordinate of the rectangle hit
     * Function: Checks each ship and whether it is contained within the rectangle, if so it is
     *           added to the ArrayList sel
     * Returns:
     *    - sel: An ArrayList<Groupable> of the groubables
     */
    public ArrayList<Groupable> isContained(double x, double y) {
        ArrayList<Groupable> sel = new ArrayList<>();
        ships.forEach(e -> {
            if (rect.contains(e.getLeft(), e.getTop(), e.getRight(), e.getBottom())) {
                sel.add(e);
            }
        });
        return sel;
    }

    /*
     * Parameters: None
     * Function: Adds the groupable to the ships ArrayList
     *           (Used when pasting)
     * Returns: Void
     */
    public void addCopied(Groupable g) {
        ships.add(g);
    }

    /*
     * Parameters:
     *    - x: The x coordinate of the selection rectangle
     *    - y: The y coordinate of the selection rectangle
     * Function: Creates a new selection rectangle and notifies subscribers
     * Returns:
     *    - rect: A new selRect
     */
    public selRect createRectSelect(double x, double y) {
        rect = new selRect(x, y);
        notifySubscribers();
        return rect;
    }

    /*
     * Parameters: None
     * Function: Gets the selection rectangle
     * Returns:
     *    - rect: A selRect
     */
    public selRect getRect() {
        return rect;
    }

    /*
     * Parameters:
     *    - rect: The selection rectangle to resize
     *    - x: The new x coordinate
     *    - y: The new y coordinate
     * Function: Resizes the selection rectangle then notifies subscribers
     * Returns: Void
     */
    public void resizeSelRect(selRect rect, double x, double y) {
        rect.resize(x, y);
        notifySubscribers();
    }

    /*
     * Parameters: None
     * Function: Removes the selection rectangle then notifies subscribers
     * Returns: Void
     */
    public void clearSelRect() {
        rect = null;
        notifySubscribers();
    }

    /*
     * Parameters:
     *    - newGroup: An ArrayList of groupable objects
     * Function: Removes the objects within the ArrayList then notifies subscribers
     *           (Used after a group is made)
     * Returns: Void
     */
    public void removePostGrouping(ArrayList<Groupable> newGroup) {
        newGroup.forEach(e -> ships.remove(e));
        notifySubscribers();
    }

    /*
     * Parameters:
     *    - group: A groupable object
     * Function: Removes the object then notifies subscribers
     * Returns: Void
     */
    public void removeGroup(Groupable group) {
        ships.remove(group);
        notifySubscribers();
    }

    /*
     * Parameters:
     *    - selShips: An ArrayList of groupable objects
     * Function: Creates and adds a new group with the contents of selShips, the notifies subscribers
     * Returns:
     *    - g: A new ShipGroup object
     */
    public ShipGroup createShipGroup(ArrayList<Groupable> selShips) {
        ShipGroup g = new ShipGroup(selShips);
        ships.add(g);
        notifySubscribers();
        System.out.println(g.getChildren());
        return g;
    }

    /*
     * Parameters:
     *    - selGroup: A ShipGroup to be divided
     * Function: Creates and ArrayList and for each child of the group adds them to that list
     *           and the main ships then notifies subscribers
     * Returns:
     *    - dividedItems: An ArrayList<Groupable> of seperated groupables
     */
    public ArrayList<Groupable> divideShipGroup(Groupable selGroup) {
        ArrayList<Groupable> dividedItems = new ArrayList<>();
        selGroup.getChildren().forEach(g -> {
            ships.add(g);
            dividedItems.add(g);
        });
        notifySubscribers();
        return dividedItems;
    }

    /*
     * Parameters:
     *    - aSub: A new ShipModelSubscriber object
     * Function: Adds a new subscriber (surprise surprise)
     * Returns: Void
     */
    public void addSubscriber (ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    /*
     * Parameters: None
     * Function: Notifies each subscriber of a model change
     * Returns: Void
     */
    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }
}
