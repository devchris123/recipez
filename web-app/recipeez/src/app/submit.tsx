"use client";

import { Loader } from "@mantine/core";
import { FormEvent } from "react";
import { useFormStatus } from "react-dom";
import { ResponsiveButton } from "./recipes/_components/responsiveButton";

export const Submit = ({
  text,
  onSubmit,
}: {
  text: string;
  onSubmit?: (_e: FormEvent) => void;
}) => {
  const { pending } = useFormStatus();
  return (
    <ResponsiveButton onSubmit={onSubmit} type="submit" disabled={pending}>
      {pending ? <Loader size={"sm"} /> : text}
    </ResponsiveButton>
  );
};

export const Cancel = ({
  text,
  onClick,
}: {
  text: string;
  onClick?: () => void;
}) => {
  const { pending } = useFormStatus();
  return (
    <ResponsiveButton onClick={onClick} type="button" disabled={pending}>
      {pending ? <Loader size={"sm"} /> : text}
    </ResponsiveButton>
  );
};
