import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class MultipleChoice extends Question implements Serializable {

    private static final long serialVersionUID = 1L;
    private ArrayList<String> choices;

    public MultipleChoice(String prompt) {
        super(prompt);
        this.choices = new ArrayList<>();
    }

    public void addChoice(String choice) {
        this.choices.add(choice);
    }

    @Override
    public void display() {
        System.out.println(this.prompt);
        for (int i = 0; i < this.choices.size(); i++) {
            System.out.println("  " + (char)('A' + i) + ") " + this.choices.get(i));
        }
    }

    @Override
    public void takeQuestion() {
        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            System.out.print("Enter your answer (A, B, C...): ");
            userInput = scanner.nextLine().trim().toUpperCase();

            if (userInput.length() != 1) {
                System.out.println("Invalid input. Please enter a single letter.");
                continue;
            }

            char selectedLetter = userInput.charAt(0);

            if (selectedLetter >= 'A' && selectedLetter < ('A' + this.choices.size())) {
                break;
            } else {
                System.out.println("Invalid choice. Please select from the available options.");
            }
        }

        Response newResponse = new Response(userInput);
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

        System.out.println("\n--- Modifying Choices ---");
        System.out.print("Do you wish to modify the choices? (yes/no): ");

        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            for (int i = 0; i < this.choices.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + this.choices.get(i));
            }

            int choiceIndex = -1;
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

            this.choices.set(choiceIndex - 1, newChoiceText);

            System.out.println("Choice " + choiceIndex + " has been updated.");
        } else {
            System.out.println("Choices were not modified.");
        }
    }
}