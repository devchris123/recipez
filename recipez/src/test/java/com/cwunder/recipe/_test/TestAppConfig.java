package com.cwunder.recipe._test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.cwunder.recipe._config.BaseConfig;

@Configuration
@Import(BaseConfig.class)
@Profile("test")
public class TestAppConfig {
    @Autowired
    DataSource dataSource;

    // Since in testing, the sql scripts are not executed due to dialect
    // discrepances
    // and the database is populated dynamically through entity annotations
    // we create the user schema via an extra script
    void configureTestDatabase() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(
                new ClassPathResource("0000_recipes.sql"));
        populator.execute(dataSource);
    }

    @Bean
    UserFixture userFixture() {
        return new UserFixture();
    }

    @Bean
    TestFixture testFixture() {
        return new TestFixture();
    }

    @Bean
    RecipeFixture recipeFixture() {
        return new RecipeFixture();
    }

}
