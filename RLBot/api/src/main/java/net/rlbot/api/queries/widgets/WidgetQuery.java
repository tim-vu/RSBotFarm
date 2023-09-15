package net.rlbot.api.queries.widgets;

import net.rlbot.api.adapter.component.Widget;
import net.rlbot.api.common.Predicates;
import net.rlbot.api.queries.Query;
import net.rlbot.api.queries.results.WidgetQueryResults;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class WidgetQuery extends Query<Widget, WidgetQuery, WidgetQueryResults>
{

	private Set<Integer> widgetIds = null;

	private Set<Integer> types = null;

	private Set<String> texts = null;

	private Set<String> actions = null;

	private Boolean visible = null;

	public WidgetQuery(Supplier<List<Widget>> supplier) {

		super(supplier);
	}

	@Override
	protected WidgetQueryResults results(List<Widget> list) {
		return new WidgetQueryResults(list);
	}

	public WidgetQuery ids(int... ids) {

		this.widgetIds = Arrays.stream(ids).boxed().collect(Collectors.toUnmodifiableSet());
		return this;
	}

	public WidgetQuery types(int... types) {

		this.types = Arrays.stream(types).boxed().collect(Collectors.toUnmodifiableSet());
		return this;
	}

	public WidgetQuery texts(String... texts) {

		this.texts = Arrays.stream(texts).collect(Collectors.toUnmodifiableSet());
		return this;
	}

	public WidgetQuery actions(String... actions) {

		this.actions = Arrays.stream(actions).collect(Collectors.toUnmodifiableSet());
		return this;
	}

	public WidgetQuery visible(Boolean visible) {

		this.visible = visible;
		return this;
	}

	@Override
	public boolean test(Widget widget) {

		if (widgetIds != null && !widgetIds.contains(widget.getId())) {
			return false;
		}

		if (types != null && !types.contains(widget.getType())) {
			return false;
		}

		if (texts != null && !texts.contains(widget.getText())) {
			return false;
		}

		if (actions != null && actions.stream().noneMatch(Predicates.texts(widget.getActions()))) {
			return false;
		}

		if (visible != null && visible != widget.isVisible()) {
			return false;
		}

		return super.test(widget);
	}
}
