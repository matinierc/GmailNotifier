package fr.cmat.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import fr.cmat.core.ConfigurationManager;
import fr.cmat.data.Configuration;

public class UIConfiguration {
	private static final String LABEL_TITLE = "-==: Configuration :==-";
	private static final String LABEL_EMAIL = "Email";
	private static final String LABEL_PASSWORD = "Password";
	private static final String LABEL_SAVE = "Save";
	private static final String LABEL_EXIT = "Close";

	private JFrame frame = null;

	private JTextField email = null;
	private JPasswordField password = null;
	private JButton saveButton;

	public UIConfiguration() {
		super();
		initialiaze();
	}

	private void initialiaze() {
		frame = new JFrame();

		frame.setTitle(LABEL_TITLE);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		frame.add(buildPanel());
		frame.pack();
	}

	private JPanel buildPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		email = new JTextField(Configuration.getInstance().getEmail(), 20);
		password = new JPasswordField(Configuration.getInstance().getEmail(), 20);
		GridBagConstraints constraints = null;

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel(LABEL_EMAIL + ": "), constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(email, constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.EAST;
		panel.add(new JLabel(LABEL_PASSWORD + ": "), constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(password, constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		panel.add(buildActionPanel(), constraints);

		password.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					saveButton.doClick();
				}
			}
		});
		
		
		return panel;
	}

	private JPanel buildActionPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = null;

		saveButton = new JButton(LABEL_SAVE);
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		panel.add(saveButton, constraints);

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((frame != null) && (email != null) && (password != null)) {
					Configuration.getInstance().setEmail(email.getText());
					Configuration.getInstance().setPassword(new String(password.getPassword()));
					ConfigurationManager.write();
					frame.dispose();
				}
			}
		});

		JButton exitButton = new JButton(LABEL_EXIT);
		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 0;
		panel.add(exitButton, constraints);

		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (frame != null) {
					frame.dispose();
				}
			}
		});

		return panel;
	}

	public void launch() {
		frame.setVisible(true);
	}
}
