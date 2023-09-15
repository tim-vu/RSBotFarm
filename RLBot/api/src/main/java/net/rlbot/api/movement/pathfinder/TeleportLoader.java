package net.rlbot.api.movement.pathfinder;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Worlds;
import net.rlbot.api.input.Keyboard;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.magic.Magic;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.pathfinder.model.Teleport;
import net.rlbot.api.movement.pathfinder.model.TeleportItem;
import net.rlbot.api.movement.pathfinder.model.TeleportSpell;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.Quests;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.api.widgets.Minigames;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.Widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static net.rlbot.api.movement.pathfinder.model.MovementConstants.*;

@Slf4j
public class TeleportLoader
{

	private static final Pattern WILDY_PATTERN = Pattern.compile("Okay, teleport to level [\\d,]* Wilderness\\.");

	private static final List<Teleport> LAST_TELEPORT_LIST = new ArrayList<>();

	public static List<Teleport> buildTeleports() {

		List<Teleport> teleports = new ArrayList<>();
		teleports.addAll(LAST_TELEPORT_LIST);
		teleports.addAll(buildTimedTeleports());
		return teleports;
	}

	private static List<Teleport> buildTimedTeleports() {

		List<Teleport> teleports = new ArrayList<>();
		if (Worlds.isInMemberWorld()) {
			// Minigames
			if (Game.getWildyLevel() == 0 && Minigames.canTeleport()) {

				for (Minigames.Destination tp : Minigames.Destination.values()) {

					if (!tp.canUse()) {
						continue;
					}

					teleports.add(new Teleport(tp.getLocation(), () -> Minigames.teleport(tp)));
				}
			}
		}

		if (Game.getWildyLevel() <= 20) {
			for (TeleportSpell teleportSpell : TeleportSpell.values()) {
				if (!teleportSpell.canCast() || teleportSpell.getPoint() == null) {
					continue;
				}

				if(teleportSpell.getPoint().distanceTo(Players.getLocal()) <= 50) {
					continue;
				}

				teleports.add(new Teleport(teleportSpell.getPoint(), () ->
				{
					var spell = teleportSpell.getSpell();

					if(!Magic.cast(spell)) {
						log.warn("Failed to cast teleportation spell");
						return false;
					}

					return Time.sleepUntil(() -> teleportSpell.getPoint().distance() < 20, () -> Players.getLocal().isAnimating(), 8000);
				}));
			}
		}

		return teleports;
	}

	public static void refreshTeleports() {

		List<Teleport> teleports = new ArrayList<>();
		if (Worlds.isInMemberWorld()) {

			// One click teleport items
			for (TeleportItem tele : TeleportItem.values()) {
				if (!tele.canUse() || tele.getDestination().distanceTo(Players.getLocal()) <= 20) {
					continue;
				}

				if (tele == TeleportItem.ROYAL_SEED_POD) {
					if (Game.getWildyLevel() <= 30) {
						teleports.add(createItemTeleport(tele));
					}
				}

				if (Game.getWildyLevel() <= 20) {
					teleports.add(createItemTeleport(tele));
				}

			}

			if (Game.getWildyLevel() <= 20) {

				if (ringOfDueling()) {
					teleports.add(createJewelryTeleport(new Position(3315, 3235, 0), "PvP Arena", RING_OF_DUELING));
					teleports.add(createJewelryTeleport(new Position(2440, 3090, 0), "Castle Wars", RING_OF_DUELING));
					teleports.add(createJewelryTeleport(new Position(3151, 3635, 0), "Ferox Enclave", RING_OF_DUELING));
				}

				if (gamesNecklace()) {
					teleports.add(createJewelryTeleport(new Position(2898, 3553, 0), "Burthorpe", GAMES_NECKLACE));
					teleports.add(createJewelryTeleport(new Position(2520, 3571, 0), "Barbarian Outpost", GAMES_NECKLACE));
					teleports.add(createJewelryTeleport(new Position(2964, 4382, 2), "Corporeal Beast", GAMES_NECKLACE));
					teleports.add(createJewelryTeleport(new Position(3244, 9501, 2), "Tears of Guthix", GAMES_NECKLACE));
					teleports.add(createJewelryTeleport(new Position(1624, 3938, 0), "Wintertodt Camp", GAMES_NECKLACE));
				}

				if (necklaceOfPassage()) {
					teleports.add(createJewelryTeleport(new Position(3114, 3179, 0), "Wizards' Tower", NECKLACE_OF_PASSAGE));
					teleports.add(createJewelryTeleport(new Position(2430, 3348, 0), "The Outpost", NECKLACE_OF_PASSAGE));
					teleports.add(createJewelryTeleport(new Position(3405, 3157, 0), "Eagle's Eyrie", NECKLACE_OF_PASSAGE));
				}

				if (xericsTalisman()) {
					teleports.add(createJewelryTeleport(new Position(1576, 3530, 0), "Xeric's Lookout", XERICS_TALISMAN));
					teleports.add(createJewelryTeleport(new Position(1752, 3566, 0), "Xeric's Glade", XERICS_TALISMAN));
					teleports.add(createJewelryTeleport(new Position(1504, 3817, 0), "Xeric's Inferno", XERICS_TALISMAN));
					if (Quests.isFinished(Quest.ARCHITECTURAL_ALLIANCE)) {
						teleports.add(createJewelryTeleport(new Position(1640, 3674, 0), "Xeric's Heart", XERICS_TALISMAN));
					}
				}

				if (digsitePendant()) {
					teleports.add(createJewelryTeleport(new Position(3341, 3445, 0), "Digsite", DIGSITE_PENDANT));
					teleports.add(createJewelryTeleport(new Position(3764, 3869, 1), "Fossil Island", DIGSITE_PENDANT));
					if (Quests.isFinished(Quest.DRAGON_SLAYER_II)) {
						teleports.add(createJewelryTeleport(new Position(3549, 10456, 0), "Lithkren", DIGSITE_PENDANT));
					}
				}
			}

			if (Game.getWildyLevel() <= 30) {

				if (combatBracelet()) {
					teleports.add(createJewelryTeleport(new Position(2882, 3548, 0), "Warriors' Guild", COMBAT_BRACELET));
					teleports.add(createJewelryTeleport(new Position(3191, 3367, 0), "Champions' Guild", COMBAT_BRACELET));
					teleports.add(createJewelryTeleport(new Position(3052, 3488, 0), "Monastery", COMBAT_BRACELET));
					teleports.add(createJewelryTeleport(new Position(2655, 3441, 0), "Ranging Guild", COMBAT_BRACELET));
				}

				if (skillsNecklace()) {
					teleports.add(createJewelryPopupTeleport(new Position(2611, 3390, 0), "Fishing Guild", SKILLS_NECKLACE));
					teleports.add(createJewelryPopupTeleport(new Position(3050, 9763, 0), "Mining Guild", SKILLS_NECKLACE));
					teleports.add(createJewelryPopupTeleport(new Position(2933, 3295, 0), "Crafting Guild", SKILLS_NECKLACE));
					teleports.add(createJewelryPopupTeleport(new Position(3143, 3440, 0), "Cooking Guild", SKILLS_NECKLACE));
					teleports.add(createJewelryPopupTeleport(new Position(1662, 3505, 0), "Woodcutting Guild", SKILLS_NECKLACE));
					teleports.add(createJewelryPopupTeleport(new Position(1249, 3718, 0), "Farming Guild", SKILLS_NECKLACE));
				}


				if (ringOfWealth()) {

					teleports.add(createJewelryTeleport(new Position(3163, 3478, 0), "Grand Exchange", RING_OF_WEALTH));
					teleports.add(createJewelryTeleport(new Position(2996, 3375, 0), "Falador", RING_OF_WEALTH));

					if (Quests.isFinished(Quest.THRONE_OF_MISCELLANIA)) {
						teleports.add(createJewelryTeleport(new Position(2538, 3863, 0), "Miscellania", RING_OF_WEALTH));
					}

					if (Quests.isFinished(Quest.BETWEEN_A_ROCK)) {
						teleports.add(createJewelryTeleport(new Position(2828, 10166, 0), "Miscellania", RING_OF_WEALTH));
					}
				}

				if (amuletOfGlory()) {
					teleports.add(createJewelryTeleport(new Position(3087, 3496, 0), "Edgeville", AMULET_OF_GLORY));
					teleports.add(createJewelryTeleport(new Position(2918, 3176, 0), "Karamja", AMULET_OF_GLORY));
					teleports.add(createJewelryTeleport(new Position(3105, 3251, 0), "Draynor Village", AMULET_OF_GLORY));
					teleports.add(createJewelryTeleport(new Position(3293, 3163, 0), "Al Kharid", AMULET_OF_GLORY));
				}

				if (burningAmulet()) {
					teleports.add(createJewelryWildernessTeleport(new Position(3235, 3636, 0), "Chaos Temple", BURNING_AMULET));
					teleports.add(createJewelryWildernessTeleport(new Position(3038, 3651, 0), "Bandit Camp", BURNING_AMULET));
					teleports.add(createJewelryWildernessTeleport(new Position(3028, 3842, 0), "Lava Maze", BURNING_AMULET));
				}

				if (slayerRing()) {

					teleports.add(createSlayerRingTeleport(new Position(2432, 3423, 0), "Stronghold Slayer Cave", SLAYER_RING));
					teleports.add(createSlayerRingTeleport(new Position(3422, 3537, 0), "Slayer Tower", SLAYER_RING));
					teleports.add(createSlayerRingTeleport(new Position(2802, 10000, 0), "Fremennik Slayer Dungeon", SLAYER_RING));
					teleports.add(createSlayerRingTeleport(new Position(3185, 4601, 0), "Tarn's Lair", SLAYER_RING));

					if (Quests.isFinished(Quest.MOURNINGS_END_PART_II)) {
						teleports.add(createSlayerRingTeleport(new Position(2028, 4636, 0), "Dark Beasts", SLAYER_RING));
					}
				}
			}
		}

		LAST_TELEPORT_LIST.clear();
		LAST_TELEPORT_LIST.addAll(teleports);
	}

	private static Teleport createJewelryTeleport(Position destination, String target, int... ids) {
		return new Teleport(destination, () -> {

			var item = Inventory.getFirst(ids);

			if (item != null) {

				if (!Dialog.isViewingOptions()) {

					if(!item.interact("Rub") || !Time.sleepUntil(Dialog::isViewingOptions, 1200)){
						log.warn("Failed to rub " + item.getName());
						Time.sleepTick();
						return false;
					}

					Time.sleepTick();
					return true;
				}

				return Dialog.chooseOption(target) && Time.sleepUntil(() -> destination.distance() < 10, () -> Players.getLocal().isAnimating(), 8000);
			}

			var equipped = Equipment.getFirst(ids);

			if (equipped == null) {
				log.warn("Unable to find teleportation item");
				return false;
			}

			return equipped.interact(target) && Time.sleepUntil(() -> destination.distance() < 10, () -> Players.getLocal().isAnimating(), 8000);
		});
	}

	private static final WidgetAddress TELEPORT_POPUP = new WidgetAddress(187, 3);

	private static Teleport createJewelryPopupTeleport(Position destination, String target, int... ids) {
		return new Teleport(destination, () -> {

			var item = Inventory.getFirst(ids);

			if (item != null) {

				var popup = TELEPORT_POPUP.resolve();

				if (Widgets.isVisible(popup)) {

					var children = popup.getChildren();

					if (children == null) {
						log.warn("Teleport popup children are null");
						Time.sleepTick();
						return false;
					}

					for (int i = 0; i < children.length; i++) {

						var teleportItem = children[i];

						if (!teleportItem.getText().contains(target)) {
							continue;
						}

						Keyboard.sendText(String.valueOf((i + 1)), true);
						return Time.sleepUntil(() -> destination.distance() < 10, () -> Players.getLocal().isAnimating(), 8000);
					}
				}

				if(item.interact("Rub")) {
					log.warn("Failed to rub {}", item.getName());
					Time.sleepTick();
					return false;
				}

				Time.sleepTick();
				return true;
			}

			var equipped = Equipment.getFirst(ids);
			if (equipped == null) {
				log.warn("Unable to find teleport item");
				Time.sleepTick();
				return false;
			}

			return equipped.interact(target) && Time.sleepUntil(() -> destination.distance() < 10, () -> Players.getLocal().isMoving(), 8000);
		});
	}

	public static void jewelryPopupTeleport(String target, int... ids) {


	}

	public static Teleport createItemTeleport(TeleportItem teleportItem) {
		return new Teleport(teleportItem.getDestination(), () ->
		{
			var item = Inventory.getFirst(teleportItem.getItemIds());

			if (item == null) {
				log.warn("Unable to find teleport item");
				Time.sleepTick();
				return false;
			}

			return item.interact(teleportItem.getAction()) && Time.sleepUntil(() -> teleportItem.getDestination().distance() < 10, () -> Players.getLocal().isMoving(), 8000);
		});
	}

	public static Teleport createJewelryWildernessTeleport(Position destination, String target, int... ids) {
		return new Teleport(destination, () -> {

			if(isWildernessTeleportWarningVisible()) {

				if(!Dialog.chooseOption(1)) {
					log.warn("Failed to select option {}", 1);
					Time.sleepTick();
					return false;
				}

				return Time.sleepUntil(() -> destination.distance() < 10, () -> Players.getLocal().isAnimating(), 8000);
			}

			var item = Inventory.getFirst(ids);

			if (item != null) {

				if (!Dialog.isViewingOptions()) {

					if(!item.interact("Rub") || !Time.sleepUntil(Dialog::isViewingOptions, 1200)){
						log.warn("Failed to rub " + item.getName());
						Time.sleepTick();
						return false;
					}

					Time.sleepTick();
					return true;
				}

				if(!Dialog.chooseOption(target) || !Time.sleepUntil(TeleportLoader::isWildernessTeleportWarningVisible, 1200)) {
					log.warn("Failed to choose option {}", target);
					Time.sleepTick();
					return false;
				}

				Time.sleepTick();
				return true;
			}

			var equipped = Equipment.getFirst(ids);

			if (equipped == null) {
				log.warn("Unable to find teleportation item");
				return false;
			}

			return equipped.interact(target) && Time.sleepUntil(TeleportLoader::isWildernessTeleportWarningVisible, 1200);
		});

	}

	private static boolean isWildernessTeleportWarningVisible() {
		return Dialog.isViewingOptions() && Dialog.getOptions().stream()
				.anyMatch(o ->  WILDY_PATTERN.matcher(o).matches());
	}

	public static Teleport createSlayerRingTeleport(Position destination, String target, int... ids) {
		return new Teleport(destination, () -> {
			var item = Inventory.getFirst(ids);

			if(item == null) {
				item = Equipment.getFirst(ids);
			}

			if(item == null) {
				log.warn("Unable to find teleport item");
				Time.sleepTick();
				return false;
			}

			if (!Dialog.isViewingOptions()) {

				if (!item.interact("Teleport") || !Time.sleepUntil(Dialog::isViewingOptions, 1200)) {
					log.warn("Failed to use teleport item");
					Time.sleepTick();
					return false;
				}

				Time.sleepTick();
				return true;
			}

			if (Dialog.hasOption("Teleport")) {
				return Dialog.chooseOption("Teleport") && Time.sleepUntil(() -> destination.distance() < 10, () -> Players.getLocal().isAnimating(), 8000);
			}

			return Dialog.chooseOption(target) && Time.sleepUntil(() -> destination.distance() < 10, () -> Players.getLocal().isAnimating(), 8000);
		});
	}

	public static boolean ringOfDueling() {

		return Inventory.getFirst(RING_OF_DUELING) != null
				|| (Equipment.getFirst(RING_OF_DUELING) != null);
	}

	public static boolean gamesNecklace() {

		return Inventory.getFirst(GAMES_NECKLACE) != null
				|| (Equipment.getFirst(GAMES_NECKLACE) != null);
	}

	public static boolean combatBracelet() {

		return Inventory.getFirst(COMBAT_BRACELET) != null
				|| (Equipment.getFirst(COMBAT_BRACELET) != null);
	}

	public static boolean skillsNecklace() {

		return Inventory.getFirst(SKILLS_NECKLACE) != null
				|| (Equipment.getFirst(SKILLS_NECKLACE) != null);
	}

	public static boolean ringOfWealth() {

		return Inventory.getFirst(RING_OF_WEALTH) != null
				|| (Equipment.getFirst(RING_OF_WEALTH) != null);
	}

	public static boolean amuletOfGlory() {

		return Inventory.getFirst(AMULET_OF_GLORY) != null
				|| (Equipment.getFirst(AMULET_OF_GLORY) != null);
	}

	public static boolean necklaceOfPassage() {

		return Inventory.getFirst(NECKLACE_OF_PASSAGE) != null
				|| (Equipment.getFirst(NECKLACE_OF_PASSAGE) != null);
	}

	public static boolean xericsTalisman() {

		return Inventory.getFirst(XERICS_TALISMAN) != null
				|| (Equipment.getFirst(XERICS_TALISMAN) != null);
	}

	public static boolean slayerRing() {

		return Inventory.getFirst(SLAYER_RING) != null
				|| (Equipment.getFirst(SLAYER_RING) != null);
	}

	public static boolean digsitePendant() {

		return Inventory.getFirst(DIGSITE_PENDANT) != null
				|| (Equipment.getFirst(DIGSITE_PENDANT) != null);
	}

	public static boolean burningAmulet() {

		return Inventory.getFirst(BURNING_AMULET) != null
				|| Equipment.getFirst(BURNING_AMULET) != null;
	}
}
