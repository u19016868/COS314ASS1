import java.util.ArrayList;

public class Bin {
    public int c;
    ArrayList<Integer> contents;

    public Bin() {
        this.c = 0;
        contents = new ArrayList<>();
    }

    public Bin(int i) {
        this.c = i;
        contents = new ArrayList<>();
        contents.add(i);
    }

    public Bin copy() {
        Bin result = new Bin();
        for (Integer i : contents) {
            result.add(i);
        }
        return result;
    }

    public void add(int i) {
        c += i;
        contents.add(i);
    }

    public void add(Bin b) {
        c += b.c;
        contents.addAll(b.contents);
    }

    public ArrayList<Integer> dump() {
        ArrayList<Integer> result = contents;
        contents = new ArrayList<>();
        c = 0;
        return result;
    }

    public int remove(int i) {
        int result = contents.remove(i);
        c -= result;
        return result;
    }

    public boolean contains(int j) {
        for (int i : contents) {
            if (i == j)
                return true;
        }
        return false;
    }

    public int removeItem(int j) {
        if (this.contains(j))
            for (int i = 0; i < this.contents.size(); i++) {
                if (this.contents.get(i) == j) {
                    this.contents.remove(i);
                    c -= j;
                    return j;
                }
            }
        return -1;
    }

    public int largest() {
        int largest = 0;
        for (int i : contents) {
            if (i > largest)
                largest = i;
        }
        return largest;

    }

    public int largestFit(int room) {
        int largest = -1;
        for (int i : contents)
            if (i > largest && i <= room)
                largest = i;
        return largest;
    }

    public String toString() {
        String result = c + ":";
        for (int i : contents)
            result += " " + i;
        return "[" + result + "]";
    }
}
