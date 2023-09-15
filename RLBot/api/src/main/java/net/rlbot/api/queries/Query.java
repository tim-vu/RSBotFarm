package net.rlbot.api.queries;

import lombok.AccessLevel;
import lombok.Setter;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Query<TElement, TQuery, TQueryResult> implements Predicate<TElement>
{
	@Setter(AccessLevel.PROTECTED)
	protected Supplier<List<TElement>> supplier;

	private Predicate<TElement> customFilter = null;

	protected Query(Supplier<List<TElement>> supplier)
	{
		this.supplier = supplier;
	}

	public TQueryResult results()
	{
		return results(supplier.get().stream().filter(this).collect(Collectors.toList()));
	}

	public TQuery filter(Predicate<TElement> filter)
	{
		if (customFilter != null)
		{
			Predicate<? super TElement> old = customFilter;
			customFilter = TElement -> old.test(TElement) && filter.test(TElement);
		}
		else
		{
			customFilter = filter;
		}

		return (TQuery) this;
	}

	@Override
	public boolean test(TElement telement)
	{
		return customFilter == null || customFilter.test(telement);
	}

	protected abstract TQueryResult results(List<TElement> list);
}
