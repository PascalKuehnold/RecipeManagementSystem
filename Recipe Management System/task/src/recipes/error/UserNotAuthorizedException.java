package recipes.error;

public class UserNotAuthorizedException extends RuntimeException {

    public UserNotAuthorizedException() {
        super("User is not authorized to delete this recipe");
    }
}