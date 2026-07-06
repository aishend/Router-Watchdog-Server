import { useEffect, useState } from "react";

import {
  getCommands,
  getDevices,
  queueCommand,
  updateDeviceMetadata,
} from "../../api/routerWatchdogApi";
import { CommandTable } from "../../components/CommandTable";
import { ConfirmDialog } from "../../components/ConfirmDialog";
import { DeviceCard } from "../../components/DeviceCard";
import { EditDeviceDialog } from "../../components/EditDeviceDialog";
import type { Command, Device } from "../../types/api";

export function DashboardPage() {
  const [devices, setDevices] = useState<Device[]>([]);
  const [commands, setCommands] = useState<Command[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [devicePendingReboot, setDevicePendingReboot] = useState<Device | null>(null);
  const [devicePendingEdit, setDevicePendingEdit] = useState<Device | null>(null);

  async function loadDashboard() {
    try {
      const [devicesResponse, commandsResponse] = await Promise.all([
        getDevices(),
        getCommands(),
      ]);

      setDevices(devicesResponse.devices);
      setCommands(commandsResponse);
      setError(null);
    } catch {
      setError("Failed to load dashboard");
    }
  }

  async function confirmRebootRouter() {
    if (!devicePendingReboot) {
      return;
    }

    await queueCommand(devicePendingReboot.deviceId, "REBOOT_ROUTER");
    setDevicePendingReboot(null);
    await loadDashboard();
  }

  async function saveDeviceMetadata(values: {
    displayName: string;
    location: string | null;
    notes: string | null;
    enabled: boolean;
  }) {
    if (!devicePendingEdit) {
      return;
    }

    await updateDeviceMetadata(devicePendingEdit.deviceId, values);
    setDevicePendingEdit(null);
    await loadDashboard();
  }

  useEffect(() => {
    loadDashboard();

    const intervalId = window.setInterval(loadDashboard, 3000);

    return () => {
      window.clearInterval(intervalId);
    };
  }, []);

  const onlineDevices = devices.filter((device) => device.deviceStatus === "UP").length;

  return (
    <main className="min-h-screen bg-slate-100 px-4 py-10 text-slate-900">
      <div className="mx-auto max-w-6xl">
        <header className="mb-8">
          <p className="text-sm font-semibold uppercase tracking-wide text-slate-500">
            Router Watchdog
          </p>
          <h1 className="mt-1 text-3xl font-bold">Network Monitoring Dashboard</h1>
          <p className="mt-2 text-slate-600">
            {onlineDevices} / {devices.length} devices online
          </p>
        </header>

        {error && (
          <div className="mb-6 rounded-xl border border-red-200 bg-red-50 p-4 text-red-700">
            {error}
          </div>
        )}

        <section className="mb-10">
          <h2 className="mb-4 text-xl font-bold">Devices</h2>

          {devices.length === 0 ? (
            <div className="rounded-2xl border border-dashed border-slate-300 bg-white p-8 text-center text-slate-500">
              Waiting for devices...
            </div>
          ) : (
            <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
              {devices.map((device) => (
                <DeviceCard
                  key={device.deviceId}
                  device={device}
                  onRebootRouter={() => setDevicePendingReboot(device)}
                  onEditDevice={setDevicePendingEdit}
                />
              ))}
            </div>
          )}
        </section>

        <section>
          <h2 className="mb-4 text-xl font-bold">Commands</h2>
          <CommandTable commands={commands} />
        </section>
      </div>

      <ConfirmDialog
        open={devicePendingReboot !== null}
        title="Reboot router?"
        message={
          devicePendingReboot
            ? `This will power-cycle the router controlled by ${devicePendingReboot.deviceId}. Internet may be unavailable during recovery.`
            : ""
        }
        confirmLabel="Reboot router"
        onConfirm={confirmRebootRouter}
        onCancel={() => setDevicePendingReboot(null)}
      />

      <EditDeviceDialog
        open={devicePendingEdit !== null}
        device={devicePendingEdit}
        onSave={saveDeviceMetadata}
        onCancel={() => setDevicePendingEdit(null)}
      />
    </main>
  );
}