package com.cwunder.recipe.ingredientquantity;

// Java SE
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.cwunder.recipe._test.TestFixture;
import com.cwunder.recipe._test.UserFixture;
import com.cwunder.recipe._test.WithMockCustomUser;
import com.cwunder.recipe.recipe.Recipe;
import com.cwunder.recipe.recipe.RecipeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
@Transactional
@WithMockCustomUser(username = "testuser")
@ActiveProfiles("test")
public class IngredientQuantityRepositoryTest {
    @Autowired
    private IngredientQuantityRepository repo;

    @Autowired
    private RecipeRepository recRepo;

    @Autowired
    private UserFixture userFxt;

    @Autowired
    private TestFixture testFixture;

    @BeforeEach
    public void setupEach() {
        testFixture.cleanDB();

    }

    @Test
    public void testCreateIngredientQuantity() throws JsonProcessingException {
        // setup
        var user = userFxt.createUser("testuser", "testpw");
        var newRec = new Recipe();
        newRec.setName("myrecipe");
        // We need to set the username to satisfy validation
        newRec.setUsername("testuser");
        newRec.setUser(user);
        var rec = recRepo.save(newRec);
        var ingQ = new IngredientQuantity();
        ingQ.setRecipe(rec);
        ingQ.setUnit("kg");
        ingQ.setIngredient("cheese");
        ingQ.setQuantity(new BigDecimal(10));

        // execute
        var newIngQ = repo.save(ingQ);

        // assert
        var foundIngQ = repo.findByPublicId(newIngQ.getPublicId());
        assertEquals(newIngQ, foundIngQ.orElseThrow(IllegalArgumentException::new));
    }
}
