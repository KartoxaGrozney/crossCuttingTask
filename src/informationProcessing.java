import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class informationProcessing {
    public static void main(String[] args) {
        String fileOut = "output.txt", fileIn = "input.txt";
        String arithmeticOperations = readerFromFile(fileIn);
        double result = evaluate(arithmeticOperations);
        writerToFile(fileOut, result);  // simple method
        //writerToFile(fileOut, believesWithLib(arithmeticOperations));  //include lib
    }

    public static double believesWithLib(String arithmetic){
        Expression expression = new ExpressionBuilder(arithmetic).build();
        double result = expression.evaluate();
        return result;
    }

    public static void writerToFile(String fileName, double result){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.valueOf(result));
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readerFromFile(String fileIn){
        BufferedReader reader;
        String str = "";
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

        return str;
    }

    private static int precedence(char op) {
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
    private static double applyOperator(double a, double b, char op) {
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
    public static double evaluate(String expression) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            // Пропускаем пробелы
            if (currentChar == ' ') {
                continue;
            }

            // Если текущий символ - число
            if (Character.isDigit(currentChar)) {
                double num = 0;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    num = num * 10 + (expression.charAt(i) - '0');
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

        return values.pop(); // Возвращаем результат
    }

}
