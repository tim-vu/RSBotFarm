import { useTheme } from "@mui/material"
import { Box, Button, Modal, TextField, Typography } from "@mui/material"
import { Controller, useForm } from "react-hook-form"
import { DatePicker } from "@mui/x-date-pickers"
import { AddMuleAccount } from "../../api/types"
import { useAddMuleAccountMutation } from "../../store/apiSlice"

interface AddMuleModalProps {
  isOpen: boolean
  close: () => void
}

export const AddMuleModal: React.FC<AddMuleModalProps> = ({
  isOpen,
  close,
}) => {
  const theme = useTheme()

  const [addMule] = useAddMuleAccountMutation()
  const { control, handleSubmit, reset } = useForm<AddMuleAccount>({})

  const onSubmit = handleSubmit((data) => {
    addMule(data)
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
          Add a mule
        </Typography>
        <Controller
          name="username"
          control={control}
          rules={{
            required: true,
            minLength: 5,
          }}
          defaultValue=""
          render={({ field, fieldState }) => (
            <TextField
              sx={{ marginTop: theme.spacing(2) }}
              id="username"
              label="Username"
              variant="standard"
              error={!!fieldState.error}
              {...field}
            />
          )}
        />
        <Controller
          name="password"
          control={control}
          rules={{
            required: true,
            minLength: 5,
          }}
          defaultValue=""
          render={({ field, fieldState }) => (
            <TextField
              sx={{ marginTop: theme.spacing(2) }}
              id="password"
              label="Password"
              type="password"
              variant="standard"
              error={!!fieldState.error}
              {...field}
            />
          )}
        />
        <Controller
          name="gold"
          control={control}
          rules={{
            required: true,
            min: 0,
            max: 2147483647,
          }}
          defaultValue={0}
          render={({ field, fieldState }) => (
            <TextField
              sx={{ marginTop: theme.spacing(2) }}
              id="gold"
              label="Gold"
              type="number"
              variant="standard"
              error={!!fieldState.error}
              {...field}
            />
          )}
        />
        <Controller
          name="membershipExpiry"
          control={control}
          defaultValue={undefined}
          render={({ field }) => (
            <DatePicker
              sx={{ marginTop: theme.spacing(5) }}
              label="Membership expiry"
              slotProps={{
                textField: {
                  id: "membershipEpiry",
                },
              }}
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
