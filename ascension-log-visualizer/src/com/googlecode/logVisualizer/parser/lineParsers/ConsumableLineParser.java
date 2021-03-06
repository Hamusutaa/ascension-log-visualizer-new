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
package com.googlecode.logVisualizer.parser.lineParsers;

import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.logVisualizer.logData.LogDataHolder;
import com.googlecode.logVisualizer.logData.Statgain;
import com.googlecode.logVisualizer.logData.consumables.Consumable;
import com.googlecode.logVisualizer.logData.turn.TurnInterval;
import com.googlecode.logVisualizer.parser.UsefulPatterns;
import com.googlecode.logVisualizer.util.DataTablesHandler;

/**
 * A parser for the consumption notation in preparsed ascension logs.
 * <p>
 * The format can look like this, but there are several variations:
 * <p>
 * {@code o> Ate _numberOfConsumables_ _nameOfConsumable_ (_adventureGain_
 * adventures gained) [_mus_,_myst_,_mox_]}
 */
public final class ConsumableLineParser extends AbstractLineParser {
    private static final Pattern NOT_CONSUMABLE_NAME = Pattern
            .compile("^\\s*o>\\s*\\w+\\s+\\d*\\s*|(?:\\s*\\(.*\\))?\\s*(?:\\[[\\-?\\d,]+\\])?$");
    private static final Pattern STATS_PATTERN = Pattern
            .compile(".+\\[\\-?\\d+,\\-?\\d+,\\-?\\d+\\]\\s*$");
    private static final Pattern ADVENTURE_GAIN_CAPTURE_PATTERN = Pattern
            .compile(".+\\((\\d+) adventures gained\\).*\\s*$");
    private static final String ADVENTURE_GAINED_STRING = "adventures gained";
    private static final String ATE_STRING = "Ate";
    private static final String DRANK_STRING = "Drank";
    private final Matcher consumedMatcher = UsefulPatterns.CONSUMED
            .matcher(UsefulPatterns.EMPTY_STRING);
    private final Matcher consumableStatsMatcher = ConsumableLineParser.STATS_PATTERN
            .matcher(UsefulPatterns.EMPTY_STRING);

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doParsing(final String line, final LogDataHolder logData) {
        final TurnInterval lastInterval = logData.getTurnsSpent().last();
        final String name;
        final int consumablesUsed;
        final int consumedOnTurn;
        int adventureGain = 0;
        final Statgain consumableStats = new Statgain();
        // Consumable name
        try (Scanner scanner = new Scanner(line)) {
            scanner.useDelimiter(ConsumableLineParser.NOT_CONSUMABLE_NAME);
            name = scanner.next();
            scanner.close();
            // Amount used
            try (Scanner scanner2 = new Scanner(line)) {
                scanner2.useDelimiter(UsefulPatterns.NOT_A_NUMBER);
                consumablesUsed = scanner2.nextInt();
                scanner2.close();
            }
        }
        // Adventure gain
        if (line.contains(ConsumableLineParser.ADVENTURE_GAINED_STRING)) {
            try (final Scanner s = new Scanner(line)) {
                s.findInLine(ConsumableLineParser.ADVENTURE_GAIN_CAPTURE_PATTERN);
                final MatchResult result = s.match();
                s.close();
                adventureGain = Integer.parseInt(result.group(1));
            }
        }
        // Stat gain
        if (this.consumableStatsMatcher.reset(line).matches()) {
            final String statGains = line.substring(
                    line.lastIndexOf(UsefulPatterns.SQUARE_BRACKET_OPEN) + 1,
                    line.lastIndexOf(UsefulPatterns.SQUARE_BRACKET_CLOSE));
            final int muscle = Integer.parseInt(statGains.substring(0,
                    statGains.indexOf(UsefulPatterns.COMMA)));
            final int myst = Integer.parseInt(statGains.substring(
                    statGains.indexOf(UsefulPatterns.COMMA) + 1,
                    statGains.lastIndexOf(UsefulPatterns.COMMA)));
            final int moxie = Integer.parseInt(statGains.substring(statGains
                    .lastIndexOf(UsefulPatterns.COMMA) + 1));
            consumableStats.setMuscle(muscle);
            consumableStats.setMyst(myst);
            consumableStats.setMoxie(moxie);
        }
        // Turn number (only eyeballed because no specific is given)
        consumedOnTurn = lastInterval.getStartTurn()
                + (lastInterval.getTotalTurns() / 2);
        final Consumable consumable;
        if (line.contains(ConsumableLineParser.ATE_STRING)) {
            consumable = Consumable.newFoodConsumable(name, adventureGain,
                    consumablesUsed, consumedOnTurn);
        } else if (line.contains(ConsumableLineParser.DRANK_STRING)) {
            consumable = Consumable.newBoozeConsumable(name, adventureGain,
                    consumablesUsed, consumedOnTurn);
        } else if ((DataTablesHandler.getSpleenHit(name) > 0)
                && (adventureGain > 0)) {
            consumable = Consumable.newSpleenConsumable(name, adventureGain,
                    consumablesUsed, consumedOnTurn);
        } else {
            consumable = Consumable.newOtherConsumable(name, adventureGain,
                    consumablesUsed, consumedOnTurn);
        }
        consumable.setDayNumberOfUsage(logData.getLastDayChange()
                .getDayNumber());
        consumable.setStatGain(consumableStats);
        // Add the consumable
        lastInterval.addConsumableUsed(consumable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCompatibleLine(final String line) {
        return this.consumedMatcher.reset(line).matches();
    }
}
