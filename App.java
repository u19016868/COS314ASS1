import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class App {
    public static void main(String[] args) throws FileNotFoundException {
        File WaescherList = new File("DataList.txt");
        Scanner Reader;
        try {
            ReentrantLock lock = new ReentrantLock();
            Reader = new Scanner(WaescherList);

            while (Reader.hasNextLine()) {
                while (Thread.activeCount() > 1) {
                }
                String line = Reader.nextLine();
                if (line.length() > 0) {
                    String[] NameOptima = line.split(",");
                    Data d = new Data(NameOptima[0]);
                    ILS ils = new ILS(d, lock);
                    ils.start();
                    Tabu t = new Tabu(d, lock);
                    t.start();
                }
            }
            Reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}