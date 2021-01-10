package ArtificialIntelligence;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class FarmerWolfCabbageSheep
{

    private String[] moves = { "F", "FW", "FS", "FC" };
    private ArrayList<Node> queue;
    private ArrayList<Node> solutions;
    private Node root;

    public FarmerWolfCabbageSheep()
    {
        queue = new ArrayList<Node>();
        solutions = new ArrayList<Node>();

    }

    private class State
    {
        private String bank; // The active bank where the farmer is currently
                             // located
        private TreeSet<String> left, right; // left and right bank with its
                                             // occupants.

        public State(String bank, TreeSet<String> left, TreeSet<String> right)
        {
            this.bank = bank;
            this.left = left;
            this.right = right;
        }

     
        private boolean checkAllowBank(TreeSet<String> b)
        {
            // Wolf and Sheep together without Farmer
            if (b.contains("W") && b.contains("S") && (b.contains("F") == false))
                return false;
            // Sheep and Cabbage together without Farmer
            if (b.contains("S") && b.contains("C") && (b.contains("F") == false))
                return false;

            return true;
        }

        public boolean isAllow()
        {
            if (checkAllowBank(left) && checkAllowBank(right))
                return true;
            else
                return false;
        }

        public boolean isSolution()
        {
            if (left.isEmpty() && right.contains("W") && right.contains("S") && right.contains("C")
                    && right.contains("F"))
                return true;
            else
                return false;
        }

        public State transits(String move)
        {
            String nbank;
            TreeSet<String> nleft = new TreeSet<String>();
            TreeSet<String> nright = new TreeSet<String>();

            if (bank.equalsIgnoreCase("left"))
                nbank = "right";
            else
                nbank = "left";

            copylist(right, nright);
            copylist(left, nleft);

            for (int i = 0; i < move.length(); i++)
            {
                String item = move.substring(i, i + 1);
                if (bank.equalsIgnoreCase("left"))
                {
                    if (nleft.remove(item))
                        nright.add(item);
                    else
                        return null; // return null if the move contains
                                     // occupants that are not present.
                }
                else
                {
                    if (nright.remove(item))
                        nleft.add(item);
                    else
                        return null; // return null if the move contains
                                     // occupants that are not present.
                }
            }

            return new State(nbank, nleft, nright);

        }

        private void copylist(TreeSet<String> src, TreeSet<String> dst)
        {
            for (String e : src)
                dst.add(e);
        }

        public boolean compare(State s)
        {
            TreeSet<String> tmp;

            if (!s.getBank().equalsIgnoreCase(bank))
                return false;

            tmp = s.getLeft();
            for (String e : left)
            {
                if (!tmp.contains(e))
                    return false;
            }

            tmp = s.getRight();
            for (String e : right)
            {
                if (!tmp.contains(e))
                    return false;
            }

            return true;
        }

        public String getBank()
        {
            return bank;
        }

        public TreeSet<String> getLeft()
        {
            return left;
        }

        public TreeSet<String> getRight()
        {
            return right;
        }

        @Override
        public String toString()
        {
            StringBuffer ret = new StringBuffer();
            ret.append("{L:");

            for (String e : left)
                ret.append(e);

            ret.append(" ");
            ret.append("R:");

            for (String e : right)
                ret.append(e);

            ret.append("}");
            return ret.toString();
        }

    }

    /**
     * Private Inner class Node for constructing the State graph
     */
    private class Node
    {
        public Node parent; // Parent of the node
        public State data; // State of the node
        public ArrayList<Node> adjlist; // Children of the node
        public int level; // Depth of the node
        public String move; // The move (transition) that creates the current
                            // node state.

        public Node(State data)
        {
            parent = null;
            this.data = data;
            adjlist = new ArrayList<Node>();
            level = 0;
            move = "";
        }

        /**
         * Checks if a Node that has the same State is an ancestor of the
         * current Node.
         * 
         * @return true if a an ancestor node has the same state, false
         *         otherwise
         */
        public boolean isAncestor()
        {
            Node n = parent;
            boolean ret = false;
            while (n != null)
            {
                if (data.compare(n.data))
                {
                    ret = true;
                    break;
                }

                n = n.parent;
            }

            return ret;
        }

    }

    public void startBreadthFirstSearch()
    {
        solutions = new ArrayList<Node>(); // Initialize solutions to zero
        TreeSet<String> left = new TreeSet<String>();
        left.add("W");
        left.add("S");
        left.add("C");
        left.add("F");

        State inits = new State("left", left, new TreeSet<String>());
        root = new Node(inits);
        root.level = 0;
        queue.add(root);

        while (!queue.isEmpty())
        {
            Node n = queue.remove(0);
            System.out.println("Processing Level " + n.level + " " + n.data);
            for (String m : moves)
            {

                State s = n.data.transits(m);

                if (s != null && s.isAllow()) // Check if it is allowable state
                {

                    Node child = new Node(s);
                    child.parent = n;
                    child.level = n.level + 1;
                    child.move = m + " moves " + child.data.getBank();

                    // Check that a node doesn't occur already as ancestor to
                    // prevent cycle in the graph
                    if (!child.isAncestor())
                    {
                        n.adjlist.add(child);

                        if (child.data.isSolution() == false)
                        {
                            queue.add(child);
                            System.out.println("Adding state " + child.data);
                        }
                        else
                        {
                            solutions.add(child);
                            System.out.println("Found solution " + child.data);

                        }
                    }

                }

            }

        }
    }

    /**
     * Method to start the creation of the state graph using iterative depth
     * first search
     */
    public void startDepthFirstSearch()
    {

        int dlimit = 1; // Maximum depth limit
        solutions = new ArrayList<Node>(); // Initialize solutions to zero

        while (solutions.size() == 0 && dlimit <= 10)
        {
            TreeSet<String> left = new TreeSet<String>();
            left.add("W");
            left.add("S");
            left.add("C");
            left.add("F");

            State inits = new State("left", left, new TreeSet<String>());
            root = new Node(inits);
            root.level = 0;

            System.out.println("Starting iterative DFS with depth: " + dlimit);
            startDFS(dlimit, root);
            dlimit++;
        }

    }

    public void startDFS(int depth, Node r)
    {
        if (depth == 0)
        {
            System.out.println("Maximum depth limit");
            return;
        }

        System.out.println("Processing Level " + r.level + " " + r.data);

        for (String m : moves)
        {
            State s = r.data.transits(m);

            if (s != null && s.isAllow()) // Check if it is allowable state
            {

                Node child = new Node(s);
                child.parent = r;
                child.level = r.level + 1;
                child.move = m + " moves " + child.data.getBank();

                if (!child.isAncestor()) // Check that the node doesn't occur
                                         // already as an ancestor
                {
                    r.adjlist.add(child);

                    if (child.data.isSolution())
                    {// Found a solution

                        solutions.add(child);
                        System.out.println("Found solution " + child.data);
                        return;
                    }
                    else
                    {// Recursive call
                        startDFS(depth - 1, child);
                    }

                }
            }

        }

        // No valid states
        return;

    }

    public void printBFSGraph()
    {
        ArrayList<Node> queue = new ArrayList<Node>();

        queue.add(root);

        while (!queue.isEmpty())
        {
            Node n = queue.remove(0);
            System.out.println("Level " + n.level + " " + n.data);

            ArrayList<Node> adjlist = n.adjlist;
            for (Node e : adjlist)
            {
                queue.add(e);
            }

        }

    }

    public void printSolution()
    {
        System.out.println("No. of solutions:  " + solutions.size());
        ArrayList<Node> stack;

        Iterator<Node> iter = solutions.iterator();
        int i = 1;
        while (iter.hasNext())
        {
            stack = new ArrayList<Node>();
            Node n = iter.next();
            stack.add(n);

            n = n.parent;
            while (n != null)
            {
                stack.add(n);
                n = n.parent;
            }
            System.out.println("Solution " + i);
            printSequence(stack);
            i++;
        }

    }

    private void printSequence(ArrayList<Node> stack)
    {
        StringBuffer buf = new StringBuffer();
        buf.append("No. of moves: ");
        buf.append(stack.size() - 1);
        buf.append("\n");
        for (int i = stack.size() - 1; i >= 0; i--)
        {
            Node n = stack.get(i);
            buf.append(n.data.toString());
            if (i != 0)
            {
                buf.append("--");
                buf.append(stack.get(i - 1).move);
                buf.append("->>");

            }
        }

        System.out.println(buf.toString());

    }

    public static void main(String[] args)
    {
        System.out.println("Solving Wolf, Sheep, Cabbage, Farmer, River Crossing Puzzle\n");
        FarmerWolfCabbageSheep obj = new FarmerWolfCabbageSheep();

        System.out.println("Creating State Graph using Breadth First Search");
        obj.startBreadthFirstSearch();

        System.out.println("\n\nState Graph in Breadth first order");
        obj.printBFSGraph();
        System.out.println("\n\n");

        System.out.println("Solutions to the River Crossing Puzzle BFS");
        obj.printSolution();

        System.out.println("\n\nCreating State Graph using Iterative Depth First Search");
        obj.startDepthFirstSearch();

        System.out.println("\n\nState Graph in Breadth first order");
        obj.printBFSGraph();
        System.out.println("\n\n");

        System.out.println("Solutions to the River Crossing Puzzle Iterative DFS");
        obj.printSolution();

    }

}

