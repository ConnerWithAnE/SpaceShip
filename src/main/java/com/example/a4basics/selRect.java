package com.example.a4basics;

public class selRect {
    private double left, top, right, bottom;
    private double startX, startY;

    public selRect(double x, double y) {
        startX = x;
        startY = y;
        left = x;
        top = y;
        right = x;
        bottom = y;
    }

    /*
     * Parameters:
     *    - x: The new x coordinate
     *    - y: The new y coordinate
     * Function: Resizes the selction rectangle
     * Returns: Void
     */
    public void resize(double x, double y) {
        left = Math.min(x, startX);
        right = Math.max(x, startX);
        top = Math.min(y, startY);
        bottom = Math.max(y, startY);
    }

    /*
     * Parameters: None
     * Function: Gets the left coordinate
     * Returns:
     *    - The left x coordinate
     */
    public double getLeft() {
        return this.left;
    }

    /*
     * Parameters: None
     * Function: Gets the top coordinate
     * Returns:
     *    - The left y coordinate
     */
    public double getTop() {
        return this.top;
    }

    /*
     * Parameters: None
     * Function: Gets the right coordinate
     * Returns:
     *    - The right x coordinate
     */
    public double getRight() {
        return this.right;
    }

    /*
     * Parameters: None
     * Function: Gets the bottom coordinate
     * Returns:
     *    - The right y coordinate
     */
    public double getBottom() {
        return this.bottom;
    }

    /*
     * Parameters:
     *    - x: The left x coordinate to check
     *    - y: The top y coordinate to check
     *    - width: The right x coordinate to check
     *    - height: The bottom x coordinate to check
     * Function: Checks if the given coordinates are within the rectangle
     * Returns:
     *    - A true or false if is in the rectangle
     */
    public boolean contains(double x, double y, double width, double height) {
        return x >= left && width <= right && y >= top && height <= bottom;
    }

}
