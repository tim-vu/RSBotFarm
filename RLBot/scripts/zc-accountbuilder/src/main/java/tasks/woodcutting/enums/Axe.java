package tasks.woodcutting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.script.api.data.ItemId;

@AllArgsConstructor
public enum Axe {

    IRON(ItemId.IRON_AXE, 1, 1),
    STEEL(ItemId.STEEL_AXE, 6, 5),
    BLACK(ItemId.BLACK_AXE, 11, 10),
    MITRHIL(ItemId.MITHRIL_AXE, 21, 20),
    ADAMTANT(ItemId.ADAMANT_AXE, 31, 30),
    RUNE(ItemId.RUNE_AXE, 41, 40),
    DRAGON(ItemId.DRAGON_AXE, 61, 60);

    @Getter
    private final int itemId;

    @Getter
    private final int minimumWoodcuttingLevel;

    @Getter
    private final int minimumAttackLevel;

    public boolean canEquip() {
        return Skills.getLevel(Skill.ATTACK) >= this.minimumAttackLevel;
    }

    public static Axe getBestUsable() {

        var wcLevel = Skills.getLevel(Skill.WOODCUTTING);

        Axe bestAxe = Axe.IRON;

        for(var axe : Axe.values()) {

            if(wcLevel < axe.getMinimumWoodcuttingLevel()) {
                break;
            }

            bestAxe = axe;
        }

        return bestAxe;
    }

}
