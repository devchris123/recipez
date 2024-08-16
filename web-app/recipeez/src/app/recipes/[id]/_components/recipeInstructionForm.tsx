"use client";

import { TextInput, Flex, Fieldset, Divider } from "@mantine/core";
import { Cancel, Submit } from "@/app/submit";
import { RecipeInstruction, FormActionResponse } from "../../api/types";
import { useForm } from "@mantine/form";
import { useFormState } from "react-dom";

const initialState: FormActionResponse<RecipeInstruction> = {
  result: null,
  error: null,
};

export const RecipeInstructionForm = ({
  onSubmit,
  onSuccess,
  onCancel,
  ri,
}: {
  onSubmit: (e: FormData) => Promise<FormActionResponse<RecipeInstruction>>;
  onSuccess: (d: RecipeInstruction) => void;
  onCancel: () => void;
  ri?: RecipeInstruction;
}) => {
  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      description: ri?.description ?? "",
    },
    validate: {
      description: (v) => (v !== "" ? null : "must not be blank"),
    },
  });

  const actionStateAdapter = async (
    _: FormActionResponse<RecipeInstruction>,
    fd: FormData
  ) => {
    const response = await onSubmit(fd);
    if (response.result) {
      onSuccess(response.result);
    }
    return response;
  };

  const [state, formAction] = useFormState<
    FormActionResponse<RecipeInstruction>,
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
      style={{ maxWidth: "300px" }}
    >
      <Fieldset>
        Add instruction
        <Flex direction="row" gap="5px">
          <TextInput
            flex="0.8"
            withAsterisk
            name="description"
            label={"Description"}
            placeholder="knead the dough..."
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
      <Flex gap="10px">
        <Submit text={ri ? "Update" : "Create"} />
        <Cancel text={"Cancel"} onClick={onCancel} />
      </Flex>
    </Flex>
  );
};
