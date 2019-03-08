package me.vankka.placeholdersplus.common;

import me.vankka.placeholdersplus.common.model.PlaceholderLookupResult;

public interface IPlaceholderHook {

    PlaceholderLookupResult getPlaceholderReplacementFromHook(final String placeholder, final Object... extraObjects);
    String replacePlaceholdersFromHook(final String input, final Object... extraObjects);

    PlaceholderLookupResult getPlaceholderReplacement(final String placeholder, final Object... extraObjects);
    String replacePlaceholders(final String input, final Object... extraObjects);
}
