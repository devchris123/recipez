import { RecipeData } from "../api/types";
import { Button, Card, Title, Center, Flex } from "@mantine/core";
import Image from "next/image";
import Link from "next/link";

export default function ListRecipe({
  rec,
  onDelete,
}: {
  rec: RecipeData;
  onDelete: (id: string) => void;
}) {
  const src = "";

  return (
    <Card color="primary" key={rec.publicId} withBorder>
      <Flex direction={{ base: "column", sm: "row" }} gap="10px">
        {src !== "" ? (
          <Image src={src} alt="a recipe" width="200" height="150" />
        ) : (
          <Center style={{ border: "1px solid black" }} w="300" h={"150"}>
            <div>no image</div>
          </Center>
        )}
        <Flex direction={{ base: "column" }} flex="1">
          <Flex direction={{ base: "row" }} justify="space-between">
            <Title c="yellow.9" order={3}>
              {rec.name}
            </Title>
            <Button onClick={() => onDelete(rec.publicId)} color="red">
              Delete recipe
            </Button>
          </Flex>
          <p>{rec.description !== "" ? rec.description : "no description"}</p>
        </Flex>
      </Flex>
      <Button
        m="10px 0 0 0"
        component={Link}
        variant="outline"
        prefetch={true}
        href={`/recipes/${rec.publicId}`}
      >
        {rec.name}
      </Button>
    </Card>
  );
}
