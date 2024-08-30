import { createTheme } from "@mantine/core";

export const theme = createTheme({
    fontFamily: "Montserrat, sans-serif",
    defaultRadius: "md",
    breakpoints: {
        xxs: "22em",
        xs: '30em',
        sm: '48em',
        md: '64em',
        lg: '74em',
        xl: '90em',
    },
    colors: {
        primary: [
            "#fff0e4",
            "#ffe0cf",
            "#fac0a1",
            "#f69e6e",
            "#f28043",
            "#f06d27",
            "#f06418",
            "#d6530c",
            "#bf4906",
            "#a73c00"
        ],
        secondary: [
            "#e4f7ff",
            "#d1ebfe",
            "#a5d5f6",
            "#74bef0",
            "#4eabea",
            "#369ee7",
            "#2598e8",
            "#1484ce",
            "#0075b9",
            "#0066a5"
        ],
        tertiary: [
            "#ffe9e9",
            "#ffd2d2",
            "#faa1a2",
            "#f56e6e",
            "#f14343",
            "#ef2928",
            "#f01919",
            "#d60a0d",
            "#bf020a",
            "#a70005"
        ],
        yellow: [
            "#fffae1",
            "#fdf4cd",
            "#f9e79f",
            "#f5da6c",
            "#f2cf42",
            "#f0c727",
            "#efc415",
            "#d4ac03",
            "#be9900",
            "#a38400",
        ],
        red: [
            "#ffe9e9",
            "#ffd1d1",
            "#fba0a1",
            "#f76d6d",
            "#f34141",
            "#f22625",
            "#f21616",
            "#d8070b",
            "#c10008",
            "#a90003"
        ],
        green: [
            "#e5feee",
            "#d2f9e0",
            "#a8f1c0",
            "#7aea9f",
            "#53e383",
            "#3bdf70",
            "#2bdd66",
            "#1ac455",
            "#0caf49",
            "#00963c"
        ]
    },
    primaryColor: "primary",
});
