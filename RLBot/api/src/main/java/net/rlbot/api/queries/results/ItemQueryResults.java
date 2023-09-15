package net.rlbot.api.queries.results;

import net.rlbot.api.adapter.component.Item;

import java.util.List;

public class ItemQueryResults extends QueryResults<Item, ItemQueryResults>
{
	public ItemQueryResults(List<Item> results)
	{
		super(results);
	}
}
