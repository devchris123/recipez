import { notFound } from "next/navigation";
import { fetchRecipe } from "../api/fetchRecipe";
import { Recipe } from "./_components/recipe";
import { NextAuthProvider } from "@/app/nextAuthProvider";
import { Flex } from "@mantine/core";

export default async function RecipePage({
  params,
}: {
  params: { id: string };
}) {
  const d = await fetchRecipe(params.id);
  if (!d.result || d.error) {
    return notFound();
  }
  return (
    <Flex component="main" direction="column" align="center">
      <NextAuthProvider>
        <div style={{ maxWidth: "500px" }}>
          <Recipe recipe={d.result} />
        </div>
      </NextAuthProvider>
    </Flex>
  );
}
