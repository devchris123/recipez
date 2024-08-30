"use client";

import { Card, List } from "@mantine/core";
import { useState } from "react";
import { FormActionResponse, RecipeInstruction } from "../../api/types";
import { ListButton } from "./listButton";
import { RecipeInstructionListItem } from "./recipeInstructionListItem";
import { postRecipeInstruction } from "../../api/postRecipeInstruction";
import { deleteRecipeInstruction } from "../../api/deleteRecipeInstruction";
import { putRecipeInstruction } from "../../api/putRecipeInstruction";
import { RecipeInstructionApiProvider } from "./recipeInstructionApiContext";
import { RecipeInstructionForm } from "./recipeInstructionForm";

export const RecipeInstructions = ({
  recId,
  initialRecipeInstructions,
}: {
  recId: string;
  initialRecipeInstructions: RecipeInstruction[];
}) => {
  const [recInstr, setRecInstr] = useState(initialRecipeInstructions);

  const onCreateRI = async (e: FormData) => {
    return await postRecipeInstruction(recId, e);
  };
  const onCreateRISuccess = (ri: RecipeInstruction) => {
    setRecInstr((ris) => [...ris, ri]);
  };
  const onUpdateRI = async (id: string, e: FormData) => {
    return await putRecipeInstruction(id, e);
  };
  const onUpdateRISuccess = (updatedRI: RecipeInstruction) => {
    setRecInstr((iqs) =>
      iqs.map((iq) => (iq.publicId === updatedRI.publicId ? updatedRI : iq))
    );
  };
  const onDeleteRI = async (id: string) => {
    await deleteRecipeInstruction(id);
    setRecInstr((ris) => ris.filter((ri) => ri.publicId !== id));
  };

  return (
    <RecipeInstructionApiProvider onSubmit={onCreateRI}>
      <Card withBorder>
        {recInstr.length === 0 ? (
          <>
            <p>You haven&apos;t added any instructions for this recipe</p>
            <InstructionFormButton
              onSubmit={onCreateRI}
              onSuccess={onCreateRISuccess}
            />
          </>
        ) : (
          <List component="ol">
            {recInstr.map((ri) => {
              return (
                <RecipeInstructionListItem
                  key={ri.publicId}
                  ri={ri}
                  onDelete={() => onDeleteRI(ri.publicId)}
                  onUpdate={(fd: FormData) => onUpdateRI(ri.publicId, fd)}
                  onSuccess={onUpdateRISuccess}
                />
              );
            })}
            <InstructionFormButton
              onSubmit={onCreateRI}
              onSuccess={onCreateRISuccess}
            />
          </List>
        )}
      </Card>
    </RecipeInstructionApiProvider>
  );
};

const InstructionFormButton = ({
  onSubmit,
  onSuccess,
}: {
  onSubmit: (e: FormData) => Promise<FormActionResponse<RecipeInstruction>>;
  onSuccess: (ri: RecipeInstruction) => void;
}) => {
  const [addInstr, setAddInstr] = useState(false);

  return (
    <>
      {addInstr ? (
        <RecipeInstructionForm
          onCancel={() => setAddInstr(!addInstr)}
          onSubmit={onSubmit}
          onSuccess={onSuccess}
        />
      ) : (
        <ListButton onClick={() => setAddInstr(true)}>
          + Add instruction
        </ListButton>
      )}
    </>
  );
};
