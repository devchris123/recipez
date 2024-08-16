"use client";

import { TextInput, NumberInput, Flex, Fieldset } from "@mantine/core";
import { Cancel, Submit } from "@/app/submit";
import { FormActionResponse, IngredientQuantity } from "../../api/types";
import { useForm, isNotEmpty } from "@mantine/form";
import { useFormState } from "react-dom";

const initialState: FormActionResponse<IngredientQuantity> = {
  result: null,
  error: null,
};

export const RecipeIngredientForm = ({
  onSubmit,
  onSuccess,
  onCancel,
  iq,
}: {
  onSubmit: (e: FormData) => Promise<FormActionResponse<IngredientQuantity>>;
  onSuccess: (d: IngredientQuantity) => void;
  onCancel: () => void;
  iq?: IngredientQuantity;
}) => {
  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      ingredient: iq?.ingredient ?? "",
      quantity: iq?.quantity ?? "",
      unit: iq?.unit ?? "",
    },
    validate: {
      ingredient: isNotEmpty("ingredient must not be blank"),
      quantity: isNotEmpty("quantity must not be null"),
      unit: isNotEmpty("unit must not be blank"),
    },
  });

  const actionStateAdapter = async (
    _: FormActionResponse<IngredientQuantity>,
    fd: FormData
  ) => {
    const response = await onSubmit(fd);
    if (response.result) {
      onSuccess(response.result);
    }
    return response;
  };

  const [state, formAction] = useFormState<
    FormActionResponse<IngredientQuantity>,
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
        {iq ? "Update ingredient" : "Add ingredient"}
        <Flex direction={{ base: "column", xxs: "row" }} gap="5px">
          <NumberInput
            flex="0.3"
            withAsterisk
            hideControls
            name="quantity"
            label={"Quantity"}
            placeholder="100"
            min={0}
            minLength={1}
            key={form.key("quantity")}
            {...form.getInputProps("quantity")}
            error={
              state.error?.errors.quantity ??
              form.getInputProps("quantity").error
            }
          />
          <TextInput
            flex="0.2"
            withAsterisk
            name="unit"
            label={"Unit"}
            placeholder="g"
            minLength={1}
            key={form.key("unit")}
            {...form.getInputProps("unit")}
            error={state.error?.errors.unit ?? form.getInputProps("unit").error}
          />
          <TextInput
            flex="0.5"
            withAsterisk
            name="ingredient"
            label={"Ingredient"}
            placeholder="butter"
            minLength={1}
            key={form.key("ingredient")}
            {...form.getInputProps("ingredient")}
            error={
              state.error?.errors.ingredient ??
              form.getInputProps("ingredient").error
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
