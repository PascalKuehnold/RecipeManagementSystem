package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.entity.AppUser;
import recipes.entity.Recipe;
import recipes.model.UserModel;
import recipes.repository.AppUserRepository;

import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService{

    AppUserRepository appUserRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<AppUser> registerNewUser(UserModel userModel) {

        // Check if the email is already in use
        Optional<AppUser> tempUser = appUserRepository.findAppUserByEmail(userModel.getEmail());

        // If the email exists, throw a DuplicateEmailException
        if (tempUser.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        // Create a new AppUser object
        AppUser user = new AppUser();

        // Set the user's email and password
        user.setEmail(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));

        // Set the user's role to ROLE_USER
        user.setAuthority("ROLE_USER");

        // Save the new user to the database
        appUserRepository.save(user);

        // Create a success response
        return ResponseEntity.ok().build();
    }

    @Override
    public AppUser getAppUserByName(String name) {
        Optional<AppUser> tempUser =
                appUserRepository.findAppUserByEmail(name);

        if(tempUser.isEmpty()){
            throw new UsernameNotFoundException("User was not found");
        }

        return tempUser.get();
    }

    @Override
    public void updateUserRecipes(AppUser user, Recipe recipe) {
        Optional<AppUser> tempUser =
                appUserRepository.findAppUserByEmail(user.getEmail());

        if(tempUser.isEmpty()){
            throw new UsernameNotFoundException("User was not found");
        }

        AppUser userToUpdate = tempUser.get();
        userToUpdate.addRecipe(recipe);

        appUserRepository.save(userToUpdate);
    }
}
