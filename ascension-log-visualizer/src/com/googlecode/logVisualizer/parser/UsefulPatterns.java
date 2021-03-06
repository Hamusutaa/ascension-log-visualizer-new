/* Copyright (c) 2008-2010, developers of the Ascension Log Visualizer
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.googlecode.logVisualizer.parser;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * This utility class holds various useful regex patterns, strings and static
 * methods which are used for parsing preparsed or normal mafia ascension logs.
 */
public final class UsefulPatterns {
    public static final Set<String> MUSCLE_SUBSTAT_NAMES = new HashSet<>(
            Arrays.asList("Beefiness", "Fortitude", "Muscleboundness",
                    "Strengthliness", "Strongness"));
    public static final Set<String> MYST_SUBSTAT_NAMES = new HashSet<>(
            Arrays.asList("Enchantedness", "Magicalness", "Mysteriousness",
                    "Wizardliness"));
    public static final Set<String> MOXIE_SUBSTAT_NAMES = new HashSet<>(
            Arrays.asList("Cheek", "Chutzpah", "Roguishness", "Sarcasm",
                    "Smarm"));
    public static final Set<String> MP_NAMES = new HashSet<>(Arrays.asList(
            "Muscularity Points", "Mana Points", "Mojo Points"));
    public static final Pattern NOT_A_NUMBER = Pattern.compile("\\D+");
    public static final Pattern ALL_BEFORE_COLON = Pattern.compile("^.*:\\s*");
    public static final Pattern NAME_COLON_NUMBER = Pattern
            .compile("^\\S.*:\\s+\\d+.*");
    public static final Pattern TURNS_USED = Pattern
            .compile("^\\[\\d+(?:\\-\\d+)?].+");
    public static final Pattern NOT_AREA_NAME = Pattern
            .compile("^\\[[\\d\\p{Punct}]+\\]\\s*|\\s+$|\\s*\\[[\\d\\p{Punct}]+\\]\\s*$");
    public static final Pattern NOT_TURNCOUNT_STRING = Pattern
            .compile("^\\[|\\][\\w\\p{Punct}\\s]+.*");
    public static final Pattern AREA_STATGAIN = Pattern
            .compile(".*\\[\\-?\\d+,\\-?\\d+,\\-?\\d+\\].*");
    public static final Pattern ITEM_FOUND = Pattern.compile("^\\s*\\+>.+");
    public static final Pattern CONSUMED = Pattern
            .compile("^\\s*o>\\s(?:Ate|Drank|Used).+");
    public static final Pattern FAMILIAR_CHANGED = Pattern
            .compile("^\\s*->\\sTurn.+");
    public static final Pattern PULL = Pattern
            .compile("^\\s*#>\\sTurn\\s\\[\\d+\\]\\spulled.+");
    public static final Pattern DAY_CHANGE = Pattern
            .compile("^=+Day\\s+(?:[2-9]|\\d\\d+).*");
    public static final Pattern SEMIRARE = Pattern
            .compile("^\\s*#>\\s\\[\\d+\\]\\sSemirare:\\s.+");
    public static final Pattern BADMOON = Pattern.compile("^\\s*%>.+");
    public static final Pattern HUNTED_COMBAT = Pattern
            .compile("^\\s*\\*>\\s\\[\\d+\\]\\sStarted\\shunting.*");
    public static final Pattern DISINTEGRATED_COMBAT = Pattern
            .compile("^\\s*\\}> \\[\\d+\\] Disintegrated .*");
    public static final Pattern FREE_RUNAWAYS_USAGE = Pattern
            .compile("^\\s*\\&> \\d+ \\\\ \\d+ free retreats.*");
    public static final Pattern CONSUMABLE_USED = Pattern
            .compile("(?:(?:use|eat|drink)|Buy and (?:eat|drink)) \\d+ .+");
    public static final Pattern GAIN = Pattern
            .compile("^You gain \\d*,?\\d+ [\\w\\s]+");
    public static final Pattern GAIN_LOSE_CAPTURE_PATTERN = Pattern
            .compile("^You (?:gain|lose) (\\d*,?\\d+) ([\\w\\s]+)");
    public static final Pattern GAIN_LOSE = Pattern
            .compile("^You (?:gain|lose) \\d*,?\\d+ [\\w\\s]+");
    public static final Pattern USUAL_FORMAT_LOG_NAME = Pattern
            .compile(".+\\-\\d{8}$");
    public static final String COMBAT_ROUND_LINE_BEGINNING_STRING = "Round ";
    public static final String COMMA = ",";
    public static final String MINUS = "-";
    public static final String COLON = ":";
    public static final String PERCENTAGE_SIGN = "%";
    public static final String SQUARE_BRACKET_OPEN = "[";
    public static final String SQUARE_BRACKET_CLOSE = "]";
    public static final String ROUND_BRACKET_OPEN = "(";
    public static final String ROUND_BRACKET_CLOSE = ")";
    public static final String WHITE_SPACE = " ";
    public static final String EMPTY_STRING = "";

    /**
     * Returns the creation date of the given mafia log file. Uses the method
     * {@link #getMafiaLogDate(String)} to parse out the creation date.
     *
     * @param mafiaLog
     *            The file name of the mafia log of which the creation date
     *            should be returned.
     * @return The creation date of the given mafia log.
     * @throws NullPointerException
     *             if mafiaLog is {@code null}
     */
    public static final Calendar getMafiaLogCalendarDate(final File mafiaLog) {
        return UsefulPatterns.getMafiaLogCalendarDate(mafiaLog.getName());
    }

    /**
     * Returns the creation date of the given mafia log file. Uses the method
     * {@link #getMafiaLogDate(String)} to parse out the creation date.
     *
     * @param mafiaLogFileName
     *            The file name of the mafia log of which the creation date
     *            should be returned.
     * @return The creation date of the given mafia log.
     * @throws NullPointerException
     *             if mafiaLogFileName is {@code null}
     */
    public static final Calendar getMafiaLogCalendarDate(
            final String mafiaLogFileName) {
        final Calendar logDate = Calendar.getInstance();
        final String ascensionDate = String.valueOf(UsefulPatterns
                .getLogDate(mafiaLogFileName));
        logDate.clear();
        logDate.set(Integer.parseInt(ascensionDate.substring(0, 4)),
                Integer.parseInt(ascensionDate.substring(4, 6)) - 1,
                Integer.parseInt(ascensionDate.substring(6)));
        return logDate;
    }

    /**
     * Parses the creation date out from the given mafia log file. This method
     * makes use of the mafia log file name format which always looks like
     * {@code USERNAME_YYYYMMDD.txt}, where Y is year, M is month and D is day.
     *
     * @param mafiaLog
     *            The file name of the mafia log of which the creation date
     *            should be returned.
     * @return The creation date of the given mafia log. The format of the
     *         returned integer is YYYYMMDD, where Y is year, M is month and D
     *         is day.
     * @throws NullPointerException
     *             if mafiaLog is {@code null}
     */
    public static final int getMafiaLogDate(final File mafiaLog) {
        return UsefulPatterns.getLogDate(mafiaLog.getName());
    }

    /**
     * Parses the creation date out from the given log name. This method makes
     * use of the often used log name format which looks like
     * {@code SOMETHINGYYYYMMDD.txt} or {@code SOMETHINGYYYYMMDD} (essentially
     * the date is at the end and not directly interlinked with a number
     * before/after it), where Y is year, M is month and D is day. Log names
     * that do not follow this format cannot be parsed by this method.
     *
     * @param logName
     *            The name of the log of which the creation date should be
     *            returned.
     * @return The creation date of the given log. The format of the returned
     *         integer is YYYYMMDD, where Y is year, M is month and D is day.
     * @throws NullPointerException
     *             if mafiaLogFileName is {@code null}
     */
    public static final int getLogDate(final String logName) {
        if (logName == null) {
            throw new NullPointerException("logName must not be null.");
        }
        int ascensionDate;
        try (final Scanner scanner = new Scanner(logName)) {
            scanner.useDelimiter(UsefulPatterns.NOT_A_NUMBER);
            // The last number in the file name is the date.
            do {
                ascensionDate = scanner.nextInt();
            } while (scanner.hasNextInt());
            scanner.close();
            return ascensionDate;
        }
    }

    // This class is not to be instanced.
    private UsefulPatterns() {
    }
}
