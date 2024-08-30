"use client";

import { useState } from "react";
import { RecipeInstruction, FormActionResponse } from "../../api/types";
import { RecipeListItem } from "./recipeListItem";
import classes from "./recipeListItem.module.css";
import { RecipeInstructionForm } from "./recipeInstructionForm";

export const RecipeInstructionListItem = ({
  ri,
  onUpdate,
  onSuccess,
  onDelete,
}: {
  ri: RecipeInstruction;
  onUpdate: (fd: FormData) => Promise<FormActionResponse<RecipeInstruction>>;
  onSuccess: (d: RecipeInstruction) => void;
  onDelete: (id: string) => void;
}) => {
  const [openUpdate, setOpenUpdate] = useState(false);
  return (
    <>
      <RecipeListItem
        onDelete={() => onDelete(ri.publicId)}
        onUpdate={() => setOpenUpdate(!openUpdate)}
      >
        <div>{ri.description}</div>
      </RecipeListItem>
      {openUpdate && (
        <li className={classes.formLi}>
          <RecipeInstructionForm
            onCancel={() => setOpenUpdate(!openUpdate)}
            onSubmit={onUpdate}
            onSuccess={onSuccess}
            ri={ri}
          />
        </li>
      )}
    </>
  );
};
