/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TweetRating;

/**
 *
 * @author kevinsancho
 */
public class OneRow {
    private String text;
    private String subject;
    private String evaluation;
    private String hideAuthor;
    private String hideIdTweet;
    private String comment;
    private String rater;

    public String getRater() {
        return rater;
    }

    public void setRater(String rater) {
        this.rater = rater;
    }

    public OneRow(String text, String subject, String hideAuthor, String hideIdTweet) {
        this.text = text;
        this.subject = subject;
        this.hideAuthor = hideAuthor;
        this.hideIdTweet = hideIdTweet;
    }

    public String getHideIdTweet() {
        return hideIdTweet;
    }

    public void setHideIdTweet(String hideIdTweet) {
        this.hideIdTweet = hideIdTweet;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getHideAuthor() {
        return hideAuthor;
    }

    public void setHideAuthor(String hideAuthor) {
        this.hideAuthor = hideAuthor;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    
}
