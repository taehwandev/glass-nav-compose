import { createNotmidApiClient } from "@notmid/api-client";
import {
  notmidClips,
  notmidFakeAuthSession,
  notmidRoutes,
  type NotmidAuthProvider,
} from "@notmid/contracts";
import { cookies } from "next/headers";
import Link from "next/link";
import { redirect } from "next/navigation";
import {
  getNotmidAuthStatus,
  normalizeNotmidReturnTo,
  noStoreFetch,
  notmidAuthCookieName,
} from "../../../lib/notmidAuth";

type LoginPageProps = {
  searchParams?: Promise<Record<string, string | string[] | undefined>>;
};

export default async function LoginPage({ searchParams }: LoginPageProps) {
  const params = await searchParams;
  const rawNext = Array.isArray(params?.next) ? params?.next[0] : params?.next;
  const nextPath = normalizeNotmidReturnTo(rawNext) ?? notmidRoutes.capture;
  const auth = await getNotmidAuthStatus();
  const heroClip = notmidClips[1] ?? notmidClips[0];

  return (
    <main className="login-shell">
      <Link className="detail-back" href={notmidRoutes.home}>
        notmid
      </Link>

      <section
        className="login-media"
        style={{
          backgroundImage: `linear-gradient(180deg, rgba(9, 10, 8, 0.1), rgba(9, 10, 8, 0.78)), url(${heroClip.coverImageUrl})`,
        }}
      >
        <div className="login-media-copy">
          <span>{heroClip.creatorHandle}</span>
          <h1>{heroClip.title}</h1>
          <p>{heroClip.caption}</p>
        </div>
      </section>

      <section className="login-panel" aria-labelledby="notmid-login-title">
        <p className="login-kicker">{auth.authenticated ? "signed in" : "notmid account"}</p>
        <h2 id="notmid-login-title">
          {auth.authenticated ? `ready as @${auth.user?.handle}` : "keep your receipts attached"}
        </h2>
        <p className="login-copy">
          Feed and map stay open. Capture, saves, chats, and profile edits start behind this local
          auth boundary.
        </p>

        <div className="auth-mode-row">
          <span>{auth.mode}</span>
          <strong>
            {auth.authenticated ? auth.user?.homeNeighborhood : "no production Firebase config"}
          </strong>
        </div>

        <div className="login-actions">
          <form action={continueWithProvider}>
            <input name="provider" type="hidden" value="fake" />
            <input name="returnTo" type="hidden" value={nextPath} />
            <button className="login-primary" type="submit">
              Continue in local mode
            </button>
          </form>
          <form action={continueWithProvider}>
            <input name="provider" type="hidden" value="google" />
            <input name="returnTo" type="hidden" value={nextPath} />
            <button className="login-secondary" type="submit">
              Preview Google handoff
            </button>
          </form>
          <Link className="login-secondary" href={notmidRoutes.feed}>
            Browse signed out
          </Link>
        </div>

        <div className="auth-contract" aria-label="auth contract">
          <span>GET /v1/auth/status</span>
          <span>POST /v1/auth/fake-sign-in</span>
          <span>{nextPath}</span>
        </div>
      </section>
    </main>
  );
}

async function continueWithProvider(formData: FormData) {
  "use server";

  const provider = parseProvider(formData.get("provider"));
  const returnTo = normalizeNotmidReturnTo(formData.get("returnTo")) ?? notmidRoutes.capture;
  const api = createNotmidApiClient({
    baseUrl: process.env.NOTMID_API_BASE_URL,
    fetcher: noStoreFetch,
  });

  const response = await api
    .signIn({ provider, intent: provider === "google" ? "profile" : "capture", returnTo })
    .catch(() => ({
      mode: "fake" as const,
      session: {
        ...notmidFakeAuthSession,
        provider,
      },
      nextPath: returnTo,
    }));

  const cookieStore = await cookies();
  cookieStore.set(notmidAuthCookieName, response.session.accessToken, {
    httpOnly: true,
    maxAge: 60 * 60 * 24 * 7,
    path: "/notmid",
    sameSite: "lax",
    secure: process.env.NODE_ENV === "production",
  });

  redirect(response.nextPath);
}

function parseProvider(value: FormDataEntryValue | null): NotmidAuthProvider {
  return value === "anonymous" || value === "google" ? value : "fake";
}
