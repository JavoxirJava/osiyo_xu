package osiyo.xalqaro.osiyo_xu.bot;

import java.util.List;
import java.util.Set;

public interface Template {
    // Admins Chat Id
    Long CREATOR_ID = 6146809388L;
    Long CREATOR_ID_2 = 1085241246L;
    Long CREATOR_ID_3 = 6186922376L;
    Long CREATOR_ID_4 = 5097029511L;

    // Messages
    String START = "/start";
    String CHOOSE_SEMESTER = "Semestr ni tanlang.";
    String CHOOSE_DEPARTMENT = "Kearkli bo'limni tanlang";
    String CHOOSE_DIRECTION = "Yunalishni tanlanng.";
    String CHOOSE_SCIENCE = "Fanini tanlang.";
    String CHOOSE_SUBJECT = "Mavzuni tanlang.";
    String SUBJECT_NAME = "Mavzu nomini yozing.";
    String SUBJECT_CONTENT = "Mavzu Malumotlarini yozing.";
    String SUBJECT_SAVED = "Mavzu saqlandi.\n\n" + CHOOSE_DEPARTMENT;
    String SUBJECT_DELETED = "Mavzu o'chirild.\n\n" + CHOOSE_DEPARTMENT;
    String LAVE_AN_OFFER = "Taklif qoldirish";
    String ACCEPT = "Tasdiqlash";
    String BACK = "\uD83D\uDD19 Ortga qaytish";
    String MENU = "Menu ga qaytish";

    // Admin Start
    String DENTISTRY = "Stomatologiya";
    String TREATMENT_WORK = "Davolash ishi";
    String PHARMACEUTICALS = "Farmatsevtika";

    // Admin Subject
    String ADD_SUBJECT = "Mavzu qo'shish";
    String GET_SUBJECT = "Mavzularni ko'rish";
    String DELETE_SUBJECT = "Mavzuni o`chirish";

    List<String> ADMIN_START = List.of(DENTISTRY, TREATMENT_WORK, PHARMACEUTICALS);
    List<String> ADMIN_SUBJECT = List.of(ADD_SUBJECT, GET_SUBJECT, DELETE_SUBJECT, MENU, BACK);
    Set<Long> ADMINS = Set.of(CREATOR_ID, CREATOR_ID_2, CREATOR_ID_3, CREATOR_ID_4);
    List<String> BACK_BUTTON = List.of(BACK);
    List<String> BACK_AND_ACCEPT_BUTTON = List.of(ACCEPT, BACK);
    List<String> OFFER = List.of(LAVE_AN_OFFER);
    String BOT_TOKEN = "7449202231:AAFq5zWJqPS_CmwVUcNl5G3C3sorTziWFJ8";
    String BOT_USERNAME = "@Medical_ship_bot";

}