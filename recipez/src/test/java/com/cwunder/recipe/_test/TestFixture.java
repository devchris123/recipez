package com.cwunder.recipe._test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestFixture {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void cleanDB() {
        jdbcTemplate.execute("DELETE FROM RecipeInstruction");
        jdbcTemplate.execute("DELETE FROM IngredientQuantity");
        jdbcTemplate.execute("DELETE FROM Recipe");
        jdbcTemplate.execute("DELETE FROM Authority");
        jdbcTemplate.execute("DELETE FROM User");
    }
}
