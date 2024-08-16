import { createContext, ReactNode } from "react";

interface IRecipeInstructionApiContext {
  onSubmit: (e: FormData) => void;
}

export const RecipeInstructionApiContext =
  createContext<IRecipeInstructionApiContext>({
    onSubmit: () => null,
  });

export const RecipeInstructionApiProvider = ({
  onSubmit,
  children,
}: {
  onSubmit: (e: FormData) => void;
  children: ReactNode;
}) => {
  return (
    <RecipeInstructionApiContext.Provider value={{ onSubmit }}>
      {children}
    </RecipeInstructionApiContext.Provider>
  );
};
