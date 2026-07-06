export type DeviceStatus = "UP" | "DOWN";

export type Device = {
  deviceId: string;
  deviceStatus: DeviceStatus;
  ip: string;
  gateway: string;
  failures: number;
  uptime: number;
  rssi: number;
  freeHeap: number;
  firmwareVersion: string;
  lastReceivedAt: string;
  secondsSinceLastHeartbeat: number;
};

export type DevicesResponse = {
  devices: Device[];
  serverTime: string;
};

export type CommandStatus = "QUEUED" | "DELIVERED" | "COMPLETED" | "FAILED";

export type CommandType = "REBOOT_ROUTER" | "REBOOT_DEVICE";

export type Command = {
  id: string;
  type: CommandType;
  status: CommandStatus;
  createdAt: string;
  deliveredAt: string | null;
  completedAt: string | null;
};