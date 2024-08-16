"use client";

import { Button } from "@mantine/core";

import classes from "./listButton.module.css";

export const ListButton = Button.withProps({
  component: "li",
  className: classes.button,
  size: "xs",
  styles: {
    label: { whiteSpace: "normal" },
  },
});
