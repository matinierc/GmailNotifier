package fr.cmat.core;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.cmat.data.Configuration;

public class ConfigurationManager {
	private static final Logger LOGGER = Logger.getLogger(ConfigurationManager.class);

	private static final String KEY = "XMzDdG4D03CKm2IxIWQw7g==";
	private static final String XML_FILE = "notifier.xml";

	private static final String XML_NOTIFIER = "notifier";
	private static final String XML_SYSTEM = "system";
	private static final String XML_REFRESH = "refresh";
	private static final String XML_GMAIL = "gmail";
	private static final String XML_EMAIL = "email";
	private static final String XML_PASSWORD = "password";

	public static void read() {
		Configuration configuration = Configuration.getInstance();

		synchronized (configuration) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new File(XML_FILE));

				NodeList list = document.getElementsByTagName(XML_SYSTEM);
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						configuration.setRefresh(Integer.parseInt(element.getElementsByTagName(XML_REFRESH).item(0).getTextContent()));
					}
				}

				list = document.getElementsByTagName(XML_GMAIL);
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						configuration.setEmail(element.getAttribute(XML_EMAIL));
						String cryptedPassword = element.getAttribute(XML_PASSWORD);
						if (!"".equals(cryptedPassword.trim())) {
//							configuration.setPassword(Crypto.decrypt(StringEscapeUtils.unescapeXml(cryptedPassword), KEY));
							configuration.setPassword(Crypto.decrypt(cryptedPassword, KEY));
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("Impossible de lire la configuration : ", e);
			}
		}
	}

	public static void write() {
		Configuration configuration = Configuration.getInstance();

		synchronized (configuration) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();

				Document doc = builder.newDocument();
				Element rootElement = doc.createElement(XML_NOTIFIER);
				doc.appendChild(rootElement);

				Element system = doc.createElement(XML_SYSTEM);
				rootElement.appendChild(system);

				Element refresh = doc.createElement(XML_REFRESH);
				refresh.appendChild(doc.createTextNode("" + configuration.getRefresh()));
				system.appendChild(refresh);

				Element gmail = doc.createElement(XML_GMAIL);
				gmail.setAttribute(XML_EMAIL, "" + configuration.getEmail());
				
				if ((configuration.getPassword() != null) && (!configuration.getPassword().trim().isEmpty())) {
					String cryptedPassword = Crypto.encrypt(configuration.getPassword(), KEY);
					gmail.setAttribute(XML_PASSWORD, cryptedPassword);
				} else {
					gmail.setAttribute(XML_PASSWORD, "");
				}
				rootElement.appendChild(gmail);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(XML_FILE));
				
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.transform(source, result);
			} catch (Exception e) {
				LOGGER.error("Impossible de sauvegarder la configuration : ", e);
			}
		}
	}
}
