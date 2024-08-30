import { AuthOptions, DefaultUser, User, } from "next-auth";
import { getServerSession } from "next-auth/next";
import CredentialsProvider from "next-auth/providers/credentials";

declare module "next-auth" {
    interface Session {
        user?: DefaultUser & User;
        token: string;
    }

    interface JWT {
        sub: string,
        user: User
    }

    interface User {
        id: string;
        token: string;
    }
}

const SESSION_URL = `${process.env.API_RECIPEEZ_ORIGIN}/sessions`;

export const authOptions: AuthOptions = {
    providers: [
        CredentialsProvider({
            type: "credentials",
            name: "credentials",
            credentials: {
                username: { label: "Username", type: "text", placeholder: "jsmith" },
                password: { label: "Password", type: "password" }
            },
            async authorize(credentials) {
                if (!credentials) return null;
                const headers = new Headers();
                headers.set(
                    "Authorization",
                    "Basic " + btoa(credentials.username + ":" + credentials.password)
                );
                const resp = await fetch(SESSION_URL, {
                    method: "post",
                    headers: headers,
                })
                if (resp.status === 401) {
                    return null // { fieldErrors: {}, formErrors: ["Invalid credentials. Please verify your username and password."] };
                }
                if (400 <= resp.status && resp.status < 500) {
                    return null // { fieldErrors: {}, formErrors: ["An error ocurred. Please verify your inputs."] };
                }
                if (resp.status >= 500) {
                    return null // { fieldErrors: {}, formErrors: ["We are sorry. An error ocurred on the server. Please try again later."] };
                }
                const token = await resp.json().then((data) => data.token);
                if (!token) return null;
                return {
                    id: credentials.username,
                    name: credentials.username,
                    email: credentials.username,
                    token: token
                }
            },
        })
    ],
    session: { strategy: "jwt" },
    callbacks: {
        async session({ session, token }) {
            return {
                ...session,
                token: token.token as User
            }
        },
        async jwt({ user, token }) {
            return {
                ...user,
                ...token
            };
        }
    },
    events: {
        signIn: async (message) => { }
    }
}

export const getServerSessionWithConf = async () => getServerSession(authOptions)