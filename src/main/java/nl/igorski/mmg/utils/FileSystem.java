/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Igor Zinken
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/
package nl.igorski.mmg.utils;

import java.io.File;

public final class FileSystem
{
    private static String RELATIVE_DIR = "";

    public static String getDirectoryNameFromString( String path )
    {
        final int lastIndex = path.lastIndexOf( File.separator );

        if ( lastIndex > -1 )
            return path.substring( 0, lastIndex );

        return RELATIVE_DIR;
    }

    public static String getFileNameFromString( String path )
    {
        final int lastIndex = path.lastIndexOf( File.separator );

        if ( lastIndex > -1 )
            return path.substring( lastIndex );

        return path;
    }

    public static boolean createDirectory( String directoryName )
    {
        File directory = new File( directoryName );

        // build directory structure if it didn't exist
        return directory.mkdirs();
    }

    public static File createFile( String fileName )
    {
        if ( !getDirectoryNameFromString( fileName ).equals( RELATIVE_DIR ))
        {
            final boolean createdDir = createDirectory( getDirectoryNameFromString( fileName ));

            if ( !createdDir )
                return null;
        }
        return new File( fileName );
    }
}
