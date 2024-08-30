"use client";

import { Anchor, Breadcrumbs } from "@mantine/core";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { createBreadCrumbDataFromPathName } from "./createBreadCrumbData";

export default function BreadCrumbNav() {
  const pn = usePathname();
  const items = [
    { title: "Home", href: "/" },
    ...createBreadCrumbDataFromPathName(pn),
  ];

  return (
    <Breadcrumbs style={{ flexWrap: "wrap" }}>
      {items.map((i) => (
        <Anchor key={i.href} component={Link} href={i.href}>
          {i.title}
        </Anchor>
      ))}
    </Breadcrumbs>
  );
}
