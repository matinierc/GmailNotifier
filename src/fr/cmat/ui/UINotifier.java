package fr.cmat.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import fr.cmat.core.NotifierConstant;

public class UINotifier {
	private static final Logger LOGGER = Logger.getLogger(UINotifier.class);
	private Toolkit toolkit = null;
	private TrayIcon trayIcon = null;

	public UINotifier() {
		super();
	}

	public void start(URL url) throws AWTException {
		SystemTray tray = SystemTray.getSystemTray();
		toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(url);

		trayIcon = new TrayIcon(image, "Gmail Notifier", buildMenu());
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (Desktop.isDesktopSupported()) {
						Desktop desktop = Desktop.getDesktop();
						try {
							desktop.browse(new URI(NotifierConstant.GMAIL_URL));
						} catch (Exception exception) {
							LOGGER.error("Impossible d'accéder à la boite mail : ", exception);
						}
					}
				}
			}
		});

		tray.add(trayIcon);
	}

	public void setIcon(URL url) {
		trayIcon.setImage(toolkit.getImage(url));
	}

	public void setIcon(URL url, int count) throws IOException {
		final BufferedImage image = ImageIO.read(UIResource.geURL(UIResource.MAIL_ALERT_ICON));
		int x = 0;
		int y = (int) (image.getHeight() * 0.45);
		int width = 80;
		int height = 80;
		float size = 50f;

		if (count < 10) {
			x = (int) (image.getWidth() * 0.58);
		} else if (count < 100) {
			x = (int) (image.getWidth() * 0.45);
		} else {
			x = (int) (image.getWidth() * 0.45);
		}

		String text = (count < 100) ? "" + count : "99";

		Graphics g = image.getGraphics();
		g.setColor(Color.RED);
		g.fillOval(image.getWidth() - width, 0, width, height);
		g.setColor(Color.WHITE);
		g.setFont(g.getFont().deriveFont(Font.BOLD, size));
		g.drawString(text, x, y);
		if (count > 99) {
			g.setFont(g.getFont().deriveFont(Font.BOLD, 40f));
			g.drawString("+", (int) (x + size), 40);
		}
		g.dispose();
		trayIcon.setImage(image);
	}

	private PopupMenu buildMenu() {
		PopupMenu popupMenu = new PopupMenu();

		MenuItem configMenuItem = new MenuItem("Configuration");
		configMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UIConfiguration uiConfiguration = new UIConfiguration();
				uiConfiguration.launch();
			}
		});

		MenuItem closeMenuItem = new MenuItem("Fermer");
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		popupMenu.add(configMenuItem);
		popupMenu.add(closeMenuItem);

		return popupMenu;
	}

	public static void main(String[] args) {
		try {
			int count = 500;

			final BufferedImage image = ImageIO.read(UIResource.geURL(UIResource.MAIL_ALERT_ICON));
			int x = 0;
			int y = (int) (image.getHeight() * 0.45);
			int width = 80;
			int height = 80;
			float size = 50f;

			if (count < 10) {
				x = (int) (image.getWidth() * 0.58);
			} else if (count < 100) {
				x = (int) (image.getWidth() * 0.45);
			} else {
				x = (int) (image.getWidth() * 0.45);
			}

			String text = (count < 100) ? "" + count : "99";

			Graphics g = image.getGraphics();
			g.setColor(Color.RED);
			g.fillOval(image.getWidth() - width, 0, width, height);
			g.setColor(Color.WHITE);
			g.setFont(g.getFont().deriveFont(Font.BOLD, size));
			g.drawString(text, x, y);
			if (count > 99) {
				g.setFont(g.getFont().deriveFont(Font.BOLD, 40f));
				g.drawString("+", (int) (x + size), 40);
			}
			g.dispose();

			//		trayIcon.setImage(image);

			ImageIO.write(image, "png", new File("test.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
