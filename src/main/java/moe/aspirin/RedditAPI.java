package moe.aspirin;

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
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;
import java.util.Random;

class RedditAPI {

    final private int pageLimit = Integer.parseInt(Main.prop.getProperty("pageLimit"));
    final private SubredditSort sort = IdentifySubredditSort();
    final private TimePeriod period = IdentifyTimePeriod();
    final private int postLimit = Integer.parseInt(Main.prop.getProperty("postLimit"));
    final private int retryMax = Integer.parseInt(Main.prop.getProperty("retryMax"));

    private final RedditClient RedditAPI;
    final private TextResponce textResponce = new TextResponce();
    final private Preferences preferences = new Preferences();
    final private Random random = new Random();

    RedditAPI() {

        UserAgent userAgent = new UserAgent("bot", "com.aspirinswag.bot", "v1.1.1", "ASPIRINswag");
        Credentials oauthCreds = Credentials.script(Main.prop.getProperty("Reddit_Username"), Main.prop.getProperty("Reddit_Password"), Main.prop.getProperty("Reddit_Clientid"), Main.prop.getProperty("Reddit_Clientsecret"));
        this.RedditAPI = OAuthHelper.automatic(new OkHttpNetworkAdapter(userAgent), oauthCreds);
        RedditAPI.setLogHttp(Boolean.parseBoolean(Main.prop.getProperty("HttpLog")));
    }

    SubredditSort IdentifySubredditSort() {
        return switch (Main.prop.getProperty("sort")) {
            case "HOT" -> SubredditSort.HOT;
            case "BEST" -> SubredditSort.BEST;
            case "CONTROVERSIAL" -> SubredditSort.CONTROVERSIAL;
            case "NEW" -> SubredditSort.NEW;
            case "TOP" -> SubredditSort.TOP;
            case "RISING" -> SubredditSort.RISING;
            default -> throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("sort"));
        };
    }

    TimePeriod IdentifyTimePeriod() {
        return switch (Main.prop.getProperty("period")) {
            case "ALL" -> TimePeriod.ALL;
            case "YEAR" -> TimePeriod.YEAR;
            case "MONTH" -> TimePeriod.MONTH;
            case "WEEK" -> TimePeriod.WEEK;
            case "DAY" -> TimePeriod.DAY;
            case "HOUR" -> TimePeriod.HOUR;
            default -> throw new IllegalStateException("Unexpected value: " + Main.prop.getProperty("period"));
        };
    }

    public String getPicFromSub(RedditAPI api, String subreddit, Update update) {
        if (api.RedditAPI.searchSubredditsByName(subreddit).isEmpty())
            return textResponce.redditWrongResponse(update.getMessage(), subreddit);

        SubredditReference sub = api.RedditAPI.subreddit(subreddit);

        DefaultPaginator<Submission> build = sub.posts().sorting(sort).timePeriod(period).build();
        build.accumulate(random.nextInt(postLimit) + 1);

        int postIndex = (random.nextInt(pageLimit - retryMax));

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