package com.example.a4basics;

import java.util.ArrayList;

public class InteractionModel {
    private ArrayList<ShipModelSubscriber> subscribers;
    private ArrayList<Groupable> selectedShips;
    private selRect rect;
    private ShipClipboard clipboard;

    public InteractionModel() {
        subscribers = new ArrayList<>();
        selectedShips = new ArrayList<>();
        clipboard = new ShipClipboard();
    }

    /*
     * Parameters: None
     * Function: Empties the selectedShips ArrayList then notifies subscribers
     * Returns: Void
     */
    public void clearSelection() {
        selectedShips.clear();
        notifySubscribers();
    }

    /*
     * Parameters:
     *    - newSelection: The new Groupable object to add to selectedShips ArrayList
     *    - notClear: A boolean to check if selectedShips should be emptied before adding
     *    - isRectSelect: A boolean to check if a rectangle is being created, and whether ships should clear
     * Function: Adds the given Group or Ship to the ArrayList of selectedShips. Checks if ctrl is held
     *           or if the add is part of a rectangle selection while adding. Then notifies subscribers
     * Returns: Void
     */
    public void setSelected(Groupable newSelection, Boolean notClear, Boolean isRectSelect) {
        if (!notClear && !isRectSelect) {
            selectedShips.clear();
            selectedShips.add(newSelection);
        } else {
            if (!selectedShips.contains(newSelection)) {
                selectedShips.add(newSelection);
            }
        }
        notifySubscribers();
    }

    /*
     * Parameters:
     *    - newSelection: The Groupable object to be removed selectedShips ArrayList
     * Function: Removes the given Groupable object from selectedShips ArrayList then notifies subscribers
     * Returns: Void
     */
    public void removeSelected(Groupable newSelection) {
        selectedShips.remove(newSelection);
        notifySubscribers();
    }

    /*
     * Parameters: None
     * Function: Gets the selectedShips ArrayList
     * Returns:
     *    - selectedShips ArrayList
     */
    public ArrayList<Groupable> getSelectedShips() {
        return selectedShips;
    }

    /*
     * Parameters: None
     * Function: Empties the current clipboard then adds each object from selected ships to the clipboard
     *           as a deep copy
     * Returns: Void
     */
    public void copyToClipboard() {
        clipboard.emptyClip();
        selectedShips.forEach(e -> {
            clipboard.addItem(e.duplicate());
        });
    }

    /*
     * Parameters: None
     * Function: Creates a new ArrayList then adds a deep copy of every item in the clipboard
     * Returns:
     *    - newItems: An ArrayList of type Groupable which contains a deep copy of everything on the clipboard
     */
    public ArrayList<Groupable> getFromClipboard() {
        ArrayList<Groupable> newItems = new ArrayList<>();
        clipboard.getClipBoard().forEach(e -> {
            newItems.add(e.duplicate());
        });
        return newItems;
    }

    /*
     * Parameters:
     *    - newRect: A new selection rectangle object
     * Function: Sets the rect object to the new rectangle then notifies subscribers
     * Returns: Void
     */
    public void setRect(selRect newRect) {
        rect = newRect;
        notifySubscribers();
    }

    /*
     * Parameters: None
     * Function: Gets the select rectangle
     * Returns:
     *    - rect select Rectangle
     */
    public selRect getRect() {
        return rect;
    }

    /*
     * Parameters:
     *    - aSub: A new ShipModelSubscriber object
     * Function: Adds a new subscriber (surprise surprise)
     * Returns: Void
     */
    public void addSubscriber(ShipModelSubscriber aSub) {
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
