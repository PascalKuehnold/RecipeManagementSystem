package recipes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import recipes.entity.Recipe;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api")
public class RecipeController {
    RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<Recipe>> getAllRecipes(){
        return recipeService.getAllRecipes();
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id){
        return recipeService.getRecipeById(id);
    }

    @GetMapping("/recipe/search")
    public ResponseEntity<List<Recipe>> getRecipesByNameOrCategory(@RequestParam @Nullable String category, @RequestParam @Nullable String name){
        return recipeService.getRecipeByNameOrCategory(category, name);
    }

    @PostMapping("/recipe/new")
    public ResponseEntity<Map<String, Long>> addRecipe(@AuthenticationPrincipal UserDetails details, @Valid @RequestBody Recipe reqRecipe){
        return recipeService.addRecipe(details, reqRecipe);
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(@AuthenticationPrincipal UserDetails details, @PathVariable Long id, @Valid @RequestBody Recipe reqRecipe) {
        return recipeService.updateRecipeById(details, id, reqRecipe);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@AuthenticationPrincipal UserDetails details, @PathVariable Long id){
        return recipeService.deleteRecipeById(details, id);
    }

}
