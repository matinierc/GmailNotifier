package fr.cmat.core;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;

import com.sun.mail.imap.IMAPSSLStore;

public class GmailConnector {
	private static final String HOST = "imap.gmail.com";
	private static final int PORT = 993;

	public static int getNewMessagesCount(String email, String password) throws MessagingException {
		int count = 0;

		Properties props = new Properties();
		Session session = Session.getInstance(props);
		final URLName unusedUrlName = null;
		IMAPSSLStore store = new IMAPSSLStore(session, unusedUrlName);
		store.connect(HOST, PORT, email, password);
		count = store.getFolder("INBOX").getUnreadMessageCount();
		store.close();

		return count;
	}

}
