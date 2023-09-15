package behaviour.actions;

import net.rlbot.api.common.Time;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.script.api.Action;
import net.rlbot.script.api.reaction.Reaction;
import net.rlbot.script.api.tree.decisiontree.LeafNodeBase;

public class EnableAutocastAction extends LeafNodeBase {
    @Override
    public String getStatus() {
        return "Enabling autocast";
    }

    @Override
    public void execute() {
        Action.logPerform("ENABLE_AUTOCAST");
        if(!Magic.Autocast.selectSpell(Magic.Autocast.Mode.OFFENSIVE, SpellBook.Standard.IBAN_BLAST)) {
            Action.logFail("ENABLE_AUTOCAST");
            Time.sleepTick();
            return;
        }

        Reaction.PREDICTABLE.sleep();
    }
}
