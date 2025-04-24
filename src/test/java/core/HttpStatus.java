package core;

public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    UNAUTHORIZED(401);

    private final int value;

    private HttpStatus(int value) {
        this.value = value;
    }

    public int code() {
        return value;
    }
}
