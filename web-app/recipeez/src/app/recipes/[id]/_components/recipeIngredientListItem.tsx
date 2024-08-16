"use client";

import { useState } from "react";
import { FormActionResponse, IngredientQuantity } from "../../api/types";
import { RecipeListItem } from "./recipeListItem";
import { RecipeIngredientForm } from "./recipeIngredientForm";
import classes from "./recipeListItem.module.css";

export const RecipeIngredientListItem = ({
  iq,
  onUpdate,
  onSuccess,
  onDelete,
}: {
  iq: IngredientQuantity;
  onUpdate: (fd: FormData) => Promise<FormActionResponse<IngredientQuantity>>;
  onSuccess: (iq: IngredientQuantity) => void;
  onDelete: (id: string) => void;
}) => {
  const [openUpdate, setOpenUpdate] = useState(false);

  return (
    <>
      <RecipeListItem
        onDelete={() => onDelete(iq.publicId)}
        onUpdate={() => setOpenUpdate(!openUpdate)}
      >
        <div>
          {iq.quantity} {iq.unit} {iq.ingredient}
        </div>
      </RecipeListItem>
      {openUpdate && (
        <li className={classes.formLi}>
          <RecipeIngredientForm
            onCancel={() => setOpenUpdate(!openUpdate)}
            onSubmit={onUpdate}
            onSuccess={onSuccess}
            iq={iq}
          />
        </li>
      )}
    </>
  );
};
