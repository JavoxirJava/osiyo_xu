package osiyo.xalqaro.osiyo_xu.bot;

import java.util.List;
import java.util.Set;

public interface Semester {
    String ONE_SEMESTER = "1-semestr";
    String TWO_SEMESTER = "2-semestr";
    String THREE_SEMESTER = "3-semestr";
    String FOUR_SEMESTER = "4-semestr";
    String FIVE_SEMESTER = "5-semestr";
    String SIX_SEMESTER = "6-semestr";
    String SEVEN_SEMESTER = "7-semestr";
    String EIGHT_SEMESTER = "8-semestr";
    String NINE_SEMESTER = "9-semestr";
    String TEN_SEMESTER = "10-semestr";
    String ELEVEN_SEMESTER = "11-semestr";
    String TWELVE_SEMESTER = "12-semestr";

    List<String> ALL_SEMESTERS = List.of(ONE_SEMESTER, TWO_SEMESTER, THREE_SEMESTER, FOUR_SEMESTER, FIVE_SEMESTER,
            SIX_SEMESTER, SEVEN_SEMESTER, EIGHT_SEMESTER, NINE_SEMESTER, TEN_SEMESTER, ELEVEN_SEMESTER, TWELVE_SEMESTER);
    List<String> TEN_SEMESTERS = ALL_SEMESTERS.subList(0, 10);

}
