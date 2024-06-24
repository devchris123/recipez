package com.cwunder.recipe.recipe;

// Java SE
import java.io.IOException;

// Junit
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

// Annotations
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

// Spring Boot Test
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

// Recipe
import com.cwunder.recipe._shared.IdGenerator;
import com.cwunder.recipe._test.WithMockCustomUser;
import com.cwunder.recipe.user.User;
import com.cwunder.recipe.user.UserRepository;

@SpringBootTest
@AutoConfigureJsonTesters
@ActiveProfiles("test")
public class RecipeRepositoryTest {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RecipeRepository recRepo;

    @Autowired
    private JacksonTester<Recipe> json;

    @Test
    @WithMockCustomUser(username = "user", password = "pw")
    public void testCreateRecipe() throws IOException {
        // setup
        var user = new User(IdGenerator.genId(), "user", "pw", true);
        user = userRepo.save(user);
        var user1 = new User(IdGenerator.genId(), "user1", "pw", true);
        userRepo.save(user1);
        var rec = new Recipe();
        rec.setName("recipe");
        rec.setDescription("description");
        rec.setUser(user);

        // execute
        var rec0 = recRepo.save(rec);
        var rec1 = recRepo.save(rec);

        // assert
        // Refetch the entity
        rec0 = recRepo.getByPublicId(rec0.getPublicId());
        rec1 = recRepo.getByPublicId(rec1.getPublicId());
        var recJson = json.write(rec0);
        assertThat(recJson).hasJsonPathStringValue("@.name");
        assertThat(recJson).hasJsonPathStringValue("@.description");
        assertThat(recJson).extractingJsonPathArrayValue("@.ingredientQuantities").isEmpty();
    }
}
