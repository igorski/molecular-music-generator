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
package nl.igorski.mmg.config;

import java.util.Vector;

public final class Config
{
    // read / overridden from input JSON file

    public static String INPUT_FILENAME            = null;
    public static String OUTPUT_FILENAME           = "output.mid";

    public static float TEMPO                      = 120f;
    public static int TIME_SIGNATURE_BEAT_AMOUNT   = 4;
    public static int TIME_SIGNATURE_BEAT_UNIT     = 4;

    public static float NOTE1_LENGTH               = 4f;
    public static float NOTE2_LENGTH               = 3f;
    public static int   PATTERN_LENGTH_IN_BARS     = 4;
    public static int   AMOUNT_OF_PATTERNS         = 4;
    public static int   MIN_OCTAVE                 = 2;
    public static int   MAX_OCTAVE                 = 3;
    public static String[] SCALE                   = { "E", "F", "G", "A", "B", "C", "D" }; // C major starting on E

    public static boolean UNIQUE_TRACK_PER_PATTERN = false;

    // used internally

    public static Vector<Double> pitches = new Vector<Double>();
}
