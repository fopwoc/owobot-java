package com.aspirin;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

public class Bot extends TelegramLongPollingBot {

    final static private ArrayList<String> RandomAnimeGirls = new ArrayList<String>() {{
        add("awwnime");
        add("wholesomeyuri");
        add("tsunderes");
        add("animelegwear");
        add("kitsunemimi");
        add("animeponytails");
        add("tyingherhairup");
        add("Moescape");
        add("headpats");
        add("cutelittlefangs");
        add("kemonomimi");
        add("twintails");
        add("pouts");
        add("animelegs");
        add("Animewallpaper");
        add("pantsu");
        add("Patchuu");
        add("animehotbeverages");
        add("silverhair");
        add("longhairedwaifus");
        add("Smugs");
    }};

    final static private ArrayList<String> RandomHentai = new ArrayList<String>() {{     //Опа! Вы нашли незадокументированную фичу!
        add("ecchi");
        add("hentai");
        add("ahegao");
        add("hentaifemdom");
        add("consentacles");
        add("rule34");
        add("hentaibondage");
        add("HentaiPetgirls");
        add("CumHentai");
        add("Artistic_Hentai");
        add("HentaiAnal");
        add("AnimeMILFS");
        add("Nekomimi");
    }};

    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message m = update.getMessage();
            final String[] message_text = {m.getText().toLowerCase()};

            Thread ThreadHandler = new Thread(() -> {
                if (m.getChat().isGroupChat() || m.getChat().isSuperGroupChat()) {
                    if (message_text[0].contains("@" + Main.prop.getProperty("BOT_USERNAME"))) {
                        System.out.println("[" + new SimpleDateFormat("MM/dd ").format(new Date()) + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()) + "] Message \"" + message_text[0].replace("@" + Main.prop.getProperty("BOT_USERNAME"), "") + "\" has been received from user " + m.getFrom().getUserName() + " in group chat");
                    }
                } else {
                    System.out.println("[" + new SimpleDateFormat("MM/dd ").format(new Date()) + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()) + "] Message \"" + message_text[0] + "\" has been received from user " + m.getFrom().getUserName());
                }

                RedditAPI API = new RedditAPI();

                if (message_text[0].contains("@" + Main.prop.getProperty("BOT_USERNAME"))) {
                    message_text[0] = message_text[0].replace("@" + Main.prop.getProperty("BOT_USERNAME"), "");
                }

                Main.savePreference("UsedTimes", Main.readPreference("UsedTimes") + 1);
                try {
                    switch (message_text[0]) {
                        case "owo":
                            sendText(m, "owo");
                            break;
                        case "uwu":
                            sendText(m, "uwu");
                            break;
                        case "/start":
                            sendText(m, "Добро пожаловать, " + m.getFrom().getFirstName() + "!\n\nЧто-бы получить больше информации напишите /info\n\nТак-же нажмите на иконочку со слешем у клавиатуры что-бы узнать о командах.");
                            break;
                        case "/info":
                            sendText(m, "Приветик! Я owobot v1.1 - бот, который прислылает милых девочек!\n\nЯ создана на java и беру картинки с reddit. Я немного медлительная, потому что меня держат на очень слабой машине в шкафу моего создателя. Но не беспокойтесь обо мне, меня это устраивает. Обычно мне нужно по 10-15 секунд что-бы прислать вам картинку, однако, вам не стоит бояться отправлять мне по 25-50 запросов за раз, я справлюсь с этим!\n\nПару слов по поводу приватности - я не собираю никаких данных о вас, ни кто пишет, ни что пишет, ни сколько. Только общее число запросов, просто для статистики.\n\nМоя страничка на GitHub: https://github.com/ASPIRINswag/owobot-java\n\nЯ надеюсь что буду полезной вам! (☆ω☆)");
                            break;
                        case "/status":
                            long uptime = System.currentTimeMillis() - Main.StartTime;
                            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                            sendText(m, "Я жива! Uptime: " + (int) Math.floor(uptime / 1000 / 60 / 60 / 24) + " дней " + formatter.format(new Date(uptime)) + ". Всего запросов: " + Main.readPreference("UsedTimes"));
                            break;
                        case "/get":
                            sendText(m, "Напишите боту /get_*название сабреддита* и он отправит вам случайную картинку оттуда.\n" +
                                    "Вот вам моя подборочка:\n" +
                                    "/get_awwnime - Что-то милое.\n" +
                                    "/get_wholesomeyuri - Юри. Все любят юри.\n" +
                                    "/get_handholding - Милые девочки деражатся за ручки.\n" +
                                    "/get_tsunderes - Милые цундеры!\n" +
                                    "/get_animelegwear - Очень голготочки и не только.\n" +
                                    "/get_kitsunemimi - Кошкодевочки!\n" +
                                    "/get_animeponytails - Милые девочки с конским хвостиком.\n" +
                                    "/get_tyingherhairup - Девочки поправляют свои волосы.\n" +
                                    "/get_Moescape - Милые арты с милыми девочками.\n" +
                                    "/get_headpats - Девочек глядят по голове.\n" +
                                    "/get_cutelittlefangs - Зубики и клыки у милых девочек.\n" +
                                    "/get_kemonomimi - Опять ушки!\n" +                                                              //тут меня понесло немножко
                                    "/get_twintails - Девочки с прическами из 2 хвостиков.\n" +
                                    "/get_pouts - Милые девочки с надутыми щёчками.\n" +
                                    "/get_animelegs - Ножки!\n" +
                                    "/get_Animewallpaper - Красивые обои с милыми девочками.\n" +
                                    "/get_pantsu - Панцушоты owo!\n" +
                                    "/get_Patchuu - Загаточны и изысканные милые девочки.\n" +
                                    "/get_animehotbeverages - Девочки пьют горячие напитки.\n" +
                                    "/get_AnimeLounging - Милые девочки отдыхают.\n" +
                                    "/get_silverhair - Девочки с серебряными волосами.\n" +
                                    "/get_longhairedwaifus - Милашки с длинными волосами.\n" +
                                    "/get_Smugs - Самодовольные девочки.");
                            break;
                        case "/random":
                            sendText(m, API.getPicFromSub(API, RandomAnimeGirls.get(new Random().nextInt(RandomAnimeGirls.size())), m));
                            break;
                        case "/hentai":
                            sendText(m, API.getPicFromSub(API, RandomHentai.get(new Random().nextInt(RandomHentai.size())), m)); //Опа! Вы нашли незадокументированную фичу!
                            break;
                        default:
                            if (message_text[0].contains("/get_")) {
                                sendText(m, API.getPicFromSub(API, message_text[0].substring(5), m));
                                break;
                            } else if (message_text[0].contains("/feedback")) {
                                String Subedmessage = message_text[0].substring(9);
                                if (Subedmessage.length() == 0) {
                                    sendText(m, "Для того, что-бы написать создателю бота, использутей форму \"/feedback привет, тут текст!\"");
                                } else {
                                    sendTextFeedback(m, "Сообщение от " + m.getFrom().getUserName() + ":" + Subedmessage);
                                }
                                break;
                            } else {
                                sendText(m, "Eaah? Я не поняла вас!");
                                break;
                            }
                    }
                } catch (Exception e) {
                    sendText(m, "Ой! Запрос " + message_text[0] + " какой-то не такой, я не могу получить от него данные... \nПростите (⇀ε↼‶)");
                    System.out.println("Thread " + Thread.currentThread().getId() + " has ended (execution error). message: " + message_text[0]);
                }
                Thread.currentThread().stop();
            });

            CompletableFuture.runAsync(() -> {
                if (m.getChat().isGroupChat() || m.getChat().isSuperGroupChat()) {
                    if (!message_text[0].contains("@" + Main.prop.getProperty("BOT_USERNAME"))) {
                        Thread.currentThread().stop();
                    }
                    if (message_text[0].equals("/hentai@" + Main.prop.getProperty("BOT_USERNAME"))) {
                        Thread.currentThread().stop();
                    }
                }

                ThreadHandler.start();
                SendChatAction sendChatAction = new SendChatAction();
                sendChatAction.setChatId(m.getChatId());
                sendChatAction.setAction(ActionType.TYPING);

                while (ThreadHandler.isAlive()) {
                    try {
                        execute(sendChatAction);
                        Thread.sleep(1000);
                    } catch (TelegramApiException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Thread.currentThread().stop();
            });
        }
    }

    public String getBotUsername() {
        return Main.prop.getProperty("BOT_USERNAME");
    }

    public String getBotToken() {
        return Main.prop.getProperty("BOT_TOKEN");
    }

    private void sendTextFeedback(@NotNull Message message, String text) {
        try {
            execute(new SendMessage().setChatId(Main.prop.getProperty("BOT_FEEDBACKID")).setText(text).setParseMode("HTML"));
            execute(new SendMessage().setChatId(message.getChatId().toString()).setText("Готово! Я отправила ваше сообщение своему создателю! Спасибо вам (\\\\\\ω\\\\\\)").setParseMode("HTML"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendText(Message message, @NotNull String text) {
        try {
            if (text.length() == 0) {
                execute(new SendMessage().setChatId(message.getChatId().toString()).setText("Ой! Супер странная ошибка все поломалось, просите (ᗒᗣᗕ)").setParseMode("HTML"));
                System.out.println("Thread " + Thread.currentThread().getId() + " has ended (unknown). message: " + message.getText().toLowerCase());
                return;
            }

            execute(new SendMessage().setChatId(message.getChatId().toString()).setText(text).setParseMode("HTML"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
