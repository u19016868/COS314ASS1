import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Tabu extends Thread {
    private Data data;
    private int C;
    private int z;
    public ArrayList<Bin> x;
    private int N;
    private ReentrantLock lock;
    private FileWriter fw;

    public Tabu(Data d, ReentrantLock lock) {
        this.lock = lock;
        this.data = d;
        z = 1000;
        this.data = d;
        N = data.count * 10;
        C = d.C;
    }

    public void run() {
        System.out.println("TAB" + data.Name + " Start");
        solve();
    }

    public void solve() {
        /*
         * Initalize x
         * L = [];
         * z = Max Length of L
         * do
         * x' = neigbor of x
         * if (x not in L)
         * if(length of L > z)
         * Remove oldest from L
         * L.add(x');
         * if(x'>x)
         * x = x'
         * while(criteria)
         * return x;
         */

        x = new ArrayList<Bin>();

        // generate initial solution
        for (Integer i : data.items)
            x.add(new Bin(i));

        x = FisrtFit(x);

        ArrayList<Node> L = new ArrayList<>();
        Node x1;
        Node bestNeighbour;
        do {
            x1 = generateNeighbors(x, 2);
            Node Canditate = null;
            bestNeighbour = x1;
            for (Node n : x1.neighbours) // save all neighbours
            {
                if (contains(L, n))
                    continue;
                if (L.size() >= z)
                    L.remove(0);
                if (n.data.size() < bestNeighbour.data.size()) {
                    bestNeighbour = n;
                }
                if (Canditate == null || n.data.size() < Canditate.data.size()) {
                    Canditate = n;
                }
            }
            if (bestNeighbour.equals(x1) && Canditate != null)
                bestNeighbour = Canditate;
            x = bestNeighbour.data;
        } while (N-- > 0 && !bestNeighbour.equals(x1));
        lock.lock();
        try {
            this.fw = new FileWriter("Results.txt");
            System.out.println("TAB" + data.Name + " " + x.size());
            fw.write("TAB" + data.Name + "\t\t\t\t\t" + x.size() + "\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private boolean contains(ArrayList<Node> l, Node n) {
        boolean flag = false;
        for (int i = 0; i < l.size() && !flag; i++) {
            flag = false;
            for (int j = 0; j < l.get(i).data.size(); j++) {
                if (n.data.contains(l.get(i).data.get(j)) && n.data.size() == l.get(i).data.size())
                    flag = true;
                else {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    private Node generateNeighbors(ArrayList<Bin> currSol, int x) {
        Node root = new Node(currSol);
        if (x > 0) {
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
                        b.add(new Bin(temp));
                    else
                        b.get(index).add(temp);
                    root.add(generateNeighbors(b, x - 1));
                }
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
