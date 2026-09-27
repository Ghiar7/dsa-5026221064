import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class BankTransactionProcessor {

    public static void main(String[] args) {

        // 1. Read and store transactions
        LinkedList<String[]> transactions = new LinkedList<>();
        // 2. Create customer data (name, balance)
        LinkedList<String[]> customers = new LinkedList<>();

        String fileName = "src/lw02/prelab/transactions.txt";
        File inputFile = resolveInputFile(fileName);

        try (Scanner fileScanner = new Scanner(inputFile)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    continue; // skip malformed lines
                }

                String name = parts[0];
                String type = parts[1];
                String amount = parts[2];

                // Store the transaction, preserving original order
                transactions.add(new String[]{name, type, amount});

                // Add the customer only the first time their name appears
                if (findCustomer(customers, name) == null) {
                    customers.add(new String[]{name, "0"});
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: could not find file '" + fileName + "'.");
            return;
        }

        // 3. Process transactions using Queue (FIFO)
        Queue<String[]> transactionQueue = new LinkedList<>();
        transactionQueue.addAll(transactions);

        // 4. Store failed WITHDRAW transactions using Stack
        Stack<String[]> failedTransactions = new Stack<>();

        while (!transactionQueue.isEmpty()) {
            String[] transaction = transactionQueue.poll();
            if (transaction == null || transaction.length < 3) {
                continue;
            }

            String name = transaction[0];
            String type = transaction[1];
            Integer amount = parseAmount(transaction[2]);
            if (amount == null) {
                continue;
            }

            String[] customer = findCustomer(customers, name);
            if (customer == null) {
                // Should not happen since every customer was registered above
                continue;
            }

            int balance = Integer.parseInt(customer[1]);

            if (type.equalsIgnoreCase("DEPOSIT")) {
                balance += amount;
                customer[1] = String.valueOf(balance);
            } else if (type.equalsIgnoreCase("WITHDRAW")) {
                if (amount > balance) {
                    // Insufficient balance: record as failed, balance unchanged
                    failedTransactions.push(transaction);
                } else {
                    balance -= amount;
                    customer[1] = String.valueOf(balance);
                }
            }
        }

        // 5. Display final balances
        System.out.println("=== Final Balances ===");
        for (String[] customer : customers) {
            System.out.println(customer[0] + " : " + customer[1]);
        }

        // 6. Display failed transactions in LIFO order
        System.out.println();
        System.out.println("=== Failed Transactions ===");
        while (!failedTransactions.isEmpty()) {
            String[] failed = failedTransactions.pop();
            System.out.println(failed[0] + " " + failed[1] + " " + failed[2]);
        }
    }

    /**
     * Finds a customer record by name inside the customer LinkedList.
     * Returns the matching String[] (name, balance) or null if not found.
     */
    private static String[] findCustomer(LinkedList<String[]> customers, String name) {
        for (String[] customer : customers) {
            if (customer[0].equals(name)) {
                return customer;
            }
        }
        return null;
    }

    private static Integer parseAmount(String rawAmount) {
        if (rawAmount == null) {
            return null;
        }

        try {
            return Integer.parseInt(rawAmount);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static File resolveInputFile(String relativePath) {
        File currentDir = new File(System.getProperty("user.dir"));
        String fileName = new File(relativePath).getName();

        File candidate = new File(currentDir, relativePath);
        if (candidate.exists()) {
            return candidate;
        }

        File localFile = new File(relativePath);
        if (localFile.exists()) {
            return localFile;
        }

        File directory = currentDir;
        while (directory != null) {
            File parentCandidate = new File(directory, relativePath);
            if (parentCandidate.exists()) {
                return parentCandidate;
            }

            File sameNameCandidate = new File(directory, fileName);
            if (sameNameCandidate.exists()) {
                return sameNameCandidate;
            }

            File found = findFileByName(directory, fileName);
            if (found != null) {
                return found;
            }

            directory = directory.getParentFile();
        }

        return new File(fileName);
    }

    private static File findFileByName(File rootDir, String fileName) {
        if (rootDir == null || !rootDir.exists() || !rootDir.isDirectory()) {
            return null;
        }

        File[] children = rootDir.listFiles();
        if (children == null) {
            return null;
        }

        for (File child : children) {
            if (child.isDirectory()) {
                File found = findFileByName(child, fileName);
                if (found != null) {
                    return found;
                }
            } else if (child.getName().equals(fileName)) {
                return child;
            }
        }

        return null;
    }
}
