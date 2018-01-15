package fr.cmat.core;

import org.apache.log4j.Logger;

import fr.cmat.data.Configuration;
import fr.cmat.ui.UINotifier;
import fr.cmat.ui.UIResource;

public class GmailListener implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(GmailListener.class);

	private UINotifier uiNotifier;
	
	private boolean error = false;

	public GmailListener(UINotifier uiNotifier) {
		super();
		this.uiNotifier = uiNotifier;
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				LOGGER.debug("Début d'exécution du notifier...");
				Configuration configuration = Configuration.getInstance();

				synchronized (configuration) {
					try {
						if (!error) {
							int count = GmailConnector.getNewMessagesCount(configuration.getEmail(), configuration.getPassword());
							if (count > 0) {
								uiNotifier.setIcon(UIResource.geURL(UIResource.MAIL_ALERT_ICON), count);
							} else {
								uiNotifier.setIcon(UIResource.geURL(UIResource.DEFAULT_ICON));
							}
							LOGGER.debug("Fin d'exécution du notifier...");
						}
					} catch (Exception e) {
						LOGGER.error("Erreur lors de la connexion à la boite mail : ", e);
						if (e instanceof javax.mail.AuthenticationFailedException) {
							uiNotifier.setIcon(UIResource.geURL(UIResource.ERROR_ICON));
							error = true;
						}
					}

					Thread.sleep(configuration.getRefresh());
				}
			}
		} catch (InterruptedException e) {
			LOGGER.error("Listener interrompu : ", e);
		}
	}
}
