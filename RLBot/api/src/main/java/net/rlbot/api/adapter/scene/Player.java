package net.rlbot.api.adapter.scene;

import net.rlbot.api.packet.MousePackets;
import net.rlbot.api.packet.PlayerPackets;
import net.rlbot.internal.Interaction;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

import static net.logstash.logback.argument.StructuredArguments.kv;

public class Player extends Actor {

	private final net.runelite.api.Player player;

	public Player(@NotNull net.runelite.api.Player player) {

		super(player);
		this.player = player;
	}

	@Override
	public int getId() {
		return this.player.getId();
	}

	public String getName() {
		return this.player.getName();
	}

	@Override
	public int getCombatLevel() {
		return this.player.getCombatLevel();
	}

	public int getAnimation() {
		return this.player.getAnimation();
	}

	public SkullIcon getSkullIcon() {

		var skullIcon = this.player.getSkullIcon();

		if(skullIcon == null) {
			return null;
		}

		return switch(skullIcon) {

			case SKULL -> SkullIcon.SKULL;
			case SKULL_FIGHT_PIT -> SkullIcon.SKULL_FIGHT_PIT;
			case DEAD_MAN_ONE -> SkullIcon.DEAD_MAN_ONE;
			case DEAD_MAN_TWO -> SkullIcon.DEAD_MAN_TWO;
			case DEAD_MAN_THREE -> SkullIcon.DEAD_MAN_THREE;
			case DEAD_MAN_FOUR -> SkullIcon.DEAD_MAN_FOUR;
			case DEAD_MAN_FIVE -> SkullIcon.DEAD_MAN_FIVE;
		};
	}

	@Override
	public boolean interact(int index) {

		int num = index + 1;

		if(num < 1 || num > 10) {
			return false;
		}

		Interaction.log("PLAYER", kv("menuIndex", index));
		MousePackets.queueClickPacket();
		PlayerPackets.queueAction(num, this.player.getId(), false);
		return true;
	}

	@Override
	public String[] getActions() {
		return API_CONTEXT.getClient().getPlayerOptions();
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Player player1 = (Player) o;

		return this.getId() == player1.getId();
	}

	@Override
	public int hashCode() {

		int result = super.hashCode();
		result = 31 * result + (player != null ? player.hashCode() : 0);
		return result;
	}

	public enum SkullIcon {
		SKULL,
		SKULL_FIGHT_PIT,
		DEAD_MAN_ONE,
		DEAD_MAN_TWO,
		DEAD_MAN_THREE,
		DEAD_MAN_FOUR,
		DEAD_MAN_FIVE,
	}
}