package me.vankka.placeholdersplus.hook;

import me.vankka.placeholdersplus.common.logger.DefaultLogger;
import me.vankka.placeholdersplus.common.logger.Logger;
import me.vankka.placeholdersplus.common.object.PlaceholderLookupResult;
import me.vankka.placeholdersplus.common.object.PlaceholderReplacer;
import me.vankka.placeholdersplus.common.object.Placeholderable;
import me.vankka.placeholdersplus.common.object.Replacement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "unchecked", "Duplicates", "WeakerAccess"})
public class PlaceholderHook {

    private static final String unrelocatedClassName = new String(new byte[]{'m', 'e', '.'})
            + "vankka.placeholdersplus.hook.PlaceholderHook";
    private static PlaceholderHook instance;

    private final String placeholderServiceClassName = "me.vankka.placeholdersplus.common.PlaceholderService";
    private final List<PlaceholderReplacer> replacers = new LinkedList<>();
    private final Pattern pattern = Pattern.compile("%([^%]+)%");
    private final boolean pluginHooked;
    private Logger logger = new DefaultLogger();

    private PlaceholderHook() {
        this(null);
    }

    private PlaceholderHook(Logger logger) {
        if (logger != null)
            this.logger = logger;
        this.pluginHooked = hookPlatformPlugin();
        this.logger.info("PlaceholderHook loaded, plugin hooked: " + pluginHooked);
    }

    /**
     * Creates a new instance. It's recommended to use {@link PlaceholderHook#createInstance(Logger)} instead.
     *
     * @return the instance
     */
    public static PlaceholderHook createInstance() {
        return createInstance(null);
    }

    /**
     * Creates a new instance and sets the logger.
     *
     * @param logger logger to be used for this PlaceholderHook
     * @return the instance
     */
    public static PlaceholderHook createInstance(Logger logger) {
        if (PlaceholderHook.class.getName().equals(unrelocatedClassName))
            throw new RuntimeException("PlaceholderHook class not relocated, this is unsupported");

        if (logger != null)
            instance = new PlaceholderHook();
        else
            instance = new PlaceholderHook();

        return instance;
    }

    /**
     * Gets the PlaceholderHook instance. This class should always be relocated.
     */
    public static PlaceholderHook getInstance() {
        if (instance == null)
            throw new RuntimeException("Instance hasn't been created yet, use PlaceholderHook#createInstance");

        return instance;
    }

    /**
     * Adds placeholder replacers to this PlaceholderHook
     *
     * @param placeholderReplacers the replacers to be added
     */
    public void addPlaceholderReplacers(PlaceholderReplacer... placeholderReplacers) {
        for (PlaceholderReplacer placeholderReplacer : placeholderReplacers)
            if (!replacers.contains(placeholderReplacer))
                replacers.add(placeholderReplacer);
    }

    /**
     * Removes placeholder replacers from this PlaceholderHook
     *
     * @param placeholderReplacers the replacers to be removed
     */
    public void removePlaceholderReplacers(PlaceholderReplacer... placeholderReplacers) {
        replacers.removeAll(Arrays.asList(placeholderReplacers));
    }

    /**
     * Looks up a placeholder replacement from all available PlaceholderHook's replacers.
     *
     * @param placeholder The placeholder
     * @param extraObjects Objects to be used for placeholder replacements and {@link Placeholderable}s
     * @return The result of the placeholder lookup
     */
    public PlaceholderLookupResult getPlaceholderReplacement(final String placeholder, final Object... extraObjects) {
        if (pluginHooked) {
            try {
                Class placeholderService = Class.forName(placeholderServiceClassName);

                return (PlaceholderLookupResult) placeholderService
                        .getMethod("getPlaceholderReplacement", String.class, Object[].class)
                        .invoke(null, placeholder, extraObjects);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
        }

        return getPlaceholderReplacementFromHook(placeholder, extraObjects);
    }

    /**
     * Looks up a placeholder replacement <b>only from this</b> PlaceholderHook's replacers.
     *
     * @param placeholder The placeholder
     * @param extraObjects Objects to be used for placeholder replacements and {@link Placeholderable}s
     * @return The result of the placeholder lookup
     */
    public PlaceholderLookupResult getPlaceholderReplacementFromHook(final String placeholder, final Object... extraObjects) {
        PlaceholderLookupResult bestNonSuccessfulResult = null;

        for (Object placeholderable : extraObjects) {
            if (!(placeholderable instanceof Placeholderable))
                continue;

            List<Object> objects = new ArrayList<>(Arrays.asList(extraObjects));
            objects.add(placeholderable);

            PlaceholderLookupResult result = getResultFromClass(placeholder, placeholderable, objects);
            if (result != null) {
                switch (result.getResultType()) {
                    case SUCCESS:
                        return result;
                    case DATA_NOT_LOADED:
                        bestNonSuccessfulResult = result;
                }
            }
        }

        for (PlaceholderReplacer replacer : replacers) {
            PlaceholderLookupResult result = getResultFromClass(placeholder, replacer, new ArrayList<>(Arrays.asList(extraObjects)));
            if (result != null) {
                switch (result.getResultType()) {
                    case SUCCESS:
                        return result;
                    case DATA_NOT_LOADED:
                        bestNonSuccessfulResult = result;
                }
            }

            result = replacer.lookup(placeholder, new ArrayList<>(Arrays.asList(extraObjects)));

            if (result.getResultType() == PlaceholderLookupResult.ResultType.DATA_NOT_LOADED)
                bestNonSuccessfulResult = result;
            else if (result.getResultType() == PlaceholderLookupResult.ResultType.SUCCESS)
                return result;
        }

        if (bestNonSuccessfulResult != null)
            return bestNonSuccessfulResult;

        return new PlaceholderLookupResult(PlaceholderLookupResult.ResultType.UNKNOWN_PLACEHOLDER);
    }

    /**
     * Replaces placeholders from all available PlaceholderHook's replacers.
     *
     * @param input Input string
     * @param extraObjects Objects to be used for placeholder replacements and {@link Placeholderable}s
     * @return Output string with placeholders replaced
     */
    public String replacePlaceholders(final String input, final Object... extraObjects) {
        if (pluginHooked) {
            try {
                Class placeholderService = Class.forName(placeholderServiceClassName);

                return (String) placeholderService
                        .getMethod("replacePlaceholders", String.class, Object[].class)
                        .invoke(null, input, extraObjects);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
        }

        return replacePlaceholdersFromHook(input, extraObjects);
    }

    /**
     * Replaces placeholders <b>only from this</b> PlaceholderHook's replacers.
     *
     * @param input Input string
     * @param extraObjects Objects to be used for placeholder replacements and {@link Placeholderable}s
     * @return Output string with placeholders replaced
     */
    public String replacePlaceholdersFromHook(final String input, final Object... extraObjects) {
        Matcher matcher = pattern.matcher(input);

        String output = input;
        while (matcher.find()) {
            String placeholder = matcher.group(1);

            PlaceholderLookupResult result = getPlaceholderReplacementFromHook(placeholder, extraObjects);
            if (result == null)
                continue;

            if (result.getResultType() == PlaceholderLookupResult.ResultType.SUCCESS)
                output = output.replace("%" + placeholder + "%", result.getReplacement());
            else if (result.getResultType() == PlaceholderLookupResult.ResultType.DATA_NOT_LOADED)
                output = output.replace("%" + placeholder + "%", "Data not loaded");
        }

        return output;
    }

    /**
     * If the plugin is hooked (PlaceholderHook's from other plugins are available)
     *
     * @return weather or not the PlaceholdersPlus is hooked
     */
    public boolean isPluginHooked() {
        return pluginHooked;
    }

    /**
     * Set the logger used for this PlaceholderHook
     *
     * @param logger {@link Logger} implementation
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @SuppressWarnings("unchecked")
    private boolean hookPlatformPlugin() {
        try {
            Class placeholderService = Class.forName(placeholderServiceClassName);
            placeholderService.getMethod("hook", PlaceholderHook.class).invoke(null, this);

            return true;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            return false;
        }
    }

    private PlaceholderLookupResult getResultFromClass(String placeholder, Object object, List<Object> extraObjects) {
        for (Field field : object.getClass().getFields()) {
            Replacement replacement = field.getAnnotation(Replacement.class);
            if (replacement == null || !replacement.placeholder().equals(placeholder))
                continue;

            try {
                if (!field.isAccessible())
                    field.setAccessible(true);

                return new PlaceholderLookupResult(field.get(object).toString());
            } catch (IllegalAccessException exception) {
                logger.error("Could not access field: " + field.getName()
                        + ", in: " + object.getClass().getName(), exception);
            }
        }

        for (Method method : object.getClass().getMethods()) {
            Replacement replacement = method.getAnnotation(Replacement.class);
            if (replacement == null  || !replacement.placeholder().equals(placeholder))
                continue;

            Object[] params = new Object[method.getParameterCount()];
            for (int param = 0; param < method.getParameterCount(); param++) {
                Parameter parameter = method.getParameters()[param];

                Object parameterValue = null;
                for (Object o : extraObjects) {
                    if (parameter.getType().isInstance(o)) {
                        parameterValue = parameter.getType().cast(o);
                        break;
                    }
                }

                if (parameterValue == null) {
                    params = null;
                    break;
                }

                params[param] = parameterValue;
            }

            if (params == null)
                continue;

            try {
                if (!method.isAccessible())
                    method.setAccessible(true);

                return new PlaceholderLookupResult(method.invoke(object, params).toString());
            } catch (IllegalAccessException | InvocationTargetException exception) {
                logger.error("Could not access method: " + method.getName()
                        + ", in: " + object.getClass().getName(), exception);
            }
        }

        return null;
    }
}
