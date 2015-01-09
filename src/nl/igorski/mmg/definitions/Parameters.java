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
package nl.igorski.mmg.definitions;

import nl.igorski.mmg.config.Config;
import nl.igorski.mmg.utils.Output;

public final class Parameters
{
    public static String INPUT_FILENAME  = "-i";
    public static String OUTPUT_FILENAME = "-o";

    public static void showSupportedParameters()
    {
        Output.print( "" );
        Output.print( "No arguments provided. Supported parameters are:" );
        Output.print( "------------------------------------------------" );
        Output.print( "" );

        Output.print( INPUT_FILENAME  + " [STRING] the path to the input .json file" );
        Output.print( OUTPUT_FILENAME + " [STRING] the filename of the created MIDI output, optional, defaults to: " + Config.OUTPUT_FILENAME );
    }
}
