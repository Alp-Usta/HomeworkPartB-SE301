import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class MultipleChoice extends Question implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<String> choices;

    // Constructor
    public MultipleChoice(String prompt, int numResponses) {
        // Pass both up to the Question class
        super(prompt, numResponses);
        this.choices = new ArrayList<>();
    }

    // Helper method to add choices when creating the survey
    public void addChoice(String choice) {
        this.choices.add(choice);
    }

    @Override
    public void display() {
        System.out.println(this.prompt);
        // Loop and print choices as A, B, C...
        for (int i = 0; i < this.choices.size(); i++) {
            System.out.println("  " + (char)('A' + i) + ") " + this.choices.get(i));
        }
    }

    @Override
    public void takeQuestion() {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        // Loop for the number of required responses
        for (int i = 0; i < this.numResponses; i++) {
            while (true) {
                System.out.print("Enter your answer " + (i + 1) + " of " + this.numResponses + " (A, B, C...): ");
                userInput = scanner.nextLine().trim().toUpperCase();

                if (userInput.length() != 1) {
                    System.out.println("Invalid input. Please enter a single letter.");
                    continue;
                }

                char selectedLetter = userInput.charAt(0);

                // Check if the letter is in the valid range (e.g., A, B, or C)
                if (selectedLetter >= 'A' && selectedLetter < ('A' + this.choices.size())) {
                    break;
                } else {
                    System.out.println("Invalid choice. Please select from the available options.");
                }
            }
            Response newResponse = new Response(userInput);
            this.addUserResponse(newResponse);
        }
    }

    @Override
    public void modify() {
        Scanner scanner = new Scanner(System.in);

        // Part 1: Modify the Prompt
        System.out.println("Current prompt: " + this.prompt);
        System.out.print("Do you wish to modify the prompt? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.print("Enter new prompt: ");
            this.prompt = scanner.nextLine();
            System.out.println("Prompt has been updated.");
        }

        // Part 2: Modify the Choices
        System.out.println("\n--- Modifying Choices ---");
        System.out.print("Do you wish to modify the choices? (yes/no): ");

        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            // Show all current choices
            for (int i = 0; i < this.choices.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + this.choices.get(i));
            }

            int choiceIndex = -1;
            // Loop to get a valid choice number to edit
            while (true) {
                System.out.print("Enter the number of the choice you want to modify (e.g., 1): ");
                try {
                    choiceIndex = Integer.parseInt(scanner.nextLine());
                    if (choiceIndex >= 1 && choiceIndex <= this.choices.size()) {
                        break;
                    } else {
                        System.out.println("Invalid number. Please enter a number from 1 to " + this.choices.size());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            System.out.print("Enter the new text for this choice: ");
            String newChoiceText = scanner.nextLine();

            // Use .set() to replace the old text at the correct index
            this.choices.set(choiceIndex - 1, newChoiceText);

            System.out.println("Choice " + choiceIndex + " has been updated.");
        } else {
            System.out.println("Choices were not modified.");
        }
    }
}