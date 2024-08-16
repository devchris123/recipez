import type { Metadata } from "next";
import { Inter } from "next/font/google";
import { ColorSchemeScript, MantineProvider } from "@mantine/core";
import Footer from "./footer/footer";
import { theme } from "./theme";
import { Notifications } from "@mantine/notifications";
import BreadCrumbNav from "./_components/breadcrumbNav";
import HeaderWrapper from "./header/HeaderWrapper";

/* Global styles */
import "./globals.css";
import "normalize.css/normalize.css";

/* Mantine global styles */
import "@mantine/core/styles.css";
import "@mantine/notifications/styles.css";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Recipeez",
  description: "A recipe app to keep track of your favourite recipes",
};

export default async function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" data-mantine-color-scheme="light">
      <head>
        <ColorSchemeScript defaultColorScheme="light" />
      </head>
      <body className={inter.className}>
        <MantineProvider theme={theme}>
          <HeaderWrapper />
          <nav>
            <BreadCrumbNav />
          </nav>
          {children}
          <Footer />
          <Notifications limit={2} />
        </MantineProvider>
      </body>
    </html>
  );
}
