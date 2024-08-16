"use client";
import { IconTrashX } from "@tabler/icons-react";
import { IconPencil } from "@tabler/icons-react";

import { Flex, ActionIcon } from "@mantine/core";
import { ReactNode } from "react";

export const RecipeListItem = ({
  children,
  onDelete,
  onUpdate,
}: {
  children: ReactNode;
  onDelete: () => void;
  onUpdate: () => void;
}) => {
  return (
    <>
      <Flex
        component={"li"}
        display="flex"
        direction="row"
        justify="space-between"
        style={{ padding: "5px 0", ":before": { content: "hell" } }}
        gap={{ xxs: "50px" }}
      >
        <div>{children}</div>
        <Flex gap="5px">
          <ActionIcon onClick={onUpdate} aria-label="Update">
            <IconPencil />
          </ActionIcon>
          <ActionIcon onClick={onDelete} aria-label="Delete">
            <IconTrashX />
          </ActionIcon>
        </Flex>
      </Flex>
    </>
  );
};
