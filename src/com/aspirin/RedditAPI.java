package com.aspirin;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubredditReference;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;
import java.util.Random;

class RedditAPI {

    private static int limit;
    private static SubredditSort sort;
    private static TimePeriod period;
    private static Integer postlimit;
    private static boolean EnableHttpLog;
    private RedditClient RedditAPI;

    RedditAPI() {
        UserAgent userAgent = new UserAgent("bot", "com.aspirinswag.bot", "v1.1", "ASPIRINswag");
        Credentials credentials = Credentials.script(Main.prop.getProperty("Reddit_Username"), Main.prop.getProperty("Reddit_Password"), Main.prop.getProperty("Reddit_Clientid"), Main.prop.getProperty("Reddit_Clientsecret"));
        OkHttpNetworkAdapter okHttpNetworkAdapter = new OkHttpNetworkAdapter(userAgent);
        this.RedditAPI = OAuthHelper.automatic(okHttpNetworkAdapter, credentials);
        RedditAPI.setLogHttp(EnableHttpLog);
    }

    RedditAPI(int limit, SubredditSort sort, TimePeriod period, Integer postlimit, boolean EnableHttpLog) {
        System.out.println("Initializing Reddit API");
        com.aspirin.RedditAPI.limit = limit;
        com.aspirin.RedditAPI.sort = sort;
        com.aspirin.RedditAPI.period = period;
        com.aspirin.RedditAPI.postlimit = postlimit;
        com.aspirin.RedditAPI.EnableHttpLog = EnableHttpLog;

        UserAgent userAgent = new UserAgent("bot", "com.aspirinswag.bot", "v1.1", "ASPIRINswag");
        Credentials credentials = Credentials.script(Main.prop.getProperty("Reddit_Username"), Main.prop.getProperty("Reddit_Password"), Main.prop.getProperty("Reddit_Clientid"), Main.prop.getProperty("Reddit_Clientsecret"));
        OkHttpNetworkAdapter okHttpNetworkAdapter = new OkHttpNetworkAdapter(userAgent);
        this.RedditAPI = OAuthHelper.automatic(okHttpNetworkAdapter, credentials);
        RedditAPI.setLogHttp(EnableHttpLog);

        System.out.println("Reddit API has been succesfully initialized");
    }

    String getPicFromSub(RedditAPI API, String subreddit, Message message) {
        if (API.RedditAPI.searchSubredditsByName(subreddit).isEmpty())
            return "ОЙ! Возможно, вы ошиблись сабреддитом! Я не смогла найти найти по запросу " + subreddit + " (´-﹏-`)";

        SubredditReference sub = API.RedditAPI.subreddit(subreddit);

        DefaultPaginator<Submission> build = sub.posts().sorting(sort).timePeriod(period).build();
        build.accumulate(new Random().nextInt(limit) + 1);

        int postIndex = new Random().nextInt(postlimit);
        Submission post = Objects.requireNonNull(build.getCurrent()).get(postIndex);

        if (message.isGroupMessage() || message.isSuperGroupMessage()) if (post.isNsfw())
            return "Ой! Запрос " + message.getText().replace("@" + Main.prop.getProperty("BOT_USERNAME"), "") + " от юзера " + message.getFrom().getUserName() + " содержит ЛЕВДЫ!\n\nМне нельзя отправлять NSFW кратинки в групповые чаты!\nЯ не могу показать вам эту картинку, простите (｡ŏ﹏ŏ)";

        return "Пам! Вот вам картинка с r/" + sub.getSubreddit() + "\n" + "Название поста: " + post.getTitle() + "\n" + "NSFW: " + post.isNsfw() + "\n" + "Ссылка на оригинал: " + post.getUrl() + "\n" + "Ссылка на пост: https://reddit.com" + post.getPermalink();
    }
}