package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.rlbot.api.adapter.scene.Actor;
import net.rlbot.api.game.HintArrow;
import net.rlbot.api.game.Prayer;
import net.rlbot.api.game.Varbits;
import net.rlbot.api.game.Vars;
import net.rlbot.api.movement.position.Area;
import net.rlbot.api.scene.Players;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@FieldDefaults(makeFinal = true)
@AllArgsConstructor
public enum Brother {

    DHAROK(
            "Dharok the Wretched",
            115,
            Varbits.BARROWS_KILLED_DHAROK,
            Area.rectangular(3574, 3299, 3576, 3297),
            Area.rectangular(3550, 9718, 3559, 9711, 3),
            CombatStyle.MAGIC,
            Prayer.PROTECT_FROM_MELEE
    ),
    GUTHAN(
            "Guthan the Infested",
            115,
            Varbits.BARROWS_KILLED_GUTHAN,
            Area.rectangular(3576, 3283, 3578, 3281),
            Area.rectangular(3534, 9707, 3544, 9700, 3),
            CombatStyle.MAGIC,
            Prayer.PROTECT_FROM_MELEE
    ),
    KARIL(
            "Karil the Tainted",
            98,
            Varbits.BARROWS_KILLED_KARIL,
            Area.rectangular(3565, 3276, 3567, 3274),
            Area.rectangular(3546, 9687, 3556, 9679, 3),
            CombatStyle.MAGIC,
            Prayer.PROTECT_FROM_MISSILES
    ),
    TORAG(
            "Torag the Corrupted",
            115,
            Varbits.BARROWS_KILLED_TORAG,
            Area.rectangular(3553, 3284, 3555, 3282),
            Area.rectangular(3565, 9692, 3574, 9683, 3),
            CombatStyle.MAGIC,
            Prayer.PROTECT_FROM_MELEE
    ),
    VERAC(
            "Verac the Defiled",
            115,
            Varbits.BARROWS_KILLED_VERAC,
            Area.rectangular(3556, 3299, 3558, 3297),
            Area.rectangular(3578, 9703, 3569, 9709, 3),
            CombatStyle.MAGIC,
            Prayer.PROTECT_FROM_MELEE
    ),
    AHRIM(
            "Ahrim the Blighted",
            98,
            Varbits.BARROWS_KILLED_AHRIM,
            Area.rectangular(3564, 3290, 3566, 3288),
            Area.rectangular(3551, 9703, 3560, 9694, 3),
            CombatStyle.RANGED,
            Prayer.PROTECT_FROM_MAGIC
    );

    String name;

    int combatLevel;

    int varbitId;

    Area digArea;

    Area moundArea;

    CombatStyle combatStyle;

    Prayer prayer;

    public static Brother getBrotherByName(String name) {

        for (var brother : Brother.values()) {

            if (!brother.getName().contains(name)) {
                continue;
            }

            return brother;
        }

        return null;
    }

    public static Brother getBrotherByMound(){

        var local = Players.getLocal();
        for(var brother : Brother.values()){

            if(!brother.getMoundArea().contains(local)) {
                continue;
            }

            return brother;
        }

        return null;
    }

    public static Set<Brother> getLivingBrothers(){

        var result = new HashSet<Brother>();

        for(var brother : Brother.values()){

            if(!brother.isAlive()) {
                continue;
            }

            result.add(brother);
        }

        return result;
    }

    public boolean isAlive() {
        return Vars.getBit(getVarbitId()) <= 0;
    }

    public static boolean isBrotherPresent(){
        return getMyBrother() != null;
    }

    public static boolean areBrothersDead() {
        return Arrays.stream(Brother.values()).noneMatch(Brother::isAlive);
    }

    public static Actor getMyBrother(){
        var brother = HintArrow.getTargetActor();
        return brother != null && brother.getHealthPercent() > 0 ? brother : null;
    }

    public static Brother getMyBrotherType() {
        var brother = HintArrow.getTargetActor();
        return brother == null ? null : Arrays.stream(Brother.values()).filter(b -> b.getName().equals(brother.getName())).findFirst().orElse(null);
    }

    public static int getRewardPotential() {
        return Vars.getBit(Varbits.BARROWS_REWARD_POTENTIAL);
    }
}