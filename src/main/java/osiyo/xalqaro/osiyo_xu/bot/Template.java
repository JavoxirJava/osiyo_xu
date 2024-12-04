package osiyo.xalqaro.osiyo_xu.bot;

import java.util.List;

public interface Template {
    String START = "/start";
    String CHOOSE_DEPARTMENT = "Kearkli bo'limni tanlang";
    String LAVE_AN_OFFER = "Taklif qoldirish";
    String ACCEPT = "Tasdiqlash";
    String BACK = "Bekor qilish";
    String MENU = "Menu ga qaytish";

    // Admin Start
    String SCIENCE_DEPARTMENT = "Fanlar bo`limi";
    String SUBJECT_DEPARTMENT = "Mavzular bo`limi";

    // Admin Science
    String ADD_SCIENCE = "Fan qo'shish";
    String EDIT_SCIENCE = "Fanni tahrirlash";
    String DELETE_SCIENCE = "Fanni o`chirish";
    String GET_SCIENCE = "Fanlarni ko'rish";

    // Admin Subject
    String ADD_SUBJECT = "Mavzu qo'shish";
    String EDIT_SUBJECT = "Mavzuni tahrirlash";
    String DELETE_SUBJECT = "Mavzuni o`chirish";
    String GET_SUBJECT = "Mavzularni ko'rish";
    String EDIT_SUBJECT_SCIENCE = "Fanini o'zgartirish";
    String EDIT_NAME = "Nomini o'zgartirish";
    String EDIT_CONTENT = "Malumotlarini o'zgartirish";

    List<String> ADMIN_START = List.of(SCIENCE_DEPARTMENT, SUBJECT_DEPARTMENT);
    List<String> ADMIN_SCIENCE = List.of(ADD_SCIENCE, EDIT_SCIENCE, DELETE_SCIENCE, GET_SCIENCE, MENU);
    List<String> ADMIN_SUBJECT = List.of(ADD_SUBJECT, EDIT_SUBJECT, DELETE_SUBJECT, GET_SUBJECT, MENU);
    List<String> ADMIN_SUBJECT_EDIT = List.of(EDIT_SUBJECT_SCIENCE, EDIT_NAME, EDIT_CONTENT, BACK);
    List<String> BACK_BUTTON = List.of(BACK);
    List<String> BACK_AND_ACCEPT_BUTTON = List.of(ACCEPT, BACK);
    List<String> OFFER = List.of(LAVE_AN_OFFER);
    String BOT_TOKEN = "7383627105:AAFrktVAWW7g6tIiNwxd8pi8xsGLMPR_8ZQ";
    String BOT_USERNAME = "@full_testbot";
    String CREATOR_ID = "1085241246";
}