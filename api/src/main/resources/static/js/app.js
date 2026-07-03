const HEARTBEAT_ENDPOINT = "/api/v1/heartbeat/latest";
const DEVICES_REFRESH_MS = 3000;
const PAGE_TIMER_REFRESH_MS = 1000;

let lastPageUpdateAt = Date.now();

const elements = {
    devicesTable: document.getElementById("devicesTable"),
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

function escapeHtml(value) {
    return String(value)
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

function renderEmptyState() {
    elements.devicesTable.innerHTML = `
        <tr>
            <td colspan="3" class="devices__empty">Waiting for devices...</td>
        </tr>
    `;
}

function renderDeviceRow(device) {
    const statusClass = device.deviceStatus === "UP" ? "status--up" : "status--down";

    return `
        <tr>
            <td>${escapeHtml(device.deviceId)}</td>
            <td><span class="status ${statusClass}">${device.deviceStatus}</span></td>
            <td>${formatMinutes(device.secondsSinceLastHeartbeat)}</td>
        </tr>
    `;
}

function renderDevices(devices) {
    if (!devices.length) {
        renderEmptyState();
        return;
    }

    elements.devicesTable.innerHTML = devices.map(renderDeviceRow).join("");
}

async function loadDevices() {
    const response = await fetch(HEARTBEAT_ENDPOINT);
    const data = await response.json();

    renderDevices(data.devices || []);
    lastPageUpdateAt = Date.now();
    renderPageUpdatedAgo();
}

loadDevices();
setInterval(loadDevices, DEVICES_REFRESH_MS);
setInterval(renderPageUpdatedAgo, PAGE_TIMER_REFRESH_MS);
