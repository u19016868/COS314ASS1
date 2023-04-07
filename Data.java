import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {

    public int C;
    public int count;
    public ArrayList<Integer> items;
    public String Name;

    public Data(String fileName) {
        readFile(fileName);
        Name = fileName;
    }

    public void readFile(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));
            items = new ArrayList<Integer>();
            count = sc.nextInt();
            C = sc.nextInt();
            while (sc.hasNextInt()) {
                items.add(sc.nextInt());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public String toString() {
        String result = count + ", " + C + ":";
        for (int i = 0; i < items.size(); i++)
            result += "[" + i + ", " + items.get(i) + "]";
        return "[" + result + "]";
    }
}