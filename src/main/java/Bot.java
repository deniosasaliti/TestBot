import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.*;


public class Bot extends TelegramLongPollingBot {

    public static final String TOKEN = "1908273706:AAG7kcbbkH9XjhrjP48pR4LdBqFkPUw9Ows";
    public static final String USERNAME = "qweqweqweasdasdasdzxczxczxcbot";
    public static Map<Integer, String> MAP = new HashMap<>();
    public static int buttonCounter = 1;
    public static int counter = 1;
    public static boolean isRepeat = false;

    static {
        MAP.put(1, "Вітаю тебе!");
        MAP.put(2, "Я твій помічник і допоможу тобі корисною інформаціею");
        MAP.put(3, "Тож, тобі необхідно обовязково взяти з собою орігінали наступних документів у відділ кадрів...");
        MAP.put(4, "Оформлення в офісі за адресою_next");
        MAP.put(5, "Тепер залишилося тільки підїхати в відділ кадрів з необхідними документами....");
        MAP.put(6, " Інформація про розташування відділу кадрів та контактні телефони");
        MAP.put(7, "м.Київ вулиця Хрущатик....");
    }

    public static Map<Integer, String> BUTTONS = new HashMap<>();

    static {
        BUTTONS.put(1,"Вибір регіональної філії_6");
        BUTTONS.put(2,"new_Київ_3_11_34");
        BUTTONS.put(3,"КМД_6");
        BUTTONS.put(4,"ЦАУ_7");
        BUTTONS.put(5,"new_Змінити дирекція_1_1");
    }


    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    public void sendMsg(Message message, String text,boolean isMarkdown) {

        SendMessage sendMessage = new SendMessage(message.getChatId(), text);
        if (isMarkdown){
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendButton(Message message, String text,int ButtonAmount) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.enableMarkdown(true);
        String[] s = BUTTONS.get(1).split("_");

        InlineKeyboardButton button1 = getButton("1",s[1],0,"0",
                "first");
        InlineKeyboardButton button2 = getButton("2",BUTTONS.get(2),2,"11",
                "second");
//        keyboardButtonsRow1.add(getButton(type[i],callbacks[i], k, array[3]));

//        button2.setCallbackData("123");
//        button1.setCallbackData("new_2_11");
        sendMessage.setReplyMarkup(getInlineKeyboard(new ArrayList<>(
                Arrays.asList(button1, button2)
        )));
        sendMessage.setChatId(message.getChatId());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();


        if (message != null && message.hasText()) {

            switch (message.getText()) {

                case "/start":
                    counter=1;
                    String[] s = MAP.get(counter).split("_");
                    while (s.length == 1) {
                         s = MAP.get(counter + 1).split("_");
                        sendMsg(message, MAP.get(counter),false);
                        counter++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    sendButton(message, "123");
                    break;


            }


        } else if (update.hasCallbackQuery()) {

            if (update.getCallbackQuery().getData().startsWith("new")){

//new_Київ_2_11_34
                String [] array = update.getCallbackQuery().getData().split("_");

                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
                int k = Integer.parseInt(array[2]);
                String [] type = array[3].split("");
                System.out.println( "typeArray :" +  Arrays.toString(type));
                String[] callbacks = array[4].split("");
                System.out.println( "callBackArray :" +  Arrays.toString(callbacks));
                int buttons = 0;
//        BUTTONS.put(2,"Київ_new_2_11_34");
                for (int i = 0; i<k; i++){

//                    InlineKeyboardButton button1 = BUTTONS.get(Integer.parseInt(array[1]));
//                    InlineKeyboardButton button2 = BUTTONS.get(array[1]);
                    int currentType = Integer.parseInt(type[i]);


                            keyboardButtonsRow1.add(getButton(type[i],callbacks[i], k, array[3],"new_button"));

                }




//                button1.setCallbackData("123123123");
//                button2.setCallbackData("2331");
//
//                keyboardButtonsRow1.add(button1);
//                keyboardButtonsRow1.add(button2);



                List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
                rowList.add(keyboardButtonsRow1);

                inlineKeyboardMarkup.setKeyboard(rowList);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Зробіть вибір");
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//                sendMessage.enableMarkdown(true);
                sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());


                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


            }else{

                SendMessage qwe = new SendMessage().setText(MAP.get(Integer.parseInt(update.getCallbackQuery().getData())))
                        .setChatId(update.getCallbackQuery().getMessage().getChatId());
                try {
                    execute(qwe);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }



        }


//            }else  if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("new")){
////                String[] bC= update.getCallbackQuery().getData().split("_");
////                int s = Integer.parseInt(bC[1]);
////                List<InlineKeyboardButton> list = new ArrayList();
//////                        for (int i=0;i<s;i++){
////                list.add(BUTTONS.get(2).setCallbackData("new4").setText("qew"));
////                list.add(BUTTONS.get(3).setCallbackData("new4").setText("22"));
////                        }
//                List<InlineKeyboardButton> list = new ArrayList();
//                list.add(new InlineKeyboardButton().setText("qwe").
//                        setCallbackData("qwess"));
//                System.out.println("123123123");
//                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
//                List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
//                rowList.add(list);
//                inlineKeyboard.setKeyboard(rowList);
//                String answer = "Updated message text";
//                EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
//                        .setChatId(update.getCallbackQuery().getId()).setMessageId(Integer.parseInt(update.getCallbackQuery().getId()))
//                        .setInlineMessageId(update.getCallbackQuery().getId());
//                InlineKeyboardButton dk1=new InlineKeyboardButton();
//                dk1.setText("label1");
//                dk1.setCallbackData("change_the_label");
//                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//                List<InlineKeyboardButton> rowInline = new ArrayList<>();
//
//                rowInline.add(dk1);
//
//                rowsInline.add(rowInline);
//
//                markupInline.setKeyboard(rowsInline);
//                new_message.setReplyMarkup(markupInline);
//
//                try {execute(new_message);
//
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//
//
//
//                ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
//
//                        SendMessage sendMessage = new SendMessage();
//                        sendMessage.setText("qwe");
//                        sendMessage.setReplyMarkup(inlineKeyboard);
//                        sendMessage.enableMarkdown(true);
//                        sendMessage.setChatId(message.getChatId());
//                        sendMessage.enableMarkdown(true);
//
//                        try {
//                            execute(sendMessage);
//                        } catch (TelegramApiException e) {
//                            System.out.println("qweqweqwe");
//                        }
//
//                System.out.println("333333333");
//
//
//
//            }
//
//        }

//        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
//        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();

//        inlineKeyboardButton1.setText("Вибір регіональної філії");
//        inlineKeyboardButton1.setCallbackData("6");
//
//        inlineKeyboardButton2.setText("Київ");
//        inlineKeyboardButton2.setCallbackData("newM");


    }
    //        BUTTONS.put(2,"Київ_new_2_11_34");
    private InlineKeyboardButton   getButton(String type,String cB,int countOfNewButtons,
                                             String newButtonsTypes,String text) {
//
//        String callback;




        switch (type){
            case "1":


//                callback = cB;



                     return new InlineKeyboardButton().setCallbackData(cB).setText(text);


            case "2":
//                callback = "new" + "_" + countOfNewButtons + "_" + newButtonsTypes;
//                if (isRepeat) {
//
//                    --buttonCounter;
//                }        BUTTONS.put(2,"Київ_new_2_11_34");

//                    callback = MAP.get(counter++);


                    return new InlineKeyboardButton().setCallbackData(cB).setText(text);


            default:
                throw new IllegalStateException("Unexpected value: " + type);

        }

    }

    public static InlineKeyboardMarkup getInlineKeyboard (List < InlineKeyboardButton > buttons) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();


            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(buttons);


            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow1);

            inlineKeyboardMarkup.setKeyboard(rowList);
            return inlineKeyboardMarkup;
        }
    }

