package recipes.service;

import org.springframework.http.ResponseEntity;
import recipes.entity.AppUser;
import recipes.entity.Recipe;
import recipes.model.UserModel;

public interface AppUserService {
    ResponseEntity<AppUser> registerNewUser(UserModel userModel);
    AppUser getAppUserByName(String name);

    void updateUserRecipes(AppUser user, Recipe recipe);
}
