import java.io.IOException;

public class informationProcessing {
    public static void main(String[] args) {
        String fileOut = "output.txt", fileIn = "input.txt";

        MathematicalExpression operation = new MathematicalExpression();

        operation.readerFromFile(fileIn);
        operation.evaluate();
        //operation.believesWithLib();
        operation.writerToFile(fileOut);

        try {
            AuxiliaryАunctions.archiveFileZip(fileOut, "arch.zip");
            AuxiliaryАunctions.unzipFile("arch.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
