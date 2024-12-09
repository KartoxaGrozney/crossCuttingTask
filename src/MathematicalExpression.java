import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import javax.xml.parsers.*;

import org.json.JSONObject;
import org.w3c.dom.Document; // Использование полного имени
import org.jsoup.Jsoup;
import javax.xml.parsers.DocumentBuilder; // Для создания W3C Document
import javax.xml.parsers.DocumentBuilderFactory; // Для создания W3C Document
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.yaml.snakeyaml.Yaml;
import com.google.protobuf.InvalidProtocolBufferException;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class MathematicalExpression {
    private static String str = "";
    private static double rez;


    public double getResExp(){
        return rez;
    }

    public void readerFromFile(String fileIn){
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(fileIn));
            String line = reader.readLine();
            while (line != null) {
                str = str + line + " ";
                line = reader.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void readXmlFile(String filename) {
        try {
            // Создание фабрики и построителя документа
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Парсинг XML файла
            Document document = builder.parse(new File(filename));
            document.getDocumentElement().normalize();

            // Получение корневого элемента
            Element root = document.getDocumentElement();
            str = parseExpressionXml(root); // Получаем выражение из элемента

        } catch (Exception e) {
            e.printStackTrace();
            str = "Ошибка при чтении файла.";
        }
    }

    private String parseExpressionXml(Element root) {
        // Получаем текстовое содержимое элемента <expr>
        Element exprElement = (Element) root.getElementsByTagName("expr").item(0);
        return exprElement.getTextContent().trim(); // Возвращаем строку с выражением
    }

    public String getResult() {
        return str;
    }

    public void readHtmlFile(String filename) {
        try {
            // Создание фабрики и построителя документа
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Парсинг HTML файла
            Document document = builder.parse(new File(filename));
            document.getDocumentElement().normalize();

            // Получение корневого элемента
            Element root = document.getDocumentElement();
            str = parseExpressionHTML(root); // Получаем выражение из элемента <expr>

        } catch (Exception e) {
            e.printStackTrace();
            str = "Ошибка при чтении файла.";
        }
    }

    private String parseExpressionHTML(Element root) {
        // Получаем текстовое содержимое элемента <expr>
        NodeList exprList = root.getElementsByTagName("expr");
        if (exprList.getLength() > 0) {
            Element exprElement = (Element) exprList.item(0);
            return exprElement.getTextContent().trim(); // Возвращаем строку с выражением
        }
        return ""; // Если элемент <expr> не найден
    }

    public void readerFromJSON(String filename) {
        try {
            // Чтение содержимого файла в строку
            String content = new String(Files.readAllBytes(Paths.get(filename)));

            // Парсинг JSON
            JSONObject jsonObject = new JSONObject(content);
            str = jsonObject.getString("expr").trim(); // Получаем выражение из ключа "expr"

        } catch (Exception e) {
            e.printStackTrace();
            str = "Ошибка при чтении файла.";
        }
    }

    public void readerFromYAML(String filename) {
        try (InputStream inputStream = new FileInputStream(filename)) {
            Yaml yaml = new Yaml();
            Map<String, String> data = (Map<String, String>) yaml.load(inputStream); // Загружаем данные из YAML

            // Получаем выражение из ключа "expr"
            str = data.get("expr").trim();

        } catch (Exception e) {
            e.printStackTrace();
            str = "Ошибка при чтении файла.";
        }
    }

//    public void readerFromProtobuf(String fileIn) {
//        try (FileInputStream input = new FileInputStream(fileIn)) {
//            MyProtoMessage message = MyProtoMessage.parseFrom(input);
//            System.out.println(message);
//        } catch (IOException | InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
//    }

    public void writerToFile(String fileName){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.valueOf(rez));
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writerToFileXML(String fileName) {
        try {
            // Создание документа и корневого элемента
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            // Создание корневого элемента
            Element rootElement = document.createElement("root");
            document.appendChild(rootElement);

            // Создание элемента для данных
            Element dataElement = document.createElement("data");
            dataElement.appendChild(document.createTextNode(String.valueOf(rez))); // Преобразование double в String
            rootElement.appendChild(dataElement);

            // Запись документа в XML файл
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при записи в файл.");
        }
    }

    public void writeToHtmlFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Записываем HTML-структуру
            writer.write("<!DOCTYPE html>");
            writer.write("<html lang=\"ru\">");
            writer.write("<head>");
            writer.write("<meta charset=\"UTF-8\">");
            writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            writer.write("<title>Результат</title>");
            writer.write("</head>");
            writer.write("<body>");
            writer.write("<h1>Результат вычисления</h1>");
            writer.write("<p>Значение: " + rez + "</p>"); // Записываем значение типа double
            writer.write("</body>");
            writer.write("</html>");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при записи в файл.");
        }
    }

    public void writeToYamlFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Записываем данные в формате YAML
            writer.write("rez: " + rez); // Пример записи значения
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при записи в файл.");
        }
    }

    public void believesWithLib(){
        Expression expression = new ExpressionBuilder(str).build();
        rez = expression.evaluate();
    }

    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1; // приоритет 1
            case '*':
            case '/':
                return 2; // приоритет 2
            default:
                return 0;
        }
    }

    // Метод для выполнения операции
    private double applyOperator(double a, double b, char op) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b; // Предполагается, что деление на 0 не происходит
        }
        return 0;
    }

    // Метод для вычисления выражения
    public void evaluate() {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);

            // Пропускаем пробелы
            if (currentChar == ' ') {
                continue;
            }

            // Если текущий символ - число
            if (Character.isDigit(currentChar)) {
                double num = 0;
                while (i < str.length() && Character.isDigit(str.charAt(i))) {
                    num = num * 10 + (str.charAt(i) - '0');
                    i++;
                }
                values.push(num);
                i--; // Уменьшаем индекс, чтобы не пропустить следующий символ
            }
            // Если текущий символ - открывающая скобка
            else if (currentChar == '(') {
                operators.push(currentChar);
            }
            // Если текущий символ - закрывающая скобка
            else if (currentChar == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    double b = values.pop();
                    double a = values.pop();
                    char op = operators.pop();
                    values.push(applyOperator(a, b, op));
                }
                operators.pop(); // Удаляем '('
            }
            // Если текущий символ - оператор
            else {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(currentChar)) {
                    double b = values.pop();
                    double a = values.pop();
                    char op = operators.pop();
                    values.push(applyOperator(a, b, op));
                }
                operators.push(currentChar);
            }
        }

        // Обрабатываем оставшиеся операторы в стеке
        while (!operators.isEmpty()) {
            double b = values.pop();
            double a = values.pop();
            char op = operators.pop();
            values.push(applyOperator(a, b, op));
        }
        rez = values.pop();
    }




    public void evaluateWithRegular() {
        // Убираем пробелы
        str = str.replaceAll(" ", "");
        rez = evaluate(str);
    }

    private double evaluate(String expression) {
        // Проверяем наличие скобок
        int openParenIndex = expression.lastIndexOf('(');
        if (openParenIndex != -1) {
            // Находим соответствующую закрывающую скобку
            int closeParenIndex = expression.indexOf(')', openParenIndex);
            if (closeParenIndex != -1) {
                // Вычисляем выражение внутри скобок
                double innerResult = evaluate(expression.substring(openParenIndex + 1, closeParenIndex));
                // Заменяем выражение в строке
                expression = expression.substring(0, openParenIndex) + innerResult + expression.substring(closeParenIndex + 1);
                return evaluate(expression); // Рекурсивно вычисляем снова
            }
        }

        // Если скобок нет, вычисляем выражение по порядку операций
        return evaluateSimple(expression);
    }

    private static double evaluateSimple(String expression) {
        // Определяем регулярные выражения для чисел и операций
        String numberPattern = "(\\d+(?:\\.\\d+)?)"; // Числа (целые и дробные)
        String operationPattern = "([+\\-*/])"; // Операции

        // Полное регулярное выражение для поиска
        String fullPattern = numberPattern + operationPattern + numberPattern;

        // Пока есть операции в выражении
        while (true) {
            Pattern pattern = Pattern.compile(fullPattern);
            Matcher matcher = pattern.matcher(expression);

            if (!matcher.find()) {
                break; // Если нет совпадений, выходим из цикла
            }

            // Извлекаем числа и операцию
            double num1 = Double.parseDouble(matcher.group(1));
            String operator = matcher.group(2);
            double num2 = Double.parseDouble(matcher.group(3));

            // Вычисляем результат
            double result = compute(num1, operator, num2);

            // Заменяем в исходном выражении
            expression = matcher.replaceFirst(Double.toString(result));
        }

        return Double.parseDouble(expression);
    }

    private static double compute(double num1, String operator, double num2) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Неизвестная операция: " + operator);
        }
    }
}
