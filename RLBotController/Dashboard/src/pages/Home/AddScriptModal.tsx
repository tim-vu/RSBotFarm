import {
  Modal,
  Box,
  Typography,
  TextField,
  Button,
  useTheme,
} from "@mui/material"
import { DatePicker } from "@mui/x-date-pickers"
import { useForm, Controller } from "react-hook-form"
import { AddScript } from "../../api/types"
import { useAddScriptMutation } from "../../store/apiSlice"

interface AddScriptModalProps {
  isOpen: boolean
  close: () => void
}

const AddScriptModal: React.FC<AddScriptModalProps> = ({ isOpen, close }) => {
  const theme = useTheme()

  const [addScript] = useAddScriptMutation()
  const { control, handleSubmit, reset } = useForm<AddScript>({})

  const onSubmit = handleSubmit((data) => {
    addScript(data)
    close()
    reset()
  })

  return (
    <Modal open={isOpen} onClose={close}>
      <Box
        sx={{
          position: "absolute",
          left: "50%",
          top: "50%",
          transform: "translate(-50%, -50%)",
          width: "40vw",
          bgcolor: "background.paper",
          boxShadow: theme.shadows,
          padding: theme.spacing(5),
          borderRadius: theme.spacing(1),
          display: "flex",
          flexDirection: "column",
        }}
        component="form"
        onSubmit={onSubmit}
      >
        <Typography variant="h4" component="h4">
          Add a script
        </Typography>
        <Controller
          name="name"
          control={control}
          rules={{
            required: true,
            minLength: 5,
          }}
          defaultValue=""
          render={({ field, fieldState }) => (
            <TextField
              sx={{ marginTop: theme.spacing(2) }}
              id="name"
              label="Name"
              variant="standard"
              error={!!fieldState.error}
              {...field}
            />
          )}
        />
        <Controller
          name="instances"
          control={control}
          rules={{
            required: true,
            min: 0,
          }}
          defaultValue={5}
          render={({ field, fieldState }) => (
            <TextField
              sx={{ marginTop: theme.spacing(2) }}
              id="instances"
              label="Instances"
              type="number"
              variant="standard"
              error={!!fieldState.error}
              {...field}
            />
          )}
        />
        <Controller
          name="priority"
          control={control}
          rules={{
            required: true,
            min: 0,
          }}
          defaultValue={5}
          render={({ field, fieldState }) => (
            <TextField
              sx={{ marginTop: theme.spacing(2) }}
              id="priority"
              label="Priority"
              type="number"
              variant="standard"
              error={!!fieldState.error}
              {...field}
            />
          )}
        />
        <Box
          sx={{
            display: "flex",
            justifyContent: "end",
            marginTop: theme.spacing(3),
          }}
        >
          <Button variant="contained" onClick={close}>
            Cancel
          </Button>
          <Button
            type="submit"
            variant="contained"
            sx={{ marginLeft: theme.spacing(2) }}
          >
            Add
          </Button>
        </Box>
      </Box>
    </Modal>
  )
}

export default AddScriptModal
