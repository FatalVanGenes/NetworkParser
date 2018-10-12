package xml.parser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import xml.core.model.Element;
import xml.core.model.Hierarchical;
import xml.core.model.Network;
import xml.core.model.Node;
import xml.core.model.Subnet;

public class NetworkParser {

	private static final Map<String, Function<Iterator<?>, Element>> factory;

	private static XMLEventFactory eventFactory;
	private static final XMLEvent end;
	private static final XMLEvent tab;

	static {
		eventFactory = XMLEventFactory.newInstance();
		end = eventFactory.createDTD("\n");
		tab = eventFactory.createDTD("\t");
		factory = new HashMap<>();
		factory.put("network", Network::build);
		factory.put("subnet", Subnet::build);
		factory.put("node", Node::build);
	}

	private NetworkParser() {
	}

	public static Network getNetworkFromXML(String fileName) throws Exception {
		Stack<Element> stack = new Stack<>();
		int indentLevel = 0;
		Network n = null;
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

		XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
		while (xmlEventReader.hasNext()) {
			XMLEvent xmlEvent = xmlEventReader.nextEvent();
			if (xmlEvent.isStartElement()) {
				StartElement startElement = xmlEvent.asStartElement();
				String tagName = startElement.getName().getLocalPart();
				if (factory.containsKey(tagName)) {
					Element currentElement = factory.get(tagName).apply(startElement.getAttributes());
					currentElement.setRenderIndentLevel(indentLevel);
					if (tagName.equals("network")) {
						n = (Network) currentElement;
					}
					if (!stack.isEmpty()) {
						Element top = stack.peek();
						if ((top instanceof Hierarchical) && ((Hierarchical) top).acceptsChildType(currentElement)) {
							((Hierarchical) top).acceptChild(currentElement);
						}
					}
					stack.push(currentElement);
				}
				indentLevel++;
			} else if (xmlEvent.isEndElement()) {
				EndElement endElement = xmlEvent.asEndElement();
				String name = endElement.getName().getLocalPart();
				if (!stack.isEmpty()) {
					Element current = stack.peek();
					if (name.equals(current.getTypeName())) {
						// System.out.println("pop " + current);
						stack.pop();
					}
				}
				indentLevel--;
				if (indentLevel < 0) {
					throw new RuntimeException("these are the worst of times");
				}
			}
		}

		n.postProcess();
		return n;
	}

	public static void mergeAndWriteXML(final String inputPath, final String outputPath, Network network) {

		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		String rootElement = "network";
		Map<String, String> elementsMap = new HashMap<>();
		elementsMap.put("name", "Pankaj");
		elementsMap.put("age", "29");
		elementsMap.put("role", "Java Developer");
		elementsMap.put("gender", "Male");
		Map<String, String> attrs = new HashMap<>();

		try {
			// XMLEventWriter xmlEventWriter = xmlOutputFactory.createXMLEventWriter(new
			// FileOutputStream(outputPath),
			// "UTF-8");
			// For Debugging - below code to print XML to Console
			XMLEventWriter xmlEventWriter = xmlOutputFactory.createXMLEventWriter(System.out);

			writeDocumentStart(xmlEventWriter);

			xmlEventWriter.add(eventFactory.createStartElement("", "", rootElement));
			xmlEventWriter.add(eventFactory.createAttribute("version", "2.0"));
			xmlEventWriter.add(end);

			// Write the element nodes
			Set<String> elementNodes = elementsMap.keySet();
			for (Map.Entry<String, String> entry : elementsMap.entrySet()) {
				writeNode(xmlEventWriter, entry.getKey(), attrs, entry.getValue());
			}

			xmlEventWriter.add(eventFactory.createEndElement("", "", rootElement));
			xmlEventWriter.add(end);

			writeDocumentEnd(xmlEventWriter);

			xmlEventWriter.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeDocumentStart(XMLEventWriter eventWriter) throws XMLStreamException {
		eventWriter.add(eventFactory.createStartDocument());
		eventWriter.add(end);
	}

	private static void writeDocumentEnd(XMLEventWriter eventWriter) throws XMLStreamException {
		eventWriter.add(eventFactory.createEndDocument());
	}

	private static void writeNode(XMLEventWriter eventWriter, String elementName, Map<String, String> attributes,
			String elementValue) throws XMLStreamException {

		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", elementName);
		eventWriter.add(tab);
		eventWriter.add(sElement);

		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			eventWriter.add(eventFactory.createAttribute(entry.getKey(), entry.getValue()));
		}

		// Create Content
		if (elementValue != null) {
			Characters characters = eventFactory.createCharacters(elementValue);
			eventWriter.add(characters);
		}

		// Create End node
		EndElement eElement = eventFactory.createEndElement("", "", elementName);
		eventWriter.add(eElement);
		eventWriter.add(end);
	}
}
