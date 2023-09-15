import { Modal, Box, Typography, TextField, Button, Paper } from "@mui/material"
import { theme } from "../../theme"
import { useCallback } from "react"
import { useDropzone } from "react-dropzone"
import CloudUploadIcon from "@mui/icons-material/CloudUpload"
import { Controller, useForm } from "react-hook-form"
import { BulkAddAccount } from "../../api/extra_types"
import { useBulkAddWorkerAccountsMutation } from "../../store/apiSlice"

interface BulkAddWorkerProps {
  isOpen: boolean
  close: () => void
}

const BulkAddWorkerModal: React.FC<BulkAddWorkerProps> = ({
  isOpen,
  close,
}) => {
  const { control, handleSubmit, reset, setValue } = useForm<BulkAddAccount>()
  const [bulkAddWorker] = useBulkAddWorkerAccountsMutation()

  const onSubmit = handleSubmit((data) => {
    bulkAddWorker(data)
    close()
    reset()
  })

  const onDrop = useCallback((files: File[]) => {
    const file = files[0]

    const reader = new FileReader()
    reader.onabort = () => console.log("file reading was aborted")
    reader.onerror = () => console.log("file reading has failed")
    reader.onload = () => {
      setValue("fileContent", reader.result as string)
    }
    reader.readAsBinaryString(file)
  }, [])

  const { getRootProps, getInputProps } = useDropzone({
    onDrop,
    maxFiles: 1,
    accept: {
      "text/csv": [".csv"],
    },
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
          Import accounts
        </Typography>
        <Paper
          {...getRootProps()}
          sx={{
            marginTop: theme.spacing(3),
            height: "5rem",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <input {...getInputProps()} />
          <Typography>Drag and drop a file here or click here</Typography>
        </Paper>
        <Controller
          name="fileContent"
          control={control}
          render={({ field }) => (
            <TextField
              {...field}
              label="Accounts"
              multiline
              rows={10}
              InputLabelProps={{ shrink: true }}
              sx={{ marginTop: theme.spacing(3) }}
            />
          )}
        />
        <Box display="flex" justifyContent="end" marginTop={theme.spacing(3)}>
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

export default BulkAddWorkerModal
