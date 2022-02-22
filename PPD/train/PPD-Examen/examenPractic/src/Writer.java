import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    BufferedWriter bufferedWriter;

    public Writer() throws IOException {
    }

    public void write(String string) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("out.txt", true))) {
            bufferedWriter.write(string);
            bufferedWriter.write('\n');
        }
    }
}
