package osiyo.xalqaro.osiyo_xu.bot;

import java.util.List;

public interface Template {
    // Admins Chat Id
    Long CREATOR_ID = 6146809388L;
    Long CREATOR_ID_2 = 1085241246L;
    Long CREATOR_ID_3 = 6186922376L;
    Long CREATOR_ID_4 = 5097029511L;

    // Messages
    String START = "/start";
    String CHOOSE_SEMESTER = "semestr ni tanlang.";
    String CHOOSE_DEPARTMENT = "Kearkli bo'limni tanlang";
    String CHOOSE_DIRECTION = "Yunalishni tanlanng.";
    String CHOOSE_SCIENCE = "Fanini tanlang.";
    String LAVE_AN_OFFER = "Taklif qoldirish";
    String ACCEPT = "Tasdiqlash";
    String BACK = "Bekor qilish";
    String MENU = "Menu ga qaytish";

    // Admin Start
    String DENTISTRY = "Stomatologiya";
    String TREATMENT_WORK = "Davolash ishi";
    String PHARMACEUTICALS = "Farmatsevtika";

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

    List<String> ADMIN_START = List.of(DENTISTRY, TREATMENT_WORK, PHARMACEUTICALS);
    List<String> ADMIN_SCIENCE = List.of(ADD_SCIENCE, EDIT_SCIENCE, DELETE_SCIENCE, GET_SCIENCE, MENU);
    List<String> ADMIN_SUBJECT = List.of(ADD_SUBJECT, EDIT_SUBJECT, DELETE_SUBJECT, GET_SUBJECT, MENU);
    List<String> ADMIN_SUBJECT_EDIT = List.of(EDIT_SUBJECT_SCIENCE, EDIT_NAME, EDIT_CONTENT, BACK);
    List<Long> ADMINS = List.of(CREATOR_ID, CREATOR_ID_2, CREATOR_ID_3, CREATOR_ID_4);
    List<String> BACK_BUTTON = List.of(BACK);
    List<String> BACK_AND_ACCEPT_BUTTON = List.of(ACCEPT, BACK);
    List<String> OFFER = List.of(LAVE_AN_OFFER);
    String BOT_TOKEN = "7449202231:AAFq5zWJqPS_CmwVUcNl5G3C3sorTziWFJ8";
    String BOT_USERNAME = "@Medical_ship_bot";

}