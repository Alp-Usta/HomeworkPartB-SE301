import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

    private static final Scanner scanner = new Scanner(System.in);
    private static Survey currentSurvey = null;

    private static final String SURVEY_DIR = "surveys";
    private static final String RESPONSE_DIR = "responses";

    public static void main(String[] args) {

        new File(SURVEY_DIR).mkdir();
        new File(RESPONSE_DIR).mkdir();

        System.out.println("Welcome to the Survey System!");
        boolean running = true;

        while (running) {
            showMenu1();
            int choice = getUserInt();
            switch (choice) {
                case 1:
                    createNewSurvey();
                    break;
                case 2:
                    displayCurrentSurvey();
                    break;
                case 3:
                    loadSurvey();
                    break;
                case 4:
                    saveSurvey();
                    break;
                case 5:
                    takeCurrentSurvey();
                    break;
                case 6:
                    modifyCurrentSurvey();
                    break;
                case 7:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void showMenu1() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1) Create a new Survey");
        System.out.println("2) Display an existing Survey");
        System.out.println("3) Load an existing Survey");
        System.out.println("4) Save the current Survey");
        System.out.println("5) Take the current Survey");
        System.out.println("6) Modify the current Survey");
        System.out.println("7) Quit");
        System.out.print("Enter your choice: ");
    }

    private static void createNewSurvey() {
        System.out.print("Enter a title for the new survey: ");
        String title = scanner.nextLine();
        currentSurvey = new Survey(title);
        System.out.println("Survey '" + title + "' created.");

        boolean creating = true;
        while (creating) {
            showMenu2();
            int choice = getUserInt();
            switch (choice) {
                case 1:
                    addTrueFalse();
                    break;
                case 2:
                    addMultipleChoice();
                    break;
                case 3:
                    addShortAnswer();
                    break;
                case 4:
                    addEssay();
                    break;
                case 5:
                    addDate();
                    break;
                case 6:
                    addMatching();
                    break;
                case 7:
                    creating = false;
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void showMenu2() {
        System.out.println("\n--- CREATE SURVEY MENU ---");
        System.out.println("1) Add a new T/F question");
        System.out.println("2) Add a new multiple-choice question");
        System.out.println("3) Add a new short answer question");
        System.out.println("4) Add a new essay question");
        System.out.println("5) Add a new date question");
        System.out.println("6) Add a new matching question");
        System.out.println("7) Return to previous menu");
        System.out.print("Enter your choice: ");
    }

    private static void displayCurrentSurvey() {
        if (currentSurvey == null) {
            System.out.println("You must have a survey loaded in order to display it.");
        } else {
            currentSurvey.display();
        }
    }

    private static void loadSurvey() {
        System.out.println("Please select a file to load:");
        File[] files = listFiles(SURVEY_DIR, ".survey");

        if (files == null || files.length == 0) {
            System.out.println("No surveys found.");
            return;
        }

        int fileChoice = getUserFileChoice(files);
        if (fileChoice == -1) return;

        String filename = files[fileChoice].getPath();

        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            currentSurvey = (Survey) ois.readObject();
            System.out.println("Survey '" + currentSurvey.getTitle() + "' successfully loaded.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading survey: " + e.getMessage());
        }
    }

    private static void saveSurvey() {
        if (currentSurvey == null) {
            System.out.println("You must have a survey loaded in order to save it.");
            return;
        }
        System.out.print("Enter a filename to save as: ");
        String filename = scanner.nextLine();

        saveSurveyToFile(currentSurvey, SURVEY_DIR, filename, ".survey");
    }

    private static void takeCurrentSurvey() {
        if (currentSurvey == null) {
            System.out.println("You must have a survey loaded in order to take it.");
            return;
        }

        Survey responseSurvey = createSurveyCopy(currentSurvey);
        if (responseSurvey == null) {
            System.err.println("Error starting survey. Could not copy.");
            return;
        }

        System.out.println("Taking survey: " + responseSurvey.getTitle());
        responseSurvey.take();

        System.out.print("Enter a name for your responses (e.g., 'user1_responses'): ");
        String filename = scanner.nextLine();

        saveSurveyToFile(responseSurvey, RESPONSE_DIR, filename, ".response");
        System.out.println("Responses saved.");
    }

    private static void modifyCurrentSurvey() {
        if (currentSurvey == null) {
            System.out.println("You must have a survey loaded in order to modify it.");
            return;
        }

        System.out.println("Modifying survey: " + currentSurvey.getTitle());
        currentSurvey.display();
        System.out.print("What question do you wish to modify? (Enter number): ");
        int qNum = getUserInt();

        try {
            Question q = currentSurvey.getQuestion(qNum - 1);
            q.modify();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid question number.");
        }
    }

    private static void addTrueFalse() {
        System.out.print("Enter the prompt for your True/False question: ");
        String prompt = scanner.nextLine();
        currentSurvey.addQuestion(new TrueFalse(prompt));
    }

    private static void addMultipleChoice() {
        System.out.print("Enter the prompt for your multiple-choice question: ");
        String prompt = scanner.nextLine();
        MultipleChoice q = new MultipleChoice(prompt);

        System.out.print("Enter the number of choices: ");
        int numChoices = getUserInt();

        for (int i = 0; i < numChoices; i++) {
            System.out.print("Enter choice #" + (i + 1) + ": ");
            q.addChoice(scanner.nextLine());
        }
        currentSurvey.addQuestion(q);
    }

    private static void addShortAnswer() {
        System.out.print("Enter the prompt for your short answer question: ");
        String prompt = scanner.nextLine();
        currentSurvey.addQuestion(new ShortAnswer(prompt));
    }

    private static void addEssay() {
        System.out.print("Enter the prompt for your essay question: ");
        String prompt = scanner.nextLine();
        currentSurvey.addQuestion(new Essay(prompt));
    }

    private static void addDate() {
        System.out.print("Enter the prompt for your date question: ");
        String prompt = scanner.nextLine();
        currentSurvey.addQuestion(new Date(prompt));
    }

    private static void addMatching() {
        System.out.print("Enter the prompt for your matching question: ");
        String prompt = scanner.nextLine();
        Matching q = new Matching(prompt);

        System.out.print("Enter the number of matching pairs: ");
        int numPairs = getUserInt();

        for (int i = 0; i < numPairs; i++) {
            System.out.print("Enter left-column item #" + (i + 1) + ": ");
            String left = scanner.nextLine();
            System.out.print("Enter right-column item #" + (i + 1) + ": ");
            String right = scanner.nextLine();
            q.addMatch(left, right);
        }
        currentSurvey.addQuestion(q);
    }

    private static int getUserInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static File[] listFiles(String dir, String extension) {
        File folder = new File(dir);
        return folder.listFiles((d, name) -> name.endsWith(extension));
    }

    private static int getUserFileChoice(File[] files) {
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ") " + files[i].getName());
        }
        System.out.print("Enter the number of the file: ");

        while (true) {
            int choice = getUserInt();
            if (choice >= 1 && choice <= files.length) {
                return choice - 1;
            } else {
                System.out.print("Invalid choice. Enter a number between 1 and " + files.length + ": ");
            }
        }
    }

    private static void saveSurveyToFile(Survey survey, String dir, String filename, String extension) {
        if (!filename.endsWith(extension)) {
            filename += extension;
        }
        String filepath = dir + File.separator + filename;

        try (FileOutputStream fos = new FileOutputStream(filepath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(survey);
            System.out.println("Survey successfully saved to " + filepath);

        } catch (IOException e) {
            System.err.println("Error saving survey: " + e.getMessage());
        }
    }

    private static Survey createSurveyCopy(Survey original) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.flush();
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Survey) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}