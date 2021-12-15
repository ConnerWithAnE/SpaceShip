package com.example.a4basics;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;
import java.util.Optional;

public class ShipView extends StackPane implements ShipModelSubscriber {
    Canvas myCanvas;
    GraphicsContext gc;
    ShipModel model;
    InteractionModel iModel;

    public ShipView() {
        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().add(myCanvas);
        this.setStyle("-fx-background-color: black");
    }

    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    public void setController(ShipController controller) {
        myCanvas.setOnMousePressed(e -> controller.handlePressed(e.getX(),e.getY(), e));
        myCanvas.setOnMouseDragged(e -> controller.handleDragged(e.getX(),e.getY(), e));
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e.getX(),e.getY(), e));
    }

    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        if (model.rect != null) {
            gc.setFill(Color.rgb(184, 134, 11, 1));
            gc.setStroke(Color.rgb(203, 175, 98, 1));
            gc.fillRect(model.rect.left, model.rect.top,
                    model.rect.right - model.rect.left,
                    model.rect.bottom - model.rect.top);
        }
        model.ships.forEach(ship -> {
            checkShip(ship, false);
        });
        model.ships.forEach(g -> {
            if (g.hasChildren()) {
                drawOutline(g);
            }
        });
    }

    public void drawOutline(Groupable group) {
        if (iModel.selectedShip != null && iModel.selectedShip.contains(group)) {
            gc.setStroke(Color.WHITE);
            gc.strokeRect(group.getLeft(), group.getTop(), group.getRight() - group.getLeft(), group.getBottom() - group.getTop() + 2);
        }
    }


    public void checkShip(Groupable group, Boolean isSelected) {
        boolean isSelect = isSelected;
        if (iModel.selectedShip != null && iModel.selectedShip.contains(group)) isSelect = true;
        if (group.hasChildren()) {
            boolean finalIsSelect = isSelect;
            group.getChildren().forEach(c -> {
                checkShip(c, finalIsSelect);
            });
        } else {
            drawShip(group, isSelect);
        }
    }

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

    @Override
    public void modelChanged() {
        draw();
    }
}
