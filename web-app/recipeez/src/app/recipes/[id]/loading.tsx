import { Skeleton } from "@mantine/core";

export default function Loading() {
  return (
    <main
      style={{
        padding: "20px 10px",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        gap: "10px",
      }}
    >
      <Skeleton width="200" height="100"></Skeleton>
      <Skeleton width="200" height="50"></Skeleton>
      <Skeleton height="200"></Skeleton>
      <Skeleton width="200" height="50"></Skeleton>
      <Skeleton height="400"></Skeleton>
    </main>
  );
}
