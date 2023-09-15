package net.rlbot.api.magic;

import lombok.NonNull;
import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.adapter.scene.SceneObject;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.common.Time;
import net.rlbot.api.game.Combat;
import net.rlbot.api.game.VarPlayer;
import net.rlbot.api.game.Varbits;
import net.rlbot.api.packet.ObjectPackets;
import net.rlbot.api.widgets.WidgetAddress;
import net.rlbot.api.widgets.Widgets;
import net.rlbot.api.game.Vars;
import net.rlbot.api.packet.MousePackets;
import net.rlbot.api.packet.NpcPackets;
import net.rlbot.api.packet.WidgetPackets;
import net.rlbot.internal.ApiContext;
import net.rlbot.internal.Interaction;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static net.logstash.logback.argument.StructuredArguments.kv;

public class Magic
{
    private static ApiContext API_CONTEXT;

    private static void init(ApiContext apiContext) {
        API_CONTEXT = apiContext;
    }

    private static final int AUTOCAST_VARP = 108;

    public static boolean isAutoCasting()
    {
        return Vars.getVarp(AUTOCAST_VARP) != 0;
    }

    public static boolean isSpellSelected(@NonNull Spell spell)
    {
        Widget widget = Widgets.get(spell.getWidgetInfo());
        if (widget != null)
        {
            return widget.getBorderType() == 2;
        }

        return false;
    }

    public static boolean isSpellSelected() {
        var spellbook = SpellBook.getCurrent();

        for(var spell : spellbook.getSpells()) {

            if(!isSpellSelected(spell)) {
                continue;
            }

            return true;
        }

        return false;
    }

    public static boolean cast(@NonNull Spell spell) {
        var widget = Widgets.get(spell.getWidgetInfo());

        if(widget == null) {
            return false;
        }

        //TODO: Verify if this works
        return widget.interact(Predicates.always());
    }

    public static boolean cast(@NonNull Spell spell, @NonNull Npc target) {

        var widget = Widgets.get(spell.getWidgetInfo());

        if(widget == null) {
            return false;
        }

        Interaction.log("SPELL_ON_NPC", kv("npcId", target.getIndex()));
        MousePackets.queueClickPacket();
        NpcPackets.queueWidgetOnNpc(target.getIndex(), -1, -1, widget.getId(), false);
        return true;
    }

    public static boolean cast(@NonNull Spell spell, @NonNull SceneObject target) {

        var widget = Widgets.get(spell.getWidgetInfo());

        if(widget == null) {
            return false;
        }

        var point = target.getMenuPoint();
        LocalPoint lp = new LocalPoint(point.x, point.y);
        WorldPoint wp = WorldPoint.fromScene(API_CONTEXT.getClient(), lp.getX(), lp.getY(), target.getPlane());


        Interaction.log("SPELL_ON_OBJECT", kv("objectId", target.getId()));
        MousePackets.queueClickPacket();
        ObjectPackets.queueWidgetOnObject(target.getId(), wp.getX(), wp.getY(), -1, -1, widget.getId(), false);
        return true;
    }

    public static boolean cast(@NonNull Spell spell, @NonNull Item item) {
        var widget = Widgets.get(spell.getWidgetInfo());

        if(widget == null) {
            return false;
        }

        Interaction.log("SPELL_ON_ITEM", kv("itemId", item.getId()));
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetOnWidget(widget.getId(), -1, -1, item.getWidgetId(), item.getSlot(), item.getId());
        return true;
    }

    public static Instant getLastHomeTeleportUsage()
    {
        return Instant.ofEpochSecond(Vars.getVarp(VarPlayer.LAST_HOME_TELEPORT.getId()) * 60L);
    }

    public static boolean isHomeTeleportOnCooldown()
    {
        return getLastHomeTeleportUsage().plus(30, ChronoUnit.MINUTES).isAfter(Instant.now());
    }

    public static class Autocast {

        private static final WidgetAddress SELECT_COMBAT_SPELL_TEXT = new WidgetAddress(201, 2, 0);

        public static boolean isSpellSelected() {
            return Vars.getBit(Varbits.AUTO_CAST_SPELL) != 0;
        }

        @Nullable
        public static Spell getSelectedSpell() {
            var selectedSpellIndex = Vars.getBit(Varbits.AUTO_CAST_SPELL);

            if(selectedSpellIndex == 0) {
                return null;
            }

            var current = SpellBook.getCurrent();

            for(var spell : current.getSpells()) {

                if(selectedSpellIndex == spell.getAutocastIndex()) {
                    return spell;
                }
            }

            return null;
        }

        public static boolean selectSpell(Mode mode, Spell spell) {

            var autocastIndex = spell.getAutocastIndex();

            if(autocastIndex == -1) {
                return false;
            }

            var attackStyle = mode == Mode.OFFENSIVE ? Combat.AttackStyle.SPELLS : Combat.AttackStyle.SPELLS_DEFENSIVE;

            if(!Combat.setAttackStyle(attackStyle) || !Time.sleepUntil(() -> SELECT_COMBAT_SPELL_TEXT.resolve() != null, 1200)) {
                return false;
            }

            var textWidget = SELECT_COMBAT_SPELL_TEXT.resolve();

            if(textWidget == null) {
                return false;
            }

            var widget = Widgets.get(201, 1, spell.getAutocastIndex());

            if(widget == null) {
                return false;
            }

            return widget.interact(Predicates.always()) && Time.sleepUntil(() -> Vars.getBit(net.rlbot.api.game.Varbits.AUTO_CAST_SPELL) == autocastIndex, 1200);
        }

        public enum Mode {
            OFFENSIVE,
            DEFENSIVE
        }
    }
}
