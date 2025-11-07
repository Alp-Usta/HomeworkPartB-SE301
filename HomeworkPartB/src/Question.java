import java.io.Serializable;
import java.util.ArrayList;


public abstract class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String prompt;
    protected ArrayList<Response> userResponses = new ArrayList<>();

    //Constructor
    public Question(String prompt){
        this.prompt = prompt;
    }
    //Abstract methods
    public abstract void display();
    public abstract void takeQuestion();
    public abstract void modify();

    //Concrete Methods
    public void addUserResponse(Response response) {
        this.userResponses.add(response);
    }
    public ArrayList<Response> getResponses(){
        return userResponses;
    }
    public String getPrompt(){
        return prompt;
    }




}
