import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

/**
 * Created by Admin on 4/13/2017.
 */
public class Solver {
    private final Board initial;
    private int minMoves;
    private Iterable<Board> graph;


    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException();
        this.initial = initial;
        graph = solution();
    }

    // find a solution to the initial board (using the A* algorithm)
    public boolean isSolvable() {

        return solution() != null;
    }            // is the initial board solvable?

    public int moves() {
        return minMoves;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> pqTwin = new MinPQ<>();
        Board twin = initial.twin();
        Stack<Board> result = new Stack<>();
        Node root = new Node(initial, 0, initial.manhattan(), null);
        Node rootTwin = new Node(twin, 0, twin.manhattan(), null);
//        System.out.println(initial.toString());
        pq.insert(root);
        pqTwin.insert(rootTwin);
        Node node;
        Node nodeTwin;
        while (true) {
            node = pq.delMin();
            nodeTwin = pqTwin.delMin();
//            result.enqueue(node.board);
//            System.out.println("Moves: " + node.moves + " priority: " + node.priority);
//            System.out.println(node.board.toString());

            if (node.board.isGoal()) {
                minMoves = node.moves;
                while (node.prev != null) {
                    result.push(node.board);
                    node = node.prev;
                }
                result.push(node.board);
                return result;
            }
            if (nodeTwin.board.isGoal()) {
//                System.out.println("Twin success!");
                minMoves = -1;
                result = null;
                return result;
            }
            for (Board b : node.board.neighbors()) {
                if (node.prev == null) {
                    pq.insert(new Node(b, node.moves + 1, b.manhattan() + node.moves + 1, node));
                } else {
                    if (!b.equals(node.prev.board)) {
                        pq.insert(new Node(b, node.moves + 1, b.manhattan() + node.moves + 1, node));
                    }
                }
            }
            for (Board bTwin : nodeTwin.board.neighbors()) {
                if (nodeTwin.prev == null) {
                    pqTwin.insert(new Node(bTwin, nodeTwin.moves + 1, bTwin.manhattan() + nodeTwin.moves + 1, nodeTwin));
                } else {
                    if (!bTwin.equals(nodeTwin.prev.board)) {
                        pqTwin.insert(new Node(bTwin, nodeTwin.moves + 1, bTwin.manhattan() + nodeTwin.moves + 1, nodeTwin));
                    }
                }
            }
        }
    }      // sequence of boards in a shortest solution; null if unsolvable

    private class Node implements Comparable<Node> {
        Board board;
        int moves;
        int priority;
        Node prev;

        public Node(Board board, int moves, int priority, Node prev) {
            this.board = board;
            this.moves = moves;
            this.priority = priority;
            this.prev = prev;
        }

        @Override
        public int compareTo(Node o) {
            if (priority < o.priority) return -1;
            if (priority > o.priority) return 1;
            return 0;
        }
    }

    public static void main(String[] args) {

//        int[][] test = new int[3][3];
//        test[0][0] = 1;
//        test[0][1] = 2;
//        test[0][2] = 3;
//        test[1][0] = 4;
//        test[1][1] = 5;
//        test[1][2] = 6;
//        test[2][0] = 8;
//        test[2][1] = 7;
//        test[2][2] = 0;
//        Board board = new Board(test);
//        System.out.println(board.manhattan());
//        Solver solver = new Solver(board);
//        Iterable<Board> solution = solver.solution();
//
//        if (solution != null) {
//            for (Board b : solution) {
//                System.out.println(b.toString());
//            }
//        } else
//            System.out.println("unsolvable");

    } // solve a slider puzzle (given below)
}
