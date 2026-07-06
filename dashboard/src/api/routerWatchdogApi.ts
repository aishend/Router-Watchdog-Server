import type { Command, CommandType, DevicesResponse } from "../types/api";

async function request<T>(url: string, options?: RequestInit): Promise<T> {
  const response = await fetch(url, options);

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`);
  }

  return response.json() as Promise<T>;
}

export function getDevices(): Promise<DevicesResponse> {
  return request<DevicesResponse>("/api/v1/devices");
}

export function getCommands(): Promise<Command[]> {
  return request<Command[]>("/api/v1/commands");
}

export function queueCommand(deviceId: string, command: CommandType): Promise<Command> {
  return request<Command>(`/api/v1/commands/${encodeURIComponent(deviceId)}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ command }),
  });
}