"use client";

import { Button } from "@mantine/core";

export const ResponsiveButton = Button.withProps({
  size: "xs",
  styles: {
    label: { whiteSpace: "normal" },
  },
});
