package net.rlbot.api.widgets;


import net.rlbot.api.game.Vars;
import net.rlbot.api.input.Keyboard;

public class Production {

    private static final WidgetAddress OPTIONS = new WidgetAddress(WidgetID.MULTISKILL_MENU_GROUP_ID, 13);

    public static boolean isOpen()
    {
        return OPTIONS.isWidgetVisible();
    }

    public static boolean chooseOption(String option)
    {
        if (!isOpen())
        {
            return false;
        }

        var optionsWidget = OPTIONS.resolve();
        if (!Widgets.isVisible(optionsWidget))
        {
            return false;
        }

        var options = optionsWidget.getChildren() != null ? optionsWidget.getChildren().length : 1;
        for (int i = 0; i < options; i++)
        {
            var currentOption = Widgets.get(WidgetID.MULTISKILL_MENU_GROUP_ID, 14 + i);
            if (currentOption != null && currentOption.getName().toLowerCase().contains(option.toLowerCase()))
            {
                return chooseOption(i + 1);
            }
        }

        return false;
    }

    public static boolean chooseOption(int index)
    {
        if (!isOpen())
        {
            return false;
        }

        Keyboard.sendText(Integer.toString(index), false);
        return true;
    }

    public static boolean choosePreviousOption()
    {
        if (!isOpen())
        {
            return false;
        }

        Keyboard.sendSpace();
        return true;
    }

    private static final int AMOUNT_VARC = 200;

    public static int getSelectedAmount() {
        return Vars.getVarcInt(AMOUNT_VARC);
    }

    public static void setSelectedAmount(int amount) {
        Vars.setVarcInt(AMOUNT_VARC, amount);
    }
}
