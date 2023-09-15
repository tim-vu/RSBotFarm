import Home from "./pages/Home/Home"
import { CssBaseline, ThemeProvider } from "@mui/material"
import { theme } from "./theme"
import { LocalizationProvider } from "@mui/x-date-pickers"
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs"
import { RouterProvider, createBrowserRouter } from "react-router-dom"
import Accounts from "./pages/Accounts/Accounts"
import About from "./pages/About/About"
import Sidebar from "./components/Sidebar"

const router = createBrowserRouter([
  {
    path: "/",
    element: <Sidebar />,
    children: [
      { path: "", element: <Home /> },
      { path: "accounts", element: <Accounts /> },
      { path: "about", element: <About /> },
    ],
  },
])

function App() {
  return (
    <ThemeProvider theme={theme}>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <CssBaseline />
        <RouterProvider router={router} />
      </LocalizationProvider>
    </ThemeProvider>
  )
}

export default App
