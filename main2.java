import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;

class Node {
    int tID; //Task ID
    String tName; //Task name;
    int tPriority;  //Task priority number
    double tETime;  //Task execution time
    String tState;  //Task completion state (completed, failed, pending)
    Node prev; //Previous Node
    Node next; //Next Node
}

class LinkedList
{
    Node head;
    Node temp;

    public LinkedList()
    {
        head = null;
    }

    public void insert(int id, String name, int priority, double etime, String status)
    {
        if (head == null)
        {
            Node n1 = new Node();
            n1.tID = id;
            n1.tName = name;
            n1.tPriority = priority;
            n1.tETime = etime;
            n1.tState = status;
            head = n1;
        }
        else
        {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            Node n1 = new Node();
            n1.tID = id;
            n1.tName = name;
            n1.tPriority = priority;
            n1.tETime = etime;
            n1.tState = status;
            current.next = n1;
            head.prev = current;
            temp = head;
        }
    }
}



class AVLNodeByPriority //The Node class for the AVL Tree sorting with priority
{
    int tID; //Task ID
    String tName; //Task name;
    double tETime;  //Task execution time
    String tState;  //Task completion state (completed, failed, pending)
    int priority;
    Node data;
    int height;
    AVLNodeByPriority left;
    AVLNodeByPriority right;

    AVLNodeByPriority(Node data) {
        this.tID = data.tID;
        this.tName = data.tName;
        this.tETime = data.tETime;
        this.tState = data.tState;
        this.data = data;
        this.priority = data.tPriority;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}

class AVLTreeByPriority {
    AVLNodeByPriority root;

    AVLNodeByPriority rotateRight(AVLNodeByPriority y) {
        AVLNodeByPriority x = y.left;
        AVLNodeByPriority T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    AVLNodeByPriority rotateLeft(AVLNodeByPriority x)
    {
        AVLNodeByPriority y = x.right;
        AVLNodeByPriority T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    int height(AVLNodeByPriority node)
    {
        if (node == null)
            return 0;
        return node.height;
    }

    int getBalance(AVLNodeByPriority node)
    {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    AVLNodeByPriority insert(AVLNodeByPriority node, Node data)
    {
        if (node == null)
            return new AVLNodeByPriority(data);

        if (data.tPriority <= node.priority)
            node.left = insert(node.left, data);
        else
            node.right = insert(node.right, data);

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && data.tPriority <= node.left.priority)
            return rotateRight(node);

        if (balance < -1 && data.tPriority > node.right.priority)
            return rotateLeft(node);

        if (balance > 1 && data.tPriority > node.left.priority)
        {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && data.tPriority <= node.right.priority)
        {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    void insert(Node data)
    {
        root = insert(root, data);
    }
    List<String> sortedValues = new ArrayList<>(); // Define a list to store sorted values
    void inorderTraversal(AVLNodeByPriority root) {
        if (root != null) {
            inorderTraversal(root.left);
            System.out.println(root.data.tName + " " + root.data.tPriority);
            sortedValues.add(root.data.tName + " " + root.data.tPriority + " " + root.data.tETime + " " + root.data.tState); // Add sorted values to the list
            inorderTraversal(root.right);
        }
    }

    void inorder()
    {
        inorderTraversal(root);
    }
    void ETask() {
        System.out.println("Start");
        Scanner sc = new Scanner(System.in);
        ArrayList<AVLNodeByPriority> sortedTasks = new ArrayList<>();
        getSortedTasks(root, sortedTasks);

        boolean completedAllTasks = true; // Flag to track if all tasks were completed

        for (int i = 0; i < sortedTasks.size(); i++) {
            AVLNodeByPriority node = sortedTasks.get(i);
            System.out.println("Task: " + node.data.tName);
            double seconds = node.data.tETime * 60; // Convert minutes to seconds
            double countdown = seconds;

            while (countdown > 0) {
                System.out.print("\rTask in progress: " + node.data.tName);
                System.out.print(" Time left: " + String.format("%.2f", countdown / 60) + " minutes");
                countdown -= 1; // Decrement countdown by 1 second
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("\nTask completed: " + node.data.tName);

            boolean validChoice = false;
            String choice2 = "";
            while (!validChoice) {
                System.out.println("Did you complete the task?(y/n)");
                choice2 = sc.next();
                if (choice2.equalsIgnoreCase("y") || choice2.equalsIgnoreCase("n")) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                }
            }

            if (choice2.equalsIgnoreCase("y")) {
                node.data.tState = "Completed";
            } else {
                node.data.tState = "Failed";
                completedAllTasks = false; // If any task is failed, mark all tasks as not completed
            }

            if (i < sortedTasks.size() - 1) {
                validChoice = false;
                String moveNext = "";
                while (!validChoice) {
                    System.out.println("Would you like to move to the next task?(y/n)");
                    moveNext = sc.next();
                    if (moveNext.equalsIgnoreCase("y") || moveNext.equalsIgnoreCase("n")) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    }
                }

                if (!moveNext.equalsIgnoreCase("y")) {
                    completedAllTasks = false; // If user chooses not to move to next task, mark all tasks as not completed
                    System.out.println("Exiting task completion process...");
                    break;
                }
            }
        }

        if (completedAllTasks) {
            System.out.println("All tasks are completed. Hurray!");
            System.out.println("⠄⠄⠄⠄⢀⣠⣶⣶⣶⣤⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⢀⣠⣤⣄⡀⠄⠄⠄⠄⠄\n" +
                    "⠄⠄⠄⢠⣾⡟⠁⠄⠈⢻⣿⡀⠄⠄⠄⠄⠄⠄⠄⣼⣿⡿⠋⠉⠻⣷⠄⠄⠄⠄\n" +
                    "⠄⠄⠄⢸⣿⣷⣄⣀⣠⣿⣿⡇⠄⠄⠄⠄⠄⠄⢰⣿⣿⣇⠄⠄⢠⣿⡇⠄⠄⠄\n" +
                    "⠄⠄⠄⢸⣿⣿⣿⣿⣿⣿⣿⣦⣤⣤⣤⣤⣤⣤⣼⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄\n" +
                    "⠄⠄⠄⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⠄⠄⠄\n" +
                    "⠄⢀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡆⠄⠄\n" +
                    "⠄⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄⠄\n" +
                    "⠄⣿⣿⣿⣿⣿⡏⣍⡻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢛⣩⡍⣿⣿⣿⣷⠄\n" +
                    "⠄⣿⣿⣿⣿⣿⣇⢿⠻⠮⠭⠭⠭⢭⣭⣭⣭⣛⣭⣭⠶⠿⠛⣽⢱⣿⣿⣿⣿⠄\n" +
                    "⠄⣿⣿⣿⣿⣿⣿⣦⢱⡀⠄⢰⣿⡇⠄⠄⠄⠄⠄⠄⠄⢀⣾⢇⣿⣿⣿⣿⡿⠄\n" +
                    "⠄⠻⢿⣿⣿⣿⢛⣭⣥⣭⣤⣼⣿⡇⠤⠤⠤⣤⣤⣤⡤⢞⣥⣿⣿⣿⣿⣿⠃⠄\n" +
                    "⠄⠄⠄⣛⣛⠃⣿⣿⣿⣿⣿⣿⣿⢇⡙⠻⢿⣶⣶⣶⣾⣿⣿⣿⠿⢟⣛⠃⠄⠄\n" +
                    "⠄⠄⣼⣿⣿⡘⣿⣿⣿⣿⣿⣿⡏⣼⣿⣿⣶⣬⣭⣭⣭⣭⣭⣴⣾⣿⣿⡄⠄⠄\n" +
                    "⠄⣼⣿⣿⣿⣷⣜⣛⣛⣛⣛⣛⣀⡛⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄⠄\n" +
                    "⢰⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣶⣦⣭⣙⣛⣛⣩⣭⣭⣿⣿⣿⣿⣷⡀");
        } else {
            System.out.println("Not all tasks were completed.");
        }

        sc.close();
    }
    void display(AVLNodeByPriority root) {
        // Print the table header
        System.out.println("+----------------------+------------+---------------+-------------------+");
        System.out.println("| Task Name            | Priority   | Execution Time| State             |");
        System.out.println("+----------------------+------------+---------------+-------------------+");

        // Traverse the AVL tree to print tasks and their states
        inOrderTraversal(root);

        // Print the table footer
        System.out.println("+----------------------+------------+---------------+-------------------+");
    }

    // In-order traversal of the AVL tree to print tasks and their states
    void inOrderTraversal(AVLNodeByPriority node) {
        if (node != null) {
            inOrderTraversal(node.left);

            // Print task details
            System.out.printf("| %-20s | %-10s | %-14s | %-15s |\n",
                    node.data.tName, node.data.tPriority, node.data.tETime, node.data.tState);

            inOrderTraversal(node.right);
        }
    }

    void getSortedTasks(AVLNodeByPriority node, ArrayList<AVLNodeByPriority> sortedTasks) {
        if (node != null) {
            getSortedTasks(node.left, sortedTasks);
            sortedTasks.add(node);
            getSortedTasks(node.right, sortedTasks);
        }
    }
}
class AVLNodeByTime { //The class for the AVL Tree sorting by time
    int tID; //Task ID
    String tName; //Task name;
    int tPriority;  //Task priority number
    String tState;  //Task completion state (completed, failed, pending)
    double tETime;
    Node data;
    int height;
    AVLNodeByTime left;
    AVLNodeByTime right;

    AVLNodeByTime(Node data)
    {
        this.tID = data.tID;
        this.tName = data.tName;
        this.tPriority = data.tPriority;
        this.tState = data.tState;
        this.data = data;
        this.tETime = data.tETime;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}

class AVLTreeByTime
{
    AVLNodeByTime root;

    AVLNodeByTime rotateRight(AVLNodeByTime y)
    {
        AVLNodeByTime x = y.left;
        AVLNodeByTime T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    AVLNodeByTime rotateLeft(AVLNodeByTime x)
    {
        AVLNodeByTime y = x.right;
        AVLNodeByTime T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    int height(AVLNodeByTime node) {
        if (node == null)
            return 0;
        return node.height;
    }

    int getBalance(AVLNodeByTime node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    AVLNodeByTime insert(AVLNodeByTime node, Node data) //The method to insert the values into the avl tree
    {
        if (node == null)
            return new AVLNodeByTime(data);

        if (data.tETime <= node.tETime)
            node.left = insert(node.left, data);
        else
            node.right = insert(node.right, data);

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && data.tETime <= node.left.tETime)
            return rotateRight(node);

        if (balance < -1 && data.tETime > node.right.tETime)
            return rotateLeft(node);

        if (balance > 1 && data.tETime > node.left.tETime) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && data.tETime <= node.right.tETime) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    void insert(Node data)
    {
        root = insert(root, data);
    }

    void inorderTraversalLowToHigh(AVLNodeByTime root)
    {
        if (root != null)
        {
            inorderTraversalLowToHigh(root.left);
            System.out.println(root.data.tName + " " + root.data.tETime);
            inorderTraversalLowToHigh(root.right);
        }
    }

    void inorderLowToHigh()
    {
        inorderTraversalLowToHigh(root);
    }

    void inorderTraversalHighToLow(AVLNodeByTime root) {
        if (root != null) {
            inorderTraversalHighToLow(root.right);
            System.out.println(root.data.tName + " " + root.data.tETime);
            inorderTraversalHighToLow(root.left);
        }
    }

    void inorderHighToLow() {
        inorderTraversalHighToLow(root);
    }

    void ETask(boolean isAscending) {
        System.out.println("Start");
        Scanner sc = new Scanner(System.in);
        ArrayList<AVLNodeByTime> sortedTasks = new ArrayList<>();
        getSortedTasks(root, sortedTasks, isAscending);

        boolean completedAllTasks = true; // Flag to track if all tasks were completed

        for (int i = 0; i < sortedTasks.size(); i++) {
            AVLNodeByTime node = sortedTasks.get(i);
            System.out.println("Task: " + node.data.tName);
            double seconds = node.data.tETime * 60;
            double countdown = seconds;

            while (countdown > 0) {
                System.out.print("\rTask in progress: " + node.data.tName);
                System.out.print(" Time left: " + String.format("%.2f", countdown / 60) + " minutes");
                countdown -= 1; // Decrement countdown by 1 second
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("\nTask completed: " + node.data.tName);

            boolean validChoice = false;
            String choice2 = "";
            while (!validChoice) {
                System.out.println("Did you complete the task?(y/n)");
                choice2 = sc.next();
                if (choice2.equalsIgnoreCase("y") || choice2.equalsIgnoreCase("n")) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid input. Please enter 'y' or 'n'.");
                }
            }

            if (choice2.equalsIgnoreCase("y")) {
                node.data.tState = "Completed";
            } else {
                node.data.tState = "Failed";
                completedAllTasks = false; // If any task is not completed, mark all tasks as not completed
            }

            if (i < sortedTasks.size() - 1) {
                validChoice = false;
                String moveNext = "";
                while (!validChoice) {
                    System.out.println("Would you like to move to the next task?(y/n)");
                    moveNext = sc.next();
                    if (moveNext.equalsIgnoreCase("y") || moveNext.equalsIgnoreCase("n")) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    }
                }

                if (!moveNext.equalsIgnoreCase("y")) {
                    completedAllTasks = false; // If user chooses not to move to next task, mark all tasks as not completed
                    System.out.println("Exiting task completion process...");
                    break;
                }
            }
        }

        if (completedAllTasks) {
            System.out.println("All tasks are completed. Hurray!");
            System.out.println("⠄⠄⠄⠄⢀⣠⣶⣶⣶⣤⡀⠄⠄⠄⠄⠄⠄⠄⠄⠄⢀⣠⣤⣄⡀⠄⠄⠄⠄⠄\n" +
                    "⠄⠄⠄⢠⣾⡟⠁⠄⠈⢻⣿⡀⠄⠄⠄⠄⠄⠄⠄⣼⣿⡿⠋⠉⠻⣷⠄⠄⠄⠄\n" +
                    "⠄⠄⠄⢸⣿⣷⣄⣀⣠⣿⣿⡇⠄⠄⠄⠄⠄⠄⢰⣿⣿⣇⠄⠄⢠⣿⡇⠄⠄⠄\n" +
                    "⠄⠄⠄⢸⣿⣿⣿⣿⣿⣿⣿⣦⣤⣤⣤⣤⣤⣤⣼⣿⣿⣿⣿⣿⣿⣿⡇⠄⠄⠄\n" +
                    "⠄⠄⠄⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⠄⠄⠄\n" +
                    "⠄⢀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡆⠄⠄\n" +
                    "⠄⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄⠄\n" +
                    "⠄⣿⣿⣿⣿⣿⡏⣍⡻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢛⣩⡍⣿⣿⣿⣷⠄\n" +
                    "⠄⣿⣿⣿⣿⣿⣇⢿⠻⠮⠭⠭⠭⢭⣭⣭⣭⣛⣭⣭⠶⠿⠛⣽⢱⣿⣿⣿⣿⠄\n" +
                    "⠄⣿⣿⣿⣿⣿⣿⣦⢱⡀⠄⢰⣿⡇⠄⠄⠄⠄⠄⠄⠄⢀⣾⢇⣿⣿⣿⣿⡿⠄\n" +
                    "⠄⠻⢿⣿⣿⣿⢛⣭⣥⣭⣤⣼⣿⡇⠤⠤⠤⣤⣤⣤⡤⢞⣥⣿⣿⣿⣿⣿⠃⠄\n" +
                    "⠄⠄⠄⣛⣛⠃⣿⣿⣿⣿⣿⣿⣿⢇⡙⠻⢿⣶⣶⣶⣾⣿⣿⣿⠿⢟⣛⠃⠄⠄\n" +
                    "⠄⠄⣼⣿⣿⡘⣿⣿⣿⣿⣿⣿⡏⣼⣿⣿⣶⣬⣭⣭⣭⣭⣭⣴⣾⣿⣿⡄⠄⠄\n" +
                    "⠄⣼⣿⣿⣿⣷⣜⣛⣛⣛⣛⣛⣀⡛⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄⠄\n" +
                    "⢰⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣶⣦⣭⣙⣛⣛⣩⣭⣭⣿⣿⣿⣿⣷⡀");
        } else {
            System.out.println("Not all tasks were completed.");
        }

        sc.close();
    }

    void getSortedTasks(AVLNodeByTime node, ArrayList<AVLNodeByTime> sortedTasks, boolean isAscending) {
        if (node != null) {
            if (isAscending) {
                getSortedTasks(node.left, sortedTasks, isAscending);
                sortedTasks.add(node);
                getSortedTasks(node.right, sortedTasks, isAscending);
            } else {
                getSortedTasks(node.right, sortedTasks, isAscending);
                sortedTasks.add(node);
                getSortedTasks(node.left, sortedTasks, isAscending);
            }
        }
    }
}


class main2 {
    public static void main(String[] args)
    {
        System .out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                                                           Task Management System");
        System .out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        Scanner sc = new Scanner(System.in);
        LinkedList lst = new LinkedList();
        int tCount = 0;
        String tname;
        int priority = 0;
        double etime = 0;
        String status;
        String sort;

        // Input validation for the number of tasks
        boolean validInput = false;
        while (!validInput)
        {
            System.out.print("Enter number of tasks to be completed: ");
            if (sc.hasNextInt()) {
                tCount = sc.nextInt();
                if (tCount > 0) {
                    validInput = true;
                } else {
                    System.out.println("Number of tasks must be a positive integer.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                sc.next(); // Clear the invalid input
            }
        }

        for (int i = 0; i < tCount; i++) {
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.print("Enter task name: ");
            tname = sc.next();
            tname += sc.nextLine();

            // Input validation for task priority
            validInput = false;
            while (!validInput) {
                System.out.println("The task priority ranges from 1 to 10, low value represents high priority.");
                System.out.print("Enter task priority: ");
                if (sc.hasNextInt()) {
                    priority = sc.nextInt();
                    if (priority >= 1 && priority <= 10) {
                        validInput = true;
                    } else {
                        System.out.println("Priority must be between 1 and 10.");
                    }
                } else {
                    System.out.println("Please enter a valid number.");
                    sc.next(); // Clear the invalid input
                }
            }

            // Input validation for task execution time
            validInput = false;
            while (!validInput) {
                System.out.print("Enter the required completion time in minutes for the task: ");
                if (sc.hasNextDouble()) {
                    etime = sc.nextDouble();
                    if (etime > 0) {
                        validInput = true;
                    } else {
                        System.out.println("Task execution time must be a positive number.");
                    }
                } else {
                    System.out.println("Please enter a valid number.");
                    sc.next(); // Clear the invalid input
                }
            }

            status = "Pending";
            lst.insert(i, tname, priority, etime, status);
        }
        AVLTreeByPriority avlTreeByPriority = new AVLTreeByPriority();
        Node current = lst.head;
        while (current != null) {
            avlTreeByPriority.insert(current);
            current = current.next;
        }

        AVLTreeByTime avlTreeByTime = new AVLTreeByTime();
        Node current2 = lst.head;
        while (current2 != null) {
            avlTreeByTime.insert(current2);
            current2 = current2.next;
        }

        boolean validSort = false;
        do {
            System.out.println("If you want to execute the tasks according to their priority ascending order, type p");
            System.out.println("If you want to sort tasks by time (Highest time to lowest), type td");
            System.out.println("If you want to sort tasks by time (Lowest time to highest), type ta");
            sort = sc.next().toLowerCase();
            switch (sort) {
                case "p":
                    avlTreeByPriority.inorder();
                    validSort = true;
                    break;
                case "td":
                    avlTreeByTime.inorderHighToLow();
                    validSort = true;
                    break;
                case "ta":
                    avlTreeByTime.inorderLowToHigh();
                    validSort = true;
                    break;
                default:
                    System.out.println("Enter a valid sorting option.");
                    break;
            }
        } while (!validSort);

        boolean validStart = false;
        do {
            System.out.println("Would you wish to start completing the tasks in the sorted order (y/n): ");
            String conf = sc.next().toLowerCase();
            switch (conf) {
                case "y":
                    validStart = true;
                    if (sort.equals("p")) {
                        avlTreeByPriority.ETask();
                    } else if (sort.equals("td")) {
                        avlTreeByTime.ETask(false); // For time high to low
                    } else if (sort.equals("ta")) {
                        avlTreeByTime.ETask(true); // For time low to high
                    }
                    break;
                case "n":
                    validStart = true;
                    break;
                default:
                    System.out.println("Enter a valid choice.");
                    break;
            }
        } while (!validStart);

        avlTreeByPriority.display(avlTreeByPriority.root);
    }
}