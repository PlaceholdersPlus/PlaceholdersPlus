package me.vankka.placeholdersplus.common;

import me.vankka.placeholdersplus.common.model.PlaceholderLookupResult;
import me.vankka.placeholdersplus.hook.PlaceholderHook;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"unused", "Duplicates"})
public class PlaceholderService {
    private static final List<IPlaceholderHook> hooks = new LinkedList<>();

    /**
     * Called internally from PlaceholderHooks. This method should <b>not</b> be called manually.
     *
     * @param hook the hook to be added
     */
    public static void hook(IPlaceholderHook hook) {
        if (!hooks.contains(hook))
            hooks.add(hook);
    }

    /**
     * Unhooks the PlaceholderHook.
     *
     * @param hook the hook to be removed
     */
    public static void unhook(IPlaceholderHook hook) {
        hooks.remove(hook);
    }

    /**
     * Looks up the placeholder from all available PlaceholderHooks's replacers.
     * It is recommended to use {@link PlaceholderHook#getPlaceholderReplacement(String, Object...)} instead
     *
     * @param placeholder The placeholder to be looked up
     * @param extraObjects Objects to be used for placeholder replacements and {@link me.vankka.placeholdersplus.common.model.Placeholderable}s
     * @return Lookup result
     */
    public static PlaceholderLookupResult getPlaceholderReplacement(final String placeholder, final Object... extraObjects) {
        PlaceholderLookupResult bestNonSuccessfulResult = null;
        for (IPlaceholderHook hook : hooks) {
            PlaceholderLookupResult result = hook.getPlaceholderReplacementFromHook(placeholder, extraObjects);

            if (result != null) {
                switch (result.getResultType()) {
                    case SUCCESS:
                        return result;
                    case DATA_NOT_LOADED:
                        bestNonSuccessfulResult = result;
                }
            }
        }

        return bestNonSuccessfulResult != null ? bestNonSuccessfulResult : new PlaceholderLookupResult(PlaceholderLookupResult.ResultType.UNKNOWN_PLACEHOLDER);
    }

    /**
     * Replaces placeholders from all available PlaceholderReplacers.
     * It is recommended to use {@link PlaceholderHook#replacePlaceholders(String, Object...)} instead
     *
     * @param input Input string
     * @param extraObjects Objects to be used for placeholder replacements and {@link me.vankka.placeholdersplus.common.model.Placeholderable}s
     * @return Output string with placeholders replaced
     */
    public static String replacePlaceholders(final String input, final Object... extraObjects) {
        String output = input;
        for (IPlaceholderHook hook : hooks)
            output = hook.replacePlaceholdersFromHook(output, extraObjects);

        return output;
    }
}
