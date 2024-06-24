package com.cwunder.recipe._test;

import org.springframework.beans.factory.annotation.Autowired;

import com.cwunder.recipe.recipe.Recipe;
import com.cwunder.recipe.recipe.RecipeRepository;
import com.cwunder.recipe.user.User;

public class RecipeFixture {
    @Autowired
    private RecipeRepository recRepo;

    public Recipe createRecipe(User newUser) {
        var newRec = new Recipe();
        newRec.setName("myrecipe");
        // We need to set the username to satisfy validation
        newRec.setUsername(newUser.getUsername());
        newRec.setUser(newUser);
        return recRepo.save(newRec);
    }
}
