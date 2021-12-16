package com.example.a4basics;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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

    /*
     * Parameters:
     *    - newModel: An InteractionModel
     * Function: Sets an interaction model
     * Returns: Void
     */
    public void setInteractionModel(InteractionModel newModel) {
        iModel = newModel;
    }

    /*
     * Parameters:
     *    - newModel: A Model
     * Function: Sets a Model
     * Returns: Void
     */
    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    /*
     * Parameters:
     *    - x: The x coordinate of the click
     *    - y: The y coordinate of the click
     *    - event: The mouse event object
     * Function: Handles cases when the mouse is clicked
     * Returns: Void
     */
    public void handlePressed(double x, double y, MouseEvent event) {
        prevX = x;
        prevY = y;
        switch (currentState) {
            case READY -> {
                // context: on a ship?
                Optional<Groupable> hit = model.detectHit(x, y);
                if (hit.isPresent()) {
                    if (iModel.getSelectedShips().contains(hit.get()) && event.isControlDown()) {
                        // If there is a hit and ctrl pressed, deselect
                        iModel.removeSelected(hit.get());
                    } else if (!iModel.getSelectedShips().isEmpty() && iModel.getSelectedShips().contains(hit.get())) {
                        //pass
                        currentState = State.DRAGGING;
                    } else {
                        // Otherwise set the click to selected
                        iModel.setSelected(hit.get(), event.isControlDown(), false);
                    }
                    currentState = State.DRAGGING;
                } else {
                    // on background - is Shift down?
                    if (event.isShiftDown()) {
                        // create ship
                        Ship newShip = model.createShip(x, y);
                        iModel.setSelected(newShip, event.isControlDown(), false);
                        currentState = State.DRAGGING;
                    } else {
                        // Nothing hit, shift not down, clear all the things
                        if (!event.isControlDown()) {
                            iModel.clearSelection();
                        }
                        currentState = State.READY;
                    }
                }
            }
        }
    }

    /*
     * Parameters:
     *    - x: The x coordinate of the click
     *    - y: The y coordinate of the click
     *    - event: The mouse event object
     * Function: Handles cases when the mouse is dragged
     * Returns: Void
     */
    public void handleDragged(double x, double y, MouseEvent event) {
        dX = x - prevX;
        dY = y - prevY;
        prevX = x;
        prevY = y;
        switch (currentState) {
           case DRAGGING -> {
               // Moves the selected ships
               model.moveShip(iModel.getSelectedShips(), dX, dY);
           }
           case READY -> {
               Optional<Groupable> hit = model.detectHit(x, y);
               if (hit.isPresent()) {
                   // If there is a hit, drag
                   currentState = State.DRAGGING;
               } else {
                   // Otherwise create a selection rectangle
                   handleRectSelect(prevX, prevY);
                   model.resizeSelRect(iModel.getRect(), x, y);
                   currentState = State.RECTSELECTING;
                   if (!event.isControlDown()) {
                       // If control is not held clear the current selections
                       iModel.clearSelection();
                   }
               }
           }
            case RECTSELECTING -> {
                // Resize the selection rectangle
                model.resizeSelRect(iModel.getRect(), x, y);
           }
        }
    }

    /*
     * Parameters:
     *    - x: The x coordinate of the click
     *    - y: The y coordinate of the click
     *    - event: The mouse event object
     * Function: Handles cases when the mouse is released
     * Returns: Void
     */
    public void handleReleased(double x, double y, MouseEvent event) {
        switch (currentState) {
            case READY -> {
                // Whole lot of nothin here
            }
            case DRAGGING -> {
                // Set the state back to ready, dragging be done
                currentState = State.READY;
            }
            case RECTSELECTING -> {
                if (iModel.getRect() != null) {
                    // If there is a rectangle, check what was within it
                    ArrayList<Groupable> hit = model.isContained(x, y);
                    if (!hit.isEmpty()) {
                        // If the rectangle contained stuff
                        hit.forEach(s -> {
                            if (iModel.getSelectedShips().contains(s) && event.isControlDown()) {
                                // If control is down and the ship is selected remove it
                                iModel.removeSelected(s);
                            } else if (!iModel.getSelectedShips().contains(s) && event.isControlDown()) {
                                // If control is down and the ship is not selected add it
                                iModel.setSelected(s, event.isControlDown(), true);
                            } else {
                                // Otherwise add all the selected ships
                                iModel.setSelected(s, event.isControlDown(), true);
                            }
                        });
                    }
                    // Remove the rectangle
                    model.clearSelRect();
                }
                currentState = State.READY;
            }
        }
    }

    /*
     * Parameters:
     *    - keyEvent: The KeyEvent event object
     * Function: Handles cases when a key is pressed
     * Returns: Void
     */
    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.V && keyEvent.isControlDown()) {
            // If the clipboard is not empty clear selection and duplicate items from clipboard
            if (!iModel.getFromClipboard().isEmpty()) {
                iModel.clearSelection();
                iModel.getFromClipboard().forEach(g -> {
                    model.addCopied(g);
                    iModel.setSelected(g, true, false);
                });
            }
        } else if (keyEvent.getCode() == KeyCode.C  && keyEvent.isControlDown()) {
            // If the selection is not none, copy to clipboard
            if (iModel.getSelectedShips().size() >= 1) {
                iModel.copyToClipboard();
            }
        } else if (keyEvent.getCode() == KeyCode.X  && keyEvent.isControlDown()) {
            // If the selection is not none, copy to clipboard then remove
            if (iModel.getSelectedShips().size() >= 1) {
                iModel.copyToClipboard();
                model.removePostGrouping(iModel.getSelectedShips());
            }
        } else if (keyEvent.getCode() == KeyCode.G) {
            // If the selection is more than 1, create a new group
            if (iModel.getSelectedShips().size() > 1) {
                ShipGroup newGroup = model.createShipGroup(iModel.getSelectedShips()    );
                model.removePostGrouping(iModel.getSelectedShips());
                iModel.setSelected(newGroup, false, false);
            }
        } else if (keyEvent.getCode() == KeyCode.U) {
            // If only one items selected and it has children (us a group), divide it into the seperate children
            if (iModel.getSelectedShips().size() == 1 && iModel.getSelectedShips().get(0).hasChildren()) {
                ArrayList<Groupable> seperatedGroup = model.divideShipGroup(iModel.getSelectedShips().get(0));
                model.removeGroup(iModel.getSelectedShips().get(0));
                iModel.clearSelection();
                seperatedGroup.forEach(s -> {
                   iModel.setSelected(s, true, false);
                });
            }
        }
    }

    /*
     * Parameters:
     *    - x: rectangle x coordinate
     *    - y: rectangle y coordinate
     * Function: creates the rectangle
     * Returns: Void
     */
    private void handleRectSelect(double x, double y) {
        iModel.setRect(model.createRectSelect(x, y));
    }

    /*
     * Parameters:
     *    - newValue: The slider value
     * Function: Rotates selected Groupable according to newValue
     * Returns: Void
     */
    public void handleSliderMoved(double newValue) {
        if (iModel.getSelectedShips().size() >= 1) {
            iModel.getSelectedShips().forEach(s -> {
                model.rotate(s, newValue);
            });
        }
    }
}
