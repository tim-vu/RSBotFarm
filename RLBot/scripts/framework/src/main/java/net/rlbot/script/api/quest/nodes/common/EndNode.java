package net.rlbot.script.api.quest.nodes.common;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.script.api.quest.nodes.QuestNode;
import net.rlbot.script.api.reaction.Reaction;

@Slf4j
public class EndNode implements QuestNode {

    private static final WidgetAddress CLOSE_BUTTON_ADDRESS = new WidgetAddress(153, 16);

    @Override
    public QuestNode execute() {

        var open = Time.sleepUntil(CLOSE_BUTTON_ADDRESS::isWidgetVisible, 8000);

        if(!open) {
            return null;
        }

        var closeButton = CLOSE_BUTTON_ADDRESS.resolve();

        if(!Widgets.isVisible(closeButton))
        {
            log.warn("Unable to find the rewards screen");
            return null;
        }

        Reaction.REGULAR.sleep();

        if(!closeButton.interact(Predicates.always())) {
            log.warn("Failed to close the rewards screen");
            return this;
        }

        Reaction.REGULAR.sleep();
        return null;
    }

    @Override
    public String getStatus() {
        return "Closing the rewards screen";
    }

}
