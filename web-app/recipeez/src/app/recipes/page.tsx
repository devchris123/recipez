import { Title, Flex, Center } from "@mantine/core";
import { fetchRecipes } from "./api/fetchRecipes";
import ListRecipes from "./_components/ListRecipes";

export default async function RecipesPage() {
  const data = await fetchRecipes();
  if (data.result === null || data.error) {
    throw Error("No recipe data");
  }
  const listData = data.result._embedded?.["ex:recipeList"];
  return (
    <Flex m="10px 0 0 0" component="main" direction="column" gap="10px">
      <Center>
        <Title c="primary.8" order={2} style={{ textTransform: "capitalize" }}>
          your recipeez
        </Title>
      </Center>
      <Flex direction="column" gap="20px" p="0 5px">
        <ListRecipes initialRecipes={listData ?? []} />
      </Flex>
    </Flex>
  );
}
