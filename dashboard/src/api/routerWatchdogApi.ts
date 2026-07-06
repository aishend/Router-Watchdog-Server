import type { Command, CommandType, Device, DevicesResponse } from "../types/api";

type QueueCommandResponse = {
  success: boolean;
  deviceId: string;
  command: Command;
};

export type UpdateDeviceMetadataRequest = {
  displayName: string;
  location: string | null;
  notes: string | null;
  enabled: boolean;
};

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

export function queueCommand(
  deviceId: string,
  command: CommandType,
): Promise<QueueCommandResponse> {
  return request<QueueCommandResponse>(`/api/v1/commands/${encodeURIComponent(deviceId)}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ command }),
  });
}

export function updateDeviceMetadata(
  deviceId: string,
  metadata: UpdateDeviceMetadataRequest,
): Promise<Device> {
  return request<Device>(
    `/api/v1/devices/${encodeURIComponent(deviceId)}/metadata`,
    {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(metadata),
    },
  );
}