package recipes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Validated
public class AppUser {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Pattern(regexp = ".+@.+\\..+")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
    private String authority;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipe> createdRecipes = new ArrayList<>();


    public AppUser() {
    }

    public AppUser(String email, String password, String authority, List<Recipe> createdRecipes) {
        this.email = email;
        this.password = password;
        this.authority = authority;
        this.createdRecipes = createdRecipes;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public List<Recipe> getCreatedRecipes() {
        return createdRecipes;
    }

    public void setCreatedRecipes(List<Recipe> createdRecipes) {
        this.createdRecipes = createdRecipes;
    }


    public void addRecipe(Recipe recipe){
        this.createdRecipes.add(recipe);
    }
}
