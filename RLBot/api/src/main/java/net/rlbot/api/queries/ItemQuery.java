package net.rlbot.api.queries;

import net.rlbot.api.adapter.component.Item;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.queries.results.ItemQueryResults;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ItemQuery extends Query<Item, ItemQuery, ItemQueryResults>
{

	private Set<Integer> ids = null;

	private Set<Integer> notedIds = null;

	private Set<Integer> slots = null;

	private Set<String> names = null;

	private Set<String> actions = null;

	private Boolean tradable = null;

	private Boolean stackable = null;

	private Boolean members = null;

	private Boolean noted = null;

	private Integer storePrice = null;

	public ItemQuery(Supplier<List<Item>> supplier) {

		super(supplier);
	}

	public ItemQuery ids(int... ids) {

		this.ids = Arrays.stream(ids).boxed().collect(Collectors.toSet());
		return this;
	}

	public ItemQuery notedIds(int... notedIds) {

		this.notedIds = Arrays.stream(notedIds).boxed().collect(Collectors.toSet());
		return this;
	}

	public ItemQuery slots(int... slots) {

		this.slots = Arrays.stream(slots).boxed().collect(Collectors.toSet());
		return this;
	}

	public ItemQuery names(String... names) {

		this.names = Arrays.stream(names).collect(Collectors.toUnmodifiableSet());
		return this;
	}

	public ItemQuery actions(String... actions) {

		this.actions = Arrays.stream(actions).collect(Collectors.toUnmodifiableSet());
		return this;
	}

	public ItemQuery tradeable(boolean tradable) {

		this.tradable = tradable;
		return this;
	}

	public ItemQuery stackable(boolean stackable) {

		this.stackable = stackable;
		return this;
	}

	public ItemQuery members(boolean members) {

		this.members = members;
		return this;
	}

	public ItemQuery noted(boolean noted) {

		this.noted = noted;
		return this;
	}

	public ItemQuery storePrice(int storePrice) {

		this.storePrice = storePrice;
		return this;
	}

	@Override
	protected ItemQueryResults results(List<Item> list) {

		return new ItemQueryResults(list);
	}

	@Override
	public boolean test(Item item) {

		if (ids != null && !ids.contains(item.getId())) {
			return false;
		}

		if (notedIds != null && !notedIds.contains(item.getId())) {
			return false;
		}

		if (slots != null && !slots.contains(item.getSlot())) {
			return false;
		}

		if (names != null && !names.contains(item.getName())) {
			return false;
		}

		if (actions != null && actions.stream().noneMatch(Predicates.texts(item.getActions()))) {
			return false;
		}

		if (tradable != null && tradable != item.isTradeable()) {
			return false;
		}

		if (stackable != null && stackable != item.isStackable()) {
			return false;
		}

		if (members != null && members != item.isMembers()) {
			return false;
		}

		if (noted != null && noted != item.isNoted()) {
			return false;
		}

		if (storePrice != null && storePrice != item.getStorePrice()) {
			return false;
		}

		return super.test(item);
	}
}
