"use client";

import { TextInput, Flex, Fieldset, Textarea } from "@mantine/core";
import { Cancel, Submit } from "@/app/submit";
import { useForm, isNotEmpty } from "@mantine/form";
import { useFormState } from "react-dom";
import { FormActionResponse, RecipeData } from "../api/types";
import { useState } from "react";
import { ListButton } from "../[id]/_components/listButton";

const initialState: FormActionResponse<RecipeData> = {
  result: null,
  error: null,
};

export const RecipeForm = ({
  onSubmit,
  onSuccess,
  onCancel,
  iq,
}: {
  onSubmit: (e: FormData) => Promise<FormActionResponse<RecipeData>>;
  onSuccess: (d: RecipeData) => void;
  onCancel: () => void;
  iq?: RecipeData;
}) => {
  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      name: iq?.name ?? "",
      description: iq?.description ?? "",
    },
    validate: {
      name: isNotEmpty("name must not be blank"),
    },
  });

  const actionStateAdapter = async (
    _: FormActionResponse<RecipeData>,
    fd: FormData
  ) => {
    const response = await onSubmit(fd);
    if (response.result) {
      onSuccess(response.result);
    }
    return response;
  };

  const [state, formAction] = useFormState<
    FormActionResponse<RecipeData>,
    FormData
  >(actionStateAdapter, initialState);

  return (
    <Flex
      component="form"
      onSubmit={(e) => {
        e.preventDefault();
        const res = form.validate();
        if (res.hasErrors) return;
        const fd = new FormData(e.target as HTMLFormElement);
        formAction(fd);
      }}
      direction="column"
      gap="10px"
      style={{ padding: "5px 0" }}
    >
      <Fieldset>
        {iq ? "Update recipe" : "Add recipe"}
        <Flex direction={{ base: "column" }} gap="5px">
          <TextInput
            withAsterisk
            name="name"
            label={"Name"}
            placeholder="Grandma's cookies"
            minLength={1}
            key={form.key("name")}
            {...form.getInputProps("name")}
            error={
              state.error?.errors?.name ?? form.getInputProps("name").error
            }
          />
          <Textarea
            withAsterisk
            name="description"
            label={"Description"}
            placeholder="Since my grandma was a little kid she loved baking..."
            minLength={1}
            key={form.key("description")}
            {...form.getInputProps("description")}
            error={
              state.error?.errors?.description ??
              form.getInputProps("description").error
            }
          />
        </Flex>
      </Fieldset>
      <Flex gap="10px" direction={{ base: "column", xxs: "row" }}>
        <Submit text={iq ? "Update" : "Add"} />
        <Cancel text={"Cancel"} onClick={onCancel} />
      </Flex>
    </Flex>
  );
};

export const RecipeFormButton = ({
  onSubmit,
  onSuccess,
}: {
  onSubmit: (e: FormData) => Promise<FormActionResponse<RecipeData>>;
  onSuccess: (iq: RecipeData) => void;
}) => {
  const [addInstr, setAddInstr] = useState(false);

  return (
    <>
      {addInstr ? (
        <RecipeForm
          onSubmit={onSubmit}
          onSuccess={onSuccess}
          onCancel={() => setAddInstr(false)}
        />
      ) : (
        <ListButton onClick={() => setAddInstr(true)}>+ Add Recipe</ListButton>
      )}
    </>
  );
};
