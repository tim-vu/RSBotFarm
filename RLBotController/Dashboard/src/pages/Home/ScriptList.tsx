import { DataGrid, GridColDef } from "@mui/x-data-grid"
import { useState, useCallback } from "react"
import { useGetScriptsQuery } from "../../store/apiSlice"

const scriptColumns: GridColDef[] = [
  { field: "id", headerName: "ID" },
  { field: "name", headerName: "Name", flex: 0.3 },
  { field: "instances", headerName: "Instances", flex: 0.3 },
  { field: "priority", headerName: "Priority", flex: 0.3 },
]

const ScriptList: React.FC = () => {
  const { data: scripts, isLoading: isScriptsLoading } = useGetScriptsQuery()

  return (
    <DataGrid
      rows={scripts ?? []}
      columns={scriptColumns}
      sortModel={[{ field: "id", sort: "asc" }]}
      loading={isScriptsLoading}
    />
  )
}

export default ScriptList
