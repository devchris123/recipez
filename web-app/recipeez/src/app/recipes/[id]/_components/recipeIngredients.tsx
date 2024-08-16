"use client";

import { Card, List } from "@mantine/core";
import { useState } from "react";
import { FormActionResponse, IngredientQuantity } from "../../api/types";
import { ListButton } from "./listButton";
import { RecipeIngredientForm } from "./recipeIngredientForm";
import { postIngredientQuantity } from "../../api/postIngredientQuantity";
import { deleteIngredientQuantity } from "../../api/deleteIngredientQuantity";
import { putIngredientQuantity } from "../../api/putIngredientQuantity";
import { RecipeIngredientListItem } from "./recipeIngredientListItem";

export const RecipeIngredients = ({
  recId,
  initialIngredients,
}: {
  recId: string;
  initialIngredients: IngredientQuantity[];
}) => {
  const [ingredients, setIngredients] = useState(initialIngredients);

  const onSubmitIQ = async (e: FormData) => {
    return await postIngredientQuantity(recId, e);
  };

  const onCreateSuccess = async (iq: IngredientQuantity) => {
    setIngredients((iqs) => [...iqs, iq]);
  };

  const onUpdateIQ = async (id: string, e: FormData) => {
    return await putIngredientQuantity(id, e);
  };

  const onUpdateSuccess = async (updatedIq: IngredientQuantity) => {
    setIngredients((iqs) =>
      iqs.map((iq) => (iq.publicId === updatedIq.publicId ? updatedIq : iq))
    );
  };

  const onDeleteIQ = async (id: string) => {
    await deleteIngredientQuantity(id);
    setIngredients((iqs) => iqs.filter((iq) => iq.publicId !== id));
  };

  return (
    <Card withBorder>
      {ingredients.length === 0 ? (
        <>
          <p>You haven&apos;t added any ingredients for this recipe</p>
          <IngredientFormButton
            onSubmit={onSubmitIQ}
            onSuccess={onCreateSuccess}
          />
        </>
      ) : (
        <List component="ul" listStyleType="circle">
          {ingredients.map((iq) => {
            return (
              <RecipeIngredientListItem
                key={iq.publicId}
                iq={iq}
                onUpdate={(fd) => onUpdateIQ(iq.publicId, fd)}
                onSuccess={onUpdateSuccess}
                onDelete={onDeleteIQ}
              />
            );
          })}
          <IngredientFormButton
            onSubmit={onSubmitIQ}
            onSuccess={onCreateSuccess}
          />
        </List>
      )}
    </Card>
  );
};

const IngredientFormButton = ({
  onSubmit,
  onSuccess,
}: {
  onSubmit: (e: FormData) => Promise<FormActionResponse<IngredientQuantity>>;
  onSuccess: (iq: IngredientQuantity) => void;
}) => {
  const [addInstr, setAddInstr] = useState(false);

  return (
    <>
      {addInstr ? (
        <RecipeIngredientForm
          onSubmit={onSubmit}
          onSuccess={onSuccess}
          onCancel={() => setAddInstr(false)}
        />
      ) : (
        <ListButton onClick={() => setAddInstr(true)}>
          + Add ingredient
        </ListButton>
      )}
    </>
  );
};
