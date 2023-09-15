package net.rlbot.script.api.quest.nodes.action;

import lombok.extern.slf4j.Slf4j;
import net.rlbot.api.adapter.scene.Npc;
import net.rlbot.api.common.Time;
import net.rlbot.api.scene.Npcs;
import net.rlbot.api.scene.Players;
import net.rlbot.api.widgets.Dialog;
import net.rlbot.script.api.quest.common.QuestDialog;
import net.rlbot.script.api.reaction.Reaction;

import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Slf4j
public class DialogAction implements BooleanSupplier {

    private final int[] responses;

    private Supplier<Npc> npcSupplier;

    public DialogAction(String npcName, int... responses) {

        this(() -> Npcs.getNearest(npcName), responses);
    }

    public DialogAction(Supplier<Npc> npcSupplier, int... responses) {

        this.npcSupplier = npcSupplier;
        this.responses = responses;
    }

    public DialogAction(String npcName) {

        this(npcName, new int[0]);
    }

    public DialogAction(Supplier<Npc> npcSupplier) {

        this(npcSupplier, new int[0]);
    }

    public DialogAction(int... responses) {

        this.responses = responses;
    }

    public DialogAction() {

        this.responses = new int[0];
    }

    @Override
    public boolean getAsBoolean() {

        log.info("Dialog info start");

        if (npcSupplier != null && !Dialog.isOpen()) {

            var npc = npcSupplier.get();

            if (npc == null) {
                log.warn("Failed to find npc");
                Time.sleepTick();
                return false;
            }

            if (!npc.interact("Talk-to") || !Time.sleepUntil(Dialog::isOpen, () -> Players.getLocal().isMoving(), 4000)) {
                log.warn("Failed to talk to npc");
                Time.sleepTick();
                return false;
            }

            Reaction.REGULAR.sleep();
        }

        log.info("Npc dialog open");

        QuestDialog.doContinue();

        log.info("Processing responses");
        for (int response : responses) {

            log.info("Choosing response: " + response);
            if (!Dialog.chooseOption(response)) {
                log.info("Failed to choose option");
                return false;
            }

            Time.sleepTick();
            Reaction.PREDICTABLE.sleep();

            log.info("Continuing dialog");
            QuestDialog.doContinue();
        }

        QuestDialog.doContinue();
        return true;
    }

}
