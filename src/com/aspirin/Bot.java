package com.aspirin;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Bot extends TelegramLongPollingBot {

    final private ArrayList<String> RandomAnimeGirls = new ArrayList<>() {{
        add("awwnime");
        add("wholesomeyuri");
        add("Joshi_Kosei");
        add("AnimeBlush");
        add("tsunderes");
        add("Usagimimi");
        add("kitsunemimi");
        add("animeponytails");
        add("tyingherhairup");
        add("OfficialSenpaiHeat");
        add("ChurchofBelly");
        add("Moescape");
        add("MoeStash");
        add("TwoDeeArt");
        add("2DArtchive");
        add("headpats");
        add("cutelittlefangs");
        add("kemonomimi");
        add("twintails");
        add("pouts");
        add("gao");
        add("animelegs");
        add("ZettaiRyouiki");
        add("thighdeology");
        add("Animewallpaper");
        add("pantsu");
        add("Patchuu");
        add("animehotbeverages");
        add("silverhair");
        add("longhairedwaifus");
        add("shorthairedwaifus");
        add("Smugs");
    }};

    //woo! Undocumented feature! You should check "getMessageText" func carefully *wink*
    final private ArrayList<String> RandomHentai = new ArrayList<>() {{
        add("ecchi");
        add("hentai");
        add("Tentai");
        add("MonsterGirl");
        add("HENTAI_GIF");
        add("animemidriff");
        add("AnimeBooty");
        add("hentaifemdom");
        add("dekaihentai");
        add("rule34");
        add("thick_hentai");
        add("hentaibondage");
        add("HumanCumflation");
        add("BigAnimeTiddies");
        add("HentaiPetgirls");
        add("LoweredPantyhose");
        add("skindentation");
        add("doujinshi");
        add("CumHentai");
        add("waifusgonewild");
        add("UpskirtHentai");
        add("CedehsHentai");
        add("Artistic_Hentai");
        add("HentaiSchoolGirls");
        add("HentaiAnal");
        add("yuri");
        add("muchihentai");
        add("AnimeMILFS");
        add("Nekomimi");
        add("KuroiHada");
    }};

    final private SendChatAction sendChatAction = new SendChatAction().setAction(ActionType.TYPING);
    final private TextResponce textResponce = new TextResponce();
    final private Preferences preferences = new Preferences();
    final private Random random = new Random();
    final private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd ");

    Bot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        new Thread(() -> {
            if (!update.hasMessage() || !update.getMessage().hasText()) return;

            if ((update.getMessage().getChat().isGroupChat() || update.getMessage().getChat().isSuperGroupChat())
                    && !update.getMessage().getText().contains("@" + Main.prop.getProperty("BOT_USERNAME"))) return;

            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                    sendMessage(update.getMessage(), getMessageText(update, update.getMessage().getText())));

            while (!future.isDone()) {
                try {
                    execute(sendChatAction.setChatId(update.getMessage().getChatId()));

                    //Making latency between requests for "typing" in label. it's actually a random number lol.
                    Thread.sleep(random.nextInt(Thread.activeCount() * 50) + 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public String getBotUsername() {
        return Main.prop.getProperty("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return Main.prop.getProperty("BOT_TOKEN");
    }

    public String creatorId() {
        return Main.prop.getProperty("BOT_FEEDBACKID");
    }

    public String timeAndDate() {
        return ("[" + simpleDateFormat.format(new Date()) + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()) + "] ");
    }

    private void sendTextFeedback(@NotNull String text) {
        try {
            execute(new SendMessage().setChatId(creatorId()).setText(text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Message message, String text) {
        if (message == null || text == null) {
            return;
        }
        try {

            //simple logic to prevent "Too many requests" error
            //see while loop in Main to understand this shit
            while (Main.messagesGroupPerMin > 19) {
                Thread.sleep(10000);
            }

            if (text.length() == 0) {
                if (message.isGroupMessage() || message.isSuperGroupMessage()) Main.messagesGroupPerMin++;
                execute(new SendMessage().setChatId(message.getChatId().toString()).setText(textResponce.strangeErrorResponse(message)));
            }

            if (message.isGroupMessage() || message.isSuperGroupMessage()) Main.messagesGroupPerMin++;
            execute(new SendMessage().setChatId(message.getChatId().toString()).setText(text));
            preferences.reqTimesAdd(1);
        } catch (Exception e) {
            System.out.println(timeAndDate() + "Thread " + Thread.currentThread().getId() + " has got timeout. message: " + message.getText() + " reason: " + e.getCause().toString());
            e.printStackTrace();
        }
    }

    String getMessageText(Update update, String message_text) {
        if (update.getMessage().getChat().isGroupChat() || update.getMessage().getChat().isSuperGroupChat()) {
            if (message_text.contains("@" + Main.prop.getProperty("BOT_USERNAME"))) {
                System.out.println(timeAndDate() + "Message \"" + message_text.replace("@" + Main.prop.getProperty("BOT_USERNAME"), "") + "\" has been received from user " + update.getMessage().getFrom().getUserName() + " in group chat");
            }
        } else {
            System.out.println(timeAndDate() + "Message \"" + message_text + "\" has been received from user " + update.getMessage().getFrom().getUserName());
        }

        if (message_text.contains("@" + Main.prop.getProperty("BOT_USERNAME"))) {
            message_text = message_text.replace("@" + Main.prop.getProperty("BOT_USERNAME"), "");
        }

        //shitcode, but it works somehow
        try {
            switch (message_text) {
                case "owo":
                    return ("uwu");
                case "uwu":
                    return ("owo");
                case "/start":
                    return textResponce.startResponse(update.getMessage());
                case "/info":
                    return textResponce.infoResponse(update.getMessage());
                case "/status":
                    return textResponce.statusResponse(update.getMessage());
                case "/nsfw":
                    return textResponce.nsfwResponse(update.getMessage());
                case "/language":
                    return textResponce.languageResponse(update.getMessage());
                case "/get":
                    return textResponce.getResponse(update.getMessage());
                case "/random":
                    message_text = RandomAnimeGirls.get(random.nextInt(RandomAnimeGirls.size()));
                    return (new RedditAPI().getPicFromSub(new RedditAPI(), message_text, update));
                case "/hentai":
                    //Hello you, who was looking at this code just for finding this feature! Good luck owo
                    message_text = RandomHentai.get(random.nextInt(RandomHentai.size()));
                    return (new RedditAPI().getPicFromSub(new RedditAPI(), message_text, update));
                default:
                    if (message_text.contains("/get_")) {
                        if (message_text.substring(5).equals("")) {
                            return textResponce.getWrongResponse(update.getMessage());
                        } else {
                            return (new RedditAPI().getPicFromSub(new RedditAPI(), message_text.substring(5), update));
                        }
                    } else if (message_text.contains("/feedback")) {
                        String SubedMessage = message_text.substring(9);
                        if (SubedMessage.length() == 0) {
                            return textResponce.feedbackResponse(update.getMessage());
                        } else {
                            sendTextFeedback("Message from " + update.getMessage().getFrom().getUserName() + ":" + SubedMessage);
                            return textResponce.feedbackDoneResponse(update.getMessage());
                        }
                    } else if (message_text.contains("/nsfw_")) {
                        String SubedMessage = message_text.substring(6);
                        if (SubedMessage.equals("on")) {
                            System.out.println(timeAndDate() + "NSFW changed to ON by user " + update.getMessage().getFrom().getUserName());
                            preferences.settingsNSFWSet(update.getMessage().getChatId(), 1);
                            return textResponce.nsfwOnResponse(update.getMessage());
                        } else if (SubedMessage.equals("off")) {
                            System.out.println(timeAndDate() + "NSFW changed to OFF by user " + update.getMessage().getFrom().getUserName());
                            preferences.settingsNSFWSet(update.getMessage().getChatId(), 0);
                            return textResponce.nsfwOffResponse(update.getMessage());
                        } else {
                            return textResponce.nsfwWrongResponse(update.getMessage());
                        }
                    } else if (message_text.contains("/language_")) {
                        String SubedMessage = message_text.substring(10);
                        if (SubedMessage.equals("rus")) {
                            System.out.println(timeAndDate() + "language changed to RUS by user " + update.getMessage().getFrom().getUserName());
                            preferences.settingsLanguageSet(update.getMessage().getFrom().getUserName(), 1);
                            return textResponce.languageOnResponse(update.getMessage());
                        } else if (SubedMessage.equals("eng")) {
                            System.out.println(timeAndDate() + "language changed to ENG by user " + update.getMessage().getFrom().getUserName());
                            preferences.settingsLanguageSet(update.getMessage().getFrom().getUserName(), 0);
                            return textResponce.languageOffResponse(update.getMessage());
                        } else {
                            return textResponce.languageWrongResponse(update.getMessage());
                        }
                    } else if (message_text.contains("/debug")) {
                        return debug(update.getMessage(), message_text);
                    } else {
                        return textResponce.wrongResponse(update.getMessage());
                    }
            }
        } catch (Exception e) {
            System.out.println(timeAndDate() + "Thread " + Thread.currentThread().getId() + " has ended (execution error). message: " + message_text);
            e.printStackTrace();
            return textResponce.errorResponse(update.getMessage());
        }
    }

    //bullshit "debug" functions. Actually, it only can edit "reqTimes" variable
    private String debug(Message message, String message_text) {
        if (message.getFrom().getId() == Integer.parseInt(creatorId())) {
            if (message_text.contains("_")) {
                if (message_text.replace("/debug_", "").contains("addrequest")) {
                    preferences.reqTimesAdd(Long.parseLong(message_text.replace("/debug_addrequest ", "")));
                    return ("Added to requests " + message_text.replace("/debug_addrequest ", ""));
                } else if (message_text.replace("/debug_", "").contains("setrequest")) {
                    preferences.reqTimesGet(Long.parseLong(message_text.replace("/debug_setrequest ", "")));
                    return ("Set requests to " + message_text.replace("/debug_setrequest ", ""));
                } else return null;
            } else return ("Yo! \n\n/debug_addrequest\n/debug_setrequest");
        } else {
            return null;
        }
    }
}
