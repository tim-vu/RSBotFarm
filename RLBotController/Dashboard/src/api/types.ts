//----------------------
// <auto-generated>
//     Generated using the NSwag toolchain v13.19.0.0 (NJsonSchema v10.9.0.0 (Newtonsoft.Json v13.0.0.0)) (http://NSwag.org)
// </auto-generated>
//----------------------

/* tslint:disable */
/* eslint-disable */
// ReSharper disable InconsistentNaming



export interface ScriptVm {
    id: number;
    name: string;
    instances: number;
    priority: number;
    schedule?: ScheduleVm | undefined;
}

export interface ScheduleVm {
    timeRanges: TimeRange[];
}

export interface ValueObject {
}

export interface TimeRange extends ValueObject {
    start: string;
    end: string;
}

export interface ProblemDetails {
    type?: string | undefined;
    title?: string | undefined;
    status?: number | undefined;
    detail?: string | undefined;
    instance?: string | undefined;

    [key: string]: any;
}

export interface AddScript {
    name: string;
    instances: number;
    priority: number;
}

export interface UpdateScriptSchedule {
    scriptId: number;
    schedule?: Schedule | undefined;
}

export interface Schedule {
    timeRanges: TimeRange[];
}

export interface EventVm {
    id: number;
    at: number;
    message: string;
}

export interface ControlStateVm {
    status: FarmStatus;
}

export enum FarmStatus {
    Running = "Running",
    Paused = "Paused",
    Stopped = "Stopped",
}

export interface UpdateControlState {
    status: FarmStatus;
}

export interface ClientVm {
    id: number;
    botId: string;
    status: ClientStatus;
    startedAt?: number | undefined;
    activeScript?: ActiveScriptVm | undefined;
    websocketPort?: number | undefined;
}

export enum ClientStatus {
    Created = "Created",
    Starting = "Starting",
    Running = "Running",
    StopRequested = "StopRequested",
    Stopped = "Stopped",
}

export interface ActiveScriptVm {
    id: number;
    name: string;
}

export interface WorkerAccountVm {
    id: number;
    username: string;
    password: string;
    status: AccountStatus;
    membershipExpiry?: number | undefined;
}

export enum AccountStatus {
    Valid = "Valid",
    Banned = "Banned",
    InvalidCredentials = "InvalidCredentials",
}

export interface AddWorkerAccount {
    username: string;
    password: string;
    membershipExpiry?: Date | undefined;
}

export interface MuleAccountVm {
    id: number;
    username: string;
    password: string;
    status: AccountStatus;
    membershipExpiry?: number | undefined;
}

export interface AddMuleAccount {
    username: string;
    password: string;
    gold: number;
    membershipExpiry?: Date | undefined;
}

export interface FileResponse {
    data: Blob;
    status: number;
    fileName?: string;
    headers?: { [name: string]: any };
}