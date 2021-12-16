package com.example.a4basics;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Optional;

public class ShipView extends StackPane implements ShipModelSubscriber {
    Canvas myCanvas;
    GraphicsContext gc;
    ShipModel model;
    InteractionModel iModel;
    Slider rotationSlider;

    public ShipView() {
        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();
        this.setStyle("-fx-background-color: black");
        rotationSlider = new Slider(-180, 180, 0);
        VBox b = new VBox(rotationSlider, myCanvas);
        this.getChildren().addAll(b);
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
     *    - newModel: An InteractionModel
     * Function: Sets an interaction model
     * Returns: Void
     */
    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    /*
     * Parameters:
     *    - controller: A ShipController
     * Function: Handles event listeners for Mouse (Press, Drag, Release) and the Slider
     * Returns: Void
     */
    public void setController(ShipController controller) {
        myCanvas.setOnMousePressed(e -> controller.handlePressed(e.getX(),e.getY(), e));
        myCanvas.setOnMouseDragged(e -> controller.handleDragged(e.getX(),e.getY(), e));
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e.getX(),e.getY(), e));
        rotationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            controller.handleSliderMoved(rotationSlider.getValue());
        });
    }

    /*
     * Parameters: None
     * Function: Handles the calls to draw
     * Returns: Void
     */
    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        // If there is no ship selected, center the slider
        if (iModel.getSelectedShips().size() == 0) {
            rotationSlider.adjustValue(0);
        }
        // If there is a rectangle, draw it
        if (model.getRect() != null) {
            gc.setFill(Color.rgb(184, 134, 11, 1));
            gc.setStroke(Color.rgb(203, 175, 98, 1));
            gc.fillRect(model.getRect().getLeft(), model.getRect().getTop(),
                    model.getRect().getRight() - model.getRect().getLeft(),
                    model.getRect().getBottom() - model.getRect().getTop());
        }
        // For each groupable, call checkShip and drawOutline if it is a group
        model.getShips().forEach(ship -> {
            checkShip(ship, false);
            if (ship.hasChildren()) {
                drawOutline(ship);
            }
        });
    }

    /*
     * Parameters:
     *    - group: A groupable object
     * Function: If group is selected draw an outline
     * Returns: Void
     */
    public void drawOutline(Groupable group) {
        if (iModel.getSelectedShips() != null && iModel.getSelectedShips().contains(group)) {
            gc.setStroke(Color.WHITE);
            gc.strokeRect(group.getLeft(), group.getTop(), group.getRight() - group.getLeft(), group.getBottom() - group.getTop() + 2);
        }
    }

    /*
     * Parameters:
     *    - group: A groupable object
     *    - isSelected: A Boolean if the item is selected
     * Function: Sets the value isSelect, checks if it is in selected (this is only useful on the initial call)
     *           If the groupable has children (it is a group) then recursively call checkShip on each one,
     *           making sure to pass through whether it is selected or not. If no children call drawShip with
     *           groupable and selection status
     * Returns: Void
     */
    public void checkShip(Groupable group, Boolean isSelected) {
        boolean isSelect = isSelected;
        if (iModel.getSelectedShips() != null && iModel.getSelectedShips().contains(group)) isSelect = true;
        if (group.hasChildren()) {
            boolean finalIsSelect = isSelect;
            group.getChildren().forEach(c -> {
                checkShip(c, finalIsSelect);
            });
        } else {
            drawShip(group, isSelect);
        }
    }

    /*
     * Parameters:
     *    - group: A groupable object (but for sure is a ship)
     *    - selected: A Boolean if the ship is selected
     * Function: If selected is true, set to the selected colours, otherwise unselected then draw the ship
     * Returns: Void
     */
    public void drawShip(Groupable group, Boolean selected) {
        if (selected) {
            gc.setFill(Color.YELLOW);
            gc.setStroke(Color.CORAL);
        } else {
            gc.setStroke(Color.YELLOW);
            gc.setFill(Color.CORAL);
        }
        gc.fillPolygon(group.getDisplayXs(), group.getDisplayYs(), group.getDisplayXs().length);
        gc.strokePolygon(group.getDisplayXs(), group.getDisplayYs(), group.getDisplayXs().length);
    }

    /*
     * Parameters: None
     * Function: Calls draw when modelChanged() occurs
     * Returns: Void
     */
    @Override
    public void modelChanged() {
        draw();
    }
}
