package BouncingGame_JAVAFX;
/** class to set the score **/
public class Score {

    /**Store score **/
    int score=0;

    /**
     * Method to get the game
     * @return score in int
     */
    public int getScore() {
        return score;
    }

    /**
     * Method to set the Score
     * @param score is int type
     */
    public void setScore(int score) {
        this.score += score;
    }
}
