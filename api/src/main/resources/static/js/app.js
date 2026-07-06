const DEVICES_ENDPOINT = "/api/v1/devices";
const COMMANDS_ENDPOINT = "/api/v1/commands";
const COMMAND_REFRESH_MS = 3000;
const PAGE_TIMER_REFRESH_MS = 1000;

let lastPageUpdateAt = Date.now();

const elements = {
    devicesTable: document.getElementById("devicesTable"),
    commandsTable: document.getElementById("commandsTable"),
    pageUpdatedAgo: document.getElementById("pageUpdatedAgo"),
};

function formatMinutes(seconds) {
    const minutes = Math.floor(seconds / 60);

    if (minutes <= 0) {
        return "less than 1 min ago";
    }

    if (minutes === 1) {
        return "1 min ago";
    }

    return `${minutes} mins ago`;
}

function formatDateTime(value) {
    if (!value) {
        return "-";
    }

    return new Date(value).toLocaleString();
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function renderPageUpdatedAgo() {
    const seconds = Math.floor((Date.now() - lastPageUpdateAt) / 1000);
    elements.pageUpdatedAgo.textContent = formatMinutes(seconds);
}

function renderEmptyDevices() {
    elements.devicesTable.innerHTML = `
        <tr>
            <td colspan="7" class="devices__empty">Waiting for devices...</td>
        </tr>
    `;
}

function renderEmptyCommands() {
    elements.commandsTable.innerHTML = `
        <tr>
            <td colspan="6" class="commands__empty">No commands yet...</td>
        </tr>
    `;
}

function renderDeviceRow(device) {
    const statusClass = device.deviceStatus === "UP" ? "status--up" : "status--down";

    return `
        <tr>
            <td>${escapeHtml(device.deviceId)}</td>
            <td><span class="status ${statusClass}">${escapeHtml(device.deviceStatus)}</span></td>
            <td><span class="code">${escapeHtml(device.ip)}</span></td>
            <td><span class="code">${escapeHtml(device.gateway)}</span></td>
            <td>${escapeHtml(device.failures)}</td>
            <td>${formatMinutes(device.secondsSinceLastHeartbeat)}</td>
            <td>
                <button class="button" onclick="queueRebootRouter('${escapeHtml(device.deviceId)}')">
                    Reboot router
                </button>
            </td>
        </tr>
    `;
}

function renderCommandRow(command) {
    const statusClass = `status--${String(command.status).toLowerCase()}`;

    return `
        <tr>
            <td>${escapeHtml(command.type)}</td>
            <td><span class="status ${statusClass}">${escapeHtml(command.status)}</span></td>
            <td><span class="code">${escapeHtml(command.id)}</span></td>
            <td>${formatDateTime(command.createdAt)}</td>
            <td>${formatDateTime(command.deliveredAt)}</td>
            <td>${formatDateTime(command.completedAt)}</td>
        </tr>
    `;
}

function renderDevices(devices) {
    if (!devices.length) {
        renderEmptyDevices();
        return;
    }

    elements.devicesTable.innerHTML = devices.map(renderDeviceRow).join("");
}

function renderCommands(commands) {
    if (!commands.length) {
        renderEmptyCommands();
        return;
    }

    elements.commandsTable.innerHTML = commands.map(renderCommandRow).join("");
}

async function loadDevices() {
    const response = await fetch(DEVICES_ENDPOINT);
    const data = await response.json();

    renderDevices(data.devices || []);
}

async function loadCommands() {
    const response = await fetch(COMMANDS_ENDPOINT);
    const commands = await response.json();

    renderCommands(commands || []);
}

async function queueRebootRouter(deviceId) {
    await fetch(`/api/v1/commands/${encodeURIComponent(deviceId)}`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({command: "REBOOT_ROUTER"}),
    });

    await refresh();
}

async function refresh() {
    await Promise.all([
        loadDevices(),
        loadCommands(),
    ]);

    lastPageUpdateAt = Date.now();
    renderPageUpdatedAgo();
}

refresh();
setInterval(refresh, COMMAND_REFRESH_MS);
setInterval(renderPageUpdatedAgo, PAGE_TIMER_REFRESH_MS);