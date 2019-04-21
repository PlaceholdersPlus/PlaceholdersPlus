package me.vankka.placeholdersplus.common;

import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.model.PlaceholderLookupResult;
import me.vankka.placeholdersplus.common.model.PlaceholderReplacer;
import me.vankka.placeholdersplus.common.model.Placeholderable;

public interface IPlaceholderHook {

    /**
     * Adds placeholder replacers to this PlaceholderHook
     *
     * @param placeholderReplacers the replacers to be added
     */
    void addPlaceholderReplacers(PlaceholderReplacer... placeholderReplacers);

    /**
     * Removes placeholder replacers from this PlaceholderHook
     *
     * @param placeholderReplacers the replacers to be removed
     */
    void removePlaceholderReplacers(PlaceholderReplacer... placeholderReplacers);

    /**
     * Looks up a placeholder replacement from all available PlaceholderHook's replacers.
     *
     * @param placeholder The placeholder
     * @param extraObjects Objects to be used for placeholder replacements and {@link Placeholderable}s
     * @return The result of the placeholder lookup
     */
    PlaceholderLookupResult getPlaceholderReplacement(final String placeholder, final Object... extraObjects);

    /**
     * Looks up a placeholder replacement <b>only from this</b> PlaceholderHook's replacers.
     *
     * @param placeholder The placeholder
     * @param extraObjects Objects to be used for placeholder replacements and {@link Placeholderable}s
     * @return The result of the placeholder lookup
     */
    PlaceholderLookupResult getPlaceholderReplacementFromHook(final String placeholder, final Object... extraObjects);

    /**
     * Replaces placeholders from all available PlaceholderHook's replacers.
     *
     * @param input Input string
     * @param extraObjects Objects to be used for placeholder replacements and {@link Placeholderable}s
     * @return Output string with placeholders replaced
     */
    String replacePlaceholders(final String input, final Object... extraObjects);

    /**
     * Replaces placeholders <b>only from this</b> PlaceholderHook's replacers.
     *
     * @param input Input string
     * @param extraObjects Objects to be used for placeholder replacements and {@link Placeholderable}s
     * @return Output string with placeholders replaced
     */
    String replacePlaceholdersFromHook(final String input, final Object... extraObjects);

    /**
     * If the plugin is hooked (PlaceholderHook's from other plugins are available)
     *
     * @return weather or not the PlaceholdersPlus is hooked
     */
    boolean isPluginHooked();

    /**
     * Set the logger used for this PlaceholderHook
     *
     * @param logger {@link Logger} implementation
     */
    void setLogger(Logger logger);

    /**
     * Shuts down this PlaceholderHook
     */
    void shutdown();
}
