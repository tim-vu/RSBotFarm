package net.rlbot.oc.airorbs.task.magetraining.splashing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import net.rlbot.api.definitions.ItemDefinition;
import net.rlbot.api.game.Skill;
import net.rlbot.api.game.Skills;
import net.rlbot.api.magic.Spell;
import net.rlbot.api.magic.SpellBook;
import net.rlbot.script.api.data.ItemId;
import net.rlbot.script.api.loadout.InventorySupply;
import net.rlbot.script.api.loadout.Loadout;
import net.rlbot.script.api.restocking.Tradeable;

import java.util.*;

@AllArgsConstructor
public enum SplashingSpell {

    WIND_STRIKE(SpellBook.Standard.WIND_STRIKE, 1, 3,
            List.of(
                    InventorySupply.builder(ItemId.AIR_RUNE)
                            .amount(1, Integer.MAX_VALUE).build(),
                    InventorySupply.builder(ItemId.MIND_RUNE)
                            .amount(1, Integer.MAX_VALUE).build()
            ),
            Set.of(
                    new Tradeable(ItemId.AIR_RUNE, 32),
                    new Tradeable(ItemId.MIND_RUNE, 32),
                    new Tradeable(ItemId.IRON_FULL_HELM, 1),
                    new Tradeable(ItemId.IRON_PLATEBODY, 1),
                    new Tradeable(ItemId.IRON_PLATELEGS, 1),
                    new Tradeable(ItemId.IRON_KITESHIELD, 1)
            )
    ),
    CONFUSE(SpellBook.Standard.CONFUSE, 3, 11,
            List.of(
                    InventorySupply.builder(ItemId.BODY_RUNE)
                            .amount(1, Integer.MAX_VALUE).build(),
                    InventorySupply.builder(ItemId.EARTH_RUNE)
                            .amount(2, Integer.MAX_VALUE).build(),
                    InventorySupply.builder(ItemId.WATER_RUNE)
                            .amount(3, Integer.MAX_VALUE).build()
            ),
            Set.of(
                    new Tradeable(ItemId.BODY_RUNE, 92),
                    new Tradeable(ItemId.EARTH_RUNE, 184),
                    new Tradeable(ItemId.WATER_RUNE, 276),
                    new Tradeable(ItemId.IRON_FULL_HELM, 1),
                    new Tradeable(ItemId.IRON_PLATEBODY, 1),
                    new Tradeable(ItemId.IRON_PLATELEGS, 1),
                    new Tradeable(ItemId.IRON_KITESHIELD, 1)
            )
    ),
    WEAKEN(SpellBook.Standard.WEAKEN, 11, 19,
            List.of(
                    InventorySupply.builder(ItemId.BODY_RUNE)
                            .amount(1, Integer.MAX_VALUE).build(),
                    InventorySupply.builder(ItemId.EARTH_RUNE)
                            .amount(2, Integer.MAX_VALUE).build(),
                    InventorySupply.builder(ItemId.WATER_RUNE)
                            .amount(3, Integer.MAX_VALUE).build()
            ),
            Set.of(
                    new Tradeable(ItemId.BODY_RUNE, 125),
                    new Tradeable(ItemId.EARTH_RUNE, 250),
                    new Tradeable(ItemId.WATER_RUNE, 375),
                    new Tradeable(ItemId.IRON_FULL_HELM, 1),
                    new Tradeable(ItemId.IRON_PLATEBODY, 1),
                    new Tradeable(ItemId.IRON_PLATELEGS, 1),
                    new Tradeable(ItemId.IRON_KITESHIELD, 1)
            )
    ),
    CURSE(SpellBook.Standard.CURSE, 19, 43,
            List.of(
                    InventorySupply.builder(ItemId.BODY_RUNE)
                            .amount(1, Integer.MAX_VALUE).build(),
                    InventorySupply.builder(ItemId.EARTH_RUNE)
                            .amount(3, Integer.MAX_VALUE).build(),
                    InventorySupply.builder(ItemId.WATER_RUNE)
                            .amount(2, Integer.MAX_VALUE).build()
            ),
            Set.of(
                    new Tradeable(ItemId.BODY_RUNE, 1599),
                    new Tradeable(ItemId.EARTH_RUNE, 4797),
                    new Tradeable(ItemId.WATER_RUNE, 3198),
                    new Tradeable(ItemId.IRON_FULL_HELM, 1),
                    new Tradeable(ItemId.IRON_PLATEBODY, 1),
                    new Tradeable(ItemId.IRON_PLATELEGS, 1),
                    new Tradeable(ItemId.IRON_KITESHIELD, 1)
            )
    );

    @Getter
    private final Spell spell;

    @Getter
    private final int startLevel;

    @Getter
    private final int stopLevel;

    public List<InventorySupply> getRuneSupplies(){
        return Collections.unmodifiableList(runeSupplies);
    }

    private final List<InventorySupply> runeSupplies;

    public Set<Tradeable> getTradeables(){
        return Collections.unmodifiableSet(tradeables);
    }

    private final Set<Tradeable> tradeables;

    public static Loadout getLoadouts(@NonNull SplashingSpell spell){

        int index = getIndex(spell);

        var builder = Loadout.builder()
                .withEquipmentSet()
                .with(ItemId.IRON_FULL_HELM).build()
                .with(ItemId.IRON_PLATEBODY).build()
                .with(ItemId.IRON_PLATELEGS).build()
                .with(ItemId.IRON_KITESHIELD).build()
                .build();

        for(; index < SplashingSpell.values().length; index++){

            for(var supply : SplashingSpell.values()[index].getRuneSupplies()) {
                builder.with(supply);
            }

        }

        return builder.build();
    }

    public static Set<Tradeable> getAllTradeables(SplashingSpell spell){

        int index = getIndex(spell);

        if(index == -1){
            return new HashSet<>();
        }

        Set<Tradeable> tradeables = new HashSet<>();

        for(int i = SplashingSpell.values().length - 1; i >= index; i--){

            SplashingSpell currentSpell = SplashingSpell.values()[i];
            for(Tradeable tradeable : currentSpell.getTradeables()){

                if(tradeables.contains(tradeable))
                    continue;

                tradeables.add(new Tradeable(tradeable.getItemId(),
                        getTotalRestockAmount(tradeable.getItemId(), spell),
                        tradeable.getInitialPriceIncreases()));
            }
        }

        return tradeables;
    }

    public static SplashingSpell getStartSpell(){

        int currentLevel = Skills.getLevel(Skill.MAGIC);

        SplashingSpell best = null;
        for(SplashingSpell spell : SplashingSpell.values()){

            if(currentLevel >= spell.getStartLevel())
                best = spell;

        }

        return best;
    }

    private static int getTotalRestockAmount(int itemId, SplashingSpell spell){

        if(spell == null)
            return 0;


        if(!ItemDefinition.isStackable(itemId))
            return 1;

        final int index = getIndex(spell);

        return Arrays.stream(SplashingSpell.values()).filter(s -> getIndex(s) >= index).flatMap(s -> s.getTradeables().stream()).filter(t -> t.getItemId() == itemId).mapToInt(Tradeable::getRestockAmount).sum();
    }

    private static int getIndex(@NonNull SplashingSpell spell){

        int i = 0;
        for(; i < SplashingSpell.values().length; i++){

            if(spell == SplashingSpell.values()[i])
                break;

        }

        return i;
    }
}
