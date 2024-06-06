package com.cwunder.recipe.ingredientquantity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.cwunder.recipe.ingredient.IngredientRepository;
import com.cwunder.recipe.recipe.Recipe;
import com.cwunder.recipe.recipe.RecipeRepository;
import com.cwunder.recipe.unit.UnitRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
@ActiveProfiles("test")
public class IngredientQuantityRepositoryTest {
    @Autowired
    private IngredientQuantityRepository repo;
    @Autowired
    private IngredientRepository ingrRepo;
    @Autowired
    private UnitRepository unitRepo;
    @Autowired
    private RecipeRepository recRepo;

    @Test
    public void testCreateIngredientQuantity() throws JsonProcessingException {
        // setup
        var newRec = new Recipe();
        newRec.setName("myrecipe");
        var rec = recRepo.save(newRec);
        var ing = ingrRepo.getReferenceById((long) 1);
        var unit = unitRepo.getReferenceById((long) 1);
        var ingQ = new IngredientQuantity();
        ingQ.setRecipe(rec);
        ingQ.setUnit(unit);
        ingQ.setIngredient(ing);

        // execute
        var newIngQ = repo.save(ingQ);

        // assert
        var foundIngQ = repo.findByPublicId(newIngQ.getPublicId());
        assertEquals(newIngQ, foundIngQ.orElseThrow(IllegalArgumentException::new));
    }
}
