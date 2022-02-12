package it.multicoredev.aio.utils;

import it.multicoredev.aio.storage.config.Localization;
import it.multicoredev.mbcore.spigot.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright © 2021 - 2022 by Lorenzo Magni
 * This file is part of AIO.
 * AIO is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class Utils {
    private static final Pattern URL_PATTERN = Pattern.compile("((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)");

    public static boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Chat.severe("&4" + e.getMessage());
        }
    }

    public static String capitalize(String str, boolean toLowerCase, boolean replaceUnderscores) {
        if (toLowerCase) str = str.toLowerCase(Locale.ROOT);
        if (replaceUnderscores && str.contains("_")) str = str.replace("_", " ");

        if (!str.contains(" ")) {
            String c = str.substring(0, 1).toUpperCase();
            return c + str.substring(1);
        }

        String[] words = str.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String c = word.substring(0, 1).toUpperCase();
            builder.append(c).append(word.substring(1));
            if (i < words.length - 1) builder.append(" ");
        }

        return builder.toString();
    }

    public static String capitalize(String str) {
        return capitalize(str, false, true);
    }

    public static Long parseTime(String time) {
        String[] split = time.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        long number;
        String timeUnit;

        if (split.length < 2) {
            return null;
        }

        if (!"yYMdDhHmMsS".contains(split[1])) {
            return null;
        }

        try {
            number = Integer.parseInt(split[0]);
        } catch (NumberFormatException e) {
            return null;
        }

        timeUnit = split[1];

        switch (timeUnit) {
            case "s":
            case "S":
                return number * 20;
            case "m":
                return TimeUnit.SECONDS.convert(number, TimeUnit.MINUTES);
            case "h":
            case "H":
                return TimeUnit.SECONDS.convert(number, TimeUnit.HOURS);
            case "d":
            case "D":
                return TimeUnit.SECONDS.convert(number, TimeUnit.DAYS);
            case "M":
                return TimeUnit.SECONDS.convert(number * 30, TimeUnit.DAYS);
            case "y":
            case "Y":
                return TimeUnit.SECONDS.convert(number * 365, TimeUnit.DAYS);
            default:
                return null;
        }
    }

    public static String formatDelay(long seconds, Localization localization) {
        long days = TimeUnit.DAYS.convert(seconds, TimeUnit.SECONDS);
        seconds -= TimeUnit.SECONDS.convert(days, TimeUnit.DAYS);

        long hours = TimeUnit.HOURS.convert(seconds, TimeUnit.SECONDS);
        seconds -= TimeUnit.SECONDS.convert(hours, TimeUnit.HOURS);

        long minutes = TimeUnit.MINUTES.convert(seconds, TimeUnit.SECONDS);
        seconds -= TimeUnit.SECONDS.convert(minutes, TimeUnit.MINUTES);

        long years = 0;
        if (days >= 365) {
            years = days / 356;
            days -= years * 365;
        }

        long months = 0;
        if (days >= 30) {
            months = days / 30;
            days -= months * 30;
        }

        return (years > 0 ? years + " " + (years > 1 ? localization.years : localization.year) + " " : "") +
                (months > 0 ? months + " " + (months > 1 ? localization.months : localization.month) + " " : "") +
                (days > 0 ? days + " " + (days > 1 ? localization.days : localization.day) + " " : "") +
                (hours > 0 ? hours + " " + (hours > 1 ? localization.hours : localization.hour) + " " : "") +
                (minutes > 0 ? minutes + " " + (minutes > 1 ? localization.minutes : localization.minute) + " " : "") +
                (seconds > 0 ? seconds + " " + (seconds > 1 ? localization.seconds : localization.second) + " " : "");
    }

    public static boolean isValidURL(String url) {
        if (url == null || url.trim().isEmpty()) return false;
        Matcher m = URL_PATTERN.matcher(url);
        return m.matches();
    }

    public static Location getRandomLocation(Location center, int minDistance, int maxDistance) {
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            double xDistance = random.nextDouble() * (maxDistance - minDistance) + minDistance;
            double zDistance = random.nextDouble() * (maxDistance - minDistance) + minDistance;

            Location newLocation = center.clone().add(xDistance, 0, zDistance);
            if (!setY(newLocation)) continue;

            if (isSafeLocation(newLocation)) return newLocation;
        }

        return null;
    }

    private static boolean setY(Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        World.Environment environment = world.getEnvironment();
        if (environment == World.Environment.NORMAL || environment == World.Environment.THE_END || environment == World.Environment.CUSTOM) {
            location.setY(world.getHighestBlockYAt(location) + 1);
            return true;
        } else if (environment == World.Environment.NETHER) {
            List<Integer> validY = new ArrayList<>();
            for (int i = 0; i < 128; i++) validY.add(i);
            Collections.shuffle(validY);

            for (int y : validY) {
                Location loc0 = location.clone();
                loc0.setY(y);
                Location loc1 = loc0.clone().add(0, 1, 0);
                Location loc2 = loc0.clone().add(0, 2, 0);

                if (loc0.getBlock().getType().isSolid() && loc1.getBlock().getType().isAir() && loc2.getBlock().getType().isAir()) {
                    location.setY(y);
                    return true;
                }
            }

            return false;
        }

        return false;
    }

    public static boolean isSafeLocation(Location location) {
        Material mat0 = location.clone().add(0, -1, 0).getBlock().getType();
        Material mat1 = location.getBlock().getType();
        Material mat2 = location.clone().add(0, 1, 0).getBlock().getType();
        Material mat3 = location.clone().add(0, -2, 0).getBlock().getType();

        if (!mat0.isSolid() || (mat0.hasGravity() && mat3.isAir())) return false;
        if (!mat1.isAir() || !mat2.isAir()) return false;

        return !mat0.equals(Material.MAGMA_BLOCK) && !mat0.equals(Material.CACTUS);
    }
}
