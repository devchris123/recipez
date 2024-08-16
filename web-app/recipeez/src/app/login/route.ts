import { redirect } from "next/navigation";
import { getServerSessionWithConf } from "../api/auth/[...nextauth]/conf";

export async function GET() {
    await getServerSessionWithConf();
    redirect("/api/auth/signin")
}