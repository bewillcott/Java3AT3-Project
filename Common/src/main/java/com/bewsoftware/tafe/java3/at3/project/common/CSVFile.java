/*
 *  File Name:    CSVFile.java
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

import java.io.*;
import java.util.Iterator;
import java.util.Objects;

import static com.bewsoftware.tafe.java3.at3.project.common.CSVRow.parseArray;

/**
 * Reads, writes, and holds CSV data - into and out from an external file.
 *
 * @author <a href="mailto:bw.opensource@yahoo.com">Bradley Willcott</a>
 *
 * @since 1.0
 * @version 1.0
 */
public class CSVFile implements Iterable<CSVRow>
{
    private final String fileName;

    private CSVRow header;

    private final LinkedList<CSVRow> rows;

    /**
     * Create a new instance of the CSVFile class with the supplied file name.
     *
     * @param fileName name of CSV file
     */
    public CSVFile(String fileName)
    {
        this.fileName = fileName;
        this.rows = new LinkedList<>();
    }

    /**
     * Add a CSVRow to list.
     *
     * @param row to add
     *
     * @return {@code true} if successful
     */
    public boolean add(CSVRow row)
    {
        return rows.add(row);
    }

    /**
     * Get the header text as a CSV String.
     *
     * @return CSV String
     */
    public String getHeader()
    {
        return header != null ? "" + header : "";
    }

    /**
     * Set the header to the contents of the supplied array.
     *
     * @param array new header data
     */
    public void setHeader(String[] array)
    {
        if (Objects.requireNonNull(array).length > 0)
        {
            header = parseArray(array);
        }
    }

    /**
     * Returns {@code true} if a header exists.
     *
     * @return {@code true} if a header exists
     */
    public boolean hasHeader()
    {
        return header != null;
    }

    @Override
    public Iterator<CSVRow> iterator()
    {
        return rows.iterator();
    }

    /**
     * Read data from the CSV file.
     *
     * @param hasHeader Set to {@code true} if the first row of the CSV file
     *                  is a header.
     *
     * @return {@code true} if the read in is successful
     *
     * @throws IOException if any
     */
    public boolean readData(boolean hasHeader) throws IOException
    {
        boolean rtn = false;

        if (fileName != null && !fileName.isBlank())
        {
            File file = new File(fileName);

            if (file.exists())
            {
                try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
                {
                    String next;

                    while ((next = br.readLine()) != null)
                    {
                        CSVRow csvRow = CSVRow.parseCSV(next);

                        if (hasHeader)
                        {
                            header = csvRow;
                            hasHeader = false;
                        } else
                        {
                            rows.add(csvRow);
                        }
                    }

                    rtn = true;
                }
            }
        }

        return rtn;
    }

    /**
     * Writes the CSV data.
     *
     * @return {@code true} if successful.
     *
     * @throws java.io.IOException if any
     */
    public boolean writeData() throws IOException
    {
        boolean rtn = false;

        if (rows.size() > 0 && fileName != null && !fileName.isBlank())
        {
            try (FileWriter fw = new FileWriter(fileName))
            {
                fw.write(!(header == null || header.isEmpty()) ? header.toString() + "\n" : "");

                for (CSVRow row : rows)
                {
                    fw.write(row.toString() + "\n");
                }

                rtn = true;
            }
        }

        return rtn;
    }

}
