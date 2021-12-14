package com.example.a4basics;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.security.Key;
import java.util.ArrayList;
import java.util.Optional;

public class ShipController {
    InteractionModel iModel;
    ShipModel model;
    double prevX, prevY;
    double dX, dY;

    protected enum State {
        READY, RECTSELECTING, DRAGGING
    }

    protected State currentState;

    public ShipController() {
        currentState = State.READY;
    }

    public void setInteractionModel(InteractionModel newModel) {
        iModel = newModel;
    }

    public void setModel(ShipModel newModel) {
        model = newModel;
    }


    public void handlePressed(double x, double y, MouseEvent event) {
        prevX = x;
        prevY = y;
        switch (currentState) {
            case READY -> {
                // context: on a ship?
                Optional<Groupable> hit = model.detectHit(x, y);
                if (!hit.isPresent()) {
                    // on background - is Shift down?
                    if (event.isShiftDown()) {
                        // create ship
                        Ship newShip = model.createShip(x, y);
                        iModel.setSelected(newShip, event.isControlDown(), false);
                        currentState = State.DRAGGING;
                    } else {
                        currentState = State.READY;
                    }
                }
            }
        }
    }

    public void handleDragged(double x, double y, MouseEvent event) {
        dX = x - prevX;
        dY = y - prevY;
        prevX = x;
        prevY = y;
        switch (currentState) {
           case DRAGGING -> model.moveShip(iModel.selectedShip, dX, dY);
           case READY -> {
               Optional<Groupable> hit = model.detectHit(x, y);
               if (hit.isPresent()) {
                   currentState = State.DRAGGING;
               } else {
                   handleRectSelect(prevX, prevY);
                   model.resizeSelRect(iModel.rect, x, y);
                   currentState = State.RECTSELECTING;
                   if (!event.isControlDown()) {
                       iModel.clearSelection();
                   }
               }
           }
            case RECTSELECTING -> {
                model.resizeSelRect(iModel.rect, x, y);
           }
        }
    }

    public void handleReleased(double x, double y, MouseEvent event) {
        switch (currentState) {
            case READY -> {
                Optional<Groupable> hit = model.detectHit(x, y);
                if (hit.isPresent()) {
                    // on ship, so select
                    if (iModel.selectedShip.contains(hit.get()) && event.isControlDown()) {
                        iModel.removeSelected(hit.get());
                    } else {
                        iModel.setSelected(hit.get(), event.isControlDown(), false);
                    }
                } else {
                    iModel.clearSelection();
                }
            }
            case DRAGGING -> {
                currentState = State.READY;
            }
            case RECTSELECTING -> {
                if (iModel.rect != null) {
                    ArrayList<Groupable> hit = model.detectSelRectHit(x, y);
                    System.out.println(!hit.isEmpty());
                    System.out.println(hit);
                    if (!hit.isEmpty()) {
                        // on ship, so select
                        hit.forEach(s -> {
                            if (iModel.selectedShip.contains(s) && event.isControlDown()) {
                                iModel.removeSelected(s);
                            } else if (!iModel.selectedShip.contains(s) && event.isControlDown()) {
                                iModel.setSelected(s, event.isControlDown(), true);
                            } else {
                                iModel.setSelected(s, event.isControlDown(), true);
                            }
                        });
                    }
                    model.clearSelRect();
                }
                currentState = State.READY;
            }
        }
    }

    private void handleRectSelect(double x, double y) {
        iModel.setRect(model.createRectSelect(x, y));
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.V) {
            //TODO
        } else if (keyEvent.getCode() == KeyCode.C) {
            //TODO
        } else if (keyEvent.getCode() == KeyCode.G) {
            ShipGroup newGroup = model.createShipGroup(iModel.selectedShip);
            iModel.removeAllSelected();
            iModel.setSelected(newGroup, false, false);
            //TODO
        } else if (keyEvent.getCode() == KeyCode.U) {
            /*
            if (iModel.selectedShip.size() == 1) {
                iModel.selectedShip.forEach();
            }
            */
            //TODO
        }
        System.out.println(keyEvent.getCode());
    }
}
