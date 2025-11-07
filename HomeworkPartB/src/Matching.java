import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class Matching extends Question implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<String> leftColumn;
    private ArrayList<String> rightColumn;

    public Matching(String prompt) {
        super(prompt);
        this.leftColumn = new ArrayList<>();
        this.rightColumn = new ArrayList<>();
    }

    public void addMatch(String left, String right) {
        this.leftColumn.add(left);
        this.rightColumn.add(right);
    }

    public int getLeftColumnSize() {
        return leftColumn.size();
    }

    @Override
    public void display() {
        System.out.println(this.prompt);
        int maxLeftWidth = 0;
        for (String s : leftColumn) {
            if (s.length() > maxLeftWidth) {
                maxLeftWidth = s.length();
            }
        }

        for (int i = 0; i < leftColumn.size(); i++) {
            char leftLabel = (char) ('A' + i);
            int rightLabel = i + 1;

            String leftPad = String.format("%-" + (maxLeftWidth + 4) + "s", "  " + leftLabel + ") " + leftColumn.get(i));
            System.out.println(leftPad + "  " + rightLabel + ") " + rightColumn.get(i));
        }
    }

    @Override
    public void takeQuestion() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> answers = new ArrayList<>();
        int numMatches = leftColumn.size();

        System.out.println("Enter your matches one per line (e.g., A 1). Type 'DONE' to finish.");

        for (int i = 0; i < numMatches; i++) {
            while (true) {
                System.out.print("Match " + (i + 1) + ": ");
                String userInput = scanner.nextLine().trim().toUpperCase();

                if (userInput.equalsIgnoreCase("DONE")) {
                    break;
                }

                String[] parts = userInput.split("\\s+");
                if (parts.length == 2 && parts[0].length() == 1) {
                    char left = parts[0].charAt(0);
                    int right;
                    try {
                        right = Integer.parseInt(parts[1]);
                        if (left >= 'A' && left < ('A' + numMatches) && right >= 1 && right <= numMatches) {
                            answers.add(userInput);
                            break;
                        } else {
                            System.out.println("Invalid match. Please use letters/numbers in the correct range.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid format. Please enter as 'Letter Number' (e.g., A 1).");
                    }
                } else {
                    System.out.println("Invalid format. Please enter as 'Letter Number' (e.g., A 1).");
                }
            }
        }

        Response newResponse = new Response(String.join(", ", answers));
        this.addUserResponse(newResponse);
    }

    @Override
    public void modify() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Current prompt: " + this.prompt);
        System.out.print("Do you wish to modify the prompt? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.print("Enter new prompt: ");
            this.prompt = scanner.nextLine();
            System.out.println("Prompt has been updated.");
        }

        System.out.println("\n--- Modifying Matches ---");
        System.out.print("Do you wish to modify the match pairs? (yes/no): ");

        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.println("Current Pairs:");
            for (int i = 0; i < leftColumn.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + leftColumn.get(i) + " = " + rightColumn.get(i));
            }

            while (true) {
                System.out.print("Enter the number of the pair to modify (or 'quit'): ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("quit")) {
                    break;
                }

                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < leftColumn.size()) {
                        System.out.print("Enter new left-column text: ");
                        String newLeft = scanner.nextLine();
                        System.out.print("Enter new right-column text: ");
                        String newRight = scanner.nextLine();
                        leftColumn.set(index, newLeft);
                        rightColumn.set(index, newRight);
                        System.out.println("Pair " + (index + 1) + " updated.");
                    } else {
                        System.out.println("Invalid number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }
        }
    }
}