"use client";

import { notifications } from "@mantine/notifications";
import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";

export const useFetch = () => {
    const session = useSession();
    const router = useRouter();
    const newFetch = async (input: RequestInfo | URL, config?: RequestInit): Promise<globalThis.Response> => {
        if (!config) return fetch(input, config);
        const { headers, ...rest } = config;
        const _config = {
            headers: {
                ...headers,
                Accept: "application/hal+json",
                Authorization: "Bearer " + session.data?.token,
                ...(config?.method === "post" && { "Content-Type": "application/json" })
            },
            ...rest
        }
        const response = await fetch(input, _config);
        if (response.status === 401) {
            notifications.show({ message: "Your are not authenticated. We redirect you to the login page." })
            router.push("/login")
        }
        else if (response.status === 403) {
            notifications.show({ message: "Your are not authorized to do this action." })
        }
        return response
    }
    return { fetch: newFetch }
}