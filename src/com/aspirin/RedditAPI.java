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
import net.dean.jraw.pagination.Paginator;
import net.dean.jraw.references.SubredditReference;

import java.util.Objects;
import java.util.Random;

class RedditAPI {

    private RedditClient RedditAPI;

    private int limit;

    RedditAPI() {
        System.out.println("Initializing Reddit API");

        UserAgent userAgent = new UserAgent("bot", "com.aspirinswag.bot", "v1.0", "ASPIRINswag");
        Credentials credentials = Credentials.script(Main.prop.getProperty("Reddit_Username"), Main.prop.getProperty("Reddit_Password"), Main.prop.getProperty("Reddit_Clientid"), Main.prop.getProperty("Reddit_Clientsecret"));
        OkHttpNetworkAdapter okHttpNetworkAdapter = new OkHttpNetworkAdapter(userAgent);
        this.RedditAPI = OAuthHelper.automatic(okHttpNetworkAdapter, credentials);
        RedditAPI.setLogHttp(Main.EnableHttpLog);

        System.out.println("Reddit API has been succesfully initialized");
    }

    RedditAPI(int limit) {
        this.limit = limit;
        UserAgent userAgent = new UserAgent("bot", "com.aspirinswag.bot", "v1.0", "ASPIRINswag");
        Credentials credentials = Credentials.script(Main.prop.getProperty("Reddit_Username"), Main.prop.getProperty("Reddit_Password"), Main.prop.getProperty("Reddit_Clientid"), Main.prop.getProperty("Reddit_Clientsecret"));
        OkHttpNetworkAdapter okHttpNetworkAdapter = new OkHttpNetworkAdapter(userAgent);
        this.RedditAPI = OAuthHelper.automatic(okHttpNetworkAdapter, credentials);
        RedditAPI.setLogHttp(Main.EnableHttpLog);
    }

    String getPicFromSub(RedditAPI API, String subreddit) {
        if (API.RedditAPI.searchSubredditsByName(subreddit).isEmpty())
            return "Возможно, вы ошиблись сабреддитом! Я не смогла найти найти по запросу " + subreddit;

        SubredditReference sub = API.RedditAPI.subreddit(subreddit);

        DefaultPaginator<Submission> build = sub.posts().sorting(SubredditSort.HOT).timePeriod(TimePeriod.ALL).build();

        build.accumulate((new Random()).nextInt(limit) + 1);

        int postIndex = (new Random()).nextInt(Paginator.DEFAULT_LIMIT) + 1;
        Submission post = Objects.requireNonNull(build.getCurrent()).get(postIndex);

        while (!post.hasThumbnail()) {
            postIndex = (new Random()).nextInt(Paginator.DEFAULT_LIMIT);
            post = build.getCurrent().get(postIndex);
        }

        return "Пам! Вот вам картинка с r/" + sub.getSubreddit() + "\n" + "Название поста: " + post.getTitle() + "\n" +
                "NSFW: " + post.isNsfw() + "\n" +
                "Ссылка на оригинал: " + post.getUrl() + "\n" +
                "Ссылка на пост: https://reddit.com" + post.getPermalink() + "\n";
    }
}