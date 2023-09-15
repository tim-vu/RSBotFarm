import { EVENT_URL } from "."

type EventSourceListener = (event: MessageEvent) => void
type UpdateListener<T> = (payload: T) => void

interface EventType {
  type: string
  eventSourceListener: EventSourceListener
  updateListeners: UpdateListener<any>[]
}

class UpdateDispatcher {
  private _eventTypes: EventType[] = []
  private _eventSource: EventSource

  constructor() {
    this._eventSource = new EventSource(EVENT_URL)
  }

  onMessage(event: MessageEvent) {
    const eventType = this._eventTypes.find((t) => t.type === event.type)

    if (eventType === undefined) {
      return
    }

    const payload: any = JSON.parse(event.data)
    eventType.updateListeners.forEach((l) => l(payload))
  }

  addUpdateListener<T>(type: string, callback: UpdateListener<T>): void {
    var eventType = this._eventTypes.find((t) => t.type === type)

    if (eventType === undefined) {
      const eventSourceListener = (event: MessageEvent) => this.onMessage(event)

      this._eventTypes = [
        ...this._eventTypes,
        {
          type,
          eventSourceListener,
          updateListeners: [callback],
        },
      ]

      this._eventSource.addEventListener(type, eventSourceListener)

      return
    }

    if (eventType?.updateListeners.includes(callback)) {
      return
    }

    eventType.updateListeners = [...eventType?.updateListeners, callback]
  }

  removeUpdateListener<T>(callback: UpdateListener<T>) {
    const eventType = this._eventTypes.find((t) =>
      t.updateListeners.includes(callback),
    )

    if (eventType === undefined) {
      return
    }

    if (eventType.updateListeners.length === 1) {
      this._eventSource.removeEventListener(
        eventType.type,
        eventType.eventSourceListener,
      )
      this._eventTypes = this._eventTypes.filter((t) => t !== eventType)
      return
    }

    eventType.updateListeners = eventType.updateListeners.filter(
      (l) => l !== callback,
    )
  }
}

const INSTANCE = new UpdateDispatcher()

export default INSTANCE
