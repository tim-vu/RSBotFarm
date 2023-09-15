package net.rlbot.api.queries.entities;


import net.rlbot.api.adapter.common.Interactable;
import net.rlbot.api.adapter.scene.Actor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class ActorQuery<T extends Actor, Q extends ActorQuery<T, Q>> extends SceneEntityQuery<T, Q>
{

	private Set<Integer> levels = null;

	private Set<Integer> animations = null;

	private Set<Interactable> targeting = null;

	private Integer minHealth = null;

	private Integer maxHealth = null;

	private Boolean alive = null;

	private Boolean moving = null;

	protected ActorQuery(Supplier<List<T>> supplier) {

		super(supplier);
	}

	public Q levels(int... levels) {

		this.levels = Arrays.stream(levels).boxed().collect(Collectors.toSet());
		return (Q) this;
	}

	public Q animations(int... animations) {

		this.animations = Arrays.stream(animations).boxed().collect(Collectors.toSet());
		return (Q) this;
	}

	public Q targeting(Interactable... targets) {

		this.targeting = Arrays.stream(targets).collect(Collectors.toSet());
		return (Q) this;
	}

	public Q moving(Boolean moving) {

		this.moving = moving;
		return (Q) this;
	}

	public Q health(int minHealth) {
		this.minHealth = minHealth;
		return (Q) this;
	}

	public Q health(int minHealth, int maxHealth) {
		this.minHealth = minHealth;
		this.maxHealth = maxHealth;
		return (Q)this;
	}

	public Q alive() {
		this.alive = true;
		return (Q)this;
	}

	public Q alive(boolean alive) {
		this.alive = alive;
		return (Q)this;
	}

	@Override
	public boolean test(T t) {

		if (levels != null && levels.contains(t.getCombatLevel())) {
			return false;
		}

		if (animations != null && animations.contains(t.getAnimation())) {
			return false;
		}

		if (moving != null && moving != t.isMoving()) {
			return false;
		}

		if (targeting != null && targeting.contains(t.getInteracting())) {
			return false;
		}

		if(minHealth != null && t.getHealthPercent() < minHealth) {
			return false;
		}

		if(maxHealth != null && t.getHealthPercent() > maxHealth) {
			return false;
		}

		if(alive != null && t.isAlive() != alive) {
			return false;
		}

		return super.test(t);
	}
}
