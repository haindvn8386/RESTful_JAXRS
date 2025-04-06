package restful.jaxrs.util;

public final class utility {

    // Hằng số (constants)
    public static final String ERROR_ID_NULL_OR_EMPTY = "The ID cannot be null or empty";
    public static final String ERROR_ID_INVALID_FORMAT = "The ID must be a valid number, got: ";
    public static final String ERROR_ID_NON_POSITIVE = "The ID must be a positive number";

    // Private constructor để ngăn khởi tạo đối tượng
    private utility() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Hàm tĩnh để validate và convert ID
    public static Long validateAndConvertId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(ERROR_ID_NULL_OR_EMPTY);
        }

        try {
            Long projectId = Long.parseLong(id);
            if (projectId <= 0) {
                throw new IllegalArgumentException(ERROR_ID_NON_POSITIVE);
            }
            return projectId;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_ID_INVALID_FORMAT + id, e);
        }
    }
}
