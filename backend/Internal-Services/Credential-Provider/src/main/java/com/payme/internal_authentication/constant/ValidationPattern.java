package com.payme.internal_authentication.constant;


public class ValidationPattern {
    public static final String CLIENT_NAME_PATTERN = "^[A-Za-z0-9 ]{8,}$";
    public static final String CLIENT_NAME_MESSAGE = """
            Client name must be at least 8 characters long and
            contain only letters, numbers, and spaces.
            """;

    public static final String BASE_URL_PATTERN = "^(https?)://[\\w.-]+(?::\\d{1,5})?$";
    public static final String BASE_URL_MESSAGE = """
            Base URL must start with http or https and contain a valid
            domain or hostname with optional port, but no path.
            """;

    public static final String ENDPOINT_PATTERN = "^[A-Za-z0-9/_-]{4,}$";
    public static final String ENDPOINT_MESSAGE = """
            Endpoint must be at least 4 characters long and contain only letters, numbers,
            underscores, hyphens, or slashes, with no protocol, host, or port.
            """;

}