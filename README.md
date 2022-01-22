# owobot-java

An anime pics bot for Telegram, written on java, taking data from reddit.   
Бот для Telegram, присылающий аниме девочек, написанный на Java, берущий данные с Reddit.

Bot link:  [@owopics_bot](https://t.me/owopics_bot)

### Features
* Multithreading
* Works great in group chats
* Preventing timeout errors
* Chat related NSFW settings
* User related language settings (english and russian)
* Can answer on `owo` and `uwu`

### How to use
1. Go to [@owopics_bot](https://t.me/owopics_bot) and just use the bot!

If for some reason you want to run it by yourself:

1. Clone code somewhere
2. Install Java 14 and run `./gradlew shadowJar` or use Docker
3. After first run it will create empty config file
4. Fill the config
5. Run again and enjoy

Or if you want to use Docker:

```shell
docker run -d \
   -v "path/to/config:/owobot-java-config.properties" \
   -v "owobot-cache:/root/.java" \
   aspirinmoe/owobot-java
```

### Reason of choosing some questionable solutions

1. I'm java newbie
2. it was written with keeping in mind idea about implementation all features just with java, without anything outside
   like DB or something.
3. I'm stupid AF

### TODO
1. Never ever in my life to write code like I did it here
2. Completely rework the project from scratch

