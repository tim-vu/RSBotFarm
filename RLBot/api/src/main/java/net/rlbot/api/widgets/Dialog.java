package net.rlbot.api.widgets;

import net.rlbot.api.Game;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Vars;
import net.rlbot.api.input.Keyboard;
import net.rlbot.api.packet.DialogPackets;
import net.rlbot.internal.Interaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static net.logstash.logback.argument.StructuredArguments.kv;

public class Dialog
{

    private static final WidgetAddress SPRITE_CONT = new WidgetAddress(193, 0);
    private static final WidgetAddress SCROLL_BAR = new WidgetAddress(162, 557);
    private static final WidgetAddress WEIRD_CONT = new WidgetAddress(193, 3);
    private static final WidgetAddress WEIRD_CONT_2 = new WidgetAddress(633, 0);
    private static final WidgetAddress NPC_CONT = new WidgetAddress(WidgetID.DIALOG_NPC_GROUP_ID, 4);
    private static final WidgetAddress NPC_TEXT = new WidgetAddress(WidgetID.DIALOG_NPC_GROUP_ID, 6);
    private static final WidgetAddress PLAYER_CONT = new WidgetAddress(WidgetID.DIALOG_PLAYER_GROUP_ID, 3);
    private static final WidgetAddress PLAYER_NAME = new WidgetAddress(WidgetID.DIALOG_PLAYER_GROUP_ID, 4);
    private static final WidgetAddress PLAYER_TEXT = new WidgetAddress(WidgetID.DIALOG_PLAYER_GROUP_ID, 6);
    private static final WidgetAddress DEATH_CONT = new WidgetAddress(663, 0, 2);
    private static final WidgetAddress TUT_CONT = new WidgetAddress(229, 2);
    private static final WidgetAddress OPTIONS = new WidgetAddress(WidgetID.DIALOG_OPTION_GROUP_ID, 1);

    private static final WidgetAddress OPTIONS_TITLE = new WidgetAddress(WidgetID.DIALOG_OPTION_GROUP_ID, 1, 0);

    public static boolean isOpen()
    {
        return !SCROLL_BAR.isWidgetVisible();
    }

    public static boolean isNpcOpen() {
        return Widgets.isVisible(WidgetInfo.DIALOG_NPC_HEAD_MODEL) || Widgets.isVisible(WidgetInfo.DIALOG_PLAYER);
    }

    public static boolean isEnterInputOpen() {
        return Widgets.isVisible(WidgetInfo.CHATBOX_FULL_INPUT);
    }

    public static boolean enterName(String name) {
        Interaction.log("ENTER_NAME", kv("name", name));
        DialogPackets.sendNameInput(name);
        return false;
    }

    public static boolean enterAmount(int amount) {
        Interaction.log("ENTER_AMOUNT", kv("amount", amount));
        DialogPackets.sendNumberedInput(amount);
        return true;
    }

    public static boolean canContinue()
    {
        return canContinueNPC() || canContinuePlayer() || canContinueDeath()
                || canSpriteContinue() || canSprite2Continue()
                || canContinue1() || canContinue2()
                || canContinueTutIsland() || canContinueTutIsland2()
                || canContinueTutIsland3() || canLevelUpContinue();
    }

    public static boolean canLevelUpContinue()
    {
        return Widgets.isVisible(WidgetInfo.LEVEL_UP_LEVEL);
    }

    public static boolean canSpriteContinue()
    {
        return SPRITE_CONT.isWidgetVisible();
    }

    public static boolean canSprite2Continue()
    {
        return Widgets.isVisible(WidgetInfo.DIALOG2_SPRITE_CONTINUE);
    }

    public static boolean canContinue1()
    {
        return WEIRD_CONT.isWidgetVisible();
    }

    public static boolean canContinue2()
    {
        return WEIRD_CONT_2.isWidgetVisible();
    }

    public static boolean canContinueNPC()
    {
        return NPC_CONT.isWidgetVisible();
    }

    public static boolean canContinuePlayer()
    {
        return PLAYER_CONT.isWidgetVisible();
    }

    public static boolean canContinueDeath()
    {
        return DEATH_CONT.isWidgetVisible();
    }

    public static boolean canContinueTutIsland()
    {
        return TUT_CONT.isWidgetVisible();
    }

    public static boolean canContinueTutIsland2()
    {
        return Widgets.isVisible(WidgetID.DIALOG_SPRITE_GROUP_ID, 0, 2);
    }

    public static boolean canContinueTutIsland3()
    {
        var widget = Widgets.get(WidgetInfo.CHATBOX_FULL_INPUT);
        return Widgets.isVisible(widget) && widget.getText().toLowerCase().contains("continue");
    }

    public static boolean isViewingOptions()
    {
        return !getOptions().isEmpty();
    }

    private static final int DIALOG_ID_VARC = 1112;

    public static boolean continueSpace()
    {
        if (!Dialog.isOpen()) {
            return false;
        }

        var value = Vars.getVarcInt(DIALOG_ID_VARC);
        Keyboard.sendSpace();
        return Time.sleepUntil(() -> Vars.getVarcInt(DIALOG_ID_VARC) != value, 2400);
    }

    public static boolean chooseOption(int index)
    {
        if (!isViewingOptions()) {
            return false;
        }

        var value = Vars.getVarcInt(DIALOG_ID_VARC);
        Keyboard.sendText(Integer.toString(index), false);
        return Time.sleepUntil(() -> Vars.getVarcInt(DIALOG_ID_VARC) != value, 2400);
    }

    public static boolean chooseOption(String... options)
    {
        if (!isViewingOptions()) {
            return false;
        }

        for (int i = 0; i < getOptions().size(); i++)
        {
            var text = getOptions().get(i);
            for (String option : options)
            {
                if (!text.contains(option)) {
                    continue;
                }

                return chooseOption(i + 1);
            }
        }

        return false;
    }

    public static String getOptionsTitle() {
        var widget = OPTIONS_TITLE.resolve();

        if(!Widgets.isVisible(widget)) {
            return null;
        }

        return widget.getText();
    }

    public static List<String> getOptions()
    {
        var widget = OPTIONS.resolve();

        if (!Widgets.isVisible(widget))
        {
            return Collections.emptyList();
        }

        var out = new ArrayList<String>();
        var children = widget.getDynamicChildren();

        if (children == null)
        {
            return out;
        }

        // Skip first child, it's not a dialog option
        for (int i = 1; i < children.length; i++)
        {
            if (children[i].getText().isBlank() || children[i].getText() == null)
            {
                continue;
            }

            out.add(children[i].getText());
        }

        return out;
    }

    public static boolean hasOption(String option)
    {
        return hasOption(Predicates.texts(option));
    }

    public static boolean hasOption(Predicate<String> option)
    {
        return getOptions().stream()
                .anyMatch(option);
    }

    public static void close()
    {
        Game.getClient().runScript(138);
    }

    public static String getText()
    {
        Widget widget = null;

        if (canContinueNPC())
        {
            widget = NPC_TEXT.resolve();
        }
        else if (canContinuePlayer())
        {
            widget = PLAYER_TEXT.resolve();
        }

        return widget == null ? "" : widget.getText();
    }

    public static String getName()
    {
        Widget widget = null;

        if (canContinueNPC())
        {
            widget = NPC_CONT.resolve();
        }
        else if (canContinuePlayer())
        {
            widget = PLAYER_NAME.resolve();
        }

        return widget == null ? "" : widget.getText();
    }
}
