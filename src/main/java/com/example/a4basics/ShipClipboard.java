package com.example.a4basics;

import java.util.ArrayList;

public class ShipClipboard {

    ArrayList<Groupable> board;

    public ShipClipboard() {
        board = new ArrayList<>();
    }

    public ArrayList<Groupable> getClipBoard() {
        return board;
    }

    public void addItem(Groupable item) {
        board.add(item);
    }

    public boolean isEmpty() {
        return board.isEmpty();
    }

    public void emptyClip() {
        board.clear();
    }

}
