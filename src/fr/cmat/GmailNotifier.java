package fr.cmat;

import java.awt.SystemTray;

import org.apache.log4j.Logger;

import fr.cmat.core.ConfigurationManager;
import fr.cmat.core.GmailListener;
import fr.cmat.ui.UINotifier;
import fr.cmat.ui.UIResource;

public class GmailNotifier {
	private static final Logger LOGGER = Logger.getLogger(GmailNotifier.class);

	public static void main(String[] args) {
		try {
			LOGGER.info("================== GMAIL NOTIFIER ==================");
			if (!SystemTray.isSupported()) {
				LOGGER.fatal("Impossible d'utiliser le système d'icone de l'environnement");
			} else {
				ConfigurationManager.read();
				UINotifier uiNotifier = new UINotifier();
				uiNotifier.start(UIResource.geURL(UIResource.DEFAULT_ICON));

				Thread listener = new Thread(new GmailListener(uiNotifier));
				listener.run();
			}
		} catch (Exception e) {
			LOGGER.fatal("Erreur d'exécution : ", e);
		}
	}
}
