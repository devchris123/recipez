"use client";

import { TextInput, PasswordInput, Button, Fieldset } from "@mantine/core";
import classes from "./register.module.css";
import ReCAPTCHA from "react-google-recaptcha";
import { useForm } from "@mantine/form";
import { postUser, User } from "../recipes/api/postUser";
import { FormActionResponse } from "../recipes/api/types";
import { useFormState } from "react-dom";
import { useRef } from "react";
import { redirect } from "next/navigation";
import { notifications } from "@mantine/notifications";

const initialState: FormActionResponse<User> = {
  result: null,
  error: null,
};

export default function Register() {
  // Ref for recaptcha resets
  const recaptchaRef = useRef<ReCAPTCHA>(null);

  const form = useForm({
    mode: "uncontrolled",
    initialValues: {
      username: "",
      password: "",
      "g-recaptcha-response": "",
    },
    validate: {
      username: (value) => (value !== "" ? null : "Invalid username"),
      password: (value) => (value !== "" ? null : "Invalid password"),
      "g-recaptcha-response": (value) =>
        value !== "" ? null : "Please verify, that you are not a bot.",
    },
  });

  const actionStateAdapter = async (
    _: FormActionResponse<User>,
    fd: FormData
  ) => {
    const response = await postUser(fd);
    if (!response.result && !response.error) {
      notifications.show({
        message: "We are sorry, something is not right. Please try again later",
        color: "red",
      });
      return response;
    }
    if (response.result) {
      redirect("/login");
    }
    if (response.error && recaptchaRef && recaptchaRef.current) {
      recaptchaRef.current.reset();
    }
    return response;
  };

  const [state, formAction] = useFormState<FormActionResponse<User>, FormData>(
    actionStateAdapter,
    initialState
  );

  return (
    <form
      className={classes.form}
      action={(fd) => {
        const res = form.validate();
        if (res.hasErrors) return;
        formAction(fd);
      }}
    >
      <Fieldset className={classes.form}>
        Create an account
        <TextInput
          withAsterisk
          label={"Benutzername"}
          name="username"
          key={form.key("username")}
          {...form.getInputProps("username")}
          error={
            state.error?.errors?.username ??
            form.getInputProps("username").error
          }
        />
        <PasswordInput
          withAsterisk
          label={"Passwort"}
          name="password"
          key={form.key("password")}
          {...form.getInputProps("password")}
          error={
            state.error?.errors?.password ??
            form.getInputProps("password").error
          }
        />
        <ReCAPTCHA
          ref={recaptchaRef}
          sitekey={process.env.NEXT_PUBLIC_RECAPTCHA_PUBLIC_KEY ?? ""}
          key={form.key("g-recaptcha-response")}
          {...form.getInputProps("g-recaptcha-response")}
        />
        {form.errors["g-recaptcha-response"] ? (
          <div>Please verify, that you are not a bot.</div>
        ) : (
          <></>
        )}
        {state.error?.title && !state.error?.errors ? (
          <div>{state.error.title}</div>
        ) : (
          <></>
        )}
      </Fieldset>
      <Button type="submit">Register</Button>
    </form>
  );
}
