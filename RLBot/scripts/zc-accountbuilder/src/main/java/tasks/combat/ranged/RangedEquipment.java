package tasks.combat.ranged;

import net.rlbot.api.game.Skill;
import net.rlbot.api.items.EquipmentSlot;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import tasks.combat.common.Equipment;
import tasks.combat.common.EquipmentTable;

public class RangedEquipment {

        public static final EquipmentTable EQUIPMENT = EquipmentTable.builder()
                .withHeadItems(
                        Equipment.builder(EquipmentSlot.HEAD, ItemId.MITHRIL_MED_HELM)
                                .withLevelRequirement(Skill.DEFENCE, 20)
                                .build(),
                        Equipment.builder(EquipmentSlot.HEAD, ItemId.ADAMANT_MED_HELM)
                                .withLevelRequirement(Skill.DEFENCE, 30)
                                .build(),
                        Equipment.builder(EquipmentSlot.HEAD, ItemId.RUNE_MED_HELM)
                                .withLevelRequirement(Skill.DEFENCE, 40)
                                .build(),
                        Equipment.builder(EquipmentSlot.HEAD, ItemId.LEATHER_COWL).build(),
                        Equipment.builder(EquipmentSlot.HEAD, ItemId.COIF)
                                .withLevelRequirement(Skill.RANGED, 20)
                                .build()
                )
                .withAmuletItems(
                        Equipment.builder(EquipmentSlot.AMULET, ItemId.HOLY_SYMBOL).build(),
                        Equipment.builder(EquipmentSlot.AMULET, ItemId.AMULET_OF_DEFENCE).build(),
                        Equipment.builder(EquipmentSlot.AMULET, ItemId.AMULET_OF_ACCURACY).build(),
                        Equipment.builder(EquipmentSlot.AMULET, ItemId.AMULET_OF_POWER).build()
                )
                .withCapeItems(
                        Equipment.builder(EquipmentSlot.CAPE, ItemId.BLACK_CAPE).build(),
                        Equipment.builder(EquipmentSlot.CAPE, ItemId.TEAM1_CAPE).build(),
                        Equipment.builder(EquipmentSlot.CAPE, ItemId.CABBAGE_CAPE)
                                .tradeable(false)
                                .build()
                )
                .withBodyItems(
                        Equipment.builder(EquipmentSlot.BODY, ItemId.RUNE_CHAINBODY)
                                .withLevelRequirement(Skill.DEFENCE, 40)
                                .build(),
                        Equipment.builder(EquipmentSlot.BODY, ItemId.LEATHER_BODY).build(),
                        Equipment.builder(EquipmentSlot.BODY, ItemId.HARDLEATHER_BODY)
                                .withLevelRequirement(Skill.DEFENCE, 10)
                                .build(),
                        Equipment.builder(EquipmentSlot.BODY, ItemId.STUDDED_BODY)
                                .withLevelRequirement(Skill.RANGED, 20)
                                .withLevelRequirement(Skill.DEFENCE, 20)
                                .build(),
                        Equipment.builder(EquipmentSlot.BODY, ItemId.GREEN_DHIDE_BODY)
                                .withLevelRequirement(Skill.RANGED, 40)
                                .withLevelRequirement(Skill.DEFENCE, 40)
                                .withQuestRequirement(Quest.DRAGON_SLAYER_I)
                                .build()
                )
                .withLegsItems(
                        Equipment.builder(EquipmentSlot.LEGS, ItemId.LEATHER_CHAPS).build(),
                        Equipment.builder(EquipmentSlot.LEGS, ItemId.STUDDED_CHAPS)
                                .withLevelRequirement(Skill.RANGED, 20)
                                .build(),
                        Equipment.builder(EquipmentSlot.LEGS, ItemId.GREEN_DHIDE_CHAPS)
                                .withLevelRequirement(Skill.RANGED, 40)
                                .withQuestRequirement(Quest.DRAGON_SLAYER_I)
                                .build()
                )
                .withWeaponItems(
                        Equipment.builder(EquipmentSlot.WEAPON, ItemId.SHORTBOW).build(),
                        Equipment.builder(EquipmentSlot.WEAPON, ItemId.OAK_SHORTBOW)
                                .withLevelRequirement(Skill.RANGED, 5)
                                .build(),
                        Equipment.builder(EquipmentSlot.WEAPON, ItemId.WILLOW_SHORTBOW)
                                .withLevelRequirement(Skill.RANGED, 20)
                                .build(),
                        Equipment.builder(EquipmentSlot.WEAPON, ItemId.MAPLE_SHORTBOW)
                                .withLevelRequirement(Skill.RANGED, 30)
                                .build()
                )
                .withGlovesItems(
                        Equipment.builder(EquipmentSlot.GLOVES, ItemId.LEATHER_GLOVES).build(),
                        Equipment.builder(EquipmentSlot.GLOVES, ItemId.LEATHER_VAMBRACES).build(),
                        Equipment.builder(EquipmentSlot.GLOVES, ItemId.GREEN_DHIDE_VAMBRACES)
                                .withLevelRequirement(Skill.RANGED, 40)
                                .withQuestRequirement(Quest.DRAGON_SLAYER_I)
                                .build()
                )
                .withBootsItems(
                        Equipment.builder(EquipmentSlot.BOOTS, ItemId.FIGHTING_BOOTS)
                                .tradeable(false)
                                .build(),
                        Equipment.builder(EquipmentSlot.BOOTS, ItemId.FANCY_BOOTS)
                                .tradeable(false)
                                .build(),
                        Equipment.builder(EquipmentSlot.BOOTS, ItemId.DECORATIVE_BOOTS)
                                .withLevelRequirement(Skill.DEFENCE, 5)
                                .build(),
                        Equipment.builder(EquipmentSlot.BOOTS, ItemId.DECORATIVE_BOOTS_25167)
                                .withLevelRequirement(Skill.DEFENCE, 20)
                                .build(),
                        Equipment.builder(EquipmentSlot.BOOTS, ItemId.DECORATIVE_BOOTS_25171)
                                .withLevelRequirement(Skill.DEFENCE, 30)
                                .build(),
                        Equipment.builder(EquipmentSlot.BOOTS, ItemId.LEATHER_BOOTS)
                                .build()
                )
                .build();
}

