package net.rlbot.script.api.quests.tutorialisland;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.ActionResult;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.game.HintArrow;
import net.rlbot.api.game.HintArrowType;
import net.rlbot.script.api.quest.nodes.UnaryNode;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CharacterDesignNode extends UnaryNode {

    private static final WidgetAddress CHARACTER_CONFIRM_BUTTON = new WidgetAddress(679, 68, 0);

    private static final int COLOUR_OPTIONS = 30;

    public CharacterDesignNode() {
        super("Setting up the character design");
    }

    private Map<DesignOption, Integer> chosenDesign = null;
    private Map<ColourOption, Integer> chosenColours = null;
    private Map<DesignOption, Integer> currentDesign = null;
    private Map<ColourOption, Integer> currentColours = null;

    @Override
    protected ActionResult doExecute() {

        if(chosenDesign == null) {
            chosenDesign = new HashMap<>();
            chosenColours = new HashMap<>();
            currentDesign = new HashMap<>();
            currentColours = new HashMap<>();

            for(var option : DesignOption.values()) {
                chosenDesign.put(option, Random.between(0, option.getCount()));
                currentDesign.put(option, 0);
            }

            for(var option : ColourOption.values()) {
                chosenColours.put(option, Random.between(0, 5));
                currentColours.put(option, 0);
            }

        }

        for(var option : DesignOption.values()) {

            var currentDesignIndex = currentDesign.get(option);
            if(currentDesignIndex >= chosenDesign.get(option)) {
                continue;
            }

            log.info("Selecting next design for " + option.name());
            if(!nextDesign(option.getWidgetAddress())) {
                return ActionResult.FAILURE;
            }

            currentDesign.put(option, (currentDesignIndex + 1) % option.getCount());
            Time.sleep(400, 1000);
            return ActionResult.IN_PROGRESS;
        }

        for(var option : ColourOption.values()) {

            var currentDesignIndex = currentColours.get(option);
            if(currentDesignIndex >= chosenColours.get(option)) {
                continue;
            }

            log.info("Selecting next design for " + option.name());
            if(!nextDesign(option.getWidgetAddress())) {
                return ActionResult.FAILURE;
            }

            currentColours.put(option, (currentDesignIndex + 1) % COLOUR_OPTIONS);
            Time.sleep(400, 1000);
            return ActionResult.IN_PROGRESS;
        }

        log.info("Confirming character design");
        var confirmButton = CHARACTER_CONFIRM_BUTTON.resolve();

        if(confirmButton == null || !confirmButton.interact("Confirm")) {
            return ActionResult.FAILURE;
        }

        return Time.sleepUntil(() -> HintArrow.getType() == HintArrowType.NPC, 5000) ? ActionResult.SUCCESS : ActionResult.FAILURE;
    }

    private static boolean nextDesign(WidgetAddress widgetAddress) {

        var widget = widgetAddress.resolve();

        if(widget == null) {
            log.info("Failed to find widget");
            return false;
        }

        return widget.interact(Predicates.always());
    }

    public enum DesignOption {
        HEAD(22, new WidgetAddress(679, 13)),
        JAW(13, new WidgetAddress(679, 17)),
        TORSO(14, new WidgetAddress(679, 21)),
        ARMS(12, new WidgetAddress(679, 25)),
        HANDS(2, new WidgetAddress(679, 29)),
        LEGS(11, new WidgetAddress(679, 33)),
        FEET(2, new WidgetAddress(679, 37, 0));


        public int getCount() {
            return count;
        }

        private final int count;

        public WidgetAddress getWidgetAddress() {
            return widgetAddress;
        }

        private final WidgetAddress widgetAddress;

        DesignOption(int count, WidgetAddress widgetAddress) {
            this.count = count;
            this.widgetAddress = widgetAddress;
        }
    }

    public enum ColourOption {

        HAIR(new WidgetAddress(679, 44)),
        TORSO(new WidgetAddress(679, 48)),
        LEGS(new WidgetAddress(679, 52)),
        FEET(new WidgetAddress(679, 56)),
        SKIN(new WidgetAddress(679, 60));

        public WidgetAddress getWidgetAddress() {
            return widgetAddress;
        }

        private final WidgetAddress widgetAddress;

        ColourOption(WidgetAddress widgetAddress) {
            this.widgetAddress = widgetAddress;
        }

    }

}
