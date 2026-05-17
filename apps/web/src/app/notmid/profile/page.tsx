import { notmidRoutes } from "@notmid/contracts";
import Link from "next/link";
import { getNotmidAuthStatus } from "../../../lib/notmidAuth";

export default async function ProfilePage() {
  const auth = await getNotmidAuthStatus();

  return (
    <main className="simple-shell">
      <Link className="detail-back" href={notmidRoutes.home}>
        notmid
      </Link>
      <section className="simple-panel profile-panel">
        <p>{auth.authenticated ? `@${auth.user?.handle}` : "Profile"}</p>
        <h1>{auth.authenticated ? "receipts saved" : "profile starts after sign in"}</h1>
        {auth.authenticated ? (
          <div className="profile-stats">
            <span>12 clips</span>
            <span>31 saves</span>
            <Link href={notmidRoutes.profileSettings}>settings</Link>
          </div>
        ) : (
          <div className="auth-gate">
            <span>profile edit</span>
            <span>saved places</span>
            <span>creator stats</span>
            <Link href={notmidRoutes.login(notmidRoutes.profile)}>Continue</Link>
          </div>
        )}
      </section>
    </main>
  );
}
