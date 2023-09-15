import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.math.Random;
import net.rlbot.api.script.Script;
import net.rlbot.api.script.ScriptManifest;
import net.rlbot.script.api.data.ItemId;
import tasks.agility.AgilityTask;
import tasks.agility.behaviour.course.Course;
import tasks.combat.common.combattask.enums.MonsterArea;
import tasks.common.AccountBuilderTask;
import tasks.magic.enchanting.EnchantingTask;
import tasks.magic.enchanting.EnchantingTaskConfiguration;
import tasks.magic.enchanting.enums.EnchantmentProduct;
import tasks.magic.highalch.HighAlchTask;
import tasks.magic.highalch.HighAlchTaskConfiguration;
import tasks.magic.magiccombat.MagicCombatTask;
import tasks.magic.magiccombat.MagicCombatTaskConfiguration;
import tasks.magic.magiccombat.enums.CombatSpell;
import tasks.magic.superheat.SuperHeatTask;
import tasks.magic.superheat.SuperHeatTaskConfiguration;
import tasks.magic.superheat.enums.Bar;

@Slf4j
@ScriptManifest(name = "AccountBuilder", author = "Tim", version = 0.1)
public class AccountBuilder extends Script {

    private static final AccountBuilderTask TASK = new AgilityTask(Course.DRAYNOR_VILLAGE);

    static {
        TASK.initialize();
    }

    private String previousStatus;

    @Override
    public int loop() {

        var node = TASK.getNode();

        if(node != null) {

            var status = node.getStatus();

            if(status != null && !status.equals(previousStatus)) {
                log.debug("Status: {}", status);
                this.previousStatus = status;
            }

            node.execute();
        }

        return Random.between(50, 200);
    }
}
