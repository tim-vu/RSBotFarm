package net.rlbot.api.common;

import net.rlbot.api.adapter.common.EntityNameable;
import net.rlbot.api.adapter.common.Identifiable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class Predicates {

    public static <T extends EntityNameable> Predicate<T> names(String... names) {
        return n -> {
            for(var name : names) {
                if(!n.getName().equals(name))
                {
                    continue;
                }

                return true;
            }

            return false;
        };
    }

    public static <T extends Identifiable> Predicate<T> ids(int... ids) {
        return i -> {
            for(var id : ids) {
                if(i.getId() != id)
                {
                    continue;
                }

                return true;
            }

            return false;
        };
    }

    public static <T extends Identifiable> Predicate<T> ids(Collection<Integer> ids) {
        return i -> ids.contains(i.getId());
    }

    public static <T> Predicate<T> always() {
        return x -> true;
    }


    public static Predicate<String> texts(String... texts)
    {
        return t ->
        {
            for (String text : texts)
            {
                if (t != null && t.equals(text))
                {
                    return true;
                }
            }

            return false;
        };
    }

    public static Predicate<String> textContains(String subString, boolean caseSensitive)
    {
        return t ->
        {
            if (caseSensitive)
            {
                return t.contains(subString);
            }
            else
            {
                return t.toLowerCase().contains(subString.toLowerCase());
            }
        };
    }

    public static Predicate<String> textContains(String subString)
    {
        return textContains(subString, true);
    }

    public static <T extends EntityNameable> Predicate<T> nameContains(String subString, boolean caseSensitive)
    {
        return t ->
        {
            if (t.getName() == null)
            {
                return false;
            }

            if (caseSensitive)
            {
                return t.getName().contains(subString);
            }
            else
            {
                return t.getName().toLowerCase().contains(subString.toLowerCase());
            }
        };
    }

    public static <T extends EntityNameable> Predicate<T> nameContains(String subString)
    {
        return nameContains(subString, true);
    }

    public static <T extends EntityNameable> Predicate<T> nameContains(Collection<String> subStrings, boolean caseSensitive)
    {
        return t ->
        {
            if (t.getName() == null)
            {
                return false;
            }

            for (String subString : subStrings)
            {
                if (caseSensitive)
                {
                    if (t.getName().contains(subString))
                    {
                        return true;
                    }
                }
                else
                {
                    if (t.getName().toLowerCase().contains(subString.toLowerCase()))
                    {
                        return true;
                    }
                }
            }

            return false;
        };
    }
}
