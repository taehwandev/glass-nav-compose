import { notmidRoutes } from "@notmid/contracts";
import Link from "next/link";
import { getNotmidAuthStatus } from "../../../../lib/notmidAuth";

export default async function ProfileSettingsPage() {
  const auth = await getNotmidAuthStatus();

  return (
    <main className="simple-shell">
      <Link className="detail-back" href={notmidRoutes.profile}>
        Profile
      </Link>
      <section className="simple-panel settings-panel">
        <p>{auth.authenticated ? `@${auth.user?.handle}` : "Settings"}</p>
        <h1>{auth.authenticated ? "local account mode" : "open-source mode"}</h1>
        <div className="settings-list">
          <span>{auth.mode} auth</span>
          <span>{auth.authenticated ? auth.user?.homeNeighborhood : "Firebase optional"}</span>
          <span>no committed secrets</span>
          {!auth.authenticated ? <Link href={notmidRoutes.login(notmidRoutes.profileSettings)}>sign in</Link> : null}
        </div>
      </section>
    </main>
  );
}
