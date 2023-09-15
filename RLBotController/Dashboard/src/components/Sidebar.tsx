import { Home, Info, ManageAccounts } from "@mui/icons-material"
import {
  Box,
  CssBaseline,
  AppBar,
  Toolbar,
  Typography,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Link,
} from "@mui/material"
import { Outlet, useLocation } from "react-router-dom"

const drawerWidth = 240

const routes = [
  { path: "/", title: "Home", icon: <Home /> },
  { path: "/accounts", title: "Accounts", icon: <ManageAccounts /> },
  { path: "/about", title: "About", icon: <Info /> },
]

const Sidebar: React.FC = () => {
  const { pathname } = useLocation()

  return (
    <Box sx={{ display: "flex" }}>
      <AppBar
        position="fixed"
        sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}
      >
        <Toolbar>
          <Typography variant="h6" noWrap component="p">
            RSBController
          </Typography>
        </Toolbar>
      </AppBar>
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: {
            width: drawerWidth,
            boxSizing: "border-box",
          },
        }}
      >
        <Toolbar />
        <Box sx={{ overflow: "auto" }}>
          <List>
            {routes.map((route) => (
              <ListItem
                key={route.path}
                component={Link}
                href={route.path}
                disablePadding
              >
                <ListItemButton selected={route.path === pathname}>
                  <ListItemIcon>{route.icon}</ListItemIcon>
                  <ListItemText primary={route.title} />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>
      </Drawer>
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  )
}

export default Sidebar
