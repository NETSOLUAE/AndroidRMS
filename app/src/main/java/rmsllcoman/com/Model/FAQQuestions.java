package rmsllcoman.com.Model;

import java.util.ArrayList;

/**
 * Created by macmini on 10/9/17.
 */

public class FAQQuestions {

    private String id;
    private String title;
    private ArrayList<FAQAnswers> faqAnswers;

    public FAQQuestions () {

    }

    public void setID (String id) {
        this.id = id;
    }
    public String getID () {
        return id;
    }

    public void setTitle (String title) {
        this.title = title;
    }
    public String getTitle () {
        return title;
    }

    public ArrayList<FAQAnswers> getFaqAnswers() {
        return faqAnswers;
    }

    public void setFaqAnswers(ArrayList<FAQAnswers> faqAnswers) {
        this.faqAnswers = faqAnswers;
    }
}
