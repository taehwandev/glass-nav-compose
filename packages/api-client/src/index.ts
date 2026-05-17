import type {
  NotmidClip,
  NotmidAuthStatusResponse,
  NotmidFeedResponse,
  NotmidInboxResponse,
  NotmidMapResponse,
  NotmidPlace,
  NotmidResolvedRouteStack,
  NotmidSignInRequest,
  NotmidSignInResponse,
  NotmidThread,
} from "@notmid/contracts";

export type NotmidApiClientOptions = {
  baseUrl?: string;
  fetcher?: typeof fetch;
};

export type NotmidApiClient = {
  getHealth: () => Promise<{ ok: boolean; service: string }>;
  getAuthStatus: (accessToken?: string) => Promise<NotmidAuthStatusResponse>;
  signIn: (request: NotmidSignInRequest) => Promise<NotmidSignInResponse>;
  getFeed: () => Promise<NotmidFeedResponse>;
  getMap: () => Promise<NotmidMapResponse>;
  getInbox: () => Promise<NotmidInboxResponse>;
  getClip: (clipId: string) => Promise<NotmidClip>;
  getPlace: (placeId: string) => Promise<NotmidPlace>;
  getThread: (threadId: string) => Promise<NotmidThread>;
  resolveDeepLink: (url: string) => Promise<NotmidResolvedRouteStack>;
};

export function createNotmidApiClient(options: NotmidApiClientOptions = {}): NotmidApiClient {
  const baseUrl = normalizeBaseUrl(options.baseUrl ?? "http://localhost:8787");
  const fetcher = options.fetcher ?? fetch;

  return {
    getHealth: () => getJson(fetcher, `${baseUrl}/health`),
    getAuthStatus: (accessToken) =>
      getJson(fetcher, `${baseUrl}/v1/auth/status`, accessToken ? authHeaders(accessToken) : {}),
    signIn: (request) => postJson(fetcher, `${baseUrl}/v1/auth/fake-sign-in`, request),
    getFeed: () => getJson(fetcher, `${baseUrl}/v1/feed`),
    getMap: () => getJson(fetcher, `${baseUrl}/v1/map`),
    getInbox: () => getJson(fetcher, `${baseUrl}/v1/inbox/threads`),
    getClip: (clipId) => getJson(fetcher, `${baseUrl}/v1/clips/${encodeURIComponent(clipId)}`),
    getPlace: (placeId) => getJson(fetcher, `${baseUrl}/v1/places/${encodeURIComponent(placeId)}`),
    getThread: (threadId) =>
      getJson(fetcher, `${baseUrl}/v1/inbox/threads/${encodeURIComponent(threadId)}`),
    resolveDeepLink: (url) =>
      getJson(fetcher, `${baseUrl}/v1/deeplinks/resolve?url=${encodeURIComponent(url)}`),
  };
}

async function getJson<T>(
  fetcher: typeof fetch,
  url: string,
  headers: Record<string, string> = {},
): Promise<T> {
  const response = await fetcher(url, {
    headers: {
      accept: "application/json",
      ...headers,
    },
  });

  if (!response.ok) {
    throw new Error(`notmid API request failed: ${response.status} ${response.statusText}`);
  }

  return (await response.json()) as T;
}

async function postJson<TRequest, TResponse>(
  fetcher: typeof fetch,
  url: string,
  body: TRequest,
): Promise<TResponse> {
  const response = await fetcher(url, {
    method: "POST",
    headers: {
      accept: "application/json",
      "content-type": "application/json",
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    throw new Error(`notmid API request failed: ${response.status} ${response.statusText}`);
  }

  return (await response.json()) as TResponse;
}

function authHeaders(accessToken: string): Record<string, string> {
  return {
    authorization: `Bearer ${accessToken}`,
  };
}

function normalizeBaseUrl(baseUrl: string): string {
  return baseUrl.endsWith("/") ? baseUrl.slice(0, -1) : baseUrl;
}
