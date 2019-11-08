package inovus.task.mimimimetr.util;

import inovus.task.mimimimetr.model.Contender;

import java.util.List;

public class CookiesUtil {

    public static String updateCookies(String cookie, List<Contender> contenders) {
        StringBuilder builder = new StringBuilder(cookie);
        for (Contender c : contenders) {
            builder.append(c.getId()).append(".");
        }

        return String.valueOf(builder);
    }

}
