import java.io.*;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

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

    public void writerToFile(String fileName){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.valueOf(rez));
            return;
        } catch (IOException e) {
            e.printStackTrace();
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
