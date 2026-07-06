#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

echo "Building dashboard..."
cd "$ROOT_DIR/dashboard"
npm run build

echo "Copying dashboard build to Spring static resources..."
rm -rf "$ROOT_DIR/api/src/main/resources/static"
mkdir -p "$ROOT_DIR/api/src/main/resources/static"
cp -r "$ROOT_DIR/dashboard/dist/"* "$ROOT_DIR/api/src/main/resources/static/"

echo "Building Spring application..."
cd "$ROOT_DIR/api"
./mvnw clean package

if [[ "${CI:-}" == "true" || "${RENDER:-}" == "true" ]]; then
  echo "CI/Render environment detected. Skipping local run prompt."
  exit 0
fi

echo ""
read -r -p "Run locally now? [Y/n] " RUN_LOCALLY
RUN_LOCALLY="${RUN_LOCALLY:-Y}"

case "$RUN_LOCALLY" in
  y|Y|yes|YES|Yes)
    echo "Starting Spring application locally..."
    ./mvnw spring-boot:run
    ;;
  n|N|no|NO|No)
    echo "Build completed. Not starting local server."
    ;;
  *)
    echo "Invalid option: $RUN_LOCALLY"
    echo "Use Y or n."
    exit 1
    ;;
esac