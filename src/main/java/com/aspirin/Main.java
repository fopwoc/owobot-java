/*                       _           _
                        | |         | |
       _____      _____ | |__   ___ | |_
      / _ \ \ /\ / / _ \| '_ \ / _ \| __|
     | (_) \ V  V / (_) | |_) | (_) | |_
      \___/ \_/\_/ \___/|_.__/ \___/ \__|
                                            v.1.2

    An anime pics bot for Telegram, written on java, taking data from reddit.
    Бот для телеграмма, присылающий аниме девочек, написанный на java, берущий данные из reddit.

    Ссылка на бота:  https://t.me/owopics_bot
    Или его юзернейм: @owopics_bot
    Автор: @realASPIRIN                                                                                */

package com.aspirin;

import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.Paginator;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.*;
import java.util.Properties;
import java.util.prefs.Preferences;

public class Main {

    final static long StartTime = System.currentTimeMillis();

    final private static int PagesLimit = 25;                           //Лимит страниц. Хз нах это надо, не делайте много, будет долго листать.
    final private static SubredditSort sort = SubredditSort.HOT;        //Тип сортировки постов на реддите.
    final private static TimePeriod period = TimePeriod.WEEK;           //За какой период будет происхоть сортировка.
    final private static Integer postlimit = Paginator.DEFAULT_LIMIT;   //Максимум постов который будет браться с страници.
    final private static boolean EnableHttpLog = false;                 //true - показывать всю активность с реддитом, false - ничего не показывать

    static Properties prop = new Properties();

    public static void main(String[] args) throws FileNotFoundException {

        try (InputStream input = new FileInputStream("config.properties")) {
            System.out.println("Reading config file");
            prop.load(input);
        } catch (IOException ex) {
            try (OutputStream output = new FileOutputStream("owobot-java-config.properties")) {
                System.out.println("Creating config file");

                prop.store(output, "owobot-java, an anime bot for telegram. Github: https://github.com/ASPIRINswag/owobot-java");

                //все данные здесь - рандомно сгенерированные строрки, просто для примера.
                prop.setProperty("BOT_USERNAME", "botname_bot");
                prop.setProperty("BOT_TOKEN", "4805053672:example7r7m8au23ydjorptcwxj");
                prop.setProperty("BOT_FEEDBACKID", "2808022488");

                prop.setProperty("Reddit_Username", "username");
                prop.setProperty("Reddit_Password", "password");
                prop.setProperty("Reddit_Clientid", "example67q08um4n");
                prop.setProperty("Reddit_Clientsecret", "example14si92e9fllqeoxdf0");

                System.out.println("Please, setup config before next run!");
                System.exit(0);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        try {
            readPreference("UsedTimes");
        } catch (Exception e) {
            savePreference("UsedTimes", 0);
        }

        try {
            readPreference("UsedTimes");
        } catch (Exception e) {
            savePreference("UsedTimes", 0);
        }

        new RedditAPI(PagesLimit, sort, period, postlimit, EnableHttpLog);

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        System.out.println("Starting bot");
        Bot bot = new Bot();

        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            System.out.println("Error while bot initialization");
            e.printStackTrace();
        } finally {

            System.out.println("Bot has successfully started");
        }
    }


    static void savePreference(String key, Integer value) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        prefs.put(key, String.valueOf(value));
    }

    static Integer readPreference(String key) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return Integer.valueOf(prefs.get(key, "default"));
    }
}