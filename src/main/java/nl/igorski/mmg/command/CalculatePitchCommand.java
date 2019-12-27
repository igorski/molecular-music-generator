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
package nl.igorski.mmg.command;

import nl.igorski.mmg.model.Properties;
import nl.igorski.mmg.definitions.Pitch;

/**
 * CalculatePitchCommand will take the given musical scale and
 * create all pitches (in Hz) for the scale within the given octave range
 */
public final class CalculatePitchCommand
{
    public static void execute( Properties props )
    {
        final String[] SCALE = props.SCALE;

        int noteIndex = 0, maxIndex = SCALE.length - 1, octave = props.MIN_OCTAVE;

        for ( int i = noteIndex, l = SCALE.length; i < l; ++i )
        {
            props.pitches.add( Pitch.note( SCALE[ i ], octave ));

            // reached end of the note list ? increment octave

            if ( i == maxIndex &&
                 octave < props.MAX_OCTAVE )
            {
                i = -1; // restart note generation for next octave
                ++octave;
            }
        }
    }
}
