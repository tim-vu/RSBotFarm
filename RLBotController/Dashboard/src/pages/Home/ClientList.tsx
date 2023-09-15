import { Box, Menu, MenuItem, Typography } from "@mui/material"
import { DataGrid, GridColDef } from "@mui/x-data-grid"
import { ClientStatus, ClientVm } from "../../api/types"
import { formatDuration } from "../../common/time"
import { useGetClientsQuery } from "../../store/apiSlice"
import { useState, useEffect } from "react"
import VncModal from "./VncModal"

const clientColumns: GridColDef[] = [
  { field: "id", headerName: "ID" },
  { field: "botId", headerName: "BotId", flex: 0.4 },
  {
    field: "status",
    headerName: "Status",
    valueFormatter: (params) => {
      return params.value
    },
    flex: 0.15,
  },
  {
    field: "startedAt",
    headerName: "Runtime",
    valueFormatter: (params) => {
      const unixTimestamp = Math.floor(Date.now() / 1000)
      const duration = params.value ? unixTimestamp - params.value : 0
      return formatDuration(duration)
    },
    flex: 0.15,
  },
  {
    field: "activeScript",
    headerName: "Active script",
    valueGetter: (params) => {
      return params.row.activeScript.name
    },
    flex: 0.2,
  },
]

export const ClientList: React.FC = () => {
  const { data: clients, isLoading: isClientsLoading } = useGetClientsQuery()

  const [_, setTime] = useState(new Date())
  const [selectedClient, setSelectedClient] = useState<ClientVm | null>(null)
  const [isOpen, setOpen] = useState<boolean>(false)

  const showVnc = () => {
    console.log("Showing vnc")
    setContextMenu(null)
    setOpen(true)
  }

  const closeVnc = () => {
    console.log("Closing vnc")
    setOpen(false)
    setSelectedClient(null)
  }

  const [contextMenu, setContextMenu] = useState<{
    mouseX: number
    mouseY: number
  } | null>(null)

  const handleContextMenu = (event: React.MouseEvent) => {
    event.preventDefault()
    const id = Number(event.currentTarget.getAttribute("data-id"))
    const row = clients?.find((c) => c.id === id)
    setSelectedClient(row ?? null)
    setContextMenu(
      contextMenu === null
        ? { mouseX: event.clientX - 2, mouseY: event.clientY - 4 }
        : null,
    )
  }

  const handleClose = () => {
    setContextMenu(null)
  }

  useEffect(() => {
    const intervalId = setInterval(() => setTime(new Date()), 1000)

    return () => {
      clearInterval(intervalId)
    }
  }, [])

  return (
    <>
      <DataGrid
        rows={clients ?? []}
        columns={clientColumns}
        sortModel={[{ field: "id", sort: "asc" }]}
        loading={isClientsLoading}
        slotProps={{
          row: {
            onContextMenu: handleContextMenu,
            style: { cursor: "context-menu" },
          },
        }}
      />
      <Menu
        open={contextMenu !== null}
        onClose={handleClose}
        anchorReference="anchorPosition"
        anchorPosition={
          contextMenu !== null
            ? { top: contextMenu.mouseY, left: contextMenu.mouseX }
            : undefined
        }
        slotProps={{
          root: {
            onContextMenu: (e) => {
              e.preventDefault()
              handleClose()
            },
          },
        }}
      >
        <MenuItem onClick={showVnc} disabled={!selectedClient?.websocketPort}>
          Open VNC
        </MenuItem>
      </Menu>
      <VncModal isOpen={isOpen} close={closeVnc} client={selectedClient!} />
    </>
  )
}

export default ClientList
