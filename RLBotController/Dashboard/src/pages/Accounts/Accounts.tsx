import { Box, Typography, Button, useTheme } from "@mui/material"
import { DataGrid, GridColDef } from "@mui/x-data-grid"
import React, { useCallback, useState } from "react"
import { AddWorkerModal } from "./AddWorkerModal"
import BulkAddWorkerModal from "./BulkAddWorkerAccountModal"
import AddBoxIcon from "@mui/icons-material/AddBox"
import PlaylistAddIcon from "@mui/icons-material/PlaylistAdd"
import {
  useGetMuleAccountsQuery,
  useGetWorkerAccountsQuery,
} from "../../store/apiSlice"
import { AddMuleModal } from "./AddMuleModal"

const workerColumns: GridColDef[] = [
  { field: "id", headerName: "ID" },
  { field: "username", headerName: "Username", flex: 0.3 },
  { field: "password", headerName: "Password", flex: 0.3 },
]

const muleColumns: GridColDef[] = [
  { field: "id", headerName: "ID" },
  { field: "username", headerName: "Username", flex: 0.3 },
  { field: "password", headerName: "Password", flex: 0.3 },
  { field: "gold", headerName: "Gold", flex: 0.3 },
]

const Accounts: React.FC = () => {
  const theme = useTheme()
  const { data: workers, isLoading: isWorkersLoading } =
    useGetWorkerAccountsQuery()
  const { data: mules, isLoading: isMulesLoading } = useGetMuleAccountsQuery()

  const [isAddWorkerOpen, setAddWorkerOpen] = useState(false)

  const openAddWorker = useCallback(() => {
    setAddWorkerOpen(true)
  }, [])

  const closeAddWorker = useCallback(() => {
    setAddWorkerOpen(false)
  }, [])

  const [isBulkAddWorkerOpen, setBulkAddWorkersOpen] = useState(false)

  const openBulkAddWorker = useCallback(() => {
    setBulkAddWorkersOpen(true)
  }, [])

  const closeBulkAddWorker = useCallback(() => {
    setBulkAddWorkersOpen(false)
  }, [])

  const [isAddMuleOpen, setAddMuleOpen] = useState(false)

  const openAddMule = useCallback(() => {
    setAddMuleOpen(true)
  }, [])

  const closeAddMule = useCallback(() => {
    setAddMuleOpen(false)
  }, [])

  return (
    <Box display="flex" flexDirection="row" justifyContent="space-between">
      <Box display="inline-flex" flexDirection="column">
        <BulkAddWorkerModal
          isOpen={isBulkAddWorkerOpen}
          close={closeBulkAddWorker}
        />
        <AddWorkerModal isOpen={isAddWorkerOpen} close={closeAddWorker} />
        <Box
          display="inline-flex"
          flexDirection="row"
          justifyContent="space-between"
          alignItems="center"
        >
          <Typography variant="h4">Workers</Typography>
          <Box>
            <Button
              variant="contained"
              endIcon={<PlaylistAddIcon />}
              onClick={openBulkAddWorker}
            >
              Import workers
            </Button>
            <Button
              variant="contained"
              endIcon={<AddBoxIcon />}
              onClick={openAddWorker}
              sx={{ marginLeft: theme.spacing(1) }}
            >
              Add worker
            </Button>
          </Box>
        </Box>
        <Box width="40vw" height="35rem" marginTop={theme.spacing(2)}>
          <DataGrid
            rows={workers ?? []}
            columns={workerColumns}
            sortModel={[{ field: "id", sort: "asc" }]}
            loading={isWorkersLoading}
          />
        </Box>
      </Box>
      <Box display="inline-flex" flexDirection="column">
        <AddMuleModal isOpen={isAddMuleOpen} close={closeAddMule} />
        <Box
          display="inline-flex"
          flexDirection="row"
          justifyContent="space-between"
          alignItems="center"
        >
          <Typography variant="h4">Mules</Typography>
          <Box>
            <Button
              variant="contained"
              endIcon={<AddBoxIcon />}
              onClick={openAddMule}
              sx={{ marginLeft: theme.spacing(1) }}
            >
              Add mule
            </Button>
          </Box>
        </Box>
        <Box width="40vw" height="35rem" marginTop={theme.spacing(2)}>
          <DataGrid
            rows={mules ?? []}
            columns={muleColumns}
            sortModel={[{ field: "id", sort: "asc" }]}
            loading={isMulesLoading}
          />
        </Box>
      </Box>
    </Box>
  )
}

export default Accounts
