/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2019 Igor Zinken
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
package nl.igorski.mmg.model;

import java.util.Vector;

public final class Properties
{
    public String OUTPUT_FOLDER             = "";
    public String OUTPUT_FILENAME           = "output.mid";

    public float TEMPO                      = 120f;
    public int TIME_SIGNATURE_BEAT_AMOUNT   = 4;
    public int TIME_SIGNATURE_BEAT_UNIT     = 4;

    public float NOTE1_LENGTH               = 4f;
    public float NOTE2_LENGTH               = 3f;
    public int   PATTERN_LENGTH_IN_BARS     = 4;
    public int   AMOUNT_OF_PATTERNS         = 4;
    public int   MIN_OCTAVE                 = 2;
    public int   MAX_OCTAVE                 = 3;
    public String[] SCALE                   = { "E", "F", "G", "A", "B", "C", "D" }; // C major starting on E

    public boolean UNIQUE_TRACK_PER_PATTERN = false;

    // used internally

    public Vector<Double> pitches = new Vector<Double>();

    public String valueOf() {
        return "output folder: " + OUTPUT_FOLDER + "/" + OUTPUT_FILENAME + "\n" +
               TEMPO + " bpm in " + TIME_SIGNATURE_BEAT_AMOUNT + "/" + TIME_SIGNATURE_BEAT_UNIT + " time \n" +
               "with a 1st note length of " + NOTE1_LENGTH + " and a 2nd note length of " + NOTE2_LENGTH + "\n" +
               "for " + AMOUNT_OF_PATTERNS + " patterns with a length of " + PATTERN_LENGTH_IN_BARS + " bars each\n" +
               "and an octave range of " + MIN_OCTAVE + " to " + MAX_OCTAVE + " for the following notes: " +
               String.join( ", ", SCALE ) + "\n" +
               "rendering a unique track for each pattern " + UNIQUE_TRACK_PER_PATTERN;
    }
}
