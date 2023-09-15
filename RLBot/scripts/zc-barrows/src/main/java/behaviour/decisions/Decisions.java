package behaviour.decisions;

import data.Animations;
import data.Areas;
import data.Keys;
import enums.Brother;
import equipment.RechargeableEquipment;
import equipment.weapons.RechargeableWeapon;
import net.rlbot.api.game.*;
import net.rlbot.script.api.loadout.LoadoutManager;
import net.rlbot.script.api.restocking.data.RestockingKeys;
import tunnels.Loot;
import tunnels.Puzzle;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.scene.Players;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.data.ItemIds;
import net.rlbot.script.api.tree.decisiontree.Decision;
import tunnels.Tunnels;

public class Decisions {

    public static Decision isAtBarrows() {
        return b -> {
            var local = Players.getLocal();
            return Areas.BARROWS.contains(local) || Areas.MOUNDS.contains(local) || Areas.TUNNELS.contains(local);
        };
    }

    public static Decision togglePlayers() {
        return b -> b.get(Keys.TOGGLE_PRAYERS).get();
    }

    public static Decision eatFood() {
        return b -> {

            if(Players.getLocal().getAnimation() == Animations.EATING) {
                return false;
            }

            var settings = b.get(Keys.SETTINGS);
            return Health.getPercent() < settings.getMinimumHealthPercent() && Inventory.contains(i -> settings.getFoodItemIds().contains(i.getId()));
        };
    }

    public static Decision usePrayerPotion() {
        return b -> Prayers.getPoints() < b.get(Keys.SETTINGS).getMinimumPrayerPoints() && Inventory.contains(ItemIds.PRAYER_POTION) && Brother.getMyBrother() != null;
    }

    public static Decision enableAutocast() {
        return b -> !Magic.Autocast.isSpellSelected() && Equipment.contains(ItemId.IBANS_STAFF, ItemId.IBANS_STAFF_U);
    }

    public static Decision isInTunnels() {
        return b -> Areas.TUNNELS.contains(Players.getLocal());
    }

    public static Decision isMoundRemaining() {
        return b -> !b.get(Keys.REMAINING_BROTHERS).isEmpty();
    }

    public static Decision isInMound() {
        return b -> Areas.MOUNDS.contains(Players.getLocal());
    }

    public static Decision isInWrongMound() {
        return b -> Areas.MOUNDS.contains(Players.getLocal()) && Brother.getBrotherByMound() != b.get(Keys.BROTHER_IN_TUNNELS);
    }

    public static Decision isBrotherGone() {
        return b -> !b.get(Keys.REMAINING_BROTHERS).contains(Brother.getBrotherByMound());
    }

    public static Decision isBrotherPresent() {
        return b -> {
            var target = HintArrow.getTargetActor();
            return target instanceof Npc npc && npc.getHealthPercent() > 0;
        };
    }

    public static Decision enableSpecialAttack() {
        return b -> {
            if(Players.getLocal().getTarget() == null) {
                return false;
            }

            var weapon = b.get(Keys.SETTINGS).getSpecialAttackWeapons();

            return !Combat.isSpecEnabled() && weapon.stream().anyMatch(w -> Equipment.contains(w.getItemId()) && Combat.getSpecEnergy() >= w.getRequiredEnergy());
        };
    }

    public static Decision moveToStairs() {
        return b -> false;
    }

    public static Decision isPuzzleOpen() {
        return b -> Puzzle.isOpen();
    }

    public static Decision hasSearchedChest() {
        return b -> b.get(Keys.SEARCHED_CHEST);
    }

    public static Decision isInChestRoom() {
        return b -> Tunnels.CHEST_ROOM.contains(Players.getLocal());
    }

    public static Decision pickupLoot() {
        return b -> Loot.shouldPickupItems(b.get(Keys.SETTINGS));
    }

    public static Decision isRestocking() {
        return b -> b.get(RestockingKeys.IS_RESTOCKING);
    }

    public static Decision hasFullStats() {
        return b -> Prayers.getPoints() == Skills.getLevel(Skill.PRAYER) && Health.getPercent() == 100;
    }

    public static Decision checkWeaponCharges() {
        return b -> b.get(Keys.SETTINGS).getRechargeableEquipment().stream()
                .filter(e -> e instanceof RechargeableWeapon)
                .map(e -> (RechargeableWeapon)e)
                .anyMatch(w -> w.getRemainingCharges() == -1);
    }

    public static Decision rechargeEquipment() {
        return b -> {
            if(b.get(Keys.EQUIPMENT_TO_RECHARGE) != null) {
                return true;
            }

            var toRecharge = b.get(Keys.SETTINGS).getRechargeableEquipment().stream()
                    .filter(RechargeableEquipment::needsRecharging)
                    .findFirst();

            if(toRecharge.isPresent()) {
                b.put(Keys.EQUIPMENT_TO_RECHARGE, toRecharge.get());
                return true;
            }

            return false;
        };
    }

    public static Decision hasRechargeLoadout() {
        return b -> LoadoutManager.hasLoadout(b.get(Keys.EQUIPMENT_TO_RECHARGE).getRechargeLoadout());
    }

    public static Decision isAtRechargeArea() {
        return b -> b.get(Keys.EQUIPMENT_TO_RECHARGE).getRechargeArea().contains(Players.getLocal());
    }

    public static Decision hasLoadout() {
        return b -> b.get(Keys.HAS_LOADOUT).get();
    }

}
