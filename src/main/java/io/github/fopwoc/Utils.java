package io.github.fopwoc;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {
    final private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd ");

    public String timeAndDate() {
        return ("[" + simpleDateFormat.format(new Date()) + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now()) + "] ");
    }
}
