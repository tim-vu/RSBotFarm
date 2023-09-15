package net.rlbot.script.api.quests.tutorialisland;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Time;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.api.game.Vars;
import net.rlbot.api.input.Keyboard;
import net.rlbot.script.api.quest.nodes.UnaryNode;

@Slf4j
public class ChooseDisplayNameNode extends UnaryNode {

    private static final WidgetAddress SET_DISPLAY_NAME_FIELD = new WidgetAddress(558, 12);
    public static final int SET_DISPLAY_NAME_INPUT_TEXT = 436;

    private static final WidgetAddress LOOKUP_NAME_BUTTON = new WidgetAddress(558, 18);

    private static final WidgetAddress SET_NAME_BUTTON = new WidgetAddress(558, 19);

    private static final WidgetAddress NAME_RESPONSE_TEXT = new WidgetAddress(558, 13);

    private static final WidgetAddress CHARACTER_CREATOR = new WidgetAddress(679, 2);

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final int MAX_DISPLAY_NAME_LENGTH = 12;

    private String getRandomUsername(){
        StringBuilder sb = new StringBuilder(12);

        for(int i = 0; i < 12; i++)
        {
            sb.append(ALPHABET.charAt(Random.between(0, ALPHABET.length())));
        }

        return sb.toString();
    }

    public ChooseDisplayNameNode() {
        super("Choosing a display name");
    }

    private String selectedName;

    @Override
    protected ActionResult doExecute() {

        if(CHARACTER_CREATOR.isWidgetVisible()) {
            return ActionResult.SUCCESS;
        }

        if(selectedName == null) {
            selectedName = getRandomUsername();
        }

        var field = SET_DISPLAY_NAME_FIELD.resolve();

        if(field == null) {
            log.info("Failed to find field");
            return ActionResult.FAILURE;
        }

        var enteredName = getEnteredName(field);
        if(!selectedName.equals(enteredName)) {
            Time.sleep(400, 1200);
            Vars.setVarcStr(SET_DISPLAY_NAME_INPUT_TEXT, selectedName);
            Time.sleep(400, 900);

            return ActionResult.FAILURE;
        }

        var response = NAME_RESPONSE_TEXT.resolve();

        if(response == null) {
            log.info("Failed to find response");
            return ActionResult.FAILURE;
        }

        if(!response.getText().contains(selectedName)) {

            var lookupNameButton = LOOKUP_NAME_BUTTON.resolve();

            if(lookupNameButton == null) {
                log.info("Failed to find lookup name button");
                return ActionResult.SUCCESS;
            }

            log.info("Looking up name");
            if(!lookupNameButton.interact("Look up name")) {
                return ActionResult.FAILURE;
            }

            if(!Time.sleepUntil(this::containsResponse, 6000)) {
                log.info("No response received");
                return ActionResult.FAILURE;
            }

            return ActionResult.SUCCESS;
        }

        if(response.getText().contains("You may set this name now")) {
            var setNameButton = SET_NAME_BUTTON.resolve();

            if(setNameButton == null) {
                log.info("Failed to find setNameButton");
                return ActionResult.FAILURE;
            }

            log.info("Choosing name");
            if(!setNameButton.interact("Set name")) {
                log.info("  Failed to interact with setNameButton");
                return ActionResult.FAILURE;
            }

            return Time.sleepUntil(CHARACTER_CREATOR::isWidgetVisible, 10000) ? ActionResult.IN_PROGRESS : ActionResult.FAILURE;
        }

        log.info("Unknown case");
        return ActionResult.FAILURE;
    }

    private static String getEnteredName(Widget displayNameField) {
        var text = displayNameField.getText();

        if(text == null) {
            return "";
        }

        if(text.endsWith("*"))
        {
            return text.substring(0, text.length() - 1);
        }

        return text;
    }

    private boolean containsResponse() {

        var response = NAME_RESPONSE_TEXT.resolve();

        if(response == null) {
            return false;
        }

        return response.getText().contains(selectedName);
    }

}
