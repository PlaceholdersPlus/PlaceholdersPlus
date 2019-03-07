package me.vankka.placeholdersplus.common.object;

public class PlaceholderLookupResult {
    private final String replacement;
    private final ResultType resultType;

    public PlaceholderLookupResult(String replacement) {
        this.replacement = replacement;
        this.resultType = ResultType.SUCCESS;
    }

    public PlaceholderLookupResult(ResultType resultType) {
        this.replacement = null;
        this.resultType = resultType;
    }

    public String getReplacement() {
        return replacement;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public enum ResultType {
        SUCCESS,
        DATA_NOT_LOADED,
        UNKNOWN_PLACEHOLDER
    }
}

