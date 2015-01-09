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
package nl.igorski.mmg.model;

import java.util.Vector;

public final class VOPattern
{
    public Vector<VONote> notes;        // all the notes within the pattern
    public int            patternNum;   // the number of this pattern within the total sequence
    public long           offset;       // the start offset of the pattern

    public VOPattern( Vector<VONote> notes, int patternNum, long offset )
    {
        this.notes      = notes;
        this.patternNum = patternNum;
        this.offset     = offset;
    }

    public boolean offsetConflictsWithPattern( long compareNote )
    {
        final long[] noteOffsets = getNoteOffsets();

        for ( final long noteOffset : noteOffsets )
        {
            long actualOffset = noteOffset - offset;

            if ( actualOffset == 0 )
            {
                if ( actualOffset == compareNote )
                    return  true;
            }
            else if ( compareNote % actualOffset == 0 ) {
                return true;
            }
        }
        return false;
    }

    public long[] getNoteOffsets()
    {
        long[] out = new long[ notes.size() ];

        for ( int i = 0, l = notes.size(); i < l; ++i )
            out[ i ] = notes.get( i ).offset;

        return out;
    }

    public long getRangeStartOffset()
    {
        if ( notes.size() > 0 )
            return notes.get( 0 ).offset;

        return 0;
    }

    public long getRangeEndOffset()
    {
        if ( notes.size() > 0 )
            return notes.lastElement().offset + notes.lastElement().duration;

        return 0;
    }

    public long getRangeLength()
    {
        return getRangeEndOffset() - getRangeStartOffset();
    }

    @Override
    public String toString()
    {
        String output = "";

        for ( final VONote note : notes )
            output += note.toString() + " ";

        return "[ VOPattern ] " + output;
    }
}
