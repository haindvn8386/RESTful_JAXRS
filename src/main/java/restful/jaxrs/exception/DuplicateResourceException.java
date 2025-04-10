package restful.jaxrs.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DuplicateResourceException extends RuntimeException {
    private final Map<String, String> errors;

    public DuplicateResourceException(Map<String, String> errors) {
        super(buildMessageFromErrors(errors)); // Message cho log hoặc debug
        this.errors = (errors != null) ? new HashMap<>(errors) : Collections.emptyMap();
    }

    public Map<String, String> getErrors() {
        return Collections.unmodifiableMap(errors);
    }

    private static String buildMessageFromErrors(Map<String, String> errors) {
        if (errors == null || errors.isEmpty()) {
            return "Duplicate resource detected";
        }
        return errors.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("; "));
    }
}