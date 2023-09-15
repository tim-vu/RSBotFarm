package net.rlbot.api.widgets;

import net.rlbot.api.common.Time;
import net.rlbot.api.input.Keyboard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Furnace {

    private static final WidgetAddress FURNACE = new WidgetAddress(446, 0);

    public static boolean isOpen() {
        return FURNACE.isWidgetVisible();
    }

    private static final int SET_QUANTITY_SCRIPT_ID = 2930;

    private static final int OP_SOUND_SCRIPT_ID = 289;

    private static final int AMOUNT_VAR = 2224;

    private static final WidgetAddress ONE = new WidgetAddress(446, 60);
    private static final WidgetAddress FIVE = new WidgetAddress(446, 61);

    private static final WidgetAddress TEN = new WidgetAddress(446, 62);

    private static final WidgetAddress X = new WidgetAddress(446, 63);

    private static final WidgetAddress ALL = new WidgetAddress(446, 64);

    private static final Map<Amount, WidgetAddress> AMOUNT_TO_WIDGET_ADDRESS = Map.of(
            Amount.ONE, ONE,
            Amount.FIVE, FIVE,
            Amount.TEN, TEN,
            Amount.X, X,
            Amount.ALL, ALL
    );

    public static Amount getSelectedAmount() {

        if (!Furnace.isOpen()) {
            return null;
        }

        for(var amount : Amount.values()) {

            if(!isAmountSelected(amount)) {
                continue;
            }

            return amount;
        }

        return Amount.ALL;
    }

    private static boolean isAmountSelected(Amount amount) {

        var widget = AMOUNT_TO_WIDGET_ADDRESS.get(amount).resolve();

        if(!Widgets.isVisible(widget)) {
            return false;
        }

        var onOpListener = widget.getOnOpListener();
        return onOpListener != null && onOpListener[0].equals(OP_SOUND_SCRIPT_ID);
    }

    public static boolean setSelectedAmount(Amount amount) {
        return switch(amount) {

            case ONE -> setSelectedAmount(1);
            case FIVE -> setSelectedAmount(5);
            case TEN -> setSelectedAmount(10);
            case X, ALL -> setSelectedAmount(Integer.MAX_VALUE);
        };
    }

    public static boolean setSelectedAmount(int amount) {

        var buttonAddress = switch(amount) {
            case 1 -> ONE;
            case 5 -> FIVE;
            case 10 -> TEN;
            case Integer.MAX_VALUE -> ALL;
            default -> X;
        };

        var widget = buttonAddress.resolve();

        if(!Widgets.isVisible(widget)) {
            return false;
        }

        if(!widget.interact("Quantity:")) {
            return false;
        }

        if(buttonAddress == X) {

            if(!Time.sleepUntil(Dialog::isEnterInputOpen, 1800)) {
                return false;
            }

            Time.sleepTick();
            Keyboard.sendText(Integer.toString(amount), true);
            return Time.sleepUntil(() -> !Dialog.isOpen(), 1800);
        }

        return true;
    }

    private static final List<WidgetAddress> CATEGORIES = List.of(
            new WidgetAddress(446, 6),
            new WidgetAddress(446, 18),
            new WidgetAddress(446, 35),
            new WidgetAddress(446, 46)
    );

    public static boolean make(int itemId) {

        for(var category : CATEGORIES) {

            var widget = category.resolve();

            if(!Widgets.isVisible(widget)) {
                continue;
            }

            var children = widget.getStaticChildren();

            var item = Arrays.stream(children).filter(w -> w.getItemId() == itemId).findFirst();

            if(item.isEmpty()) {
                continue;
            }

            return item.get().interact(a -> a.contains("Make"));
        }

        return false;
    }

    public enum Amount {
        ONE,
        FIVE,
        TEN,
        X,
        ALL
    }
}