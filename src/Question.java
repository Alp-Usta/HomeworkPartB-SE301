import java.io.Serializable;
import java.util.ArrayList;

public abstract class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String prompt;
    protected ArrayList<Response> userResponses = new ArrayList<>();

    // Holds the number of responses this question accepts
    protected int numResponses;

    // Constructor
    public Question(String prompt, int numResponses){
        this.prompt = prompt;
        this.numResponses = numResponses;
    }

    // Abstract methods
    public abstract void display();
    public abstract void takeQuestion();
    public abstract void modify();

    // Concrete Methods
    public void addUserResponse(Response response) {
        this.userResponses.add(response);
    }
    public ArrayList<Response> getResponses(){
        return userResponses;
    }
    public String getPrompt(){
        return prompt;
    }
    // Helper getter
    public int getNumResponses() {
        return numResponses;
    }
}