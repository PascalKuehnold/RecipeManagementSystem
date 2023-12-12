package recipes.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import recipes.entity.Recipe;

import java.util.List;
import java.util.Map;

public interface RecipeService {
    ResponseEntity<Recipe> getRecipeById(Long id);

    ResponseEntity<List<Recipe>> getAllRecipes();
    ResponseEntity<Map<String, Long>> addRecipe(UserDetails details, Recipe recipe);

    ResponseEntity<String> deleteRecipeById(UserDetails details, Long id);

    ResponseEntity<Recipe> updateRecipeById(UserDetails details, Long id, Recipe reqRecipe);

    ResponseEntity<List<Recipe>> getRecipeByNameOrCategory(String category, String name);
}
