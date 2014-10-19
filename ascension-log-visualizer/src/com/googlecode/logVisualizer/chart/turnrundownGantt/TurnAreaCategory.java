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
package com.googlecode.logVisualizer.chart.turnrundownGantt;

import java.util.ArrayList;
import java.util.List;

public final class TurnAreaCategory {
    private final String categoryName;
    private final List<String> turnAreaNames = new ArrayList<>();

    public TurnAreaCategory(final String categoryName) {
        this.categoryName = categoryName;
    }

    public TurnAreaCategory(final String categoryName, final String areaName) {
        this(categoryName);
        this.turnAreaNames.add(areaName);
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void addTurnAreaName(final String name) {
        this.turnAreaNames.add(name);
    }

    public List<String> getTurnAreaNames() {
        return this.turnAreaNames;
    }

    /**
     * @return The category name of this TurnAreaCategory.
     */
    @Override
    public String toString() {
        return this.categoryName;
    }
}