package net.rlbot.api.widgets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.Game;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.*;
import net.rlbot.api.movement.Position;
import net.rlbot.api.quest.Quest;
import net.rlbot.api.quest.Quests;
import net.rlbot.api.scene.Players;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Slf4j
public class Minigames
{
    private static final Supplier<Widget> MINIGAMES_TAB_BUTTON = () -> Widgets.get(707, 6);
    private static final Supplier<Widget> MINIGAMES_DESTINATION = () -> Widgets.get(76, 11);

    private static final Set<Quest> NMZ_QUESTS = Set.of(
            Quest.THE_ASCENT_OF_ARCEUUS,
            Quest.CONTACT,
            Quest.THE_CORSAIR_CURSE,
            Quest.THE_DEPTHS_OF_DESPAIR,
            Quest.DESERT_TREASURE,
            Quest.DRAGON_SLAYER_I,
            Quest.DREAM_MENTOR,
            Quest.FAIRYTALE_I__GROWING_PAINS,
            Quest.FAMILY_CREST,
            Quest.FIGHT_ARENA,
            Quest.THE_FREMENNIK_ISLES,
            Quest.GETTING_AHEAD,
            Quest.THE_GRAND_TREE,
            Quest.THE_GREAT_BRAIN_ROBBERY,
            Quest.GRIM_TALES,
            Quest.HAUNTED_MINE,
            Quest.HOLY_GRAIL,
            Quest.HORROR_FROM_THE_DEEP,
            Quest.IN_SEARCH_OF_THE_MYREQUE,
            Quest.LEGENDS_QUEST,
            Quest.LOST_CITY,
            Quest.LUNAR_DIPLOMACY,
            Quest.MONKEY_MADNESS_I,
            Quest.MOUNTAIN_DAUGHTER,
            Quest.MY_ARMS_BIG_ADVENTURE,
            Quest.ONE_SMALL_FAVOUR,
            Quest.RECIPE_FOR_DISASTER,
            Quest.ROVING_ELVES,
            Quest.SHADOW_OF_THE_STORM,
            Quest.SHILO_VILLAGE,
            Quest.SONG_OF_THE_ELVES,
            Quest.TALE_OF_THE_RIGHTEOUS,
            Quest.TREE_GNOME_VILLAGE,
            Quest.TROLL_ROMANCE,
            Quest.TROLL_STRONGHOLD,
            Quest.VAMPYRE_SLAYER,
            Quest.WHAT_LIES_BELOW,
            Quest.WITCHS_HOUSE
    );

    public static boolean canTeleport()
    {
        return getLastMinigameTeleportUsage().plus(20, ChronoUnit.MINUTES).isBefore(Instant.now());
    }

    public static boolean teleport(Destination destination)
    {
        if (!canTeleport())
        {
            log.warn("Tried to minigame teleport, but it's on cooldown.");
            return false;
        }

        var minigamesTeleportButton = Widgets.get(WidgetInfo.MINIGAME_TELEPORT_BUTTON);

        var teleportGraphics = List.of(800, 802, 803, 804);

        if(!isOpen()) {
            return open();
        }

        if (isOpen() && minigamesTeleportButton != null)
        {
            if (Destination.getCurrent() != destination)
            {
                Game.runScript(124, destination.index);
                return true;
            }

            var button = minigamesTeleportButton.getChild(destination.index);

            if (!Widgets.isVisible(button)) {
                log.warn("Unable to find teleport button");
                return false;
            }

            return button.interact(Predicates.textContains("Teleport to")) &&
                    Time.sleepUntil(() -> destination.getLocation().distance() < 20, () -> Players.getLocal().isAnimating(), 8000);
        }

        return open();
    }

    public static boolean open()
    {
        if (!isTabOpen())
        {
            Tabs.open(Tab.CHAT);
            return true;
        }

        if (!isOpen())
        {
            Widget widget = MINIGAMES_TAB_BUTTON.get();
            if (Widgets.isVisible(widget))
            {
                return widget.interact("Grouping");
            }
        }

        return isOpen();
    }

    public static boolean isOpen()
    {
        return Widgets.isVisible(WidgetInfo.MINIGAME_TELEPORT_BUTTON);
    }

    public static boolean isTabOpen()
    {
        return Tabs.isOpen(Tab.CHAT);
    }

    public static Instant getLastMinigameTeleportUsage()
    {
        return Instant.ofEpochSecond(Vars.getVarp(VarPlayer.LAST_MINIGAME_TELEPORT.getId()) * 60L);
    }

    @Getter
    @AllArgsConstructor
    public enum Destination
    {
        BARBARIAN_ASSAULT(1, "Barbarian Assault", new Position(2531, 3577, 0), false),
        BLAST_FURNACE(2, "Blast Furnace", new Position(2933, 10183, 0), true),
        BURTHORPE_GAMES_ROOM(3, "Burthorpe Games Room", new Position(2208, 4938, 0), true),
        CASTLE_WARS(4, "Castle Wars", new Position(2439, 3092, 0), false),
        CLAN_WARS(5, "Clan Wars", new Position(3151, 3636, 0), false),
        DAGANNOTH_KINGS(6, "Dagannoth Kings", null, true),
        FISHING_TRAWLER(7, "Fishing Trawler", new Position(2658, 3158, 0), true),
        GIANTS_FOUNDARY(8, "Giants' Foundary", new Position(3361, 3147, 0), true),
        GOD_WARS(9, "God Wars", null, true),
        GUARDIANS_OF_THE_RIFT(10, "Guardians of the Rift", new Position(3616, 9478, 0), true),
        LAST_MAN_STANDING(11, "Last Man Standing", new Position(3149, 3635, 0), false),
        NIGHTMARE_ZONE(12, "Nightmare Zone", new Position(2611, 3121, 0), true),
        PEST_CONTROL(13, "Pest Control", new Position(2653, 2655, 0), true),
        PLAYER_OWNED_HOUSES(14, "Player Owned Houses", null, false),
        RAT_PITS(15, "Rat Pits", new Position(3263, 3406, 0), true),
        SHADES_OF_MORTTON(16, "Shades of Mort'ton", new Position(3500, 3300, 0), true),
        SHIELD_OF_ARRAV(17, "Shield of Arrav", null, true),
        SHOOTING_STARS(18, "Shooting Stars", null, true),
        SOUL_WARS(19, "Soul Wars", new Position(2209, 2857, 0), true),
        THEATRE_OF_BLOOD(20, "Theatre of Blood", null, true),
        TITHE_FARM(21, "Tithe Farm", new Position(1793, 3501, 0), true),
        TROUBLE_BREWING(22, "Trouble Brewing", new Position(3811, 3021, 0), true),
        TZHAAR_FIGHT_PIT(23, "TzHaar Fight Pit", new Position(2402, 5181, 0), true),
        VOLCANIC_MINE(24, "Volcanic Mine", null, true),
        NONE(-1, "None", null, false);

        private final int index;
        private final String name;
        private final Position location;
        private final boolean members;

        public boolean canUse()
        {
            if (!hasDestination())
            {
                return false;
            }

            if (members && !Worlds.isInMemberWorld())
            {
                return false;
            }

            return switch (this) {
                case BURTHORPE_GAMES_ROOM,
                        CASTLE_WARS,
                        CLAN_WARS,
                        LAST_MAN_STANDING,
                        SOUL_WARS,
                        TZHAAR_FIGHT_PIT ->
                        true;
                case GIANTS_FOUNDARY -> Quests.isFinished(Quest.SLEEPING_GIANTS);
                case BARBARIAN_ASSAULT -> Vars.getBit(3251) >= 1;
                case BLAST_FURNACE -> Vars.getBit(575) >= 1;
                case FISHING_TRAWLER -> Skills.getLevel(Skill.FISHING) >= 15;
                case GUARDIANS_OF_THE_RIFT -> Quests.isFinished(Quest.TEMPLE_OF_THE_EYE);
                case NIGHTMARE_ZONE -> NMZ_QUESTS.stream().filter(Quests::isFinished).count() >= 5;
                case PEST_CONTROL -> Players.getLocal().getCombatLevel() >= 40;
                case RAT_PITS -> Quests.isFinished(Quest.RATCATCHERS);
                case SHADES_OF_MORTTON -> Quests.isFinished(Quest.SHADES_OF_MORTTON);
                case TROUBLE_BREWING -> Quests.isFinished(Quest.CABIN_FEVER) && Skills.getLevel(Skill.COOKING) >= 40;
                case TITHE_FARM -> false;
                default ->
//					return Skills.getLevel(Skill.FARMING) >= 34 && (Vars.getBit(Varbits.KOUREND_FAVOR_HOSIDIUS) / 10) >= 100;
                        false;
            };
        }

        public boolean hasDestination()
        {
            return location != null;
        }

        public static Destination getCurrent()
        {
            Widget selectedTeleport = MINIGAMES_DESTINATION.get();

            if(!Widgets.isVisible(selectedTeleport)) {
                return NONE;
            }

            return byName(selectedTeleport.getText());
        }

        public static Destination byName(String name)
        {
            return Arrays.stream(values())
                    .filter(x -> x.getName().equals(name))
                    .findFirst()
                    .orElse(NONE);
        }
    }
}