import { serve } from "@hono/node-server";
import { Hono } from "hono";
import { cors } from "hono/cors";
import {
  findNotmidClip,
  findNotmidPlace,
  findNotmidThread,
  notmidAuthRequiredActions,
  notmidFakeAccessToken,
  notmidFakeAuthSession,
  notmidFakeAuthStatus,
  notmidFixtureFeed,
  notmidFixtureInbox,
  notmidFixtureMap,
  notmidRoutes,
  notmidSignedOutAuthStatus,
  resolveNotmidPathStack,
  type NotmidAuthMode,
  type NotmidAuthProvider,
  type NotmidSignInRequest,
} from "@notmid/contracts";

const app = new Hono();

app.use(
  "*",
  cors({
    origin: process.env.NOTMID_WEB_ORIGIN ?? "http://localhost:3000",
    allowMethods: ["GET", "POST", "OPTIONS"],
    allowHeaders: ["content-type", "authorization"],
  }),
);

app.get("/health", (context) =>
  context.json({
    ok: true,
    service: "notmid-api",
    mode: getAuthMode(),
  }),
);

app.get("/v1/auth/status", (context) => {
  const mode = getAuthMode();
  const isFakeSession =
    mode === "fake" && context.req.header("authorization") === `Bearer ${notmidFakeAccessToken}`;

  if (isFakeSession) {
    return context.json({
      ...notmidFakeAuthStatus,
      mode,
      source: "api",
    });
  }

  return context.json({
    ...notmidSignedOutAuthStatus,
    mode,
    source: "api",
    requiredFor: notmidAuthRequiredActions,
  });
});

app.post("/v1/auth/fake-sign-in", async (context) => {
  const mode = getAuthMode();

  if (mode !== "fake") {
    return context.json(
      {
        error: {
          code: "fake_auth_disabled",
          message: "Local fake sign-in is only available when NOTMID_AUTH_MODE=fake.",
        },
      },
      409,
    );
  }

  const request = await readSignInRequest(context.req);
  const provider = request.provider ?? "fake";

  if (!isAuthProvider(provider)) {
    return context.json(
      { error: { code: "invalid_auth_provider", message: "Unsupported auth provider." } },
      400,
    );
  }

  return context.json({
    mode,
    session: {
      ...notmidFakeAuthSession,
      provider,
    },
    nextPath: normalizeNextPath(request.returnTo),
  });
});

app.get("/v1/feed", (context) => context.json(notmidFixtureFeed));

app.get("/v1/map", (context) => context.json(notmidFixtureMap));

app.get("/v1/clips/:clipId", (context) => {
  const clip = findNotmidClip(context.req.param("clipId"));

  if (!clip) {
    return context.json({ error: { code: "clip_not_found", message: "Clip not found." } }, 404);
  }

  return context.json(clip);
});

app.get("/v1/places/:placeId", (context) => {
  const place = findNotmidPlace(context.req.param("placeId"));

  if (!place) {
    return context.json({ error: { code: "place_not_found", message: "Place not found." } }, 404);
  }

  return context.json(place);
});

app.get("/v1/inbox/threads", (context) => context.json(notmidFixtureInbox));

app.get("/v1/inbox/threads/:threadId", (context) => {
  const thread = findNotmidThread(context.req.param("threadId"));

  if (!thread) {
    return context.json({ error: { code: "thread_not_found", message: "Thread not found." } }, 404);
  }

  return context.json(thread);
});

app.get("/v1/deeplinks/resolve", (context) => {
  const url = context.req.query("url") ?? "/notmid";
  return context.json(resolveNotmidPathStack(url));
});

const port = Number.parseInt(process.env.NOTMID_API_PORT ?? "8787", 10);

serve(
  {
    fetch: app.fetch,
    port,
  },
  (info) => {
    console.log(`notmid API listening on http://localhost:${info.port}`);
  },
);

function getAuthMode(): NotmidAuthMode {
  const mode = process.env.NOTMID_AUTH_MODE;

  if (mode === "firebase" || mode === "disabled") {
    return mode;
  }

  return "fake";
}

async function readSignInRequest(request: {
  json: () => Promise<unknown>;
}): Promise<Partial<NotmidSignInRequest>> {
  try {
    const body = await request.json();
    return typeof body === "object" && body !== null ? (body as Partial<NotmidSignInRequest>) : {};
  } catch {
    return {};
  }
}

function isAuthProvider(provider: string): provider is NotmidAuthProvider {
  return provider === "fake" || provider === "anonymous" || provider === "google";
}

function normalizeNextPath(returnTo: unknown): string {
  if (
    typeof returnTo !== "string" ||
    !returnTo.startsWith("/notmid") ||
    returnTo.startsWith("//")
  ) {
    return notmidRoutes.capture;
  }

  return returnTo;
}
