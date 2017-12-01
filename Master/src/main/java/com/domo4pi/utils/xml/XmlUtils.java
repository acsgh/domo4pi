package com.domo4pi.utils.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.util.Scanner;

public final class XmlUtils {
	private static final Logger log = LoggerFactory.getLogger(XmlUtils.class);

	private XmlUtils() {
	}

	public static <T> T readFromFileWithSchema(File file, Class<T> objectClass, String encoding, File... schemaFiles) throws FileNotFoundException, SAXException {
		InputStream[] inputStreams = new InputStream[schemaFiles.length];
		try {
			for (int i = 0; i < schemaFiles.length; i++) {
				inputStreams[i] = new FileInputStream(schemaFiles[i]);
			}
		
			return readFromStream(new FileInputStream(file), objectClass, encoding, getSchema(inputStreams));
		} finally {
			for (InputStream is: inputStreams) {
				try {
					if (is != null) {
						is.close();
					}
				} catch (Exception ex) {}
			}
		}
	}

	public static <T> T readFromResourceWithSchema(String file, Class<T> objectClass, String encoding, String... schemaFiles) throws FileNotFoundException, SAXException {
		InputStream inputStream = loadResourceInputStream(file);

		InputStream[] schemasInputStreams = new InputStream[schemaFiles.length];
		try {
			for (int i = 0; i < schemaFiles.length; i++) {
				schemasInputStreams[i] = loadResourceInputStream(schemaFiles[i]);
			}
	
			return readFromStream(inputStream, objectClass, encoding, getSchema(schemasInputStreams));
		} finally {
			for (InputStream is: schemasInputStreams) {
				try {
					if (is != null) {
						is.close();
					}
				} catch (Exception ex) {}
			}
		}
	}

	public static <T> T readFromFile(File file, Class<T> objectClass, String encoding) throws FileNotFoundException {
		InputStream inputStream = new FileInputStream(file);
		try {
			return readFromStream(inputStream, objectClass, encoding, null);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception ex) {}
		}

	}

	public static <T> T readFromResource(String file, Class<T> objectClass, String encoding) throws FileNotFoundException {
		InputStream inputStream = loadResourceInputStream(file);
		try {
			return readFromStream(inputStream, objectClass, encoding, null);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception ex) {}
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> T readFromStream(InputStream inputStream, Class<T> objectClass, String encoding, Schema schema) {
		T object = null;
		try {
			JAXBContext context = JAXBContext.newInstance(objectClass);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			if (schema != null) {
				unmarshaller.setSchema(schema);
			}
			object = (T) unmarshaller.unmarshal(fromStreamToStringReader(inputStream, encoding));

		} catch (Exception e) {
			log.error("Error parsing xml", e);
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
		return object;
	}

	public static Schema getSchemaFromResource(String... files) throws SAXException, FileNotFoundException {
		InputStream[] inputStreams = new InputStream[files.length];
		Schema schema = null;
		try {
			for (int i = 0; i < files.length; i++) {
				inputStreams[i] = loadResourceInputStream(files[i]);
	
			}
	
			schema = getSchema(inputStreams);
			
		} finally {
			for (InputStream is: inputStreams) {
				try {
					if (is != null) {
						is.close();
					}
				} catch (Exception ex) {}
			}
		}
		
		return schema;
	}

	public static Schema getSchema(InputStream... inputs) throws SAXException {
		StreamSource[] streamSource = new StreamSource[inputs.length];

		for (int i = 0; i < inputs.length; i++) {
			streamSource[i] = new StreamSource(inputs[i]);
		}

		SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sf.newSchema(streamSource);

		return schema;
	}

	private static InputStream loadResourceInputStream(String file) throws FileNotFoundException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(file);

		if (inputStream == null) {
			throw new FileNotFoundException("File: '" + file + "', not found.");
		}

		return inputStream;
	}

	private static StringReader fromStreamToStringReader(InputStream inputStream, String encoding) {
		StringBuilder content = new StringBuilder();

		Scanner scanner = null;
		try {
			scanner = new Scanner(inputStream, encoding);

			while (scanner.hasNextLine()) {
				content.append(scanner.nextLine()).append("\n");
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return new StringReader(content.toString());
	}

	public static <T> void saveToFile(File file, T object, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
		FileOutputStream is = new FileOutputStream(file);
		try { 
			saveToStream(is, object, encoding);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (Exception ex) {}
		}
	}

	public static <T> void saveToStream(OutputStream outputStream, T object, String encoding) throws UnsupportedEncodingException {
		PrintStream stream = new PrintStream(outputStream, true, encoding);
		try {
			Class<?> clazz = object.getClass();
			JAXBContext context = JAXBContext.newInstance(clazz);

			Marshaller marshaller = context.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

			marshaller.marshal(object, stream);
		} catch (JAXBException e) {
			log.error("Error parsing xml", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (Exception e) {};
		}
	}

	public static String formatXml(String unformattedXml) {
		InputSource is = new InputSource(new StringReader(unformattedXml));
		final Document document = loadXmlFromInputSource(is);

		return formatXml(document);
	}

	public static String formatXml(Document document) {
		String result = null;
		StringWriter writer = null;
		try {
			// create a transformer
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();

			// set some options on the transformer
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			// get the supporting classes for the transformer
			writer = new StringWriter();
			StreamResult streamResult = new StreamResult(writer);
			DOMSource source = new DOMSource(document);

			// transform the xml document into a string
			transformer.transform(source, streamResult);

			// close the output file

			result = writer.toString();
		} catch (Exception e) {
			log.error("Unable to format the XML", e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception ex) {}
		}
		return result;
	}

	private static Document loadXmlFromInputSource(InputSource is) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(is);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
