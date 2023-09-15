package net.rlbot.api.adapter.scene;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.definitions.Definitions;
import net.rlbot.api.definitions.NpcDefinition;
import net.rlbot.api.packet.MousePackets;
import net.rlbot.api.packet.NpcPackets;
import net.rlbot.internal.Interaction;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ParamID;
import org.apache.commons.lang3.ArrayUtils;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
public class Npc extends Actor {

    private final NPC npc;

    private final NpcDefinition def;

    public Npc(@NonNull final NPC npc) {
        super(npc);
        this.npc = npc;

        var composition = Definitions.getNpcDefinition(this.npc.getId());

        if(composition.getConfigs() == null || composition.getTransform() == null) {
            this.def = composition;
            return;
        }

        this.def = composition.getTransform();
    }

    public String[] getActions() {
        return this.def.getActions();
    }

    public int getId() {
        return this.def.getId();
    }

    @Override
    public String getName() {
        return this.def.getName();
    }

    public int getIndex() {
        return this.npc.getIndex();
    }

    public int getCombatLevel() {
        return this.def.getCombatLevel();
    }

    @Override
    public boolean interact(int index) {

        int num = index + 1;

        if(num < 1 || num > 10) {
            return false;
        }

        Interaction.log("NPC", kv("menuIndex", index), kv("npcId", this.getId()));
        MousePackets.queueClickPacket();
        NpcPackets.queueAction(num, this.npc.getIndex(), false);
        return true;
    }

    public boolean isDying()
    {
        final int id = npc.getId();
        switch (id)
        {
            // These NPCs hit 0hp but don't actually die
            case NpcID.GARGOYLE:
            case NpcID.GARGOYLE_1543:
            case NpcID.MARBLE_GARGOYLE:
            case NpcID.DAWN_7884:
            case NpcID.DUSK_7888:
            case NpcID.ZYGOMITE:
            case NpcID.ZYGOMITE_1024:
            case NpcID.ANCIENT_ZYGOMITE:
            case NpcID.ROCKSLUG:
            case NpcID.ROCKSLUG_422:
            case NpcID.GIANT_ROCKSLUG:
            case NpcID.DESERT_LIZARD:
            case NpcID.DESERT_LIZARD_460:
            case NpcID.DESERT_LIZARD_461:
            case NpcID.LIZARD:
            case NpcID.SMALL_LIZARD:
            case NpcID.SMALL_LIZARD_463:
            case NpcID.GROWTHLING:
                // These NPCs die, but transform into forms which are attackable or interactable, so it would be jarring for
                // them to be considered dead when reaching 0hp.
            case NpcID.KALPHITE_QUEEN_963:
            case NpcID.VETION:
            case NpcID.CALVARION:
            case NpcID.WITCHS_EXPERIMENT:
            case NpcID.WITCHS_EXPERIMENT_6394:
            case NpcID.WITCHS_EXPERIMENT_HARD:
            case NpcID.WITCHS_EXPERIMENT_SECOND_FORM:
            case NpcID.WITCHS_EXPERIMENT_SECOND_FORM_6395:
            case NpcID.WITCHS_EXPERIMENT_SECOND_FORM_HARD:
            case NpcID.WITCHS_EXPERIMENT_THIRD_FORM:
            case NpcID.WITCHS_EXPERIMENT_THIRD_FORM_6396:
            case NpcID.WITCHS_EXPERIMENT_THIRD_FORM_HARD:
            case NpcID.NAZASTAROOL:
            case NpcID.NAZASTAROOL_5354:
            case NpcID.NAZASTAROOL_6398:
            case NpcID.NAZASTAROOL_6399:
            case NpcID.NAZASTAROOL_HARD:
            case NpcID.NAZASTAROOL_HARD_6338:
            case NpcID.KOLODION_1605:
            case NpcID.KOLODION_1606:
            case NpcID.KOLODION_1607:
            case NpcID.KOLODION_1608:
            case NpcID.MUTANT_TARN:
            case NpcID.XAMPHUR_10955:
            case NpcID.XAMPHUR_10956:
            case NpcID.KOSCHEI_THE_DEATHLESS:
            case NpcID.KOSCHEI_THE_DEATHLESS_3898:
            case NpcID.KOSCHEI_THE_DEATHLESS_3899:
            case NpcID.DAMIS:
            case NpcID.DAMIS_6346:
            case NpcID.DAMIS_HARD:
            case NpcID.CHOMPY_BIRD:
            case NpcID.JUBBLY_BIRD:
            case NpcID.ENT:
            case NpcID.ENT_7234:
            case NpcID.HOPELESS_CREATURE:
            case NpcID.HOPELESS_CREATURE_1073:
            case NpcID.GADDERANKS_4484:
            case NpcID.WALL_BEAST:
            case NpcID.RUNITE_GOLEM:
            case NpcID.RUNITE_ROCKS:
            case NpcID.STRANGE_CREATURE_12076: // Secrets of the North transitioning to Jhallan
                // Agrith Naar restores health upon reaching 0hp if the player does not have Silverlight
                // equipped, or moved away immediately after applying the killing blow.
            case NpcID.AGRITH_NAAR:
                return false;
            // These NPCs have no attack options, but are the dead and uninteractable form of otherwise attackable NPCs,
            // thus should not be considered alive.
            case NpcID.DRAKE_8613:
            case NpcID.GUARDIAN_DRAKE_10401:
            case NpcID.ALCHEMICAL_HYDRA_8622:
            case NpcID.XARPUS_8341:
            case NpcID.XARPUS_10769:
            case NpcID.XARPUS_10773:
            case NpcID.THE_NIGHTMARE_9433:
            case NpcID.PHOSANIS_NIGHTMARE_9424:
                // Gargoyles, Dawn, and Dusk each have cracking forms which contain their death animations, so should always
                // be considered dead.
            case NpcID.GARGOYLE_413:
            case NpcID.MARBLE_GARGOYLE_7408:
            case NpcID.DAWN_7885:
            case NpcID.DUSK_7889:
                return true;
            case NpcID.ZALCANO_9050:
                return npc.isDead();
            default:

                var npcComposition = npc.getTransformedComposition();
                if (npcComposition == null)
                {
                    return false;
                }

                var hasAttack = ArrayUtils.contains(npcComposition.getActions(), "Attack");
                return hasAttack && npc.isDead();
        }
    }

    public int getSize() {
        return this.def.getSize();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Npc npc1 = (Npc) o;

        return this.getIndex() == npc1.getIndex();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (npc != null ? npc.hashCode() : 0);
        return result;
    }

}
