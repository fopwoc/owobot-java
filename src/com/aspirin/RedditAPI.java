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
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;
import java.util.Random;

class RedditAPI {

    final private int pagesLimit = Paginator.DEFAULT_LIMIT;     //Лимит страниц. Хз нах это надо, не делайте много, будет долго листать.
    final private SubredditSort sort = SubredditSort.HOT;       //Тип сортировки постов на реддите.
    final private TimePeriod period = TimePeriod.WEEK;          //За какой период будет происхоть сортировка.
    final private int postLimit = 6;                            //Максимум постов который будет браться с страници.
    final private int retryMax = 3;                             //Максимальное число попыток перерандомить пост
    final private boolean enableHttpLog = false;                //true - показывать всю активность с реддитом, false - ничего не показывать

    private final RedditClient RedditAPI;
    final private TextResponce textResponce = new TextResponce();
    final private Preferences preferences = new Preferences();
    final private Random random = new Random();

    RedditAPI() {
        UserAgent userAgent = new UserAgent("bot", "com.aspirinswag.bot", "v1.1.1", "ASPIRINswag");
        OkHttpNetworkAdapter okHttpNetworkAdapter = new OkHttpNetworkAdapter(userAgent);
        Credentials credentials = Credentials.script(Main.prop.getProperty("Reddit_Username"), Main.prop.getProperty("Reddit_Password"), Main.prop.getProperty("Reddit_Clientid"), Main.prop.getProperty("Reddit_Clientsecret"));
        this.RedditAPI = OAuthHelper.automatic(okHttpNetworkAdapter, credentials);
        RedditAPI.setLogHttp(enableHttpLog);
    }

    public String getPicFromSub(RedditAPI api, String subreddit, Update update) {
        if (api.RedditAPI.searchSubredditsByName(subreddit).isEmpty())
            return textResponce.redditWrongResponse(update.getMessage(), subreddit);

        SubredditReference sub = api.RedditAPI.subreddit(subreddit);

        DefaultPaginator<Submission> build = sub.posts().sorting(sort).timePeriod(period).build();
        build.accumulate(random.nextInt(postLimit) + 1);

        int postIndex = (random.nextInt(pagesLimit - retryMax));

        Submission post;

        try {
            if (preferences.settingsNSFWGet(update.getMessage().getChatId()) == 0) {
                for (int i = 0; i < retryMax; i++) {
                    post = Objects.requireNonNull(build.getCurrent()).get(postIndex + i);
                    if (!post.isNsfw() && (post.getPreview() != null)) {
                        return textResponce.redditResponse(update.getMessage(), post);
                    }
                }

                post = Objects.requireNonNull(build.getCurrent()).get(postIndex);
                if (!post.isNsfw()) {
                    return textResponce.redditResponse(update.getMessage(), post);
                } else {
                    return textResponce.redditNSFWResponse(update.getMessage());
                }

            } else {
                for (int i = 0; i < retryMax; i++) {
                    post = Objects.requireNonNull(build.getCurrent()).get(postIndex + i);
                    if (post.getPreview() != null) {
                        return textResponce.redditResponse(update.getMessage(), post);
                    }
                }
            }
            post = Objects.requireNonNull(build.getCurrent()).get(postIndex);
            return textResponce.redditResponse(update.getMessage(), post);
        } catch (Exception e) {
            return textResponce.errorResponse(update.getMessage());
        }
    }
}