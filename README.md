# owobot-java
[![Build Status](https://travis-ci.com/ASPIRINswag/owobot-java.svg?branch=master)](https://travis-ci.com/ASPIRINswag/owobot-java)

An anime pics bot for Telegram, written on java, taking data from reddit.   
Бот для телеграмма, присылающий аниме девочек, написанный на java, берущий данные из reddit.

Bot link:  [@owopics_bot](https://t.me/owopics_bot)

### Features
* Multithreading
* Works great in group chats
* Preverting timeout errors
* Chat related NSFW settings
* User related language settings (english and russian)
* Can answer on `owo` and `uwu`

### How to use
1. Go to [@owopics_bot](https://t.me/owopics_bot) and just use the bot!

Or if for some reason you want to run it by yourself:

1. Clone code somewhere
2. Install Java 14 and run `mvn package` or use Docker
3. After first run it will create empty config file
4. Fill the config
5. Run again and enjoy

### Reason of choosing some questionable solutions
1. I'm java newbie
2. it was written with keeping in mind idea about implementation all features just with java, without anything outside like DB or something **BUT except** Tor proxy on host machine, cuz I'm russian and Telegram API is banned here. 

Just add this line to /etc/tor/torrc on your host machine
```
HTTPTunnelPort 127.0.0.1:9030
```
Or delete everything about proxy from code ¯\\_(ツ)_/¯

### TODO
- [ ] Rewrite in haskell someday
