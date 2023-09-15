package tasks.combat.melee;

import net.rlbot.api.game.Skill;
import net.rlbot.api.items.EquipmentSlot;
import net.rlbot.api.quest.Quest;
import net.rlbot.script.api.data.ItemId;
import tasks.combat.common.Equipment;
import tasks.combat.common.EquipmentTable;

public class MeleeEquipment {

    public static final EquipmentTable EQUIPMENT = EquipmentTable.builder()
            .withHeadItems(
                    Equipment.builder(EquipmentSlot.HEAD, ItemId.IRON_FULL_HELM).build(),
                    Equipment.builder(EquipmentSlot.HEAD, ItemId.STEEL_FULL_HELM)
                            .withLevelRequirement(Skill.DEFENCE, 5)
                            .build(),
                    Equipment.builder(EquipmentSlot.HEAD, ItemId.MITHRIL_FULL_HELM)
                            .withLevelRequirement(Skill.DEFENCE, 20)
                            .build(),
                    Equipment.builder(EquipmentSlot.HEAD, ItemId.ADAMANT_FULL_HELM)
                            .withLevelRequirement(Skill.DEFENCE, 30)
                            .build(),
                    Equipment.builder(EquipmentSlot.HEAD, ItemId.RUNE_FULL_HELM)
                            .withLevelRequirement(Skill.DEFENCE, 40)
                            .build()
            )
            .withAmuletItems(
                    Equipment.builder(EquipmentSlot.AMULET, ItemId.AMULET_OF_POWER).build()
            )
            .withBodyItems(
                    Equipment.builder(EquipmentSlot.BODY, ItemId.IRON_PLATEBODY).build(),
                    Equipment.builder(EquipmentSlot.BODY, ItemId.STEEL_PLATEBODY)
                            .withLevelRequirement(Skill.DEFENCE, 5)
                            .build(),
                    Equipment.builder(EquipmentSlot.BODY, ItemId.MITHRIL_PLATEBODY)
                            .withLevelRequirement(Skill.DEFENCE, 20)
                            .build(),
                    Equipment.builder(EquipmentSlot.BODY, ItemId.ADAMANT_PLATEBODY)
                            .withLevelRequirement(Skill.DEFENCE, 30)
                            .build(),
                    Equipment.builder(EquipmentSlot.BODY, ItemId.RUNE_CHAINBODY)
                            .withLevelRequirement(Skill.DEFENCE, 40)
                            .build(),
                    Equipment.builder(EquipmentSlot.BODY, ItemId.RUNE_PLATEBODY)
                            .withLevelRequirement(Skill.DEFENCE, 40)
                            .withQuestRequirement(Quest.DRAGON_SLAYER_I)
                            .build()
            )
            .withLegsItems(
                    Equipment.builder(EquipmentSlot.LEGS, ItemId.IRON_PLATELEGS).build(),
                    Equipment.builder(EquipmentSlot.LEGS, ItemId.STEEL_PLATELEGS)
                            .withLevelRequirement(Skill.DEFENCE, 5)
                            .build(),
                    Equipment.builder(EquipmentSlot.LEGS, ItemId.MITHRIL_PLATELEGS)
                            .withLevelRequirement(Skill.DEFENCE, 20)
                            .build(),
                    Equipment.builder(EquipmentSlot.LEGS, ItemId.ADAMANT_PLATELEGS)
                            .withLevelRequirement(Skill.DEFENCE, 30)
                            .build(),
                    Equipment.builder(EquipmentSlot.LEGS, ItemId.RUNE_PLATELEGS)
                            .withLevelRequirement(Skill.DEFENCE, 40)
                            .build()
            )
            .withWeaponItems(
                    Equipment.builder(EquipmentSlot.WEAPON, ItemId.IRON_SCIMITAR).build(),
                    Equipment.builder(EquipmentSlot.WEAPON, ItemId.STEEL_SCIMITAR)
                            .withLevelRequirement(Skill.ATTACK, 5)
                            .build(),
                    Equipment.builder(EquipmentSlot.WEAPON, ItemId.MITHRIL_SCIMITAR)
                            .withLevelRequirement(Skill.ATTACK, 20)
                            .build(),
                    Equipment.builder(EquipmentSlot.WEAPON, ItemId.ADAMANT_SCIMITAR)
                            .withLevelRequirement(Skill.ATTACK, 30)
                            .build(),
                    Equipment.builder(EquipmentSlot.WEAPON, ItemId.BARRONITE_MACE)
                            .withLevelRequirement(Skill.ATTACK, 40)
                            .tradeable(false)
                            .build(),
                    Equipment.builder(EquipmentSlot.WEAPON, ItemId.RUNE_SCIMITAR)
                            .withLevelRequirement(Skill.ATTACK, 40)
                            .build()
            )
            .withShieldItems(
                    Equipment.builder(EquipmentSlot.SHIELD, ItemId.IRON_KITESHIELD).build(),
                    Equipment.builder(EquipmentSlot.SHIELD, ItemId.STEEL_KITESHIELD)
                            .withLevelRequirement(Skill.DEFENCE, 5)
                            .build(),
                    Equipment.builder(EquipmentSlot.SHIELD, ItemId.MITHRIL_KITESHIELD)
                            .withLevelRequirement(Skill.DEFENCE, 20)
                            .build(),
                    Equipment.builder(EquipmentSlot.SHIELD, ItemId.ADAMANT_KITESHIELD)
                            .withLevelRequirement(Skill.DEFENCE, 30)
                            .build(),
                    Equipment.builder(EquipmentSlot.SHIELD, ItemId.RUNE_KITESHIELD)
                            .withLevelRequirement(Skill.DEFENCE, 40)
                            .build()
            )
            .withGlovesItems(
                    Equipment.builder(EquipmentSlot.GLOVES, ItemId.LEATHER_GLOVES).build(),
                    Equipment.builder(EquipmentSlot.GLOVES, ItemId.LEATHER_VAMBRACES).build(),
                    Equipment.builder(EquipmentSlot.GLOVES, ItemId.GREEN_DHIDE_VAMBRACES)
                            .withLevelRequirement(Skill.RANGED, 40).build()
            )
            .withBootsItems(
                    Equipment.builder(EquipmentSlot.BOOTS, ItemId.LEATHER_BOOTS).build()
            )
            .build();
}
