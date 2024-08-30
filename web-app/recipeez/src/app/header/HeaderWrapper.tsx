"use client";

import { SessionProvider } from "next-auth/react";
import Header from "./header";

export default function HeaderWrapper() {
  return (
    <SessionProvider>
      <Header />
    </SessionProvider>
  );
}
