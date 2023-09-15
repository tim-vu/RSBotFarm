import { Modal, Box, useTheme } from "@mui/material"
import { ClientVm } from "../../api/types"
import { memo, useCallback, useState } from "react"
import RFB from "@novnc/novnc/core/rfb"
import { VNC_BASE_URL } from "../../api"

interface VncModalProps {
  isOpen: boolean
  close: () => void
  client: ClientVm
}

const VncModal: React.FC<VncModalProps> = ({ isOpen, close, client }) => {
  const theme = useTheme()

  const [rfb, setRfb] = useState<any>()

  const ref = useCallback(
    (el: HTMLDivElement) => {
      if (!el) {
        return
      }

      console.log("Connect")
      const rfb = new RFB(el, `${VNC_BASE_URL}:${client.websocketPort}`, {
        credentials: {
          password: "abc12345",
        },
      })
      rfb.disconnect = () => {
        console.log("Disconnected2")
        close()
      }
      rfb.scaleViewport = true
      rfb.resizeSession = true
      setRfb(rfb)
    },
    [client],
  )

  const innerClose = () => {
    console.log("Disconnect")
    rfb?.disconnect()
    close()
  }

  return (
    <Modal open={isOpen} onClose={innerClose}>
      <Box
        sx={{
          position: "absolute",
          left: "50%",
          top: "50%",
          transform: "translate(-50%, -50%)",
          bgcolor: "background.paper",
          boxShadow: theme.shadows,
          padding: theme.spacing(5),
          borderRadius: theme.spacing(1),
          display: "flex",
          flexDirection: "column",
        }}
      >
        <Box
          id="vnc"
          ref={ref}
          sx={{
            width: "765px",
            height: "523px",
          }}
        />
      </Box>
    </Modal>
  )
}

export default memo(VncModal)
