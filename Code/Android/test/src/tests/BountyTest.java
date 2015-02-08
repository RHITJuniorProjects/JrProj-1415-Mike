package tests;

import java.io.IOException;
import java.net.ProtocolException;

import json.JSONObject;
import junit.framework.TestCase;

public class BountyTest extends TestCase {

	private JSONObject bounty;
	private String itemUrl;

	public BountyTest(String name) {
		super(name);
		try {
			itemUrl = "https://henry-test.firebaseio.com/projects/-JdEoWCJIL99xpmSBE1i/milestones/-JdEp9QPkTZNeByz1rPi/tasks/-JdEpCbB14y88_fJ9zOP";
			JSONObject json = TestHelpers.getFirebaseSync(itemUrl
					+ String.format(".json?shallow=true"));
			assertEquals(json.get("bounties"), Boolean.TRUE);

			itemUrl += "/bounties/-JhVelYEnxtVsHSS5B5e";
			this.bounty = TestHelpers.getFirebaseSync(itemUrl
					+ String.format(".json?shallow=true"));
		} catch (Exception e) {
			// do nothing
		}
	}

	public void testGetDescription() throws ProtocolException, IOException {
		assertNotNull(this.bounty);
		assertTrue(this.bounty.has("description"));
	}

	public void testGetName() {
		assertNotNull(this.bounty);
		assertTrue(this.bounty.has("name"));
	}

	public void testGetLineLimit() {
		assertNotNull(this.bounty);
		assertTrue(this.bounty.has("line_limit"));
	}

	public void testGetPoints() {
		assertNotNull(this.bounty);
		assertTrue(this.bounty.has("points"));
	}

	public void testGetHourLimit() {
		assertNotNull(this.bounty);
		assertTrue(this.bounty.has("hour_limit"));
	}

	public void testGetDueDate() {
		assertNotNull(this.bounty);
		assertTrue(this.bounty.has("due_date"));
	}

	public void testGetClaimed() {
		assertNotNull(this.bounty);
		assertTrue(this.bounty.has("claimed"));
	}
}
