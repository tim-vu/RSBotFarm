package net.rlbot.api;

import lombok.Getter;

@Getter
public enum GameState {
    /**
     * Unknown game state.
     */
    UNKNOWN(-1),
    /**
     * The client is starting.
     */
    STARTING(0),
    /**
     * The client is at the login screen.
     */
    LOGIN_SCREEN(10),
    /**
     * The client is at the login screen entering authenticator code.
     */
    LOGIN_SCREEN_AUTHENTICATOR(11),
    /**
     * There is a player logging in.
     */
    LOGGING_IN(20),
    /**
     * The game is being loaded.
     */
    LOADING(25),
    /**
     * The user has successfully logged in.
     */
    LOGGED_IN(30),
    /**
     * Connection to the server was lost.
     */
    CONNECTION_LOST(40),
    /**
     * A world hop is taking place.
     */
    HOPPING(45);

    /**
     * The raw state value.
     */
    private final int state;

    GameState(int state) {

        this.state = state;
    }

    /**
     * Utility method that maps the rank value to its respective
     * {@link net.runelite.api.GameState} value.
     *
     * @param state the raw state value
     * @return the gamestate
     */
    public static GameState of(int state) {

        for (GameState gs : GameState.values()) {
            if (gs.state == state) {
                return gs;
            }
        }
        return UNKNOWN;
    }
}
