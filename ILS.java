import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class ILS extends Thread {
    private Data data;
    public ArrayList<Bin> s1;
    public ArrayList<Bin> sStar;
    private int C;
    private Random random;
    private int N;
    private FileWriter fw;
    private ReentrantLock lock;

    public ILS(Data d, ReentrantLock lock) {
        this.data = d;
        this.lock = lock;
        s1 = new ArrayList<Bin>();
        sStar = new ArrayList<Bin>();
        C = d.C;
        random = new Random(12345);
    }

    public void run() {
        System.out.println("ILS" + data.Name + " Start");
        solve();
    }

    public void solve() {
        N = data.count * 10;
        // generate initial solution
        for (Integer i : data.items)
            s1.add(new Bin(i));

        // First Fit
        sStar = FisrtFit(s1);

        while (0 < N--) {
            s1 = Perturbation(sStar);
            ArrayList<Bin> sStar1 = localSearch(s1);
            if (sStar.size() > sStar1.size())
                sStar = sStar1;
        }
        lock.lock();
        try {
            this.fw = new FileWriter("Results.txt", true);
            System.out.println("ILS" + data.Name + " " + sStar.size());
            fw.write("ILS" + data.Name + "\t\t\t\t\t" + sStar.size() + "\n");
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private ArrayList<Bin> localSearch(ArrayList<Bin> s) {
        Node root = generateNeighbors(s, 2);
        return bestSolution(root).data;
    }

    private Node bestSolution(Node root) {
        Node best = root;
        if (root.neighbours != null)
            for (Node n : root.neighbours) {
                if (n.weight < best.weight)
                    best = bestSolution(n);
            }
        return best;
    }

    private Node generateNeighbors(ArrayList<Bin> currSol, int x) {
        Node root = new Node(currSol);
        if (x > 0)
            for (int i = 0; i < currSol.size(); i++) {
                for (int j = 0; j < currSol.get(i).contents.size(); j++) {
                    ArrayList<Bin> b = deepCopy(currSol);
                    int temp = b.get(i).remove(j);
                    if (b.get(i).c == 0)
                        b.remove(i);
                    int room = data.C;
                    int index = -1;
                    for (int k = 0; k < b.size(); k++) {
                        if (k == i)
                            continue;
                        if (b.get(k).c + temp <= C && b.get(k).c < room) {
                            room = C - b.get(k).c;
                            index = k;
                        }
                    }
                    if (index == -1)
                        continue;
                    b.get(index).add(temp);
                    root.add(generateNeighbors(b, x - 1));
                }
            }
        return root;
    }

    private ArrayList<Bin> deepCopy(ArrayList<Bin> currSol) {
        ArrayList<Bin> b = new ArrayList<Bin>();
        for (int i = 0; i < currSol.size(); i++) {
            b.add(currSol.get(i).copy());
        }
        return b;
    }

    private ArrayList<Bin> Perturbation(ArrayList<Bin> s) {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (int i = random.nextInt(data.count / 2); i > 0; i--) {
            int ran1 = random.nextInt(s.size());
            int ran2 = random.nextInt(s.get(ran1).contents.size());
            temp.add(s.get(ran1).remove(ran2));
            if (s.get(ran1).c == 0)
                s.remove(ran1);
        }
        while (!temp.isEmpty()) {
            int j = 0;
            for (j = 0; j < s.size(); j++)
                if (s.get(j).c + temp.get(0) <= C)
                    break;
            if (j == s.size())
                s.add(new Bin(temp.remove(0)));
            else
                s.get(j).add(temp.remove(0));
        }
        return s;
    }

    private ArrayList<Bin> FisrtFit(ArrayList<Bin> s) {
        ArrayList<Bin> bs = new ArrayList<>();
        bs.add(s.remove(0));
        while (s.size() > 0) {
            int j = 0;
            for (j = 0; j < bs.size(); j++)
                if (bs.get(j).c + s.get(0).c <= C)
                    break;
            if (j == bs.size())
                bs.add(s.remove(0));
            else
                bs.get(j).add(s.remove(0));
        }

        return bs;
    }
}
