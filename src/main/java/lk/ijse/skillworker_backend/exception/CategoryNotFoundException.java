package lk.ijse.skillworker_backend.exception;

public class CategoryNotFoundException extends  RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
