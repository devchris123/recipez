import { getServerSessionWithConf } from "@/app/api/auth/[...nextauth]/conf";
import { redirect } from "next/navigation";

/*
    authorization: boolean -> Used for anonymous routes. 
                            Passing an Authorization header 
                            with an invalid Bear token 
                            e.g. on signUp causes problems
 */
export const api = async (input: RequestInfo | URL, config?: RequestInit, authorization = true): Promise<globalThis.Response> => {
    const sess = await getServerSessionWithConf();
    if (!config) return fetch(input, config);
    const { headers, ...rest } = config;
    const _config = {
        headers: {
            Accept: "application/hal+json",
            ...(authorization && { Authorization: "Bearer " + sess?.token }),
            ...((config?.method === "post" || config?.method === "put") && { "Content-Type": "application/json" }),
            ...headers,
        },
        ...rest
    }
    const response = await fetch(input, _config);
    if (response.status === 401) {
        redirect("/login");
    }
    else if (response.status === 403) {
    }
    return response
}