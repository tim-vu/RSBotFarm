package net.rlbot.api.quest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;

@RequiredArgsConstructor
public enum Quest
{
    //Free Quests
    BELOW_ICE_MOUNTAIN(net.runelite.api.Quest.BELOW_ICE_MOUNTAIN, QuestVarbits.QUEST_BELOW_ICE_MOUNTAIN, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    BLACK_KNIGHTS_FORTRESS(net.runelite.api.Quest.BLACK_KNIGHTS_FORTRESS, QuestVarPlayer.QUEST_BLACK_KNIGHTS_FORTRESS, QuestDetails.Type.F2P, QuestDetails.Difficulty.INTERMEDIATE),
    COOKS_ASSISTANT(net.runelite.api.Quest.COOKS_ASSISTANT, QuestVarPlayer.QUEST_COOKS_ASSISTANT, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    THE_CORSAIR_CURSE(net.runelite.api.Quest.THE_CORSAIR_CURSE, QuestVarbits.QUEST_THE_CORSAIR_CURSE, QuestDetails.Type.F2P, QuestDetails.Difficulty.INTERMEDIATE),
    DEMON_SLAYER(net.runelite.api.Quest.DEMON_SLAYER, QuestVarbits.QUEST_DEMON_SLAYER, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    DORICS_QUEST(net.runelite.api.Quest.DORICS_QUEST, QuestVarPlayer.QUEST_DORICS_QUEST, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    DRAGON_SLAYER_I(net.runelite.api.Quest.DRAGON_SLAYER_I, QuestVarPlayer.QUEST_DRAGON_SLAYER_I, QuestDetails.Type.F2P, QuestDetails.Difficulty.EXPERIENCED),
    ERNEST_THE_CHICKEN(net.runelite.api.Quest.ERNEST_THE_CHICKEN, QuestVarPlayer.QUEST_ERNEST_THE_CHICKEN, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    GOBLIN_DIPLOMACY(net.runelite.api.Quest.GOBLIN_DIPLOMACY, QuestVarbits.QUEST_GOBLIN_DIPLOMACY, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    IMP_CATCHER(net.runelite.api.Quest.IMP_CATCHER, QuestVarPlayer.QUEST_IMP_CATCHER, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    THE_KNIGHTS_SWORD(net.runelite.api.Quest.THE_KNIGHTS_SWORD, QuestVarPlayer.QUEST_THE_KNIGHTS_SWORD, QuestDetails.Type.F2P, QuestDetails.Difficulty.INTERMEDIATE),
    MISTHALIN_MYSTERY(net.runelite.api.Quest.MISTHALIN_MYSTERY, QuestVarbits.QUEST_MISTHALIN_MYSTERY, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    PIRATES_TREASURE(net.runelite.api.Quest.PIRATES_TREASURE, QuestVarPlayer.QUEST_PIRATES_TREASURE, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    PRINCE_ALI_RESCUE(net.runelite.api.Quest.PRINCE_ALI_RESCUE, QuestVarPlayer.QUEST_PRINCE_ALI_RESCUE, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    THE_RESTLESS_GHOST(net.runelite.api.Quest.THE_RESTLESS_GHOST, QuestVarPlayer.QUEST_THE_RESTLESS_GHOST, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    ROMEO__JULIET(net.runelite.api.Quest.ROMEO__JULIET, QuestVarPlayer.QUEST_ROMEO_AND_JULIET, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    RUNE_MYSTERIES(net.runelite.api.Quest.RUNE_MYSTERIES, QuestVarPlayer.QUEST_RUNE_MYSTERIES, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    SHEEP_SHEARER(net.runelite.api.Quest.SHEEP_SHEARER, QuestVarPlayer.QUEST_SHEEP_SHEARER, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    SHIELD_OF_ARRAV(net.runelite.api.Quest.SHIELD_OF_ARRAV, QuestVarPlayer.QUEST_SHIELD_OF_ARRAV, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    VAMPYRE_SLAYER(net.runelite.api.Quest.VAMPYRE_SLAYER, QuestVarPlayer.QUEST_VAMPYRE_SLAYER, QuestDetails.Type.F2P, QuestDetails.Difficulty.INTERMEDIATE),
    WITCHS_POTION(net.runelite.api.Quest.WITCHS_POTION, QuestVarPlayer.QUEST_WITCHS_POTION, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),
    X_MARKS_THE_SPOT(net.runelite.api.Quest.X_MARKS_THE_SPOT, QuestVarbits.QUEST_X_MARKS_THE_SPOT, QuestDetails.Type.F2P, QuestDetails.Difficulty.NOVICE),

    //Members' Quests
    ANIMAL_MAGNETISM(net.runelite.api.Quest.ANIMAL_MAGNETISM,  QuestVarbits.QUEST_ANIMAL_MAGNETISM, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    ANOTHER_SLICE_OF_HAM(net.runelite.api.Quest.ANOTHER_SLICE_OF_HAM, QuestVarbits.QUEST_ANOTHER_SLICE_OF_HAM, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    BENEATH_CURSED_SANDS(net.runelite.api.Quest.BENEATH_CURSED_SANDS, QuestVarbits.QUEST_BENEATH_CURSED_SANDS, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    BETWEEN_A_ROCK(net.runelite.api.Quest.BETWEEN_A_ROCK, QuestVarbits.QUEST_BETWEEN_A_ROCK, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    BIG_CHOMPY_BIRD_HUNTING(net.runelite.api.Quest.BIG_CHOMPY_BIRD_HUNTING, QuestVarPlayer.QUEST_BIG_CHOMPY_BIRD_HUNTING, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    BIOHAZARD(net.runelite.api.Quest.BIOHAZARD, QuestVarPlayer.QUEST_BIOHAZARD, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    CABIN_FEVER(net.runelite.api.Quest.CABIN_FEVER, QuestVarPlayer.QUEST_CABIN_FEVER, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    CLOCK_TOWER(net.runelite.api.Quest.CLOCK_TOWER, QuestVarPlayer.QUEST_CLOCK_TOWER, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    COLD_WAR(net.runelite.api.Quest.COLD_WAR, QuestVarbits.QUEST_COLD_WAR, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    CONTACT(net.runelite.api.Quest.CONTACT, QuestVarbits.QUEST_CONTACT, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    CREATURE_OF_FENKENSTRAIN(net.runelite.api.Quest.CREATURE_OF_FENKENSTRAIN, QuestVarPlayer.QUEST_CREATURE_OF_FENKENSTRAIN, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    DARKNESS_OF_HALLOWVALE(net.runelite.api.Quest.DARKNESS_OF_HALLOWVALE, QuestVarbits.QUEST_DARKNESS_OF_HALLOWVALE, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    DEATH_PLATEAU(net.runelite.api.Quest.DEATH_PLATEAU, QuestVarPlayer.QUEST_DEATH_PLATEAU, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    DEATH_TO_THE_DORGESHUUN(net.runelite.api.Quest.DEATH_TO_THE_DORGESHUUN, QuestVarbits.QUEST_DEATH_TO_THE_DORGESHUUN, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_DEPTHS_OF_DESPAIR(net.runelite.api.Quest.THE_DEPTHS_OF_DESPAIR, QuestVarbits.QUEST_THE_DEPTHS_OF_DESPAIR, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    DESERT_TREASURE(net.runelite.api.Quest.DESERT_TREASURE_I, QuestVarbits.QUEST_DESERT_TREASURE, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    DEVIOUS_MINDS(net.runelite.api.Quest.DEVIOUS_MINDS, QuestVarbits.QUEST_DEVIOUS_MINDS, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    THE_DIG_SITE(net.runelite.api.Quest.THE_DIG_SITE, QuestVarPlayer.QUEST_THE_DIG_SITE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    DRAGON_SLAYER_II(net.runelite.api.Quest.DRAGON_SLAYER_II, QuestVarbits.QUEST_DRAGON_SLAYER_II, QuestDetails.Type.P2P, QuestDetails.Difficulty.GRANDMASTER),
    DREAM_MENTOR(net.runelite.api.Quest.DREAM_MENTOR, QuestVarbits.QUEST_DREAM_MENTOR, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    DRUIDIC_RITUAL(net.runelite.api.Quest.DRUIDIC_RITUAL, QuestVarPlayer.QUEST_DRUIDIC_RITUAL, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    DWARF_CANNON(net.runelite.api.Quest.DWARF_CANNON, QuestVarPlayer.QUEST_DWARF_CANNON, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    EADGARS_RUSE(net.runelite.api.Quest.EADGARS_RUSE, QuestVarPlayer.QUEST_EADGARS_RUSE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    EAGLES_PEAK(net.runelite.api.Quest.EAGLES_PEAK, QuestVarbits.QUEST_EAGLES_PEAK, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    ELEMENTAL_WORKSHOP_I(net.runelite.api.Quest.ELEMENTAL_WORKSHOP_I, QuestVarPlayer.QUEST_ELEMENTAL_WORKSHOP_I, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    ELEMENTAL_WORKSHOP_II(net.runelite.api.Quest.ELEMENTAL_WORKSHOP_II, QuestVarbits.QUEST_ELEMENTAL_WORKSHOP_II, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    ENAKHRAS_LAMENT(net.runelite.api.Quest.ENAKHRAS_LAMENT, QuestVarbits.QUEST_ENAKHRAS_LAMENT, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    ENLIGHTENED_JOURNEY(net.runelite.api.Quest.ENLIGHTENED_JOURNEY, QuestVarbits.QUEST_ENLIGHTENED_JOURNEY, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_EYES_OF_GLOUPHRIE(net.runelite.api.Quest.THE_EYES_OF_GLOUPHRIE, QuestVarbits.QUEST_THE_EYES_OF_GLOUPHRIE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    FAIRYTALE_I__GROWING_PAINS(net.runelite.api.Quest.FAIRYTALE_I__GROWING_PAINS, QuestVarbits.QUEST_FAIRYTALE_I_GROWING_PAINS, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    FAIRYTALE_II__CURE_A_QUEEN(net.runelite.api.Quest.FAIRYTALE_II__CURE_A_QUEEN, QuestVarbits.QUEST_FAIRYTALE_II_CURE_A_QUEEN, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    FAMILY_CREST(net.runelite.api.Quest.FAMILY_CREST, QuestVarPlayer.QUEST_FAMILY_CREST, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    THE_FEUD(net.runelite.api.Quest.THE_FEUD, QuestVarbits.QUEST_THE_FEUD, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    FIGHT_ARENA(net.runelite.api.Quest.FIGHT_ARENA, QuestVarPlayer.QUEST_FIGHT_ARENA, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    FISHING_CONTEST(net.runelite.api.Quest.FISHING_CONTEST, QuestVarPlayer.QUEST_FISHING_CONTEST, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    FORGETTABLE_TALE(net.runelite.api.Quest.FORGETTABLE_TALE, QuestVarbits.QUEST_FORGETTABLE_TALE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    BONE_VOYAGE(net.runelite.api.Quest.BONE_VOYAGE, QuestVarbits.QUEST_BONE_VOYAGE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_FREMENNIK_ISLES(net.runelite.api.Quest.THE_FREMENNIK_ISLES, QuestVarbits.QUEST_THE_FREMENNIK_ISLES, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    THE_FREMENNIK_TRIALS(net.runelite.api.Quest.THE_FREMENNIK_TRIALS, QuestVarPlayer.QUEST_THE_FREMENNIK_TRIALS, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    GARDEN_OF_TRANQUILLITY(net.runelite.api.Quest.GARDEN_OF_TRANQUILLITY, QuestVarbits.QUEST_GARDEN_OF_TRANQUILLITY, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    GERTRUDES_CAT(net.runelite.api.Quest.GERTRUDES_CAT, QuestVarPlayer.QUEST_GERTRUDES_CAT, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    GHOSTS_AHOY(net.runelite.api.Quest.GHOSTS_AHOY, QuestVarbits.QUEST_GHOSTS_AHOY, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_GIANT_DWARF(net.runelite.api.Quest.THE_GIANT_DWARF, QuestVarbits.QUEST_THE_GIANT_DWARF, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_GOLEM(net.runelite.api.Quest.THE_GOLEM, QuestVarbits.QUEST_THE_GOLEM, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_GRAND_TREE(net.runelite.api.Quest.THE_GRAND_TREE, QuestVarPlayer.QUEST_THE_GRAND_TREE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_GREAT_BRAIN_ROBBERY(net.runelite.api.Quest.THE_GREAT_BRAIN_ROBBERY, QuestVarPlayer.QUEST_THE_GREAT_BRAIN_ROBBERY, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    GRIM_TALES(net.runelite.api.Quest.GRIM_TALES, QuestVarbits.QUEST_GRIM_TALES, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    THE_HAND_IN_THE_SAND(net.runelite.api.Quest.THE_HAND_IN_THE_SAND, QuestVarbits.QUEST_THE_HAND_IN_THE_SAND, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    HAUNTED_MINE(net.runelite.api.Quest.HAUNTED_MINE, QuestVarPlayer.QUEST_HAUNTED_MINE, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    HAZEEL_CULT(net.runelite.api.Quest.HAZEEL_CULT, QuestVarPlayer.QUEST_HAZEEL_CULT, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    HEROES_QUEST(net.runelite.api.Quest.HEROES_QUEST, QuestVarPlayer.QUEST_HEROES_QUEST, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    HOLY_GRAIL(net.runelite.api.Quest.HOLY_GRAIL, QuestVarPlayer.QUEST_HOLY_GRAIL, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    HORROR_FROM_THE_DEEP(net.runelite.api.Quest.HORROR_FROM_THE_DEEP, QuestVarbits.QUEST_HORROR_FROM_THE_DEEP, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    ICTHLARINS_LITTLE_HELPER(net.runelite.api.Quest.ICTHLARINS_LITTLE_HELPER, QuestVarbits.QUEST_ICTHLARINS_LITTLE_HELPER, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    IN_AID_OF_THE_MYREQUE(net.runelite.api.Quest.IN_AID_OF_THE_MYREQUE, QuestVarbits.QUEST_IN_AID_OF_THE_MYREQUE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    IN_SEARCH_OF_THE_MYREQUE(net.runelite.api.Quest.IN_SEARCH_OF_THE_MYREQUE, QuestVarPlayer.QUEST_IN_SEARCH_OF_THE_MYREQUE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    JUNGLE_POTION(net.runelite.api.Quest.JUNGLE_POTION, QuestVarPlayer.QUEST_JUNGLE_POTION, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    KINGS_RANSOM(net.runelite.api.Quest.KINGS_RANSOM, QuestVarbits.QUEST_KINGS_RANSOM, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    LAND_OF_THE_GOBLINS(net.runelite.api.Quest.LAND_OF_THE_GOBLINS, QuestVarbits.QUEST_LAND_OF_THE_GOBLINS, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    LEGENDS_QUEST(net.runelite.api.Quest.LEGENDS_QUEST, QuestVarPlayer.QUEST_LEGENDS_QUEST, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    LOST_CITY(net.runelite.api.Quest.LOST_CITY, QuestVarPlayer.QUEST_LOST_CITY, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_LOST_TRIBE(net.runelite.api.Quest.THE_LOST_TRIBE, QuestVarbits.QUEST_THE_LOST_TRIBE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    LUNAR_DIPLOMACY(net.runelite.api.Quest.LUNAR_DIPLOMACY, QuestVarbits.QUEST_LUNAR_DIPLOMACY, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    MAKING_FRIENDS_WITH_MY_ARM(net.runelite.api.Quest.MAKING_FRIENDS_WITH_MY_ARM, QuestVarbits.QUEST_MAKING_FRIENDS_WITH_MY_ARM, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    MAKING_HISTORY(net.runelite.api.Quest.MAKING_HISTORY, QuestVarbits.QUEST_MAKING_HISTORY, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    MERLINS_CRYSTAL(net.runelite.api.Quest.MERLINS_CRYSTAL, QuestVarPlayer.QUEST_MERLINS_CRYSTAL, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    MONKEY_MADNESS_I(net.runelite.api.Quest.MONKEY_MADNESS_I, QuestVarPlayer.QUEST_MONKEY_MADNESS_I, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    MONKEY_MADNESS_II(net.runelite.api.Quest.MONKEY_MADNESS_II, QuestVarbits.QUEST_MONKEY_MADNESS_II, QuestDetails.Type.P2P, QuestDetails.Difficulty.GRANDMASTER),
    MONKS_FRIEND(net.runelite.api.Quest.MONKS_FRIEND, QuestVarPlayer.QUEST_MONKS_FRIEND, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    MOUNTAIN_DAUGHTER(net.runelite.api.Quest.MOUNTAIN_DAUGHTER, QuestVarbits.QUEST_MOUNTAIN_DAUGHTER, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    MOURNINGS_END_PART_I(net.runelite.api.Quest.MOURNINGS_END_PART_I, QuestVarPlayer.QUEST_MOURNINGS_END_PART_I, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    MOURNINGS_END_PART_II(net.runelite.api.Quest.MOURNINGS_END_PART_II, QuestVarbits.QUEST_MOURNINGS_END_PART_II, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    MURDER_MYSTERY(net.runelite.api.Quest.MURDER_MYSTERY, QuestVarPlayer.QUEST_MURDER_MYSTERY, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    MY_ARMS_BIG_ADVENTURE(net.runelite.api.Quest.MY_ARMS_BIG_ADVENTURE, QuestVarbits.QUEST_MY_ARMS_BIG_ADVENTURE, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    NATURE_SPIRIT(net.runelite.api.Quest.NATURE_SPIRIT, QuestVarPlayer.QUEST_NATURE_SPIRIT, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    OBSERVATORY_QUEST(net.runelite.api.Quest.OBSERVATORY_QUEST, QuestVarPlayer.QUEST_OBSERVATORY_QUEST, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    OLAFS_QUEST(net.runelite.api.Quest.OLAFS_QUEST, QuestVarbits.QUEST_OLAFS_QUEST, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    ONE_SMALL_FAVOUR(net.runelite.api.Quest.ONE_SMALL_FAVOUR, QuestVarPlayer.QUEST_ONE_SMALL_FAVOUR, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    PLAGUE_CITY(net.runelite.api.Quest.PLAGUE_CITY, QuestVarPlayer.QUEST_PLAGUE_CITY, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    PRIEST_IN_PERIL(net.runelite.api.Quest.PRIEST_IN_PERIL, QuestVarPlayer.QUEST_PRIEST_IN_PERIL, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    THE_QUEEN_OF_THIEVES(net.runelite.api.Quest.THE_QUEEN_OF_THIEVES, QuestVarbits.QUEST_THE_QUEEN_OF_THIEVES, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    RAG_AND_BONE_MAN_I(net.runelite.api.Quest.RAG_AND_BONE_MAN_I, QuestVarPlayer.QUEST_RAG_AND_BONE_MAN_I, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    RAG_AND_BONE_MAN_II(net.runelite.api.Quest.RAG_AND_BONE_MAN_II, QuestVarPlayer.QUEST_RAG_AND_BONE_MAN_II, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    RATCATCHERS(net.runelite.api.Quest.RATCATCHERS, QuestVarbits.QUEST_RATCATCHERS, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    RECIPE_FOR_DISASTER(net.runelite.api.Quest.RECIPE_FOR_DISASTER, QuestVarbits.QUEST_RECIPE_FOR_DISASTER, QuestDetails.Type.P2P, QuestDetails.Difficulty.GRANDMASTER),
    RECIPE_FOR_DISASTER_START(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Start", QuestVarbits.QUEST_RECIPE_FOR_DISASTER, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    RECIPE_FOR_DISASTER_DWARF(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Dwarf", QuestVarbits.QUEST_RECIPE_FOR_DISASTER_DWARF, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    RECIPE_FOR_DISASTER_WARTFACE_AND_BENTNOZE(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Wartface & Bentnoze", QuestVarbits.QUEST_RECIPE_FOR_DISASTER_WARTFACE_AND_BENTNOZE, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    RECIPE_FOR_DISASTER_PIRATE_PETE(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Pirate Pete", QuestVarbits.QUEST_RECIPE_FOR_DISASTER_PIRATE_PETE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    RECIPE_FOR_DISASTER_LUMBRIDGE_GUIDE(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Lumbridge Guide", QuestVarbits.QUEST_RECIPE_FOR_DISASTER_LUMBRIDGE_GUIDE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    RECIPE_FOR_DISASTER_EVIL_DAVE(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Evil Dave", QuestVarbits.QUEST_RECIPE_FOR_DISASTER_EVIL_DAVE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    RECIPE_FOR_DISASTER_MONKEY_AMBASSADOR(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Monkey Ambassador", QuestVarbits.QUEST_RECIPE_FOR_DISASTER_MONKEY_AMBASSADOR, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    RECIPE_FOR_DISASTER_SIR_AMIK_VARZE(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Sir Amik Varze", QuestVarbits.QUEST_RECIPE_FOR_DISASTER_SIR_AMIK_VARZE, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    RECIPE_FOR_DISASTER_SKRACH_UGLOGWEE(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Skrach Uglogwee", QuestVarbits.QUEST_RECIPE_FOR_DISASTER_SKRACH_UGLOGWEE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    RECIPE_FOR_DISASTER_FINALE(net.runelite.api.Quest.RECIPE_FOR_DISASTER.getId(), "RFD - Finale", QuestVarbits.QUEST_RECIPE_FOR_DISASTER, QuestDetails.Type.P2P, QuestDetails.Difficulty.GRANDMASTER),
    RECRUITMENT_DRIVE(net.runelite.api.Quest.RECRUITMENT_DRIVE, QuestVarbits.QUEST_RECRUITMENT_DRIVE, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    REGICIDE(net.runelite.api.Quest.REGICIDE, QuestVarPlayer.QUEST_REGICIDE, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    ROVING_ELVES(net.runelite.api.Quest.ROVING_ELVES, QuestVarPlayer.QUEST_ROVING_ELVES, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    ROYAL_TROUBLE(net.runelite.api.Quest.ROYAL_TROUBLE, QuestVarbits.QUEST_ROYAL_TROUBLE, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    RUM_DEAL(net.runelite.api.Quest.RUM_DEAL, QuestVarPlayer.QUEST_RUM_DEAL, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    SCORPION_CATCHER(net.runelite.api.Quest.SCORPION_CATCHER, QuestVarPlayer.QUEST_SCORPION_CATCHER, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    SEA_SLUG(net.runelite.api.Quest.SEA_SLUG, QuestVarPlayer.QUEST_SEA_SLUG, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    SHADES_OF_MORTTON(net.runelite.api.Quest.SHADES_OF_MORTTON, QuestVarPlayer.QUEST_SHADES_OF_MORTTON, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    SHADOW_OF_THE_STORM(net.runelite.api.Quest.SHADOW_OF_THE_STORM, QuestVarbits.QUEST_SHADOW_OF_THE_STORM, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    SHEEP_HERDER(net.runelite.api.Quest.SHEEP_HERDER, QuestVarPlayer.QUEST_SHEEP_HERDER, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    SHILO_VILLAGE(net.runelite.api.Quest.SHILO_VILLAGE, QuestVarPlayer.QUEST_SHILO_VILLAGE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    SLEEPING_GIANTS(net.runelite.api.Quest.SLEEPING_GIANTS, QuestVarbits.QUEST_SLEEPING_GIANTS, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_SLUG_MENACE(net.runelite.api.Quest.THE_SLUG_MENACE, QuestVarbits.QUEST_THE_SLUG_MENACE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    A_SOULS_BANE(net.runelite.api.Quest.A_SOULS_BANE, QuestVarbits.QUEST_A_SOULS_BANE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    SPIRITS_OF_THE_ELID(net.runelite.api.Quest.SPIRITS_OF_THE_ELID, QuestVarbits.QUEST_SPIRITS_OF_THE_ELID, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    SWAN_SONG(net.runelite.api.Quest.SWAN_SONG, QuestVarbits.QUEST_SWAN_SONG, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    TAI_BWO_WANNAI_TRIO(net.runelite.api.Quest.TAI_BWO_WANNAI_TRIO, QuestVarPlayer.QUEST_TAI_BWO_WANNAI_TRIO, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    A_TAIL_OF_TWO_CATS(net.runelite.api.Quest.A_TAIL_OF_TWO_CATS, QuestVarbits.QUEST_A_TAIL_OF_TWO_CATS, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    TALE_OF_THE_RIGHTEOUS(net.runelite.api.Quest.TALE_OF_THE_RIGHTEOUS, QuestVarbits.QUEST_TALE_OF_THE_RIGHTEOUS, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    A_TASTE_OF_HOPE(net.runelite.api.Quest.A_TASTE_OF_HOPE, QuestVarbits.QUEST_A_TASTE_OF_HOPE, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    TEARS_OF_GUTHIX(net.runelite.api.Quest.TEARS_OF_GUTHIX, QuestVarbits.QUEST_TEARS_OF_GUTHIX, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    TEMPLE_OF_IKOV(net.runelite.api.Quest.TEMPLE_OF_IKOV, QuestVarPlayer.QUEST_TEMPLE_OF_IKOV, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    TEMPLE_OF_THE_EYE(net.runelite.api.Quest.TEMPLE_OF_THE_EYE, QuestVarbits.QUEST_TEMPLE_OF_THE_EYE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THRONE_OF_MISCELLANIA(net.runelite.api.Quest.THRONE_OF_MISCELLANIA, QuestVarPlayer.QUEST_THRONE_OF_MISCELLANIA, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    THE_TOURIST_TRAP(net.runelite.api.Quest.THE_TOURIST_TRAP, QuestVarPlayer.QUEST_THE_TOURIST_TRAP, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    TOWER_OF_LIFE(net.runelite.api.Quest.TOWER_OF_LIFE, QuestVarbits.QUEST_TOWER_OF_LIFE, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    TREE_GNOME_VILLAGE(net.runelite.api.Quest.TREE_GNOME_VILLAGE, QuestVarPlayer.QUEST_TREE_GNOME_VILLAGE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    TRIBAL_TOTEM(net.runelite.api.Quest.TRIBAL_TOTEM, QuestVarPlayer.QUEST_TRIBAL_TOTEM, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    TROLL_ROMANCE(net.runelite.api.Quest.TROLL_ROMANCE, QuestVarPlayer.QUEST_TROLL_ROMANCE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    TROLL_STRONGHOLD(net.runelite.api.Quest.TROLL_STRONGHOLD, QuestVarPlayer.QUEST_TROLL_STRONGHOLD, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    UNDERGROUND_PASS(net.runelite.api.Quest.UNDERGROUND_PASS, QuestVarPlayer.QUEST_UNDERGROUND_PASS, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    CLIENT_OF_KOUREND(net.runelite.api.Quest.CLIENT_OF_KOUREND, QuestVarbits.QUEST_CLIENT_OF_KOUREND, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    WANTED(net.runelite.api.Quest.WANTED, QuestVarbits.QUEST_WANTED, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    WATCHTOWER(net.runelite.api.Quest.WATCHTOWER, QuestVarPlayer.QUEST_WATCHTOWER, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    WATERFALL_QUEST(net.runelite.api.Quest.WATERFALL_QUEST, QuestVarPlayer.QUEST_WATERFALL_QUEST, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    WHAT_LIES_BELOW(net.runelite.api.Quest.WHAT_LIES_BELOW, QuestVarbits.QUEST_WHAT_LIES_BELOW, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    WITCHS_HOUSE(net.runelite.api.Quest.WITCHS_HOUSE, QuestVarPlayer.QUEST_WITCHS_HOUSE, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    ZOGRE_FLESH_EATERS(net.runelite.api.Quest.ZOGRE_FLESH_EATERS, QuestVarbits.QUEST_ZOGRE_FLESH_EATERS, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_ASCENT_OF_ARCEUUS(net.runelite.api.Quest.THE_ASCENT_OF_ARCEUUS, QuestVarbits.QUEST_THE_ASCENT_OF_ARCEUUS, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    THE_FORSAKEN_TOWER(net.runelite.api.Quest.THE_FORSAKEN_TOWER, QuestVarbits.QUEST_THE_FORSAKEN_TOWER, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    SONG_OF_THE_ELVES(net.runelite.api.Quest.SONG_OF_THE_ELVES, QuestVarbits.QUEST_SONG_OF_THE_ELVES, QuestDetails.Type.P2P, QuestDetails.Difficulty.GRANDMASTER),
    THE_FREMENNIK_EXILES(net.runelite.api.Quest.THE_FREMENNIK_EXILES, QuestVarbits.QUEST_THE_FREMENNIK_EXILES, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    SINS_OF_THE_FATHER(net.runelite.api.Quest.SINS_OF_THE_FATHER, QuestVarbits.QUEST_SINS_OF_THE_FATHER, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    GETTING_AHEAD(net.runelite.api.Quest.GETTING_AHEAD, QuestVarbits.QUEST_GETTING_AHEAD, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    A_PORCINE_OF_INTEREST(net.runelite.api.Quest.A_PORCINE_OF_INTEREST, QuestVarbits.QUEST_A_PORCINE_OF_INTEREST, QuestDetails.Type.P2P, QuestDetails.Difficulty.NOVICE),
    A_KINGDOM_DIVIDED(net.runelite.api.Quest.A_KINGDOM_DIVIDED, QuestVarbits.QUEST_A_KINGDOM_DIVIDED, QuestDetails.Type.P2P, QuestDetails.Difficulty.EXPERIENCED),
    A_NIGHT_AT_THE_THEATRE(net.runelite.api.Quest.A_NIGHT_AT_THE_THEATRE, QuestVarbits.QUEST_A_NIGHT_AT_THE_THEATRE, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),
    THE_GARDEN_OF_DEATH(net.runelite.api.Quest.THE_GARDEN_OF_DEATH, QuestVarbits.QUEST_THE_GARDEN_OF_DEATH, QuestDetails.Type.P2P, QuestDetails.Difficulty.INTERMEDIATE),
    SECRETS_OF_THE_NORTH(net.runelite.api.Quest.SECRETS_OF_THE_NORTH, QuestVarbits.QUEST_SECRETS_OF_THE_NORTH, QuestDetails.Type.P2P, QuestDetails.Difficulty.MASTER),

    //Miniquests
    ENTER_THE_ABYSS(net.runelite.api.Quest.ENTER_THE_ABYSS, QuestVarPlayer.QUEST_ENTER_THE_ABYSS, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    ARCHITECTURAL_ALLIANCE(net.runelite.api.Quest.ARCHITECTURAL_ALLIANCE, QuestVarbits.QUEST_ARCHITECTURAL_ALLIANCE, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    BEAR_YOUR_SOUL(net.runelite.api.Quest.BEAR_YOUR_SOUL, QuestVarbits.QUEST_BEAR_YOUR_SOUL, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    ALFRED_GRIMHANDS_BARCRAWL(net.runelite.api.Quest.ALFRED_GRIMHANDS_BARCRAWL, QuestVarPlayer.QUEST_ALFRED_GRIMHANDS_BARCRAWL, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    CURSE_OF_THE_EMPTY_LORD(net.runelite.api.Quest.CURSE_OF_THE_EMPTY_LORD, QuestVarbits.QUEST_CURSE_OF_THE_EMPTY_LORD, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    ENCHANTED_KEY(net.runelite.api.Quest.THE_ENCHANTED_KEY, QuestVarbits.QUEST_ENCHANTED_KEY, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    THE_GENERALS_SHADOW(net.runelite.api.Quest.THE_GENERALS_SHADOW, QuestVarbits.QUEST_THE_GENERALS_SHADOW, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    SKIPPY_AND_THE_MOGRES(net.runelite.api.Quest.SKIPPY_AND_THE_MOGRES, QuestVarbits.QUEST_SKIPPY_AND_THE_MOGRES, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    THE_MAGE_ARENA(net.runelite.api.Quest.MAGE_ARENA_I, QuestVarPlayer.QUEST_THE_MAGE_ARENA, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    LAIR_OF_TARN_RAZORLOR(net.runelite.api.Quest.LAIR_OF_TARN_RAZORLOR, QuestVarbits.QUEST_LAIR_OF_TARN_RAZORLOR, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    FAMILY_PEST(net.runelite.api.Quest.FAMILY_PEST, QuestVarbits.QUEST_FAMILY_PEST, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    THE_MAGE_ARENA_II(net.runelite.api.Quest.MAGE_ARENA_II, QuestVarbits.QUEST_THE_MAGE_ARENA_II, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    IN_SEARCH_OF_KNOWLEDGE(net.runelite.api.Quest.IN_SEARCH_OF_KNOWLEDGE, QuestVarbits.QUEST_IN_SEARCH_OF_KNOWLEDGE, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    DADDYS_HOME(net.runelite.api.Quest.DADDYS_HOME, QuestVarbits.QUEST_DADDYS_HOME, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST),
    HOPESPEARS_WILL(net.runelite.api.Quest.HOPESPEARS_WILL, QuestVarbits.QUEST_HOPESPEARS_WILL, QuestDetails.Type.MINIQUEST, QuestDetails.Difficulty.MINIQUEST);

    @Getter
    private final int id;

    @Getter
    private final String name;

    @Getter
    private final QuestDetails.Type questType;

    @Getter
    private final QuestDetails.Difficulty difficulty;

    private final QuestVarbits varbit;

    private final QuestVarPlayer varPlayer;

    private Skill skill;

    @Getter
    private Object playerQuests;

    private final int completeValue;

    Quest(int id, String name, QuestVarbits varbit, QuestDetails.Type questType, QuestDetails.Difficulty difficulty)
    {
        this.id = id;
        this.name = name;
        this.varbit = varbit;
        this.varPlayer = null;
        this.questType = questType;
        this.difficulty = difficulty;
        this.completeValue = -1;
    }

    Quest(net.runelite.api.Quest quest, QuestVarbits varbit, QuestDetails.Type questType, QuestDetails.Difficulty difficulty)
    {
        this.id = quest.getId();
        this.name = quest.getName();
        this.varbit = varbit;
        this.varPlayer = null;
        this.questType = questType;
        this.difficulty = difficulty;
        this.completeValue = -1;
    }

    Quest(net.runelite.api.Quest quest, QuestVarPlayer varPlayer, QuestDetails.Type questType, QuestDetails.Difficulty difficulty)
    {
        this.id = quest.getId();
        this.name = quest.getName();
        this.varbit = null;
        this.varPlayer = varPlayer;
        this.questType = questType;
        this.difficulty = difficulty;
        this.completeValue = -1;
    }

    private QuestState getQuestState(Client client)
    {
        client.runScript(ScriptID.QUEST_STATUS_GET, id);
        return switch (client.getIntStack()[0]) {
            case 2 -> QuestState.FINISHED;
            case 1 -> QuestState.NOT_STARTED;
            default -> QuestState.IN_PROGRESS;
        };
    }

    QuestState getState(Client client)
    {
        if (id != -1)
        {
            return getQuestState(client);
        }

        if (skill != null)
        {
            if (Skills.getLevel(skill) >= completeValue)
            {
                return QuestState.FINISHED;
            }
            return QuestState.IN_PROGRESS;
        }

        if (getVar(client) == -1)
        {
            return QuestState.IN_PROGRESS;
        }

        if (completeValue != -1)
        {
            int currentState = getVar(client);
            if (currentState == completeValue)
            {
                return QuestState.FINISHED;
            }
            if (currentState == 0)
            {
                return QuestState.NOT_STARTED;
            }
            return QuestState.IN_PROGRESS;
        }

        return QuestState.NOT_STARTED;
    }

    int getVar(Client client)
    {
        if (varbit != null)
        {
            return client.getVarbitValue(varbit.getId());
        }
        else if (varPlayer != null)
        {
            return client.getVarpValue(varPlayer.getId());
        }
        else
        {
            return -1;
        }
    }
}
