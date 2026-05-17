import { createNotmidApiClient } from "@notmid/api-client";
import { notmidFixtureFeed, notmidFixtureInbox, notmidFixtureMap } from "@notmid/contracts";
import { NotmidProductShell } from "../../components/NotmidProductShell";
import { getNotmidAuthStatus } from "../../lib/notmidAuth";

export default async function NotmidPage() {
  const api = createNotmidApiClient({
    baseUrl: process.env.NOTMID_API_BASE_URL,
    fetcher: noStoreFetch,
  });

  const [feed, map, inbox, auth] = await Promise.all([
    api.getFeed().catch(() => notmidFixtureFeed),
    api.getMap().catch(() => notmidFixtureMap),
    api.getInbox().catch(() => notmidFixtureInbox),
    getNotmidAuthStatus(),
  ]);

  return <NotmidProductShell feed={feed} map={map} inbox={inbox} auth={auth} />;
}

const noStoreFetch: typeof fetch = (input, init) =>
  fetch(input, {
    ...init,
    cache: "no-store",
  });
