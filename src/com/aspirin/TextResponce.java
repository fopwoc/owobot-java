package com.aspirin;

import net.dean.jraw.models.Submission;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


//Well... Why entire class of just text?! Cuz use DB or configs for this is too lazy for me.
//Enjoy structured text here or fork it
//This class is cursed AF
public class TextResponce {

    final private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss ");
    final private Preferences preferences = new Preferences();

    String startResponse(Message message) {
        String userMention = message.getFrom().getFirstName();
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = ("@" + message.getFrom().getUserName());

        return switch (preferences.settingsLanguageGet(message.getFrom().getUserName())) {
            case 0 -> ("Welcome, " + userMention + "!\n\n" +
                    "To get more information type /info\n" +
                    "For a quick start, just type /get\n\n" +
                    "Also click on the slash icon at the keyboard to see command list.");
            case 1 -> ("Добро пожаловать, " + userMention + "!\n\n" +
                    "Чтобы получить больше информации напишите /info\n" +
                    "Для быстрого начала, просто напишите /get\n\n" +
                    "Также нажмите на иконочку со слешем у клавиатуры чтобы узнать о командах.");
            default -> null;
        };
    }

    String infoResponse(Message message) {
        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = (" @" + message.getFrom().getUserName());

        return switch (preferences.settingsLanguageGet(message.getFrom().getUserName())) {
            case 0 -> ("Hellowo" + userMention + "! I'm owobot v" + Main.version + " - a bot that sends cute girls!\n\n" +
                    "I am written in Java, taking data from reddit, multi-threaded and fully compatible with group chats. Do not be afraid to send me 25-50 requests at a time, I can handle it!\n\n" +
                    "If you’re tired of reading and you want to see anime girls already, then you are here: /get\n" +
                    "By default, I will not send you NSFW content, however you can configure this here: /nsfw\n" +
                    "You can also change the language here: /language\n\n" +
                    "A few words about privacy - I save the settings for each user and chat, as well as the total number of requests.\n\n" +
                    "My github page: https://github.com/ASPIRINmoe/owobot-java\n\n" +
                    "I hope that I will be useful to you, master!! (☆ω☆)");
            case 1 -> ("Приветик" + userMention + "! Я owobot v" + Main.version + " - бот, который прислылает милых девочек!\n\n" +
                    "Я написана на Java, беру картинки с reddit, мультипоточна и полностью соовместима с групповыми чатами. Не бойтесь отправлять мне по 25-50 запросов за раз, я справлюсь с этим!\n\n" +
                    "Если надоело читать и хочется уже видеть аниме девочек, то вам сюда: /get\n" +
                    "По умолчанию я не буду присылать вам NSFW контент, однако Вы можете настроить это тут: /nsfw\n" +
                    "Также вы можете поменять язык здесь: /language\n\n" +
                    "Пару слов по поводу приватности - Я сохраняю настройки для каждого пользователя и чата, а также общее число запросов.\n\n" +
                    "Моя страничка на GitHub: https://github.com/ASPIRINmoe/owobot-java\n\n" +
                    "Я надеюсь, что буду полезной вам! (☆ω☆)");
            default -> null;
        };
    }

    String redditResponse(Message message, Submission post) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());
        String image;
        String nsfw;

        if (language == 0) image = ("picture");
        else image = ("картинка");
        if (!post.hasThumbnail()) if (language == 0) image = ("post without picture");
        else image = ("пост без картинки");

        if (language == 0) nsfw = ("no");
        else nsfw = ("нет");
        if (post.isNsfw()) if (language == 0) nsfw = ("yes");
        else nsfw = ("да");

        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = (" for user @" + message.getFrom().getUserName());
            else userMention = (" для юзера @" + message.getFrom().getUserName());

        return switch (language) {
            case 0 -> ("Pam! Here's your " + image + " from r/" + post.getSubreddit() + userMention + "\n" +
                    "Post title: " + post.getTitle() + "\n" +
                    "NSFW: " + nsfw + "\n" +
                    "Link to the original: " + post.getUrl() + "\n" +
                    "Post Link: https://reddit.com" + post.getPermalink());
            case 1 -> ("Пам! Вот вам " + image + " с r/" + post.getSubreddit() + userMention + "\n" +
                    "Название поста: " + post.getTitle() + "\n" +
                    "NSFW: " + nsfw + "\n" +
                    "Ссылка на оригинал: " + post.getUrl() + "\n" +
                    "Ссылка на пост: https://reddit.com" + post.getPermalink());
            default -> null;
        };
    }

    String redditNSFWResponse(Message message) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());

        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = (" for user @" + message.getFrom().getUserName());
            else userMention = (" для юзера @" + message.getFrom().getUserName());

        return switch (language) {
            case 0 -> ("Oh! Request " + message.getText().replace("@" + Main.prop.getProperty("BOT_USERNAME"), "") + userMention + " contains LEWDS!\n\n" +
                    "I was told not to post NSFW posts!\n" +
                    "I can not show you this picture, sorry (｡ŏ﹏ŏ)");
            case 1 -> ("Ох! Запрос " + message.getText().replace("@" + Main.prop.getProperty("BOT_USERNAME"), "") + userMention + " содержит ЛЕВДЫ!\n\n" +
                    "Мне было сказано не отправлять NSFW посты!\n" +
                    "Я не могу показать вам эту картинку, простите (｡ŏ﹏ŏ)");
            default -> null;
        };
    }

    String redditWrongResponse(Message message, String subreddit) {
        String userMention = "";
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = (" @" + message.getFrom().getUserName());

        return switch (preferences.settingsLanguageGet(message.getFrom().getUserName())) {
            case 0 -> ("OOPS! Perhaps you" + userMention + " were mistaken by a subreddit! I could not find the search for " + subreddit + " (´-﹏-`)");
            case 1 -> ("ОЙ! Возможно, Вы" + userMention + " ошиблись сабреддитом! Я не смогла найти найти по запросу " + subreddit + " (´-﹏-`)");
            default -> null;
        };
    }

    String statusResponse(Message message) {
        long uptime = System.currentTimeMillis() - Main.StartTime;
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        String nsfwStatus;
        if (preferences.settingsNSFWGet(message.getChatId()) == 1)
            if (language == 0) nsfwStatus = ("ON");
            else nsfwStatus = ("включен");
        else if (language == 0) nsfwStatus = ("OFF");
        else nsfwStatus = ("выключен");

        String userMention;
        if (language == 0) userMention = ("I'm alive!\n\n");
        else userMention = ("Я жива!\n\n");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = ("Yo, @" + message.getFrom().getUserName() + ", I'm alive!\n\n");
            else userMention = ("Ку, @" + message.getFrom().getUserName() + ", я жива!\n\n");

        return switch (language) {
            case 0 -> (userMention + "Uptime: " + (uptime / 1000 / 60 / 60 / 24) + " days " + dateFormat.format(new Date(uptime)) + ". " +
                    "Total requests: " + preferences.reqTimesGet() + "\n" +
                    "NSFW for this chat: " + nsfwStatus + "\n" +
                    "Bot version: v" + Main.version);
            case 1 -> (userMention + "Uptime: " + (uptime / 1000 / 60 / 60 / 24) + " дней " + dateFormat.format(new Date(uptime)) + ". " +
                    "Всего запросов: " + preferences.reqTimesGet() + "\n" +
                    "NSFW для этого чата: " + nsfwStatus + "\n" +
                    "Версия бота: v" + Main.version);
            default -> null;
        };
    }

    String nsfwResponse(Message message) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());

        String nsfwStatus;
        if (preferences.settingsNSFWGet(message.getChatId()) == 1)
            if (language == 0) nsfwStatus = ("ON");
            else nsfwStatus = ("включен");
        else if (language == 0) nsfwStatus = ("OFF");
        else nsfwStatus = ("выключен");

        String userMention;
        if (language == 0) userMention = ("Have you decided");
        else userMention = ("Вы решили");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = ("@" + message.getFrom().getUserName() + ", did you decided");
            else userMention = ("@" + message.getFrom().getUserName() + " решил(a)");

        return switch (language) {
            case 0 -> ("Oagh! " + userMention + " to change the NSFW content settings?\n\n" +
                    "Currently NSFW is " + nsfwStatus + ". Here is what you can do:\n" +
                    "/nsfw_on to turn on the lewds\n" +
                    "/nsfw_off to turn off all the erotic\n\n" +
                    "*No guarantee that all NSFW tags in reddit is correct!");
            case 1 -> ("Оа!" + userMention + " решил(a) поменять настройки NSFW контента?\n\n" +
                    "В данный момент NSFW " + nsfwStatus + ". Вот что Вы можете сделать:\n" +
                    "/nsfw_on чтоб включить левды\n" +
                    "/nsfw_off чтоб выключить всю похабщину\n\n" +
                    "*Не гарантирую, что все NSFW теги с реддита верны!");
            default -> null;
        };
    }

    String nsfwOnResponse(Message message) {
        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = (", @" + message.getFrom().getUserName());

        return switch (preferences.settingsLanguageGet(message.getFrom().getUserName())) {
            case 0 -> ("Got it" + userMention + "! Now the lewds are on in this chat! ( ͡° ͜ʖ ͡°)");
            case 1 -> ("Поняла вас" + userMention + "! Теперь в этом чате левды включены! ( ͡° ͜ʖ ͡°)");
            default -> null;
        };
    }

    String nsfwOffResponse(Message message) {
        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = (", @" + message.getFrom().getUserName());

        return switch (preferences.settingsLanguageGet(message.getFrom().getUserName())) {
            case 0 -> ("Understood" + userMention + "! No more vulgarity in this chat!");
            case 1 -> ("Поняла вас" + userMention + "! Больше никакой пошлятины в этом чате!");
            default -> null;
        };
    }

    String nsfwWrongResponse(Message message) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());

        String userMention;
        if (language == 0) userMention = ("You");
        else userMention = ("Вам");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = ("@" + message.getFrom().getUserName());

        return switch (language) {
            case 0 -> ("Ehmm... " + userMention + " should read carefully about the commands in /nsfw");
            case 1 -> ("Эм... " + userMention + " стоит внимательнее прочитать о командах в /nsfw");
            default -> null;
        };
    }

    String nsfwNotAdminResponse(Message message) {
        return switch (preferences.settingsLanguageGet(message.getFrom().getUserName())) {
            case 0 -> ("@" + message.getFrom().getUserName() + ", you must be admin in this group chat to have be able to change NSFW settings!");
            case 1 -> ("@" + message.getFrom().getUserName() + ", вы должны быть админом, чтобы иметь возможность менять настройки NSFW в этом чате!");
            default -> null;
        };
    }

    String languageResponse(Message message) {
        if (message.getFrom().getUserName() == null) //specific response if username is null
            return ("Dear " + message.getFrom().getFirstName() + ", looks like your nickname is hidden or something, so here is no option for change language, im so sorry :(\n\n" +
                    "Дорогой " + message.getFrom().getFirstName() + ", похоже, что ваш никнейм скрыт или что-то в этом роде, поэтому я не могу поменять язык для вас, извините :(");

        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = (" @" + message.getFrom().getUserName());

        return switch (preferences.settingsLanguageGet(message.getFrom().getUserName())) {
            case 0 -> ("Oa!" + userMention + " are you decided to change language?\n\n" +
                    "At this moment language is english. Here is what you can do:\n" +
                    "/language_eng to switch to english\n" +
                    "/language_rus чтобы переключится на русский");
            case 1 -> ("Oa!" + userMention + " Вы решили сменить язык?\n\n" +
                    "At this moment language is russian. Вот что Вы можете сделать:\n" +
                    "/language_eng to switch to english\n" +
                    "/language_rus чтобы переключится на русский");
            default -> null;
        };
    }

    String languageOnResponse(Message message) {
        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = (", @" + message.getFrom().getUserName());

        return ("Поняла вас" + userMention + "! Теперь я буду отвечать вам на русском языке!");
    }

    String languageOffResponse(Message message) {
        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = (", @" + message.getFrom().getUserName());

        return ("Understood" + userMention + "! From now, I'll answer in english language!");
    }

    String languageWrongResponse(Message message) {
        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = (" @" + message.getFrom().getUserName());

        return switch (preferences.settingsLanguageGet(message.getFrom().getUserName())) {
            case 0 -> ("Well.. Wrong language change" + userMention + ". Read /language carefully!");
            case 1 -> ("Ну... Неправильная смена языка" + userMention + ". Прочитайте /language внимательнее!");
            default -> null;
        };
    }

    String getResponse(Message message) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());

        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = ("Yo @" + message.getFrom().getUserName() + "! ");
            else userMention = ("Ку @" + message.getFrom().getUserName() + "! ");

        return switch (language) {
            case 0 -> (userMention + "Write to the bot /get_*subreddit tittle* and it will send you a random picture from there.\n" +
                    "Here is my little compilation:\n" +
                    "/get_awwnime - All kinds of moe art\n" +
                    "/get_wholesomeyuri - Yuri. Everyone loves yuri.\n" +
                    "/get_Joshi_Kosei - Cute high school students.\n" +
                    "/get_AnimeBlush - Shy girls.\n" +
                    "/get_tsunderes - Cute tsunderes!\n" +
                    "/get_Usagimimi - Bunny girls!\n" +
                    "/get_kitsunemimi - Cat girls!\n" +
                    "/get_animeponytails - Cute ponytail girls.\n" +
                    "/get_tyingherhairup - Anime girls tying their hair up.\n" +
                    "/get_OfficialSenpaiHeat - Compilation of pictures.\n" +
                    "/get_ChurchofBelly - Tummies!\n" +
                    "/get_Moescape - Cute arts with cute girls.\n" +
                    "/get_MoeStash - More cute girls!\n" +
                    "/get_TwoDeeArt - Just 2D Art.\n" +
                    "/get_2DArtchive - Archive of 2D art.\n" +
                    "/get_headpats - Girls stroking the head..\n" +
                    "/get_cutelittlefangs - Teeth and fangs and cute girls.\n" +
                    "/get_kemonomimi - Cute girls and animal ears!\n" +
                    "/get_twintails - Anime girls with twintails.\n" +
                    "/get_pouts - Cute girls with pouting cheeks.\n" +
                    "/get_gao - Girls make scary sounds.!\n" +
                    "/get_animelegs - Legs!\n" +
                    "/get_ZettaiRyouiki - Thighs owo!\n" +
                    "/get_thighdeology - More thighs, but more open!\n" +
                    "/get_Animewallpaper - Beautiful wallpapers with cute girls.\n" +
                    "/get_pantsu - Pantsu shots owo!\n" +
                    "/get_Patchuu - Abstract, surreal, or just beautiful girls!\n" +
                    "/get_animehotbeverages - Girls drink hot drinks.\n" +
                    "/get_AnimeLounging - Cute girls are resting.\n" +
                    "/get_silverhair - Girls with silver hair.\n" +
                    "/get_longhairedwaifus - Cuties with long hair.\n" +
                    "/get_shorthairedwaifus - Girls with short hairstyles.\n" +
                    "/get_Smugs - Smug girls.\n" +
                    "/get_Lain - W̺̹̆̒̊ͪ͌̒ͦe͂̈́ ̜͕̮͉̥̔ͪ̃͗ͣa͛ͤ̌ŕ̭̈́̎̈ͯ͋e̠̤̫̪͙ͩͨ ͉̘̱͔̥͊͂́̍ͦ́ẃ̰̼̰͌̇iͧ̀̉ͫr͖̗̙̫̓e̥̣̲̳ͨ̄͌̑d̲͉̭̩̟̙̝͆ͥ");
            case 1 -> (userMention + "Напиши боту /get_*название сабреддита*, и он отправит вам случайную картинку оттуда.\n" +
                    "Вот вам моя подборочка:\n" +
                    "/get_awwnime - Что-то милое.\n" +
                    "/get_wholesomeyuri - Юри. Все любят юри.\n" +
                    "/get_Joshi_Kosei - Милые старшеклассницы.\n" +
                    "/get_AnimeBlush - Стесняющиеся девочки.\n" +
                    "/get_tsunderes - Милые цундеры!\n" +
                    "/get_Usagimimi - Девочки кролики!\n" +
                    "/get_kitsunemimi - Кошкодевочки!\n" +
                    "/get_animeponytails - Милые девочки с конским хвостиком.\n" +
                    "/get_tyingherhairup - Девочки поправляют свои волосы.\n" +
                    "/get_OfficialSenpaiHeat - Сборник картиночек.\n" +
                    "/get_ChurchofBelly - Животики!\n" +
                    "/get_Moescape - Милые арты с милыми девочками.\n" +
                    "/get_MoeStash - Еще милые девочки!\n" +
                    "/get_TwoDeeArt - Просто 2д артики.\n" +
                    "/get_2DArtchive - Архив 2д артов.\n" +
                    "/get_headpats - Девочек гладят по голове.\n" +
                    "/get_cutelittlefangs - Зубки и клыки у милых девочек.\n" +
                    "/get_kemonomimi - Опять ушки!\n" +
                    "/get_twintails - Девочки с прическами из 2 хвостиков.\n" +
                    "/get_pouts - Милые девочки с надутыми щёчками.\n" +
                    "/get_gao - Девочки делают страшные звуки!\n" +
                    "/get_animelegs - Ножки!\n" +
                    "/get_ZettaiRyouiki - Бедра ово!\n" +
                    "/get_thighdeology - Еще бедра, но открытее!\n" +
                    "/get_Animewallpaper - Красивые обои с милыми девочками.\n" +
                    "/get_pantsu - Панцушоты owo!\n" +
                    "/get_Patchuu - Абстрактные, сюрреалистические или просто красивые девушки!\n" +
                    "/get_animehotbeverages - Девочки пьют горячие напитки.\n" +
                    "/get_AnimeLounging - Милые девочки отдыхают.\n" +
                    "/get_silverhair - Девочки с серебряными волосами.\n" +
                    "/get_longhairedwaifus - Милашки с длинными волосами.\n" +
                    "/get_shorthairedwaifus - Девочки с короткими прическами.\n" +
                    "/get_Smugs - Самодовольные девочки.\n" +
                    "/get_Lain - W̺̹̆̒̊ͪ͌̒ͦe͂̈́ ̜͕̮͉̥̔ͪ̃͗ͣa͛ͤ̌ŕ̭̈́̎̈ͯ͋e̠̤̫̪͙ͩͨ ͉̘̱͔̥͊͂́̍ͦ́ẃ̰̼̰͌̇iͧ̀̉ͫr͖̗̙̫̓e̥̣̲̳ͨ̄͌̑d̲͉̭̩̟̙̝͆ͥ");
            default -> null;
        };
    }

    String getWrongResponse(Message message) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());

        String userMention;
        if (language == 0) userMention = ("you forgot");
        else userMention = ("Вы забыли");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = ("@" + message.getFrom().getUserName() + " forgot");
            else userMention = ("@" + message.getFrom().getUserName() + " забыл");

        return switch (language) {
            case 0 -> ("It looks like " + userMention + " to clarify the subreddit! You should carefully read /get");
            case 1 -> ("Похоже, что " + userMention + " уточнить сабреддит! Вам стоит внимательнее изучить /get");
            default -> null;
        };
    }

    String feedbackResponse(Message message) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());

        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = ("Ку, @" + message.getFrom().getUserName() + "! ");
            else userMention = ("Yo, @" + message.getFrom().getUserName() + "! ");

        String groupTip = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0)
                groupTip = ("Do not forget to add this at the end: \"@" + Main.prop.getProperty("BOT_USERNAME") + "\"");
            else groupTip = ("Не забудьте в конце дописать: \"@" + Main.prop.getProperty("BOT_USERNAME") + "\"");

        return switch (language) {
            case 0 -> (userMention + "To send a message to the host of the bot, use the form \"/feedback Hello, here is the text!\"\n\n" + groupTip);
            case 1 -> (userMention + "Для того, чтобы отправить сообщение создателю бота, используйте форму \"/feedback привет, тут текст!\"\n\n" + groupTip);
            default -> null;
        };
    }

    String feedbackDoneResponse(Message message) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());

        String userMention;
        if (language == 0) userMention = ("your message");
        else userMention = ("ваше сообщение");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = ("message form @" + message.getFrom().getUserName());
            else userMention = ("сообщение от @" + message.getFrom().getUserName());

        return switch (language) {
            case 0 -> ("Done! I sent " + userMention + " to my host. Thanks you! (\\\\\\ω\\\\\\)");
            case 1 -> ("Готово! Я отправила " + userMention + " своему создателю. Спасибо вам (\\\\\\ω\\\\\\)");
            default -> null;
        };
    }

    String wrongResponse(Message message) {
        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            userMention = ("@" + message.getFrom().getUserName() + ", ");

        return switch (preferences.settingsLanguageGet(message.getFrom().getUserName())) {
            case 0 -> ("Eaah? " + userMention + "I don't understand you!");
            case 1 -> ("Эаа? " + userMention + "я не поняла вас!");
            default -> null;
        };
    }

    String errorResponse(Message message) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());

        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = ("from @" + message.getFrom().getUserName());
            else userMention = ("от @" + message.getFrom().getUserName());

        return switch (language) {
            case 0 -> ("Oops! Something wrong with " + message.getText() + " request " + userMention + "\nI'm so sorry (⇀ε↼‶)");
            case 1 -> ("Ой! Запрос " + message.getText() + userMention + " какой-то не такой, я не могу получить от него данные... \nПростите (⇀ε↼‶)");
            default -> null;
        };
    }

    String strangeErrorResponse(Message message) {
        int language = preferences.settingsLanguageGet(message.getFrom().getUserName());

        String userMention = ("");
        if (message.getChat().isGroupChat() || message.getChat().isSuperGroupChat())
            if (language == 0) userMention = ("from @" + message.getFrom().getUserName());
            else userMention = ("от @" + message.getFrom().getUserName());

        return switch (language) {
            case 0 -> ("Oops! Something completely broken with " + message.getText() + " request " + userMention + "\nI'm so sorry (⇀ε↼‶)");
            case 1 -> ("Ой! Запрос " + message.getText() + userMention + " какой-то не такой, я не могу получить от него данные... \nПростите (⇀ε↼‶)");
            default -> null;
        };
    }
}
