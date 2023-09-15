package net.rlbot.api.movement;

public enum Direction
{
    /**
     * Angles ranging from 768 - 1279.
     */
    NORTH,

    /**
     * Angles ranging from 1792 - 2047 and 0 - 255.
     */
    SOUTH,

    /**
     * Angles ranging from 1280 - 1791.
     */
    EAST,

    /**
     * Angles ranging from 256 - 767.
     */
    WEST;

    public static Direction fromOrientation(int orientation) {

        if(orientation >= 768 && orientation <= 1279) {
            return NORTH;
        }

        if(orientation >= 1280 && orientation <= 1791) {
            return EAST;
        }

        if(orientation >= 256 && orientation <= 767) {
            return WEST;
        }

        return SOUTH;
    }
}
