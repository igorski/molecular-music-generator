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

/**
 * basic string enum for all properties that
 * can be defined in the input JSON file
 */
public final class JSONProperties
{
    public static String TEMPO              = "tempo";            // floating point value
    public static String NOTE_LENGTHS       = "lengths";          // Array of two floating point values
    public static String PATTERN_LENGTH     = "patternLength";    // integer value
    public static String AMOUNT_OF_PATTERNS = "amountOfPatterns"; // integer value
    public static String TRACK_PER_PATTERN  = "trackPerPattern";  // optional, boolean
    public static String MUSICAL_SCALE      = "scale";            // optional, Array of Strings
    public static String TIME_SIGNATURE     = "timeSignature";    // optional, Array of two integers
    public static String MIN_OCTAVE         = "minOctave";        // optional, integer value
    public static String MAX_OCTAVE         = "maxOctave";        // optional, integer value
    public static String OUTPUT_FILENAME    = "outputFile";       // optional, String value
}
