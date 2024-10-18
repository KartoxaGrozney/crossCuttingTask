import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UserInterface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Обработка файлов");

        // Поля для ввода типов файлов
        TextField inputFileField = new TextField();
        inputFileField.setPromptText("Введите тип входного файла");
        TextField outputFileField = new TextField();
        outputFileField.setPromptText("Введите тип выходного файла");

        // Кнопка для выполнения обработки
        Button processButton = new Button("Отправить");

        // Поле для вывода результата
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false); // Запрет редактирования

        // Обработчик нажатия кнопки
        processButton.setOnAction(e -> {
            String inputFileName = inputFileField.getText();
            String outputFileName = outputFileField.getText();
            informationProcessing.setFileName(inputFileName, outputFileName);
            informationProcessing.mainFoo();
            String result = String.valueOf(informationProcessing.getExpValue());
            showResult(result, resultArea);
        });

        // Настройка макета
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Тип входного файла:"), inputFileField,
                new Label("Тип выходного файла:"), outputFileField,
                processButton,
                new Label("Результат:"),
                resultArea
        );

        // Настройка сцены и отображение
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showResult(String result, TextArea resultArea) {
        resultArea.setText(result); // Вывод результата в TextArea
    }
}