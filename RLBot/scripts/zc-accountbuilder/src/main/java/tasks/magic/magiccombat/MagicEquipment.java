package tasks.magic.magiccombat;

import net.rlbot.api.items.EquipmentSlot;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import tasks.combat.common.Equipment;
import tasks.combat.common.EquipmentTable;

public class MagicEquipment {

    public static final EquipmentTable EQUIPMENT = EquipmentTable.builder()
            .withHeadItems(
                    Equipment.builder(EquipmentSlot.HEAD, ItemId.BLUE_WIZARD_HAT).build()
            )
            .withAmuletItems(
                    Equipment.builder(EquipmentSlot.AMULET, ItemId.AMULET_OF_MAGIC).build()
            )
            .withCapeItems(
                    Equipment.builder(EquipmentSlot.CAPE, ItemId.BLACK_CAPE).build(),
                    Equipment.builder(EquipmentSlot.CAPE, ItemId.TEAM1_CAPE).build()
            )
            .withBodyItems(
                    Equipment.builder(EquipmentSlot.BODY, ItemId.BLUE_WIZARD_ROBE).build()
            )
            .withLegsItems(
                    Equipment.builder(EquipmentSlot.LEGS, ItemId.ZAMORAK_MONK_BOTTOM).build()
            )
            .withShieldItems(
                    Equipment.builder(EquipmentSlot.SHIELD, ItemId.ANTIDRAGON_SHIELD)
                            .withQuestRequirement(Quest.DRAGON_SLAYER_I)
                            .build()
            )
            .withGlovesItems(
                    Equipment.builder(EquipmentSlot.GLOVES, ItemId.LEATHER_VAMBRACES).build()
            )
            .withBootsItems(
                    Equipment.builder(EquipmentSlot.BOOTS, ItemId.LEATHER_BOOTS).build()
            )
            .build();
}
