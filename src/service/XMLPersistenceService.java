package service;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Entry;

public class XMLPersistenceService implements PersistenceService {

	@Override
	public boolean load(File file) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			NodeList entryList = doc.getElementsByTagName("entry");
			for (int temp = 0; temp < entryList.getLength(); temp++) {
				Node entrynode = entryList.item(temp);
				if (entrynode.getNodeType() == Node.ELEMENT_NODE) {
					Element elem = (Element) entrynode;
					Entry entry = new Entry();
					entry.setName(elem.getAttribute(Entry.nameProperty));
					entry.setType(elem.getAttribute(Entry.typeProperty));

					NodeList propList = elem.getChildNodes();
					for (int proptmp = 0; proptmp < propList.getLength(); proptmp++) {
						Node propNode = propList.item(temp);
						if (propNode.getNodeType() == Node.ELEMENT_NODE) {
							Element prop = (Element) propNode;
							// TODO umwandlung in Entry verlegen bzw.
							// dynamischer machen
							switch (prop.getAttribute("type")) {
							case "string":
								entry.addProperty(prop.getNodeName(), new SimpleStringProperty(prop.getTextContent()));
								break;
							case "integer":
								entry.addProperty(prop.getNodeName(), new SimpleIntegerProperty(Integer.parseInt(prop.getTextContent())));
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean save(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getType() {
		return "xml";
	}

}
