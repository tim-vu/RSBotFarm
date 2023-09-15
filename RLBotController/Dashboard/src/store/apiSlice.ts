import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { BASE_URL } from "../api"
import updateDispatcher from "../api/updatedispatcher"
import { cacher } from "./cacheUtils"
import {
  ScriptVm,
  AddScript,
  ClientVm,
  ControlStateVm,
  UpdateControlState,
  EventVm,
  WorkerAccountVm,
  AddWorkerAccount,
  MuleAccountVm,
  AddMuleAccount,
} from "../api/types"
import { BulkAddAccount } from "../api/extra_types"

export const api = createApi({
  reducerPath: "api",
  baseQuery: fetchBaseQuery({
    baseUrl: BASE_URL,
  }),
  tagTypes: [
    ...cacher.defaultTags,
    "Script",
    "WorkerAccount",
    "MuleAccount",
    "ControlState",
  ],
  endpoints: (builder) => ({
    getScripts: builder.query<ScriptVm[], void>({
      query: (_) => "scripts",
      providesTags: cacher.providesList("Script"),
    }),
    addScript: builder.mutation<ScriptVm, AddScript>({
      query: (command) => ({
        url: "scripts",
        method: "POST",
        body: command,
      }),
      invalidatesTags: cacher.invalidatesList("Script"),
    }),
    getWorkerAccounts: builder.query<WorkerAccountVm[], void>({
      query: (_) => "accounts/workers",
      providesTags: cacher.providesList("WorkerAccount"),
    }),
    addWorkerAccount: builder.mutation<void, AddWorkerAccount>({
      query: (command) => ({
        url: "accounts/workers",
        method: "POST",
        body: command,
      }),
      invalidatesTags: cacher.invalidatesList("WorkerAccount"),
    }),
    bulkAddWorkerAccounts: builder.mutation<void, BulkAddAccount>({
      query: (command) => ({
        url: "accounts/workers",
        method: "PATCH",
        headers: {
          "Content-Type": "text/csv",
        },
        body: command.fileContent,
      }),
      invalidatesTags: cacher.invalidatesList("WorkerAccount"),
    }),
    getMuleAccounts: builder.query<MuleAccountVm[], void>({
      query: (_) => "accounts/mules",
      providesTags: cacher.providesList("MuleAccount"),
    }),
    addMuleAccount: builder.mutation<void, AddMuleAccount>({
      query: (command) => ({
        url: "accounts/mules",
        method: "POST",
        body: command,
      }),
      invalidatesTags: cacher.invalidatesList("MuleAccount"),
    }),
    getClients: builder.query<ClientVm[], void>({
      query: (_) => "clients",
      async onCacheEntryAdded(
        arg,
        { updateCachedData, cacheDataLoaded, cacheEntryRemoved },
      ) {
        const changedListener = (client: ClientVm) => {
          updateCachedData((draft) => {
            return [...draft.filter((c) => c.id !== client.id), client]
          })
        }

        const removedListener = (clientId: number) => {
          updateCachedData((draft) => {
            return draft.filter((c) => c.id !== clientId)
          })
        }

        try {
          await cacheDataLoaded
          updateDispatcher.addUpdateListener("ClientVm", changedListener)
          updateDispatcher.addUpdateListener("ClientVmRemoved", removedListener)
        } catch {}
        await cacheEntryRemoved

        updateDispatcher.removeUpdateListener(changedListener)
        updateDispatcher.removeUpdateListener(removedListener)
      },
    }),
    getControlState: builder.query<ControlStateVm, void>({
      query: (_) => "controlstate",
      providesTags: [{ type: "ControlState" }],
    }),
    updateControlState: builder.mutation<void, UpdateControlState>({
      query: (command) => ({
        url: "controlstate",
        method: "PUT",
        body: command,
      }),
      async onQueryStarted(command, { dispatch, queryFulfilled }) {
        const patchResult = dispatch(
          api.util.updateQueryData("getControlState", undefined, (draft) => {
            Object.assign(draft, command)
          }),
        )
        try {
          await queryFulfilled
        } catch {
          patchResult.undo()
        }
      },
      invalidatesTags: [{ type: "ControlState" }],
    }),
    getEvents: builder.query<EventVm[], void>({
      query: (_) => "events",
      async onCacheEntryAdded(
        arg,
        { updateCachedData, cacheDataLoaded, cacheEntryRemoved },
      ) {
        const listener = (event: EventVm) => {
          updateCachedData((draft) => {
            draft.push(event)
          })
        }

        try {
          await cacheDataLoaded
          updateDispatcher.addUpdateListener("EventVm", listener)
        } catch {}
        await cacheEntryRemoved

        updateDispatcher.removeUpdateListener(listener)
      },
    }),
  }),
})

export const {
  useGetScriptsQuery,
  useAddScriptMutation,
  useGetWorkerAccountsQuery,
  useAddWorkerAccountMutation,
  useBulkAddWorkerAccountsMutation,
  useGetMuleAccountsQuery,
  useAddMuleAccountMutation,
  useGetClientsQuery,
  useGetControlStateQuery,
  useUpdateControlStateMutation,
  useGetEventsQuery,
} = api
