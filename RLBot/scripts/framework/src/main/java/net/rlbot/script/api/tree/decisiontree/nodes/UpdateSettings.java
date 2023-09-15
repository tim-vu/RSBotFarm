package net.rlbot.script.api.tree.decisiontree.nodes;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.game.Settings;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

@Slf4j
public class UpdateSettings extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Updating settings";
    }

    @Override
    public void execute() {

        if(!Settings.DisableLevelUpInterface.isEnabled()) {

            if(!Settings.DisableLevelUpInterface.toggleEnabled()) {
                log.warn("Failed to toggle DisableLevelUpInterface");
                return;
            }

            Reaction.REGULAR.sleep();
        }

        if(Settings.AcceptTradeDelay.isEnabled()) {

            if(!Settings.AcceptTradeDelay.toggleEnabled()) {
                log.warn("Failed to toggle AcceptTradeDelay");
                return;
            }

            Reaction.REGULAR.sleep();
        }

    }
}
