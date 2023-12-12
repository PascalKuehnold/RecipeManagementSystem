package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import recipes.entity.AppUser;
import recipes.entity.Recipe;
import recipes.error.RecipeNotFoundException;
import recipes.error.UserNotAuthorizedException;
import recipes.repository.RecipeRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {

    AppUserService appUserService;
    RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, AppUserService appUserService) {
        this.recipeRepository = recipeRepository;
        this.appUserService = appUserService;
    }

    @Override
    public ResponseEntity<Recipe> getRecipeById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);

        return recipe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Recipe>> getAllRecipes() {

        List<Recipe> recipes = recipeRepository.findAll();

        System.out.println(recipes);
        return ResponseEntity.ok(recipes);
    }


    @Override
    public ResponseEntity<Map<String, Long>> addRecipe(UserDetails details, Recipe recipe) {
        String username = details.getUsername();

        try {
            recipe.setDate(LocalDateTime.now());

            AppUser user = appUserService.getAppUserByName(username);

            recipe.setAppUser(user);
            recipe.setAuthor(user.getEmail());
            Recipe savedRecipe = recipeRepository.save(recipe);
            return ResponseEntity.ok().body(Map.of("id", savedRecipe.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<String> deleteRecipeById(UserDetails details, Long id) {
        try {
            Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
            if (isNotAuthorOfRecipe(details, recipe)) {
                // User is not authorized to delete the recipe
                throw new UserNotAuthorizedException();
            }
            recipeRepository.delete(recipe);
            // Recipe deleted successfully
            return ResponseEntity.noContent().build();
        } catch (RecipeNotFoundException e) {
            // Recipe not found
            return ResponseEntity.notFound().build();
        } catch (UserNotAuthorizedException e) {
            // User not authorized to delete the recipe
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<Recipe> updateRecipeById(UserDetails details, Long id, Recipe reqRecipe) {
        try {
            Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
            if (isNotAuthorOfRecipe(details, recipe)) {
                // User is not authorized to update the recipe
                throw new UserNotAuthorizedException();
            }

            // Update the recipe data with the new data from reqRecipe
            recipe.setName(reqRecipe.getName());
            recipe.setDescription(reqRecipe.getDescription());
            recipe.setIngredients(reqRecipe.getIngredients());
            recipe.setDirections(reqRecipe.getDirections());
            recipe.setCategory(reqRecipe.getCategory());
            recipe.setDate(LocalDateTime.now());

            // Save the updated recipe to the database
            Recipe updatedRecipe = recipeRepository.save(recipe);

            return ResponseEntity.noContent().build();

        } catch (RecipeNotFoundException e) {
            // Recipe not found
            return ResponseEntity.notFound().build();
        } catch (UserNotAuthorizedException e) {
            // User not authorized to update the recipe
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<List<Recipe>> getRecipeByNameOrCategory(String category, String name) {
        if (category == null && name == null) {
            return ResponseEntity.badRequest().build();
        }

        if (category == null || category.isEmpty()) {
            List<Recipe> recipesByName = recipeRepository.findByNameContainingIgnoreCase(name);

            // Sort the recipes by date
            recipesByName.sort(Comparator.comparing(Recipe::getDate).reversed());
            return ResponseEntity.ok(recipesByName);
        }

        if (name == null || name.isEmpty()) {
            List<Recipe> recipesByCategory = recipeRepository.findByCategoryIgnoreCase(category);
            // Sort the recipes by date
            recipesByCategory.sort(Comparator.comparing(Recipe::getDate).reversed());

            return ResponseEntity.ok(recipesByCategory);
        }

        List<Recipe> recipes = recipeRepository.findByCategoryIgnoreCaseAndNameIgnoreCase(category, name);
        // Sort the recipes by date
        recipes.sort(Comparator.comparing(Recipe::getDate).reversed());
        return ResponseEntity.ok().body(recipes);
    }


    private boolean isNotAuthorOfRecipe(UserDetails details, Recipe tempRecipe) {
        return !Objects.equals(tempRecipe.getAppUser().getId(), appUserService.getAppUserByName(details.getUsername()).getId());
    }


}
