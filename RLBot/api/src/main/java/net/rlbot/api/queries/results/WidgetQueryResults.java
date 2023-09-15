package net.rlbot.api.queries.results;

import net.rlbot.api.adapter.component.Widget;

import java.util.List;

public class WidgetQueryResults extends QueryResults<Widget, WidgetQueryResults>
{
	public WidgetQueryResults(List<Widget> results)
	{
		super(results);
	}
}
