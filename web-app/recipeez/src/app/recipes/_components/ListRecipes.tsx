"use client";

import { useState } from "react";
import { FormActionResponse, Recipe, RecipeData } from "../api/types";
import ListRecipe from "./listRecipe";
import { RecipeFormButton } from "./recipeForm";
import { postRecipe } from "../api/postRecipe";
import { deleteRecipe } from "../api/deleteRecipe";
import { notifications } from "@mantine/notifications";

export default function ListRecipes({
  initialRecipes,
}: {
  initialRecipes: RecipeData[];
}) {
  const [recipes, setRecipes] = useState(initialRecipes);

  const onSubmitRecipe = async (e: FormData) => {
    return await postRecipe(e);
  };

  const onCreateRecipeSuccess = async (rec: RecipeData) => {
    setRecipes((recs) => [rec, ...recs]);
  };

  const onDeleteRecipe = async (id: string) => {
    const { error } = await deleteRecipe(id);
    if (!error) {
      setRecipes((recs) => recs.filter((r) => r.publicId !== id));
      notifications.show({
        message:
          "Successfully deleted recipe " +
          recipes.find((r) => r.publicId === id)?.name,
        color: "green",
      });
    } else {
      notifications.show({
        message:
          "We are sorry, we couldn't delete your recipe. Please try again later.",
        color: "red",
      });
    }
  };

  return (
    <>
      <RecipeFormButton
        onSubmit={onSubmitRecipe}
        onSuccess={onCreateRecipeSuccess}
      />
      {recipes?.map((rec) => {
        return (
          <ListRecipe key={rec.publicId} rec={rec} onDelete={onDeleteRecipe} />
        );
      })}
    </>
  );
}
