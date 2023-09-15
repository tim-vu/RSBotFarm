import { PlayArrow, Pause, Stop } from "@mui/icons-material"
import {
  Paper,
  ButtonGroup,
  Button,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  useTheme,
} from "@mui/material"
import { theme } from "../../theme"
import {
  useGetControlStateQuery,
  useGetEventsQuery,
  useUpdateControlStateMutation,
} from "../../store/apiSlice"
import { FarmStatus } from "../../api/types"
import { fromUnixTimestamp } from "../../common/time"
import { useEffect, useMemo, useRef } from "react"

export const ControlPanel: React.FC = () => {
  const theme = useTheme()

  const { data: controlState, isLoading: isControlStateLoading } =
    useGetControlStateQuery(undefined)
  const { data: events, isLoading: isEventsLoading } =
    useGetEventsQuery(undefined)
  const [updateControlState] = useUpdateControlStateMutation()

  const startDisabled =
    isControlStateLoading ||
    controlState?.status === FarmStatus.Paused ||
    controlState?.status == FarmStatus.Running
  const pauseDisabled =
    isControlStateLoading || controlState?.status === FarmStatus.Stopped
  const stopDisabled =
    isControlStateLoading || controlState?.status === FarmStatus.Stopped

  const startButtonClicked = () => {
    updateControlState({
      status: FarmStatus.Running,
    })
  }

  const pauseButtonClicked = () => {
    updateControlState({
      status:
        controlState?.status === FarmStatus.Paused
          ? FarmStatus.Running
          : FarmStatus.Paused,
    })
  }

  const stopButtonClicked = () => {
    updateControlState({
      status: FarmStatus.Stopped,
    })
  }

  const sortedEvents = useMemo(() => {
    return events?.slice().sort((l, r) => r.at - l.at)
  }, [events])

  return (
    <Paper
      sx={{
        padding: theme.spacing(4),
        display: "flex",
        flexDirection: "column",
        flexGrow: "1",
        width: "28rem",
      }}
    >
      <Box display="flex" justifyContent="space-between">
        <ButtonGroup variant="contained">
          <Button
            disabled={startDisabled}
            onClick={startButtonClicked}
            endIcon={<PlayArrow />}
          >
            Start
          </Button>
          <Button
            disabled={pauseDisabled}
            onClick={pauseButtonClicked}
            endIcon={<Pause />}
          >
            {controlState?.status === FarmStatus.Paused ? "Resume" : "Pause"}
          </Button>
        </ButtonGroup>
        <Button
          variant="contained"
          color="error"
          disabled={stopDisabled}
          onClick={stopButtonClicked}
          endIcon={<Stop />}
          sx={{ marginLeft: theme.spacing(2) }}
        >
          Stop
        </Button>
      </Box>
      <TableContainer
        //
        sx={{ marginTop: theme.spacing(3), flexGrow: "1", height: "0rem" }}
      >
        <Table stickyHeader size="small">
          <TableHead>
            <TableRow>
              <TableCell width="30%">At</TableCell>
              <TableCell>Message</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {sortedEvents?.map((event) => (
              <TableRow key={event.id}>
                <TableCell component="th" scope="row">
                  {fromUnixTimestamp(event.at).toLocaleTimeString()}
                </TableCell>
                <TableCell>{event.message}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Paper>
  )
}

export default ControlPanel
