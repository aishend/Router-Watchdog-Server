import type { Device } from "../../types/api";

type DeviceCardProps = {
  device: Device;
  onRebootRouter: (deviceId: string) => void;
  onEditDevice: (device: Device) => void;
};

function formatUptime(seconds: number): string {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);

  if (hours > 0) {
    return `${hours}h ${minutes}m`;
  }

  return `${minutes}m`;
}

export function DeviceCard({
  device,
  onRebootRouter,
  onEditDevice,
}: DeviceCardProps) {
  const isUp = device.deviceStatus === "UP";

  return (
    <article className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <header className="mb-5 flex items-start justify-between gap-4">
        <div>
          <h3 className="text-lg font-bold text-slate-900">
            {device.displayName || device.deviceId}
          </h3>

          <p className="font-mono text-sm text-slate-500">{device.deviceId}</p>

          {device.location && (
            <p className="mt-1 text-sm font-medium text-slate-600">
              {device.location}
            </p>
          )}
        </div>

        <span
          className={
            isUp
              ? "rounded-full bg-emerald-100 px-3 py-1 text-xs font-bold text-emerald-800"
              : "rounded-full bg-red-100 px-3 py-1 text-xs font-bold text-red-800"
          }
        >
          {device.deviceStatus}
        </span>
      </header>

      {device.notes && (
        <div className="mb-5 rounded-xl border border-slate-200 bg-slate-50 p-3 text-sm text-slate-600">
          {device.notes}
        </div>
      )}

      <dl className="mb-5 grid grid-cols-2 gap-3">
        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            IP
          </dt>
          <dd className="mt-1 font-mono text-sm font-bold text-slate-900">
            {device.ip}
          </dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Gateway
          </dt>
          <dd className="mt-1 font-mono text-sm font-bold text-slate-900">
            {device.gateway}
          </dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Firmware
          </dt>
          <dd className="mt-1 font-bold text-slate-900">
            {device.firmwareVersion}
          </dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            RSSI
          </dt>
          <dd className="mt-1 font-bold text-slate-900">{device.rssi} dBm</dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Heap
          </dt>
          <dd className="mt-1 font-bold text-slate-900">
            {Math.round(device.freeHeap / 1024)} KB
          </dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Uptime
          </dt>
          <dd className="mt-1 font-bold text-slate-900">
            {formatUptime(device.uptime)}
          </dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Failures
          </dt>
          <dd className="mt-1 font-bold text-slate-900">{device.failures}</dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Last heartbeat
          </dt>
          <dd className="mt-1 font-bold text-slate-900">
            {device.secondsSinceLastHeartbeat}s ago
          </dd>
        </div>
      </dl>

      <div className="grid grid-cols-2 gap-3">
        <button
          className="rounded-xl border border-slate-300 px-4 py-2.5 font-bold text-slate-700 hover:bg-slate-50"
          type="button"
          onClick={() => onEditDevice(device)}
        >
          Edit
        </button>

        <button
          className="rounded-xl bg-slate-900 px-4 py-2.5 font-bold text-white hover:bg-slate-700 disabled:cursor-not-allowed disabled:bg-slate-400"
          type="button"
          disabled={!device.enabled}
          onClick={() => onRebootRouter(device.deviceId)}
        >
          Reboot
        </button>
      </div>
    </article>
  );
}