import { notmidRoutes } from "@notmid/contracts";
import Link from "next/link";
import { getNotmidAuthStatus } from "../../../lib/notmidAuth";

export default async function CapturePage() {
  const auth = await getNotmidAuthStatus();

  return (
    <main className="simple-shell">
      <Link className="detail-back" href={notmidRoutes.home}>
        notmid
      </Link>
      <section className="simple-panel capture-panel">
        <p>{auth.authenticated ? `@${auth.user?.handle}` : "Capture"}</p>
        <h1>{auth.authenticated ? "show the receipt" : "sign in to publish receipts"}</h1>
        {auth.authenticated ? (
          <div className="capture-grid">
            <span>clip</span>
            <span>place</span>
            <span>caption</span>
            <span>publish</span>
          </div>
        ) : (
          <div className="auth-gate">
            <span>capture</span>
            <span>save</span>
            <span>chat</span>
            <Link href={notmidRoutes.login(notmidRoutes.capture)}>Continue</Link>
          </div>
        )}
      </section>
    </main>
  );
}
