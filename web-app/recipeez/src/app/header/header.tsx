"use client";

import {
  Group,
  Button,
  Divider,
  Box,
  Burger,
  Drawer,
  ScrollArea,
  rem,
  Title,
  Skeleton,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import classes from "./header.module.css";
import { signIn, signOut, useSession } from "next-auth/react";
import Link from "next/link";

export default function Header() {
  const [drawerOpened, { toggle: toggleDrawer, close: closeDrawer }] =
    useDisclosure(false);
  return (
    <Box>
      <header className={classes.header}>
        <Group justify="space-between" h="100%">
          <Title c="yellow.9">Recipeez</Title>
          <Group h="100%" gap={0} visibleFrom="sm">
            <a href="/" className={classes.link}>
              Home
            </a>
            <a href="/recipes" className={classes.link}>
              Recipes
            </a>
          </Group>
          <Group visibleFrom="sm">
            <LogInOutButtons toggleDrawer={toggleDrawer} />
          </Group>
          <Burger
            opened={drawerOpened}
            onClick={toggleDrawer}
            hiddenFrom="sm"
          />
        </Group>
      </header>
      <Drawer
        opened={drawerOpened}
        onClose={closeDrawer}
        size="100%"
        padding="md"
        title="Navigation"
        hiddenFrom="sm"
        zIndex={1000000}
      >
        <ScrollArea h={`calc(100vh - ${rem(80)})`} mx="-md">
          <Divider my="sm" />
          <a href="/" className={classes.link}>
            Home
          </a>
          <a href="/recipes" className={classes.link}>
            Recipes
          </a>
          <Divider my="sm" />
          <Group justify="center" grow pb="xl" px="md">
            <LogInOutButtons toggleDrawer={toggleDrawer} />
          </Group>
        </ScrollArea>
      </Drawer>
    </Box>
  );
}

const LogInOutButtons = ({ toggleDrawer }: { toggleDrawer: () => void }) => {
  const sess = useSession();

  if (sess.status === "loading") {
    return <Skeleton width={100} height={50} />;
  }
  return (
    <>
      {!sess.data && (
        <Button
          variant="default"
          onClick={async () =>
            await signIn("Credentials", { callbackUrl: "/" })
          }
        >
          Log in
        </Button>
      )}
      {sess.data ? (
        <Button
          variant="outline"
          onClick={async () => await signOut({ callbackUrl: "/" })}
        >
          Log out
        </Button>
      ) : (
        <Button
          component={Link}
          variant="outline"
          prefetch={true}
          href={"/register"}
          onClick={() => toggleDrawer()}
        >
          Sign up
        </Button>
      )}
    </>
  );
};
