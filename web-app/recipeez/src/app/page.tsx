import { Title, Flex, Button } from "@mantine/core";
import Link from "next/link";
import { getServerSessionWithConf } from "./api/auth/[...nextauth]/conf";
import LoginButton from "./LogInButton";

export default async function Home() {
  const sess = await getServerSessionWithConf();
  return (
    <Flex direction="column" align="center" gap="10px" component="main">
      {sess ? (
        <>
          <Title order={2} c="primary" style={{ textTransform: "capitalize" }}>
            Start with Recipeez
          </Title>
          <Button
            component={Link}
            variant="outline"
            prefetch={true}
            href={"/recipes"}
          >
            Recipes
          </Button>
        </>
      ) : (
        <>
          <Title order={2}>Log in now</Title>
          <LoginButton />
        </>
      )}
    </Flex>
  );
}
