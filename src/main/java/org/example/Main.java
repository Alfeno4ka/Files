package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
    private static String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

    static {
        strategy.setType(Employee.class);
        strategy.setColumnMapping(columnMapping);
    }

    public static void main(String[] args) {
        String fileNameCsv = "data.csv";
        String fileNameXml = "data.xml";
        List<Employee> listEmployeesFromCsv = parseCSV(fileNameCsv);
        listEmployeesFromCsv.forEach(System.out::println);
        String jsonFromCsv = listToJson(listEmployeesFromCsv);
        writeString(jsonFromCsv, "data.json");

        List<Employee> listEmployeesFromXml = parseXML(fileNameXml);
        String jsonFromXml = listToJson(listEmployeesFromXml);
        writeString(jsonFromXml, "data2.json");
    }

    private static List<Employee> parseCSV(String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            return csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Employee> parseXML(String fileName) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(fileName));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        List<Employee> result = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element employeeNode = (Element) node;
                long id = Long.parseLong(employeeNode.getElementsByTagName("id").item(0).getTextContent());
                String firstName = employeeNode.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = employeeNode.getElementsByTagName("lastName").item(0).getTextContent();
                String country = employeeNode.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(employeeNode.getElementsByTagName("age").item(0).getTextContent());
                Employee employee = new Employee(id, firstName, lastName, country, age);
                result.add(employee);
            }
        }
        return result;
    }

    private static String listToJson(List<Employee> employees) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(employees, listType);

    }

    private static void writeString(String json, String fileName) {
        File resultJson = new File(fileName);
        try (FileWriter writer = new FileWriter(resultJson)) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
