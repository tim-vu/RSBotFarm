package net.rlbot.api.queries.entities;

import net.rlbot.api.adapter.scene.Pickable;
import net.rlbot.api.adapter.scene.Tile;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.movement.Position;
import net.rlbot.api.queries.results.SceneEntityQueryResults;
import net.rlbot.api.scene.Pickables;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PickableQuery extends SceneEntityQuery<Pickable, PickableQuery>
{

	private Set<Integer> quantities = null;

	private Boolean tradeable = null;

	private Boolean stackable = null;

	private Boolean noted = null;

	private Boolean members = null;

	private Integer storePrice = null;

	public PickableQuery(Supplier<List<Pickable>> supplier) {

		super(supplier);
	}

	@Override
	public PickableQuery locations(Position... positions) {
		setSupplier(() -> {
			var result = new LinkedList<Pickable>();

			for(var position : positions) {
				result.addAll(Pickables.getAt(position, Predicates.always()));
			}

			return result;
		});

		return this;
	}

	public PickableQuery quantities(int... quantities) {

		this.quantities = Arrays.stream(quantities).boxed().collect(Collectors.toSet());
		return this;
	}

	public PickableQuery tradeable(boolean tradeable) {

		this.tradeable = tradeable;
		return this;
	}

	public PickableQuery stackable(boolean stackable) {

		this.stackable = stackable;
		return this;
	}

	public PickableQuery noted(boolean noted) {

		this.noted = noted;
		return this;
	}

	public PickableQuery members(boolean members) {

		this.members = members;
		return this;
	}

	public PickableQuery storePrice(int storePrice) {

		this.storePrice = storePrice;
		return this;
	}

	@Override
	protected SceneEntityQueryResults<Pickable> results(List<Pickable> list) {

		return new SceneEntityQueryResults<>(list);
	}

	@Override
	public boolean test(Pickable tileItem) {

		if (quantities != null && !quantities.contains(tileItem.getQuantity())) {
			return false;
		}

		if (tradeable != null && !tradeable.equals(tileItem.isTradeable())) {
			return false;
		}

		if (stackable != null && !stackable.equals(tileItem.isStackable())) {
			return false;
		}

		if (noted != null && !noted.equals(tileItem.isNoted())) {
			return false;
		}

		if (members != null && !members.equals(tileItem.isMembers())) {
			return false;
		}

		if (storePrice != null && !storePrice.equals(tileItem.getStorePrice())) {
			return false;
		}

		return super.test(tileItem);
	}
}
