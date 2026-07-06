import { useEffect, useState } from "react";

import type { Device } from "../../types/api";

type EditDeviceDialogProps = {
  open: boolean;
  device: Device | null;
  onSave: (values: {
    displayName: string;
    location: string | null;
    notes: string | null;
    enabled: boolean;
  }) => void;
  onCancel: () => void;
};

export function EditDeviceDialog({
  open,
  device,
  onSave,
  onCancel,
}: EditDeviceDialogProps) {
  const [displayName, setDisplayName] = useState("");
  const [location, setLocation] = useState("");
  const [notes, setNotes] = useState("");
  const [enabled, setEnabled] = useState(true);

  useEffect(() => {
    if (!device) {
      return;
    }

    setDisplayName(device.displayName || device.deviceId);
    setLocation(device.location ?? "");
    setNotes(device.notes ?? "");
    setEnabled(device.enabled);
  }, [device]);

  if (!open || !device) {
    return null;
  }

  function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();

    onSave({
      displayName: displayName.trim(),
      location: location.trim() || null,
      notes: notes.trim() || null,
      enabled,
    });
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/50 px-4">
      <form
        onSubmit={handleSubmit}
        className="w-full max-w-lg rounded-2xl bg-white p-6 shadow-xl"
      >
        <h2 className="text-xl font-bold text-slate-900">Edit device</h2>

        <p className="mt-1 font-mono text-sm text-slate-500">{device.deviceId}</p>

        <div className="mt-6 space-y-4">
          <label className="block">
            <span className="text-sm font-semibold text-slate-700">Display name</span>
            <input
              className="mt-1 w-full rounded-xl border border-slate-300 px-3 py-2 text-slate-900 outline-none focus:border-slate-500"
              value={displayName}
              onChange={(event) => setDisplayName(event.target.value)}
              required
            />
          </label>

          <label className="block">
            <span className="text-sm font-semibold text-slate-700">Location</span>
            <input
              className="mt-1 w-full rounded-xl border border-slate-300 px-3 py-2 text-slate-900 outline-none focus:border-slate-500"
              value={location}
              onChange={(event) => setLocation(event.target.value)}
              placeholder="Entrada / Rack"
            />
          </label>

          <label className="block">
            <span className="text-sm font-semibold text-slate-700">Notes</span>
            <textarea
              className="mt-1 min-h-24 w-full rounded-xl border border-slate-300 px-3 py-2 text-slate-900 outline-none focus:border-slate-500"
              value={notes}
              onChange={(event) => setNotes(event.target.value)}
              placeholder="Router principal, operador, detalhes..."
            />
          </label>

          <label className="flex items-center gap-3">
            <input
              type="checkbox"
              checked={enabled}
              onChange={(event) => setEnabled(event.target.checked)}
              className="h-4 w-4"
            />
            <span className="text-sm font-semibold text-slate-700">
              Enabled
            </span>
          </label>
        </div>

        <div className="mt-6 flex justify-end gap-3">
          <button
            type="button"
            onClick={onCancel}
            className="rounded-xl border border-slate-300 px-4 py-2 font-semibold text-slate-700 hover:bg-slate-50"
          >
            Cancel
          </button>

          <button
            type="submit"
            className="rounded-xl bg-slate-900 px-4 py-2 font-semibold text-white hover:bg-slate-700"
          >
            Save changes
          </button>
        </div>
      </form>
    </div>
  );
}