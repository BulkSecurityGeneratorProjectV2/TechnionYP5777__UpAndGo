package model.loader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.course.Course;

class xmlParser {
	public static List<Course> getCourses() {
		Course.CourseBuilder cb = new Course.CourseBuilder();
		List<Course> $ = new LinkedList<>();
		// resList.add
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("REPFILE/REP.XML");
			NodeList coursesList = doc.getElementsByTagName("Course");
			
			for (int i = 0; i < coursesList.getLength(); ++i) {
				Node p = coursesList.item(i);
				if (p.getNodeType() == Node.ELEMENT_NODE)
					$.add(cb.setId(Integer.parseInt(((Element) p).getAttribute("id")))
							.setName(((Element) p).getElementsByTagName("name").item(0).getTextContent()).build());
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return $;
	}
}