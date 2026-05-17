#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

pnpm_cmd() {
  if command -v pnpm >/dev/null 2>&1; then
    pnpm "$@"
  else
    npm exec --yes pnpm@10.12.1 -- "$@"
  fi
}

ensure_js_deps() {
  if [[ -x apps/api/node_modules/.bin/tsx && -x apps/web/node_modules/.bin/next ]]; then
    echo "Web/API dependencies already installed"
  else
    pnpm_cmd install --frozen-lockfile
  fi
}

echo "== Patch hygiene =="
git diff --check

echo "== Android tests =="
./gradlew test

echo "== Android debug APK =="
./gradlew :app:assembleDebug

echo "== Web/API dependencies =="
ensure_js_deps

echo "== Web/API typecheck =="
packages/contracts/node_modules/.bin/tsc -p packages/contracts/tsconfig.json --noEmit
packages/api-client/node_modules/.bin/tsc -p packages/api-client/tsconfig.json --noEmit
apps/api/node_modules/.bin/tsc -p apps/api/tsconfig.json --noEmit
apps/web/node_modules/.bin/tsc -p apps/web/tsconfig.json --noEmit

echo "== API build =="
apps/api/node_modules/.bin/tsc -p apps/api/tsconfig.json --noEmit

echo "== Web build =="
(
  cd apps/web
  node_modules/.bin/next build
)

echo "verify-local passed"
