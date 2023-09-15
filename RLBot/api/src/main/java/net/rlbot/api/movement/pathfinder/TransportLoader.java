package net.rlbot.api.movement.pathfinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.common.math.Distance;
import net.rlbot.api.movement.Reachable;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.api.widgets.WidgetInfo;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.game.Vars;
import net.rlbot.api.game.Worlds;
import net.rlbot.api.items.Equipment;
import net.rlbot.api.items.Inventory;
import net.rlbot.api.movement.Movement;
import net.rlbot.api.movement.Position;
import net.rlbot.api.movement.pathfinder.model.FairyRingLocation;
import net.rlbot.api.movement.pathfinder.model.Transport;
import net.rlbot.api.movement.pathfinder.model.dto.TransportDto;
import net.rlbot.api.movement.pathfinder.model.requirement.Requirements;
import net.rlbot.api.quest.QuestState;
import net.rlbot.api.quest.Quests;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.SceneObjects;
import net.runelite.api.NpcID;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.rlbot.api.movement.pathfinder.model.MovementConstants.*;


@Slf4j
public class TransportLoader
{
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static final List<Transport> ALL_STATIC_TRANSPORTS = new ArrayList<>();
	private static final List<Transport> LAST_TRANSPORT_LIST = new ArrayList<>();

	public static void init()
	{
		log.info("Loading transports");
		try (InputStream stream = Walker.class.getResourceAsStream("/transports.json"))
		{
			if (stream == null)
			{
				log.error("Failed to load transports.");
				return;
			}

			TransportDto[] json = GSON.fromJson(new String(stream.readAllBytes()), TransportDto[].class);

			List<Transport> list = Arrays.stream(json)
					.map(TransportDto::toTransport)
					.collect(Collectors.toList());
			ALL_STATIC_TRANSPORTS.addAll(list);
		}
		catch (IOException e)
		{
			log.error("Failed to load transports.", e);
		}

		log.info("Loaded {} transports", ALL_STATIC_TRANSPORTS.size());
	}

	public static List<Transport> buildTransports()
	{
		return LAST_TRANSPORT_LIST;
	}

	public static void refreshTransports()
	{
		List<Transport> filteredStatic = ALL_STATIC_TRANSPORTS.stream()
				.filter(it -> it.getRequirements().fulfilled())
				.collect(Collectors.toList());

		List<Transport> transports = new ArrayList<>();

		int gold = Inventory.getFirst(995) != null ? Inventory.getFirst(995).getQuantity() : 0;

		if (gold >= 30)
		{
			if (Quests.isFinished(Quest.PIRATES_TREASURE))
			{
				transports.add(npcTransport(new Position(3027, 3218, 0), new Position(2956, 3143, 1), 3644, "Pay-fare"));
				transports.add(npcTransport(new Position(2954, 3147, 0), new Position(3032, 3217, 1), 3648, "Pay-Fare"));
			}
			else
			{
				transports.add(npcDialogTransport(new Position(3027, 3218, 0), new Position(2956, 3143, 1), 3644, "Yes please."));
				transports.add(npcDialogTransport(new Position(2954, 3147, 0), new Position(3032, 3217, 1), 3648, "Can I journey on this ship?", "Search away, I have nothing to hide.", "Ok"));
			}
		}

		if (Worlds.isInMemberWorld())
		{
			//Shamans
			transports.add(objectTransport(new Position(1312, 3685, 0), new Position(1312, 10086, 0), 34405, "Enter"));
			/**
			 * Doors for shamans
			 */
			transports.add(objectTransport(new Position(1293, 10090, 0), new Position(1293, 10093, 0), 34642, "Pass"));
			transports.add(objectTransport(new Position(1293, 10093, 0), new Position(1293, 10091, 0), 34642, "Pass"));
			transports.add(objectTransport(new Position(1296, 10096, 0), new Position(1298, 10096, 0), 34642, "Pass"));
			transports.add(objectTransport(new Position(1298, 10096, 0), new Position(1296, 10096, 0), 34642, "Pass"));
			transports.add(objectTransport(new Position(1307, 10096, 0), new Position(1309, 10096, 0), 34642, "Pass"));
			transports.add(objectTransport(new Position(1309, 10096, 0), new Position(1307, 10096, 0), 34642, "Pass"));
			transports.add(objectTransport(new Position(1316, 10096, 0), new Position(1318, 10096, 0), 34642, "Pass"));
			transports.add(objectTransport(new Position(1318, 10096, 0), new Position(1316, 10096, 0), 34642, "Pass"));
			transports.add(objectTransport(new Position(1324, 10096, 0), new Position(1326, 10096, 0), 34642, "Pass"));
			transports.add(objectTransport(new Position(1326, 10096, 0), new Position(1324, 10096, 0), 34642, "Pass"));

			// Crabclaw island
			if (gold >= 10_000)
			{
				transports.add(npcTransport(new Position(1782, 3458, 0), new Position(1778, 3417, 0), 7483, "Travel"));
			}

			transports.add(npcTransport(new Position(1779, 3418, 0), new Position(1784, 3458, 0), 7484, "Travel"));

			// Port sarim
			if (Vars.getBit(4897) == 0)
			{
				if (Vars.getBit(8063) >= 7)
				{
					transports.add(npcDialogTransport(new Position(3054, 3245, 0),
							new Position(1824, 3691, 0),
							8484,
							"Can you take me to Great Kourend?"));
				}
				else
				{
					transports.add(npcDialogTransport(new Position(3054, 3245, 0),
							new Position(1824, 3691, 0),
							8484,
							"That's great, can you take me there please?"));
				}
			}
			else
			{
				transports.add(npcTransport(new Position(3054, 3245, 0),
						new Position(1824, 3695, 1),
						"Veos",
						"Port Piscarilius"));
			}

			if (Quests.getState(Quest.LUNAR_DIPLOMACY) != QuestState.NOT_STARTED)
			{
				transports.add(npcTransport(new Position(2222, 3796, 2), new Position(2130, 3899, 2), NpcID.CAPTAIN_BENTLEY_6650, "Travel"));
				transports.add(npcTransport(new Position(2130, 3899, 2), new Position(2222, 3796, 2), NpcID.CAPTAIN_BENTLEY_6650, "Travel"));
			}

			if (Quests.isFinished(Quest.THE_LOST_TRIBE))
			{
				transports.add(npcTransport(new Position(3229, 9610, 0), new Position(3316, 9613, 0), NpcID.KAZGAR_7301, "Mines"));
				transports.add(npcTransport(new Position(3316, 9613, 0), new Position(3229, 9610, 0), NpcID.MISTAG_7299, "Cellar"));
			}

			// Tree Gnome Village
			if (Quests.getState(Quest.TREE_GNOME_VILLAGE) != QuestState.NOT_STARTED)
			{
				transports.add(npcTransport(new Position(2504, 3192, 0), new Position(2515, 3159, 0), 4968, "Follow"));
				transports.add(npcTransport(new Position(2515, 3159, 0), new Position(2504, 3192, 0), 4968, "Follow"));
			}

			// Eagles peak cave
			if (Vars.getVarp(934) >= 15)
			{
				// Entrance
				transports.add(objectTransport(new Position(2328, 3496, 0), new Position(1994, 4983, 3), 19790,
						"Enter"));
				transports.add(objectTransport(new Position(1994, 4983, 3), new Position(2328, 3496, 0), 19891,
						"Exit"));
			}

			// Waterbirth island
			if (Quests.isFinished(Quest.THE_FREMENNIK_TRIALS) || gold >= 1000)
			{
				transports.add(npcTransport(new Position(2544, 3760, 0), new Position(2620, 3682, 0), 10407, "Rellekka"));
				transports.add(npcTransport(new Position(2620, 3682, 0), new Position(2547, 3759, 0), 5937, "Waterbirth Island"));
			}

			// Pirates cove
			transports.add(npcTransport(new Position(2620, 3692, 0), new Position(2213, 3794, 0), NpcID.LOKAR_SEARUNNER, "Pirate's Cove"));
			transports.add(npcTransport(new Position(2213, 3794, 0), new Position(2620, 3692, 0), NpcID.LOKAR_SEARUNNER_9306, "Rellekka"));

			// Corsair's Cove
			if (Skills.getBoostedLevel(Skill.AGILITY) >= 10)
			{
				transports.add(objectTransport(new Position(2546, 2871, 0), new Position(2546, 2873, 0), 31757,
						"Climb"));
				transports.add(objectTransport(new Position(2546, 2873, 0), new Position(2546, 2871, 0), 31757,
						"Climb"));
			}

			// Lumbridge castle dining room, ignore if RFD is in progress.
			if (Quests.getState(Quest.RECIPE_FOR_DISASTER) != QuestState.IN_PROGRESS)
			{
				transports.add(objectTransport(new Position(3213, 3221, 0), new Position(3212, 3221, 0), 12349, "Open"));
				transports.add(objectTransport(new Position(3212, 3221, 0), new Position(3213, 3221, 0), 12349, "Open"));
				transports.add(objectTransport(new Position(3213, 3222, 0), new Position(3212, 3222, 0), 12350, "Open"));
				transports.add(objectTransport(new Position(3212, 3222, 0), new Position(3213, 3222, 0), 12350, "Open"));
				transports.add(objectTransport(new Position(3207, 3218, 0), new Position(3207, 3217, 0), 12348, "Open"));
				transports.add(objectTransport(new Position(3207, 3217, 0), new Position(3207, 3218, 0), 12348, "Open"));
			}

			// Digsite gate
			if (Vars.getBit(3637) >= 153)
			{
				transports.add(objectTransport(new Position(3295, 3429, 0), new Position(3296, 3429, 0), 24561,
						"Open"));
				transports.add(objectTransport(new Position(3296, 3429, 0), new Position(3295, 3429, 0), 24561,
						"Open"));
				transports.add(objectTransport(new Position(3295, 3428, 0), new Position(3296, 3428, 0), 24561,
						"Open"));
				transports.add(objectTransport(new Position(3296, 3428, 0), new Position(3295, 3428, 0), 24561,
						"Open"));
			}
		}

		// Entrana
		transports.add(npcTransport(new Position(3041, 3237, 0), new Position(2834, 3331, 1), 1166, "Take-boat"));
		transports.add(npcTransport(new Position(2834, 3335, 0), new Position(3048, 3231, 1), 1170, "Take-boat"));
		transports.add(npcDialogTransport(new Position(2821, 3374, 0),
				new Position(2822, 9774, 0),
				1164,
				"Well that is a risk I will have to take."));

		// Fossil Island
		transports.add(npcTransport(new Position(3362, 3445, 0),
				new Position(3724, 3808, 0),
				8012,
				"Quick-Travel"));

		transports.add(objectDialogTransport(new Position(3724, 3808, 0),
				new Position(3362, 3445, 0),
				30914,
				new String[]{"Travel"},
				"Row to the barge and travel to the Digsite."));

		// Gnome stronghold
		transports.add(objectDialogTransport(new Position(2461, 3382, 0),
				new Position(2461, 3385, 0),
				190,
				new String[]{"Open"},
				"Sorry, I'm a bit busy."));

		// Paterdomus
		transports.add(trapDoorTransport(new Position(3405, 3506, 0), new Position(3405, 9906, 0), 1579, 1581));
		transports.add(trapDoorTransport(new Position(3423, 3485, 0), new Position(3440, 9887, 0), 3432, 3433));
		transports.add(trapDoorTransport(new Position(3422, 3484, 0), new Position(3440, 9887, 0), 3432, 3433));

		// Port Piscarilius
		transports.add(npcTransport(new Position(1824, 3691, 0), new Position(3055, 3242, 1), 10727, "Port Sarim"));

		// Glarial's tomb
		transports.add(itemUseTransport(new Position(2557, 3444, 0), new Position(2555, 9844, 0), 294, 1992));
		transports.add(itemUseTransport(new Position(2557, 3445, 0), new Position(2555, 9844, 0), 294, 1992));
		transports.add(itemUseTransport(new Position(2558, 3443, 0), new Position(2555, 9844, 0), 294, 1992));
		transports.add(itemUseTransport(new Position(2559, 3443, 0), new Position(2555, 9844, 0), 294, 1992));
		transports.add(itemUseTransport(new Position(2560, 3444, 0), new Position(2555, 9844, 0), 294, 1992));
		transports.add(itemUseTransport(new Position(2560, 3445, 0), new Position(2555, 9844, 0), 294, 1992));
		transports.add(itemUseTransport(new Position(2558, 3446, 0), new Position(2555, 9844, 0), 294, 1992));
		transports.add(itemUseTransport(new Position(2559, 3446, 0), new Position(2555, 9844, 0), 294, 1992));

		// Waterfall Island
		transports.add(itemUseTransport(new Position(2512, 3476, 0), new Position(2513, 3468, 0), 954, 1996));
		transports.add(itemUseTransport(new Position(2512, 3466, 0), new Position(2511, 3463, 0), 954, 2020));

		// Edgeville Dungeon
		transports.add(trapDoorTransport(new Position(3096, 3468, 0), new Position(3096, 9867, 0), 1579, 1581));

		// Varrock Castle manhole
		transports.add(trapDoorTransport(new Position(3237, 3459, 0), new Position(3237, 9859, 0), 881, 882));

		if (Inventory.contains(SLASH_ITEMS) || Equipment.contains(SLASH_ITEMS))
		{
			for (var pair : SLASH_WEB_POINTS)
			{
				transports.add(slashWebTransport(pair[0], pair[1]));
				transports.add(slashWebTransport(pair[1], pair[0]));
			}
		}

		LAST_TRANSPORT_LIST.clear();
		LAST_TRANSPORT_LIST.addAll(filteredStatic);
		LAST_TRANSPORT_LIST.addAll(transports);
	}

	public static Transport trapDoorTransport(
			Position source,
			Position destination,
			int closedId,
			int openedId
	)
	{
		return new Transport(source, destination, Integer.MAX_VALUE, 0, () ->
		{
			var openedTrapdoor = SceneObjects.getNearest(source, openedId);

			if (openedTrapdoor != null)
			{
				return openedTrapdoor.interact(Predicates.always()) &&
						Time.sleepUntil(() -> Reachable.getCost(destination) < 5, () -> Players.getLocal().isMoving(), 2400);
			}

			var closedTrapDoor = SceneObjects.getNearest(source, closedId);

			if (closedTrapDoor != null)
			{
				return closedTrapDoor.interact(Predicates.always()) &&
						Time.sleepUntil(() -> SceneObjects.getNearest(source, openedId) != null, () -> Players.getLocal().isMoving(), 2400);
			}

			log.warn("No trapdoor found at {}", source);
			return false;
		});
	}

	public static Transport fairyRingTransport(
			FairyRingLocation source,
			FairyRingLocation destination
	)
	{
		return new Transport(source.getPosition(), destination.getPosition(), Integer.MAX_VALUE, 0, () ->
		{
			log.debug("Looking for fairy ring at {} to {}", source.getPosition(), destination.getPosition());
			var ring = SceneObjects.getNearest(source.getPosition(), "Fairy ring");

			if (ring == null)
			{
				log.debug("Fairy ring at {} is null", source.getPosition());
				return false;
			}

			if (destination == FairyRingLocation.ZANARIS)
			{
				return ring.interact("Zanaris") &&
						Time.sleepUntil(() -> destination.getPosition().distance() < 5, 2400);
			}

			if (ring.hasAction(a -> a != null && a.contains(destination.getCode())))
			{
				return ring.interact(a -> a != null && a.contains(destination.getCode()))  &&
						Time.sleepUntil(() -> destination.getPosition().distance() < 5, 2400);
			}

			if (Widgets.isVisible(WidgetInfo.FAIRY_RING))
			{
				return destination.travel();
			}

			return ring.interact("Configure") && Time.sleepUntil(() -> Widgets.isVisible(WidgetInfo.FAIRY_RING), 2400);
		});
	}

	public static Transport itemUseTransport(
			Position source,
			Position destination,
			int itemId,
			int objId
	)
	{
		return new Transport(source, destination, Integer.MAX_VALUE, 0, () ->
		{
			var item = Inventory.getFirst(itemId);

			if (item == null)
			{
				log.warn("No item found with id {}", itemId);
				return false;
			}

			var transport = SceneObjects.getNearest(source, objId);

			if (transport == null) {
				log.warn("No transport found with id {}", objId);
				return false;
			}

			return item.useOn(transport) &&
					Time.sleepUntil(() -> Reachable.getCost(destination) < 5, () -> Players.getLocal().isMoving(), 2400);
		});
	}

	public static Transport npcTransport(
			Position source,
			Position destination,
			int npcId,
			String... actions
	)
	{
		return new Transport(source, destination, 10, 0, () ->
		{
			var npc = Npcs.getNearest(x -> x.distanceTo(source) <= 10 && x.getId() == npcId);

			if (npc == null) {
				log.warn("No npc found with id {}", npcId);
				return false;
			}

			return npc.interact(actions) && Time.sleepUntil(() -> Reachable.getCost(destination) < 5, () -> Players.getLocal().isMoving(), 2400);
		});
	}

	public static Transport npcTransport(
			Position source,
			Position destination,
			String npcName,
			String... actions
	)
	{
		return new Transport(source, destination, 10, 0, () ->
		{
			var npc = Npcs.getNearest(x -> x.distanceTo(source) <= 10 && x.getName().equalsIgnoreCase(npcName));

			if (npc == null) {
				log.warn("No npc found with name {}", npcName);
				return false;
			}

			return npc.interact(actions) && Time.sleepUntil(() -> Reachable.getCost(destination) < 5, () -> Players.getLocal().isMoving(), 2400);
		});
	}

	public static Transport npcDialogTransport(
			Position source,
			Position destination,
			int npcId,
			String... chatOptions
	)
	{
		return new Transport(source, destination, 10, 0, () ->
		{
			if (Dialog.canContinue())
			{
				Dialog.continueSpace();
				return true;
			}

			if (Dialog.isViewingOptions())
			{
				return Dialog.chooseOption(chatOptions) && Time.sleepUntil(() -> Reachable.getCost(destination) < 5, () -> Players.getLocal().isMoving(), 2400);
			}

			var npc = Npcs.getNearest(x -> x.distanceTo(source) <= 10 && x.getId() == npcId);

			if (npc == null) {
				log.warn("No npc found with id {}", npcId);
				return false;
			}

			return npc.interact(Predicates.always()) && Time.sleepUntil(Dialog::isOpen, 2400);
		});
	}

	public static Transport objectTransport(
			Position source,
			Position destination,
			int objId,
			String... actions
	)
	{
		return new Transport(source, destination, Integer.MAX_VALUE, 0, () ->
		{
			var first = SceneObjects.getAt(source, objId).stream().findFirst().orElse(null);

			if (first != null)
			{
				return first.interact(actions) && Time.sleepUntil(() -> Reachable.getCost(destination) < 5, () -> Players.getLocal().isMoving(), 2400);
			}

			var nearest = SceneObjects.getNearest(source, x -> x.getId() == objId);

			if(nearest == null) {
				log.warn("Failed to find transport object {}", objId);
				return false;
			}

			return nearest.interact(actions) && Time.sleepUntil(() -> Reachable.getCost(destination) < 5, () -> Players.getLocal().isMoving(), 2400);
		});
	}

	public static Transport objectTransport(
			Position source,
			Position destination,
			int objId,
			String actions,
			Requirements requirements
	)
	{
		return new Transport(source, destination, Integer.MAX_VALUE, 0, () ->
		{
			var first = SceneObjects.getNearest(source, objId);

			if (first == null) {
				log.warn("Failed to find transport object {}", objId);
				return false;
			}

			if(!first.interact(actions)) {
				log.warn("Failed to interact with transport object {}", objId);
				Time.sleepTick();
				return false;
			}

			return Time.sleepUntil(() ->
					Reachable.getCost(destination) < 5 && !Players.getLocal().isAnimating() || Constants.WILDY_DITCH_WARNING.isWidgetVisible() || Dialog.isOpen(),
					() -> Players.getLocal().isMoving(),
					2400
			);

		}, requirements);
	}
	public static Transport objectDialogTransport(
			Position source,
			Position destination,
			int objId,
			String[] actions,
			String... chatOptions
	)
	{
		return new Transport(source, destination, Integer.MAX_VALUE, 0, () ->
		{
			if (Dialog.isOpen())
			{
				if (Dialog.canContinue())
				{
					Dialog.continueSpace();
					return true;
				}

				return Dialog.chooseOption(chatOptions) && Time.sleepUntil(() -> Reachable.getCost(destination) < 5, () -> Players.getLocal().isMoving(), 2400);
			}

			var transport = SceneObjects.getNearest(source, objId);

			if (transport == null) {
				log.warn("Failed to find transport object {}", objId);
				return false;
			}

			return transport.interact(actions) && Time.sleepUntil(Dialog::isOpen, () -> Players.getLocal().isMoving(), 2400);
		});
	}

	public static Transport slashWebTransport(
			Position source,
			Position destination
	)
	{
		return new Transport(source, destination, Integer.MAX_VALUE, 0, () ->
		{
			var transport = SceneObjects.getNearest(source, it -> it.getName() != null && it.getName().contains("Web") && it.hasAction("Slash"));

			if (transport == null)
			{
				log.warn("Unable to find transport: Web");
				return false;
			}

			if(!transport.interact("Slash") || !Time.sleepUntil(() -> transport.distance(Distance.CHEBYSHEV) <= 1, () -> Players.getLocal().isMoving(), 1800)) {
				return false;
			}

			Time.sleepTick();
			return true;
		});
	}
}
