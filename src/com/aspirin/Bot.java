package com.aspirin;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
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
    private String BOT_USERNAME = Main.prop.getProperty("BOT_USERNAME");
    private String BOT_TOKEN = Main.prop.getProperty("BOT_TOKEN");

    private ArrayList<String> RandomAnimeGirls = new ArrayList<String>() {{
        add("awwnime");
        add("wholesomeyuri");
        add("handholding");
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
        add("AnimeLounging");
        add("silverhair");
        add("longhairedwaifus");
        add("Smugs");
    }};

    private ArrayList<String> RandomHentai = new ArrayList<String>() {{     //Опа! Вы нашли незадокументированную фичу!
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
            User user = m.getFrom();
            final String[] message_text = {m.getText().toLowerCase()};
            System.out.println("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()) + "] Message \"" + message_text[0] + "\" has been received from user " + user.getUserName());

            Thread ThreadHandler = new Thread(() -> {
                RedditAPI API = new RedditAPI(Main.PagesLimit);

                if (m.getChat().isSuperGroupChat() || m.getChat().isGroupChat()) {
                    message_text[0] = message_text[0].replace("@" + BOT_USERNAME, "");
                }

                Main.savePreference("UsedTimes", Main.readPreference("UsedTimes") + 1);

                switch (message_text[0]) {
                    case "/start":
                        sendText(m.getChatId().toString(), "Добро пожаловать!\n\nЧто-бы получить больше информации напишите /info\n\nТак-же нажмите на иконочку со слешем у клавиатуры что-бы узнать о командах.");
                        break;
                    case "/info":
                        sendText(m.getChatId().toString(), "Приветик! Я owobot - бот, который прислылает милых девочек!\n\nЯ создана на java и беру картинки с reddit. Я немного медлительная, потому что меня держат на очень слабой машине в шкафу моего создателя. Но не беспокойтесь обо мне, меня это устраивает. Обычно мне нужно по 10-15 секунд что-бы прислать вам картинку, однако, вам не стоит бояться отправлять мне по 10-20 запросов за раз, я справлюсь с этим!\n\nПару слов по поводу приватности - я не собираю никаких данных о вас, ни кто пишет, ни что пишет, ни сколько. Только общее число запросов, просто для статистики.\n\nМоя страничка на GitHub: https://github.com/ASPIRINswag/owobot-java\n\nЯ надеюсь что буду полезной вам, uwu!");
                        break;
                    case "/status":
                        long uptime = System.currentTimeMillis() - Main.StartTime;
                        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                        sendText(m.getChatId().toString(), "Я жива! Uptime: " + (int) Math.floor(uptime / 1000 / 60 / 60 / 24) + " дней " + formatter.format(new Date(uptime)) + ". Всего запросов: " + Main.readPreference("UsedTimes"));
                        break;
                    case "/get":
                        sendText(m.getChatId().toString(), "Напишите боту /get_*название сабреддита* и он отправит вам случайную картинку оттуда.\n" +
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
                        sendText(m.getChatId().toString(), API.getPicFromSub(API, RandomAnimeGirls.get(new Random().nextInt(RandomAnimeGirls.size()))));
                        break;
                    case "/hentai":
                        sendText(m.getChatId().toString(), API.getPicFromSub(API, RandomHentai.get(new Random().nextInt(RandomHentai.size())))); //Опа! Вы нашли незадокументированную фичу!
                        break;
                    default:
                        if (message_text[0].contains("/get_")) {
                            sendText(m.getChatId().toString(), API.getPicFromSub(API, message_text[0].substring(5)));
                            break;
                        } else if (message_text[0].contains("/feedback")) {
                            String SubedMessege = message_text[0].substring(9);
                            if (SubedMessege.length() == 0) {
                                sendText(m.getChatId().toString(), "Для того, что-бы написать создателю бота, использутей форму \"/feedback привет, тут текст!\"");
                            } else {
                                sendTextFeedback(Main.prop.getProperty("BOT_FEEDBACKID"), "Сообщение от " + user.getUserName() + ":" + SubedMessege, m.getChatId().toString());
                            }
                            break;
                        } else {
                            sendText(m.getChatId().toString(), "Eaah? Я не поняла вас!");
                            break;
                        }
                }
                Thread.currentThread().stop();
            });

            CompletableFuture.runAsync(() -> {
                ThreadHandler.start();
                SendChatAction sendChatAction = new SendChatAction();
                sendChatAction.setChatId(m.getChatId());
                sendChatAction.setAction(ActionType.TYPING);

                for (int x = 0; x <= 30; x = x + 1) {
                    if (!ThreadHandler.isAlive()) {
                        Thread.currentThread().interrupt();
                        return;
                    }

                    try {
                        execute(sendChatAction);
                        Thread.sleep(1000);
                    } catch (TelegramApiException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                while (ThreadHandler.isAlive()) {
                    ThreadHandler.stop();
                    ThreadHandler.interrupt();
                    sendText(m.getChatId().toString(), "Ой! Сабреддит " + message_text[0].substring(5) + " какой-то не такой, я не могу получить от него данные! Просите ");
                    System.out.println("Thread " + Thread.currentThread().getId() + " has ended (timeout). Messege: " + message_text[0]);
                }
                Thread.currentThread().stop();
            });
        }
    }

    public String getBotUsername() {
        return this.BOT_USERNAME;
    }

    public String getBotToken() {
        return this.BOT_TOKEN;
    }

    private void sendTextFeedback(String id, String text, String confirmation) {
        SendMessage msg = (new SendMessage()).setChatId(id).setText(text).setParseMode("HTML");
        try {
            execute(msg);
            sendText(confirmation, "Готово! Я отрпавила ваше сообщение своему создателю!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendText(String id, String text) {
        SendMessage msg = (new SendMessage()).setChatId(id).setText(text).setParseMode("HTML");
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
