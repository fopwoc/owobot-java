/*                       _           _
                        | |         | |
       _____      _____ | |__   ___ | |_
      / _ \ \ /\ / / _ \| '_ \ / _ \| __|
     | (_) \ V  V / (_) | |_) | (_) | |_
      \___/ \_/\_/ \___/|_.__/ \___/ \__|
                                            v.3.0.3

    An anime pics bot for Telegram, written on java, taking data from reddit.

    link:  https://t.me/owopics_bot
    username: @owopics_bot
    author: ilya.dobryakov@icloud.com                                                                            */

package io.github.fopwoc;

import net.dean.jraw.RedditException;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.*;
import java.util.Properties;

public class Main {

    final static long StartTime = System.currentTimeMillis();
    final static String version = "3.0.3";
    static final Properties prop = new Properties();

    static int messagesGroupPerMin;

    public static void main(String[] args) {
        System.out.println("owobot! v" + version + " is starting!");

        try (InputStream input = new FileInputStream("owobot-java-config.properties")) {
            System.out.println("Reading config file");
            prop.load(input);

            //not the greatest solution, but i can't find any good practices for this case
            if (prop.getProperty("BOT_USERNAME") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("BOT_USERNAME"));
            if (prop.getProperty("BOT_FEEDBACKID") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("BOT_FEEDBACKID"));
            if (prop.getProperty("Reddit_Username") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("Reddit_Username"));
            if (prop.getProperty("Reddit_Password") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("Reddit_Password"));
            if (prop.getProperty("Reddit_Clientid") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("Reddit_Clientid"));
            if (prop.getProperty("postLimit") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("postLimit"));
            if (prop.getProperty("pageLimit") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("pageLimit"));
            if (prop.getProperty("sort") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("sort"));
            if (prop.getProperty("period") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("period"));
            if (prop.getProperty("retryMax") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("retryMax"));
            if (prop.getProperty("HttpLog") == null)
                throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("HttpLog"));

        } catch (IOException ex) {
            try (OutputStream output = new FileOutputStream("owobot-java-config.properties")) {
                System.out.println("Creating config file");
                prop.store(output, "owobot-java, an anime bot for telegram. Github: https://github.com/fopwoc/owobot-java");

                PrintWriter configFile = new PrintWriter(new BufferedWriter(new FileWriter("owobot-java-config.properties", true)));
                configFile.println("BOT_USERNAME=username_bot\n" +
                        "BOT_TOKEN=token\n" +
                        "BOT_FEEDBACKID=your id\n" +
                        "\n" +
                        "Reddit_Username=username\n" +
                        "Reddit_Password=password\n" +
                        "Reddit_Clientid=id\n" +
                        "Reddit_Clientsecret=secret\n" +
                        "\n" +
                        "#Proxy settings. Leave empty if you don't want to use it\n" +
                        "PROXY_HOST=\n" +
                        "PROXY_PORT=\n" +
                        "\n" +
                        "#Reddit settings\n" +
                        "#postLimit - limit the number of received posts for each page. 25 is default, 100 is max.\n" +
                        "postLimit=25\n" +
                        "#pageLimit - 6 is optimal, more will take more time to to get the job done.\n" +
                        "pageLimit=6\n" +
                        "#sort - Reddit sort type. HOT, BEST, CONTROVERSIAL, NEW, RISING, TOP\n" +
                        "sort=HOT\n" +
                        "#period - Reddit period of time. HOUR, DAY, WEEK, MONTH, YEAR, ALL\n" +
                        "period=WEEK\n" +
                        "#retryMax - number of attempts to get a post with a picture. 3 is ok.\n" +
                        "retryMax=3\n" +
                        "#HttpLog - debug function that shows all requests to reddit. true or false\n" +
                        "HttpLog=false");
                configFile.close();

                System.out.println("Please, setup config before next run!");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.out.println("Config looks ok");
        }

        System.out.println("Trying Reddit API");
        try {
            new RedditAPI();
        } catch (RedditException | NullPointerException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.out.println("Reddit is good");
        }

        DefaultBotOptions botOptions = new DefaultBotOptions();
        if (Main.prop.getProperty("PROXY_HOST") != null) {
            System.out.println("Forwarding telegram through proxy");

            HttpHost httpHost = new HttpHost(prop.getProperty("PROXY_HOST"), Integer.parseInt(prop.getProperty("PROXY_PORT")));

            RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).setAuthenticationEnabled(false).build();
            botOptions.setRequestConfig(requestConfig);
            botOptions.setProxyHost(String.valueOf(httpHost));
        }

        System.out.println("Starting bot");

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot(botOptions));
        } catch (Exception e) {
            System.out.println("Error while bot initialization");
            e.printStackTrace();
        } finally {
            System.out.println("Bot has successfully started");
        }

        //Simple but super stupid logic to prevent "Too many requests" error for group chats.
        //Btw Telegram limit is 20 messages for 1 minute for group chats and 30 messages in one second for private chats
        //Also here is no logic for private chats. It's probably unnecessary i think.
        while (true) {
            try {
                Thread.sleep(10); //Bug! somehow, without this line this loop dies immediately
                if (messagesGroupPerMin > 0) {
                    Thread.sleep(61000); //61 secs cuz 60 is buggy sometimes
                    messagesGroupPerMin = 0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
