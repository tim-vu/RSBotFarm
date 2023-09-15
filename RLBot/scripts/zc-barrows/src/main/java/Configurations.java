import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.loadout.EquipmentSet;

import java.util.List;

public class Configurations {

    public static final BarrowsConfiguration TRIDENT = BarrowsConfiguration.builder()
            .foodItemIds(List.of(ItemId.SHARK))
            .minimumFood(3)
            .maximumFood(9)
            .minimumHealthPercent(30)
            .minimumPrayerPotions(2)
            .maximumPrayerPotions(6)
            .magicEquipmentSet(EquipmentSet.builder()
                    .with(ItemId.RUNE_FULL_HELM).build()
                    .with(ItemId.AMULET_OF_FURY).build()
                    .with(ItemId.RUNE_CHAINBODY).build()
                    .with(ItemId.RUNE_PLATELEGS).build()
                    .with(ItemId.BLACK_CAPE).build()
                    .with(ItemIds.BROODOO_SHIELD_COMBAT).build()
                    .with(ItemIds.TRIDENT_OF_THE_SEAS).build()
                    .with(ItemId.IRON_BOLTS).amount(200, 1000).build()
                    .with(ItemIds.COMBAT_BRACELET).build()
                    .with(ItemId.RUNE_BOOTS).build()
                    .with(ItemId.RING_OF_DUELING8).build()
                    .build()
            )
            .rangedEquipmentSet(EquipmentSet.builder()
                .with(ItemId.COIF).build()
                .with(ItemId.AMULET_OF_FURY).build()
                .with(ItemId.STUDDED_BODY).build()
                .with(ItemId.STUDDED_CHAPS).build()
                .with(ItemIds.BROODOO_SHIELD_COMBAT).build()
                .with(ItemId.RUNE_CROSSBOW).build()
                .with(ItemId.IRON_BOLTS).amount(200, 1000).build()
                .with(ItemIds.COMBAT_BRACELET).build()
                .with(ItemId.RUNE_BOOTS).build()
                .with(ItemId.RING_OF_DUELING8).build()
                .build()
            )
            .monsterEquipmentSet(EquipmentSet.builder()
                .with(ItemId.RUNE_FULL_HELM).build()
                .with(ItemId.AMULET_OF_FURY).build()
                .with(ItemId.RUNE_CHAINBODY).build()
                .with(ItemId.RUNE_PLATELEGS).build()
                .with(ItemId.BLACK_CAPE).build()
                .with(ItemId.RUNE_SCIMITAR).build()
                .with(ItemIds.BROODOO_SHIELD_COMBAT).build()
                .with(ItemId.IRON_BOLTS).amount(200, 1000).build()
                .with(ItemIds.COMBAT_BRACELET).build()
                .with(ItemId.RUNE_BOOTS).build()
                .with(ItemId.RING_OF_DUELING8).build()
                .build()
            )
            .build();

}
