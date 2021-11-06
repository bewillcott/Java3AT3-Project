/*
 *  File Name:    CSVRow.java
 *  Project Name: Common
 *
 *  Copyright (c) 2021 Bradley Willcott
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * ****************************************************************
 * Name: Bradley Willcott
 * ID:   M198449
 * Date: 8 Oct 2021
 * ****************************************************************
 */

package com.bewsoftware.tafe.java3.at3.project.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Contains data relevant to a single CSV row.
 * <p>
 * The information is specific to a single sorting algorithm, and the
 * data collected over multiple test runs.
 * <p>
 * The original idea for this class came from reading this
 * article/blog:<br/>
 * <a href="https://www.codeproject.com/articles/415732/reading-and-writing-csv-files-in-csharp">
 * reading-and-writing-csv-files-in-csharp</a>
 */
public final class CSVRow extends ArrayList<String> implements Comparable<CSVRow>
{
    private static final long serialVersionUID = -6189427601900989870L;

    /**
     * Parses an array of strings.
     *
     * @param data array to parse
     *
     * @return a new CSVRow populated with information from the csvString.
     */
    public static CSVRow parse(String... data)
    {
        CSVRow rtn = null;

        if (Objects.requireNonNull(data).length > 0)
        {
            rtn = new CSVRow();
            rtn.addAll(Arrays.asList(data));
        }

        return rtn;
    }

    /**
     * Parses an array of strings.
     *
     * @param data array to parse
     *
     * @return a new CSVRow populated with information from the csvString.
     */
    public static CSVRow parseArray(String[] data)
    {
        CSVRow rtn = null;

        if (Objects.requireNonNull(data).length > 0)
        {
            rtn = new CSVRow();
            rtn.addAll(Arrays.asList(data));
        }

        return rtn;
    }

    /**
     * Parses a CSV string.
     *
     * @param csvString the string to parse
     *
     * @return a new CSVRow populated with information from the csvString.
     */
    public static CSVRow parseCSV(String csvString)
    {
        CSVRow rtn = null;

        if (csvString != null && !csvString.isBlank())
        {
            String[] data = csvString.split(",");

            if (data.length > 0)
            {
                rtn = new CSVRow();
                rtn.addAll(Arrays.asList(data));
            }
        }

        return rtn;
    }

    /**
     * Initialization done by static
     * {@link #parseCSV(java.lang.String) parseCSV} method.
     */
    private CSVRow()
    {
        // NoOp
    }

    @Override
    public int compareTo(CSVRow o)
    {
        return this.equals(o) ? 0 : this.hashCode() - o.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return Objects.requireNonNull(o).getClass() == getClass() && super.equals(o);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        boolean first = true;

        for (String item : this)
        {
            if (first)
            {
                first = false;
                sb.append(item);
            } else
            {
                sb.append(",").append(item);
            }
        }

        return sb.toString();
    }
}
