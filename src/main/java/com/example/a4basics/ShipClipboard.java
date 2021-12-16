package com.example.a4basics;

import java.util.ArrayList;

public class ShipClipboard {

    private final ArrayList<Groupable> board;

    public ShipClipboard() {
        board = new ArrayList<>();
    }

    /*
     * Parameters: None
     * Function: Gets the clipboard ArrayList
     * Returns:
     *    - An ArrayList of type Groupable
     */
    public ArrayList<Groupable> getClipBoard() {
        return board;
    }

    /*
     * Parameters:
     *    - item: A Groupable item to add
     * Function: Adds the given item to the clipboard
     * Returns: Void
     */
    public void addItem(Groupable item) {
        board.add(item);
    }

    /*
     * Parameters: None
     * Function: Empties the clipboard
     * Returns: Void
     */
    public void emptyClip() {
        board.clear();
    }

}
