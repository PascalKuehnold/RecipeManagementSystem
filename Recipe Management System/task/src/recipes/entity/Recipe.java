package recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Validated
public class Recipe {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appUser_id")
    @JsonIgnore
    private AppUser appUser;

    @JsonIgnore
    private String author;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be empty or blank")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Description cannot be empty or blank")
    private String description;

    @Column(nullable = false)
    @NotBlank(message = "Category cannot be empty or blank")
    private String category;

    @ElementCollection
    @NotEmpty
    @Size(min = 1, message = "At least one ingredient is required")
    private List<String> ingredients;

    @ElementCollection
    @NotEmpty
    @Size(min = 1, message = "At least one direction is required")
    private List<String> directions;

    @Column
    private LocalDateTime date;



    public Recipe() {
    }

    public Recipe(AppUser appUser, String name, String description, String category, List<String> ingredients, List<String> directions, LocalDateTime date) {
        this.appUser = appUser;
        this.name = name;
        this.description = description;
        this.category = category;
        this.ingredients = ingredients;
        this.directions = directions;
        this.date = date;
    }

    public Long getId() {
        return id;
    }


    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime lastEditedAt) {
        this.date = lastEditedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) && Objects.equals(name, recipe.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
