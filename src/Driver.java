import java.io.*;
import java.util.Scanner;

// We need these for the deep-copy method when taking a survey
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Driver {

    // The single scanner for the whole application
    private static final Scanner scanner = new Scanner(System.in);
    // The survey currently loaded in memory
    private static Survey currentSurvey = null;

    // Define folder names for organization
    private static final String SURVEY_DIR = "surveys";
    private static final String RESPONSE_DIR = "responses";

    /**
     * The main entry point for the Survey System application.
     */
    public static void main(String[] args) {

        // --- This part is for generating sample files ---
        // 1. Uncomment the line below.
        // 2. Run the program ONCE.
        // 3. Re-comment the line. Sample files will be in the 'surveys' folder.

        //generateSampleFiles(); // <-- UNCOMMENT TO RUN ONCE

        // --- End of sample file generator ---


        // Create the folders if they don't exist
        new File(SURVEY_DIR).mkdir();
        new File(RESPONSE_DIR).mkdir();

        System.out.println("Welcome to the Survey System!");
        boolean running = true;

        // Main program loop
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
                case 7: // <-- NEW CASE
                    tabulateSurvey();
                    break;
                case 8: // <-- UPDATED CASE
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    /**
     * Displays the main menu.
     */
    private static void showMenu1() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1) Create a new Survey");
        System.out.println("2) Display the current Survey");
        System.out.println("3) Load a Survey");
        System.out.println("4) Save the current Survey");
        System.out.println("5) Take the current Survey");
        System.out.println("6) Modify the current Survey");
        System.out.println("7) Tabulate a Survey");
        System.out.println("8) Quit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Displays the survey creation menu.
     */
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

    // --- MAIN MENU ACTIONS ---

    /**
     * (Menu 1) Guides user through creating a new survey.
     */
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
                case 1: addTrueFalse(); break;
                case 2: addMultipleChoice(); break;
                case 3: addShortAnswer(); break;
                case 4: addEssay(); break;
                case 5: addDate(); break;
                case 6: addMatching(); break;
                case 7: creating = false; System.out.println("Returning to main menu."); break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * (Menu 2) Displays the currently loaded survey.
     */
    private static void displayCurrentSurvey() {
        if (currentSurvey == null) {
            System.out.println("You must have a survey loaded in order to display it.");
        } else {
            currentSurvey.display();
        }
    }

    /**
     * (Menu 3) Loads a survey from a .survey file.
     */
    private static void loadSurvey() {
        System.out.println("Please select a file to load:");
        File[] files = listFiles(SURVEY_DIR, ".survey");

        if (files == null || files.length == 0) {
            System.out.println("No surveys found in the '" + SURVEY_DIR + "' folder.");
            return;
        }

        int fileChoice = getUserFileChoice(files);
        if (fileChoice == -1) return; // User chose to cancel or invalid input

        String filename = files[fileChoice].getPath();

        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            currentSurvey = (Survey) ois.readObject();
            System.out.println("Survey '" + currentSurvey.getTitle() + "' successfully loaded.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading survey: " + e.getMessage());
        }
    }

    /**
     * (Menu 4) Saves the current survey to a .survey file.
     */
    private static void saveSurvey() {
        if (currentSurvey == null) {
            System.out.println("You must have a survey loaded in order to save it.");
            return;
        }
        System.out.print("Enter a filename to save as: ");
        String filename = scanner.nextLine();

        saveSurveyToFile(currentSurvey, SURVEY_DIR, filename, ".survey");
    }

    /**
     * (Menu 5) Takes the current survey and saves responses to a .response file.
     */
    private static void takeCurrentSurvey() {
        if (currentSurvey == null) {
            System.out.println("You must have a survey loaded in order to take it.");
            return;
        }

        // Create a deep copy of the survey to store responses
        // This leaves the survey  clean
        Survey responseSurvey = createSurveyCopy(currentSurvey);
        if (responseSurvey == null) {
            System.err.println("Error starting survey. Could not copy.");
            return;
        }

        System.out.println("Taking survey: " + responseSurvey.getTitle());
        responseSurvey.take(); // This fills the responses list

        System.out.print("Enter a name for your responses (e.g., 'user1_responses'): ");
        String filename = scanner.nextLine();

        // Save the *copy* (with responses) to the responses folder
        saveSurveyToFile(responseSurvey, RESPONSE_DIR, filename, ".response");
        System.out.println("Responses saved.");
    }

    /**
     * (Menu 6) Modifies a question in the currently loaded survey.
     */
    private static void modifyCurrentSurvey() {
        if (currentSurvey == null) {
            System.out.println("You must have a survey loaded in order to modify it.");
            return;
        }

        System.out.println("Modifying survey: " + currentSurvey.getTitle());
        currentSurvey.display(); // Show all questions
        System.out.print("What question do you wish to modify? (Enter number): ");
        int qNum = getUserInt();

        try {
            // Get the question (index is qNum - 1)
            Question q = currentSurvey.getQuestion(qNum - 1);
            // Call its modify method
            q.modify();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid question number.");
        }
    }
    /**
     * (Menu 7) Loads a specific response file and tabulates its results.
     */
    private static void tabulateSurvey() {
        System.out.println("Please select a response file to tabulate:");
        File[] files = listFiles(RESPONSE_DIR, ".response");

        if (files == null || files.length == 0) {
            System.out.println("No responses found in the '" + RESPONSE_DIR + "' folder.");
            return;
        }

        int fileChoice = getUserFileChoice(files);
        if (fileChoice == -1) return;

        String filename = files[fileChoice].getPath();

        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            // Load the response file into a temporary survey object
            Survey responseSurvey = (Survey) ois.readObject();
            System.out.println("Loading '" + files[fileChoice].getName() + "' for tabulation...");

            responseSurvey.tabulate();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading response file: " + e.getMessage());
        }
    }

    // --- QUESTION CREATION METHODS ---

    private static void addTrueFalse() {
        System.out.print("Enter the prompt for your True/False question: ");
        String prompt = scanner.nextLine();
        int numResponses = getNumResponsesFromUser();
        currentSurvey.addQuestion(new TrueFalse(prompt, numResponses));
    }

    private static void addMultipleChoice() {
        System.out.print("Enter the prompt for your multiple-choice question: ");
        String prompt = scanner.nextLine();
        int numResponses = getNumResponsesFromUser();
        MultipleChoice q = new MultipleChoice(prompt, numResponses);

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
        int numResponses = getNumResponsesFromUser();
        currentSurvey.addQuestion(new ShortAnswer(prompt, numResponses));
    }

    private static void addEssay() {
        System.out.print("Enter the prompt for your essay question: ");
        String prompt = scanner.nextLine();
        int numResponses = getNumResponsesFromUser();
        currentSurvey.addQuestion(new Essay(prompt, numResponses));
    }

    private static void addDate() {
        System.out.print("Enter the prompt for your date question: ");
        String prompt = scanner.nextLine();
        int numResponses = getNumResponsesFromUser();
        currentSurvey.addQuestion(new Date(prompt, numResponses));
    }

    private static void addMatching() {
        System.out.print("Enter the prompt for your matching question: ");
        String prompt = scanner.nextLine();

        System.out.print("Enter the number of matching pairs: ");
        int numPairs = getUserInt();

        // For Matching, the number of responses the number of pairs.
        Matching q = new Matching(prompt, numPairs);

        for (int i = 0; i < numPairs; i++) {
            System.out.print("Enter left-column item #" + (i + 1) + ": ");
            String left = scanner.nextLine();
            System.out.print("Enter right-column item #" + (i + 1) + ": ");
            String right = scanner.nextLine();
            q.addMatch(left, right);
        }
        currentSurvey.addQuestion(q);
    }

    // --- HELPER METHODS ---

    /**
     * Asks the user for the number of responses a question can have.
     */
    private static int getNumResponsesFromUser() {
        System.out.print("How many responses can this question have? (default: 1): ");
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            return 1; // Default to 1
        }
        try {
            int num = Integer.parseInt(input);
            return (num > 0) ? num : 1; // Must be at least 1
        } catch (NumberFormatException e) {
            return 1; // Default to 1 if input is bad
        }
    }

    /**
     * Safely gets an integer from the user.
     */
    private static int getUserInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    /**
     * Lists files in a directory with a specific extension.
     */
    private static File[] listFiles(String dir, String extension) {
        File folder = new File(dir);
        // Use a filter to only find files ending with the extension
        return folder.listFiles((d, name) -> name.endsWith(extension));
    }

    /**
     * Shows the user a list of files and gets their choice.
     */
    private static int getUserFileChoice(File[] files) {
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ") " + files[i].getName());
        }
        System.out.print("Enter the number of the file: ");

        while (true) {
            int choice = getUserInt();
            if (choice >= 1 && choice <= files.length) {
                return choice - 1; // Return 0-based index
            } else {
                System.out.print("Invalid choice. Enter a number between 1 and " + files.length + ": ");
            }
        }
    }

    /**
     * The core serialization (saving) logic.
     */
    private static void saveSurveyToFile(Survey survey, String dir, String filename, String extension) {
        if (!filename.endsWith(extension)) {
            filename += extension;
        }
        // Use File.separator for cross-platform safety (handles / or \)
        String filepath = dir + File.separator + filename;

        try (FileOutputStream fos = new FileOutputStream(filepath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(survey); // This one line serializes the entire object
            System.out.println("Survey successfully saved to " + filepath);

        } catch (IOException e) {
            System.err.println("Error saving survey: " + e.getMessage());
        }
    }


    private static Survey createSurveyCopy(Survey original) {
        try {
            // Serialize the object into a byte array in memory
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.flush();
            oos.close();

            // Deserialize the byte array back into a new object
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Survey) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // This error should not happen in normal operation
            e.printStackTrace();
            return null;
        }
    }


    private static void generateSampleFiles() {
        System.out.println("--- GENERATING SAMPLE SURVEYS ---");

        new File(SURVEY_DIR).mkdir();
        new File(RESPONSE_DIR).mkdir();

        // 1. Create Tech Survey
        Survey techSurvey = new Survey("Tech Opinion Poll");
        techSurvey.addQuestion(new TrueFalse("Is Java an object-oriented language?", 1));
        MultipleChoice mc1 = new MultipleChoice("What is your preferred mobile OS?", 1);
        mc1.addChoice("iOS");
        mc1.addChoice("Android");
        techSurvey.addQuestion(mc1);
        techSurvey.addQuestion(new Date("What year was the first iPhone released? (YYYY-MM-DD)", 1));

        // 2. Create Opinion Survey
        Survey opinionSurvey = new Survey("Student Opinion Poll");
        opinionSurvey.addQuestion(new ShortAnswer("What is your major?", 1));
        Essay essay1 = new Essay("What are two things you like about this class?", 2); // Multi-response!
        opinionSurvey.addQuestion(essay1);

        // 3. Create Full Demo Survey
        Survey fullSurvey = new Survey("Full Survey Demo");
        fullSurvey.addQuestion(new TrueFalse("The Earth is flat.", 1));
        MultipleChoice mc2 = new MultipleChoice("Which of these are programming languages? (Choose 2)", 2); // Multi-response!
        mc2.addChoice("Java");
        mc2.addChoice("HTML");
        mc2.addChoice("Python");
        mc2.addChoice("C#");
        fullSurvey.addQuestion(mc2);
        fullSurvey.addQuestion(new ShortAnswer("What is the capital of Pennsylvania?", 1));
        fullSurvey.addQuestion(new Essay("What is your favorite hobby?", 1));
        fullSurvey.addQuestion(new Date("When is your birthday? (YYYY-MM-DD)", 1));
        Matching match1 = new Matching("Match the team to the city", 3); // 3 pairs
        match1.addMatch("Eagles", "Philadelphia");
        match1.addMatch("Steelers", "Pittsburgh");
        match1.addMatch("Phillies", "Philadelphia");
        fullSurvey.addQuestion(match1);

        // 4. Save all surveys
        System.out.println("Saving surveys to '" + SURVEY_DIR + "' folder...");
        saveSurveyToFile(techSurvey, SURVEY_DIR, "SampleSurvey_Tech", ".survey");
        saveSurveyToFile(opinionSurvey, SURVEY_DIR, "SampleSurvey_Opinion", ".survey");
        saveSurveyToFile(fullSurvey, SURVEY_DIR, "SampleSurvey_Full", ".survey");

        System.out.println("--- DONE. Please re-comment the call to 'generateSampleFiles()' in main(). ---");
    }
}