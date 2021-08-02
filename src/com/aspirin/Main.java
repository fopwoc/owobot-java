/*                       _           _
                        | |         | |
       _____      _____ | |__   ___ | |_
      / _ \ \ /\ / / _ \| '_ \ / _ \| __|
     | (_) \ V  V / (_) | |_) | (_) | |_
      \___/ \_/\_/ \___/|_.__/ \___/ \__|
                                            v.2.0

    An anime pics bot for Telegram, written on java, taking data from reddit.
    Бот для телеграмма, присылающий аниме девочек, написанный на java, берущий данные из reddit.

    botlink:  https://t.me/owopics_bot
    its username: @owopics_bot
    author: @realASPIRIN                                                                                */

package com.aspirin;

import net.dean.jraw.RedditException;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import java.io.*;
import java.util.Properties;

public class Main {

    final static long StartTime = System.currentTimeMillis();
    static final Properties prop = new Properties();

    static int messagesGroupPerMin;

    public static void main(String[] args) {
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
                prop.store(output, "owobot-java, an anime bot for telegram. Github: https://github.com/ASPIRINswag/owobot-java");

                PrintWriter configFile = new PrintWriter(new BufferedWriter(new FileWriter("owobot-java-config.properties", true)));
                configFile.println("BOT_USERNAME=name_bot\n" +
                        "BOT_TOKEN=token\n" +
                        "BOT_FEEDBACKID=id\n" +
                        "\n" +
                        "Reddit_Username=username\n" +
                        "Reddit_Password=password\n" +
                        "Reddit_Clientid=id");
                configFile.close();

                System.out.println("Please, setup config before next run!");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        } finally {
            System.out.println("Config looks ok");
        }

        System.out.println("Trying Reddit API");
        try {
            new RedditAPI();
        } catch (RedditException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            System.out.println("Reddit is good");
        }

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        String PROXY_HOST = "localhost";
        int PROXY_PORT = 9030;

        HttpHost httpHost = new HttpHost(PROXY_HOST, PROXY_PORT);

        RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).setAuthenticationEnabled(false).build();
        botOptions.setRequestConfig(requestConfig);
        botOptions.setProxyHost(String.valueOf(httpHost));

        System.out.println("Starting bot");
        Bot bot = new Bot(botOptions);

        try {
            botsApi.registerBot(bot);
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
