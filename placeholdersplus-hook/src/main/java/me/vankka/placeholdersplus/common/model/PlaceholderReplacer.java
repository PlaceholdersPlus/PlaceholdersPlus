package me.vankka.placeholdersplus.common.model;

import java.util.List;

public interface PlaceholderReplacer {

    @SuppressWarnings("unused")
    default PlaceholderLookupResult lookup(final String placeholder, final List<Object> extraObjects) {
        return new PlaceholderLookupResult(PlaceholderLookupResult.ResultType.UNKNOWN_PLACEHOLDER);
    }
}
