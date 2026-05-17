import { createNotmidApiClient } from "@notmid/api-client";
import {
  notmidFakeAuthSession,
  notmidFakeAuthStatus,
  notmidSignedOutAuthStatus,
  type NotmidAuthStatusResponse,
} from "@notmid/contracts";
import { cookies } from "next/headers";

export const notmidAuthCookieName = "notmid_fake_access_token";

export async function getNotmidAuthStatus(): Promise<NotmidAuthStatusResponse> {
  const accessToken = await getNotmidAccessToken();
  const api = createNotmidApiClient({
    baseUrl: process.env.NOTMID_API_BASE_URL,
    fetcher: noStoreFetch,
  });

  return api
    .getAuthStatus(accessToken)
    .catch(() =>
      accessToken === notmidFakeAuthSession.accessToken
        ? notmidFakeAuthStatus
        : notmidSignedOutAuthStatus,
    );
}

export async function getNotmidAccessToken(): Promise<string | undefined> {
  const cookieStore = await cookies();
  return cookieStore.get(notmidAuthCookieName)?.value;
}

export function normalizeNotmidReturnTo(value: unknown): string | undefined {
  if (typeof value !== "string" || !value.startsWith("/notmid") || value.startsWith("//")) {
    return undefined;
  }

  return value;
}

export const noStoreFetch: typeof fetch = (input, init) =>
  fetch(input, {
    ...init,
    cache: "no-store",
  });
