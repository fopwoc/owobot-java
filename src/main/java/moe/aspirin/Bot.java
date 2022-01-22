package moe.aspirin;

import org.jetbrains.annotations.NotNull;
import org.telegram.abilitybots.api.sender.DefaultSender;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
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
        add("animeart");
        add("awwnime");
        add("wholesomeyuri");
        add("joshi_kosei");
        add("animeblush");
        add("tsunderes");
        add("kitsunemimi");
        add("animeponytails");
        add("tyingherhairup");
        add("officialsenpaiheat");
        add("churchofbelly");
        add("moescape");
        add("moestash");
        add("twodeeart");
        add("headpats");
        add("cutelittlefangs");
        add("kemonomimi");
        add("twintails");
        add("pouts");
        add("gao");
        add("animelegs");
        add("zettairyouiki");
        add("animewallpaper");
        add("pantsu");
        add("patchuu");
        add("animehotbeverages");
        add("silverhair");
        add("longhairedwaifus");
        add("shorthairedwaifus");
        add("smugs");
        add("manga");
    }};

    final private TextResponce textResponce = new TextResponce();
    final private Preferences preferences = new Preferences();
    final private Random random = new Random();
    final private Utils utils = new Utils();


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
                    SendChatAction sendChatAction = new SendChatAction();
                    sendChatAction.setChatId(update.getMessage().getChatId().toString());
                    sendChatAction.setAction(ActionType.TYPING);

                    execute(sendChatAction);

                    //Making latency between requests for "typing" in label. it's actually a random number lol.
                    Thread.sleep(random.nextInt(Thread.activeCount() * 1000) + 1000);
                } catch (Exception e) {
                    //e.printStackTrace();
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


    public boolean isAdmin(Update update) {
        GetChatAdministrators getChatAdministrators = new GetChatAdministrators();
        getChatAdministrators.setChatId(update.getMessage().getChatId().toString());

        MessageSender sender;
        sender = new DefaultSender(this);
        SilentSender silent = new SilentSender(sender);
        return silent.execute(getChatAdministrators).orElse(new ArrayList<>()).stream()
                .anyMatch(member -> member.getUser().getId().equals(update.getMessage().getFrom().getId()));
    }

    private void sendTextFeedback(@NotNull String text) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(creatorId());
            message.setText(text);

            execute(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Message messageData, String text) {
        if (messageData == null || text == null) {
            return;
        }
        try {

            //simple logic to prevent "Too many requests" error
            //see while loop in Main to understand this shit
            while (Main.messagesGroupPerMin > 19) {
                Thread.sleep(10000);
            }

            if (text.length() == 0) {
                if (messageData.isGroupMessage() || messageData.isSuperGroupMessage()) Main.messagesGroupPerMin++;
                SendMessage message = new SendMessage();
                message.setChatId(message.getChatId());
                message.setText(textResponce.strangeErrorResponse(messageData));

                execute(message);
            }

            if (messageData.isGroupMessage() || messageData.isSuperGroupMessage()) Main.messagesGroupPerMin++;
            SendMessage message = new SendMessage();
            message.setChatId(messageData.getChatId().toString());
            message.setText(text);

            execute(message);

            preferences.reqTimesAdd(1);
        } catch (Exception e) {
            System.out.println(utils.timeAndDate() + "Thread " + Thread.currentThread().getId() + " has got timeout. message: " + messageData.getText() + " reason: " + e.getCause().toString());
            e.printStackTrace();
        }
    }

    String getMessageText(Update update, String message_text) {
        if (update.getMessage().getChat().isGroupChat() || update.getMessage().getChat().isSuperGroupChat()) {
            if (message_text.contains("@" + Main.prop.getProperty("BOT_USERNAME"))) {
                System.out.println(utils.timeAndDate() + "Message \"" + message_text.replace("@" + Main.prop.getProperty("BOT_USERNAME"), "") + "\" has been received in group chat");
            }
        } else {
            System.out.println(utils.timeAndDate() + "Message \"" + message_text + "\" has been received");
        }

        if (message_text.contains("@" + Main.prop.getProperty("BOT_USERNAME"))) {
            message_text = message_text.replace("@" + Main.prop.getProperty("BOT_USERNAME"), "");
        }

        //shitcode, but it works somehow
        try {
            switch (message_text.toLowerCase()) {
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
                default:
                    if (message_text.contains("/get_")) {
                        if (message_text.substring(5).equals("")) {
                            return textResponce.getWrongResponse(update.getMessage());
                        } else {
                            return (new RedditAPI().getPicFromSub(new RedditAPI(), message_text.substring(5), update));
                        }
                    } else if (message_text.contains("/feedback")) {
                        String subbedMessage = message_text.substring(9);
                        if (subbedMessage.length() == 0) {
                            return textResponce.feedbackResponse(update.getMessage());
                        } else {
                            sendTextFeedback("Message from " + update.getMessage().getFrom().getUserName() + ":" + subbedMessage);
                            return textResponce.feedbackDoneResponse(update.getMessage());
                        }
                    } else if (message_text.contains("/nsfw_")) {
                        //not allowing not admins change NSFW in group chats
                        if (update.getMessage().isGroupMessage() || update.getMessage().isSuperGroupMessage())
                            if (!isAdmin(update)) return textResponce.nsfwNotAdminResponse(update.getMessage());

                        String subbedMessage = message_text.substring(6);
                        if (subbedMessage.equals("on")) {
                            preferences.settingsNSFWSet(update.getMessage().getChatId(), 1);
                            return textResponce.nsfwOnResponse(update.getMessage());
                        } else if (subbedMessage.equals("off")) {
                            preferences.settingsNSFWSet(update.getMessage().getChatId(), 0);
                            return textResponce.nsfwOffResponse(update.getMessage());
                        } else {
                            return textResponce.nsfwWrongResponse(update.getMessage());
                        }
                    } else if (message_text.contains("/language_")) {
                        String subbedMessage = message_text.substring(10);
                        if (subbedMessage.equals("rus")) {
                            preferences.settingsLanguageSet(update.getMessage().getFrom().getUserName(), 1);
                            return textResponce.languageOnResponse(update.getMessage());
                        } else if (subbedMessage.equals("eng")) {
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
            System.out.println(utils.timeAndDate() + "Thread " + Thread.currentThread().getId() + " has ended (execution error). message: " + message_text);
            e.printStackTrace();
            return textResponce.errorResponse(update.getMessage());
        }
    }

    //"debug" functions. Actually, it only can edit "reqTimes" variable
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
