import { Box, Button, Typography, useTheme } from "@mui/material"
import AddBoxIcon from "@mui/icons-material/AddBox"
import { useCallback, useState } from "react"
import AddScriptModal from "./AddScriptModal"
import ControlPanel from "./ControlPanel"
import ClientList from "./ClientList"
import ScriptList from "./ScriptList"

export const Home: React.FC = () => {
  const theme = useTheme()

  const [isAddScriptOpen, setAddScriptOpen] = useState(false)

  const openAddScript = useCallback(() => {
    setAddScriptOpen(true)
  }, [])

  const closeAddScript = useCallback(() => {
    setAddScriptOpen(false)
  }, [])

  return (
    <Box
      display="flex"
      flexDirection="column"
      justifyContent="center"
      width="100%"
      padding={theme.spacing(4)}
    >
      <Box
        display="flex"
        flexDirection="row"
        justifyContent="start"
        alignItems="space-between"
        width="100%"
        height="35rem"
      >
        <Box
          marginRight={theme.spacing(4)}
          display="flex"
          flexDirection="column"
        >
          <Typography variant="h4">Control</Typography>
          <ControlPanel />
        </Box>
        <Box display="flex" flexDirection="column" flexGrow="1">
          <Typography variant="h4">Clients</Typography>
          <ClientList />
        </Box>
      </Box>
      <Box
        display="flex"
        flexDirection="row"
        justifyContent="space-between"
        marginTop={theme.spacing(4)}
      >
        <Box display="flex" flexDirection="column" justifyContent="center">
          <AddScriptModal isOpen={isAddScriptOpen} close={closeAddScript} />
          <Box
            display="flex"
            justifyContent="space-between"
            alignItems="center"
          >
            <Typography variant="h4">Scripts</Typography>
            <Button
              variant="contained"
              endIcon={<AddBoxIcon />}
              onClick={openAddScript}
            >
              Add script
            </Button>
          </Box>
          <Box width="46vw" height="35rem" marginTop={theme.spacing(2)}>
            <ScriptList />
          </Box>
        </Box>
      </Box>
    </Box>
  )
}

export default Home
