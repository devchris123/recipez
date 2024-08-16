"use client";
import { Card, Flex } from "@mantine/core";
import classes from "./recipe.module.css";
import { RecipeData } from "../../api/types";
import { RecipeIngredients } from "./recipeIngredients";
import { RecipeInstructions } from "./recipeInstructions";

export const Recipe = ({ recipe }: { recipe: RecipeData }) => {
  return (
    <Flex direction="column" align="stretch">
      <div className={classes.h2}>
        <h2 color="primaryColor">{recipe.name}</h2>
      </div>
      <Card display="flex" style={{ gap: "10px" }}>
        <Card.Section display="flex" style={{ justifyContent: "center" }}>
          <h3>Ingredients</h3>
        </Card.Section>
        <RecipeIngredients
          recId={recipe.publicId}
          initialIngredients={recipe.ingredientQuantities}
        />
        <Card.Section display="flex" style={{ justifyContent: "center" }}>
          <h3>Steps</h3>
        </Card.Section>
        <RecipeInstructions
          recId={recipe.publicId}
          initialRecipeInstructions={recipe.recipeInstructions}
        ></RecipeInstructions>
      </Card>
    </Flex>
  );
};
