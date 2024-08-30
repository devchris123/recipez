"use server";

import { ORIGIN } from "./url"
import { FormActionResponse } from "./types";
import { api } from "./api";

export interface User {
    publicId: string;
    username: string;
    password: string;
}

export const postUser = async (formData: FormData): Promise<FormActionResponse<User>> => {
    const obj = Object.fromEntries(formData)
    const content = Object.entries(obj).map(([key, val]) => {
        return encodeURIComponent(key) + '=' + encodeURIComponent(val as string);
    }).join('&');
    const response = await api(ORIGIN + "/" + "users", {
        method: "post",
        body: content,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    }, false)
    if (response.status !== 201) {
        // Error case
        let jsonError;
        try {
            jsonError = await response.json()
        } catch (e) {
            jsonError = null;
        }
        return {
            error: jsonError,
            result: null
        }
    }
    return {
        error: null,
        result: await response.json()
    };
}