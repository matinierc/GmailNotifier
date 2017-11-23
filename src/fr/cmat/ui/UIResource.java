package fr.cmat.ui;

import java.net.URL;

import org.apache.log4j.Logger;

import fr.cmat.GmailNotifier;

public class UIResource {
	private static final Logger LOGGER = Logger.getLogger(GmailNotifier.class);

	public static final String DEFAULT_ICON = "fr/cmat/resources/default.png";
	public static final String MAIL_ALERT_ICON = "fr/cmat/resources/mail_alert.png";

	public static URL geURL(String resource) {
		try {
			return Thread.currentThread().getContextClassLoader().getResource(resource);
		} catch (Exception e) {
			LOGGER.error("Impossible d'obtenir la ressource ["+resource+"] : ", e);
		}
		return null;
	}
}
