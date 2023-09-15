import { createTheme } from "@mui/material"
import React from "react"
import {
  Link as RouterLink,
  LinkProps as RouterLinkProps,
} from "react-router-dom"
import { LinkProps } from "@mui/material/Link"

const LinkBehavior = React.forwardRef<
  HTMLAnchorElement,
  Omit<RouterLinkProps, "to"> & { href: RouterLinkProps["to"] }
>((props, ref) => {
  const { href, ...other } = props
  // Map href (Material UI) -> to (react-router)
  return <RouterLink ref={ref} to={href} {...other} />
})

export const theme = createTheme({
  palette: {
    mode: "dark",
  },
  spacing: 4,
  components: {
    MuiLink: {
      defaultProps: {
        component: LinkBehavior,
      } as LinkProps,
      styleOverrides: {
        root: ({ theme, ownerState }) => ({
          ...(ownerState.color === "primary" && {
            color: theme.palette.text.primary,
          }),
        }),
      },
    },
    MuiButtonBase: {
      defaultProps: {
        LinkComponent: LinkBehavior,
      },
    },
  },
})
