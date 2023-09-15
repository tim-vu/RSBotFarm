package net.rlbot.api.items;

public class GrandExchangeOffer {

    private final net.runelite.api.GrandExchangeOffer offer;

    public GrandExchangeOffer(net.runelite.api.GrandExchangeOffer offer) {
        this.offer = offer;
    }

    public int getItemId() {
        return offer.getItemId();
    }

    public State getState() {
        return switch(offer.getState()) {
            case EMPTY -> State.EMPTY;
            case CANCELLED_BUY -> State.CANCELLED_BUY;
            case CANCELLED_SELL -> State.CANCELLED_SELL;
            case BUYING -> State.BUYING;
            case BOUGHT -> State.BOUGHT;
            case SELLING -> State.SELLING;
            case SOLD -> State.SOLD;
        };
    }

    public enum Type {
        EMPTY,
        BUYING,
        SELLING
    }

    public enum Progress {
        NOT_STARTED,
        ACTIVE,
        COMPLETED,
        CANCELLED
    }

    public enum State {
        EMPTY,
        CANCELLED_BUY,
        CANCELLED_SELL,
        BUYING,
        BOUGHT,
        SELLING,
        SOLD;

        public boolean isCancelled() {
            return this == CANCELLED_BUY || this == CANCELLED_SELL;
        }

        public boolean isCompleted() {
            return this == BOUGHT || this == SOLD;
        }
    }
}
