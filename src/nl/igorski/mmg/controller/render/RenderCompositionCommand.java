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
package nl.igorski.mmg.controller.render;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;
import com.leff.midi.event.meta.TrackName;
import nl.igorski.mmg.config.Config;
import nl.igorski.mmg.definitions.MIDI;
import nl.igorski.mmg.definitions.Pitch;
import nl.igorski.mmg.model.VONote;
import nl.igorski.mmg.model.VOPattern;
import nl.igorski.mmg.utils.FileSystem;
import nl.igorski.mmg.utils.Output;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public final class RenderCompositionCommand
{
    public static boolean execute()
    {
        // calculate some constants
        final float theTempo      = Config.TEMPO; // in BPM
        final int channel         = 0;

        // create the MIDI track for the tempo and time signature

        MidiTrack tempoTrack = new MidiTrack();

        // track 0 is typically the tempo map, set tempo and time signature
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature( Config.TIME_SIGNATURE_BEAT_AMOUNT, Config.TIME_SIGNATURE_BEAT_UNIT,
                             TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION );

        Tempo t = new Tempo();
        t.setBpm( theTempo );

        tempoTrack.insertEvent( ts );
        tempoTrack.insertEvent( t );

        // add all the audio events
        final int velocity   = 100;
        final int resolution = MidiFile.DEFAULT_RESOLUTION;

        // it's best not to manually insert EndOfTrack events; MidiTrack will
        // call closeTrack() on itself before writing itself to a file
        // create a MidiFile with the tracks we created

        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add( tempoTrack );

        // --- COMPOSITION

        MidiTrack noteTrack = createTrack( "melody" );
        tracks.add( noteTrack );

        long currentPosition   = 0l;
        long currentBarLength  = 0l;
        float noteLength       = Config.NOTE1_LENGTH;

        Vector<VOPattern> patterns = new Vector<VOPattern>();
        Vector<VONote> notes       = new Vector<VONote>();

        // create patterns from all available pitches

        VOPattern currentPattern = new VOPattern( notes, 0, currentPosition );

        for ( int i = 0, l = Config.pitches.size(); i < l; ++i )
        {
            final double pitch = Config.pitches.get( i );

            // swap note length if conflicts with previously added note in other pattern

            if ( offsetConflictsWithPattern( currentPosition - currentPattern.offset, patterns ))
            {
                // swap note length
                if ( noteLength == Config.NOTE1_LENGTH )
                    noteLength = Config.NOTE2_LENGTH;
                else
                    noteLength = Config.NOTE1_LENGTH;
            }

            // create new note

            final VONote note = new VONote( Pitch.frequencyToMIDINote( pitch ),
                                            currentPosition,
                                          ( long )( noteLength * MIDI.QUARTER_NOTE ));

            // add note to Vector (so it can be re-added in next iterations)

            notes.add( note );

            // update current sequence position

            currentPosition  += note.duration;
            currentBarLength += note.duration;

            // pattern switch ? make it so (this starts the interleaving of the notes and thus, the magic!)

            if (( currentBarLength / MIDI.WHOLE_NOTE ) >= Config.PATTERN_LENGTH_IN_BARS )
            {
                patterns.add( currentPattern );

                // store current notes in new pattern
                notes          = new Vector<VONote>();
                currentPattern = new VOPattern( notes, patterns.size(), currentPosition );

                currentBarLength = 0l;
            }

            // break the loop when we've rendered the desired amount of patterns

            if ( patterns.size() >= Config.AMOUNT_OF_PATTERNS )
                break;

            // if we have reached the end of the pitch range, start again
            // from the beginning until we have rendered all the patterns

            if ( i == ( l - 1 ))
            {
                Collections.reverse( Config.pitches ); // go down the scale
                i = -1;
            }
        }

        final float totalLength = currentPosition;

        // loop all patterns to fit song length, add their notes to the MIDITrack

        for ( final VOPattern pattern : patterns )
        {
            float patternLength = 0f;

            while ( patternLength < ( totalLength - pattern.offset ))
            {
                for ( final VONote note : pattern.notes )
                {
                    noteTrack.insertNote( channel, note.note, velocity,
                                        ( long ) ( note.offset + patternLength ), note.duration );
                }
                patternLength += pattern.getRangeLength();
            }

            // create new track for pattern, if specified

            if ( Config.UNIQUE_TRACK_PER_PATTERN )
            {
                noteTrack = createTrack( "melody" );
                tracks.add( noteTrack );
            }
        }

        // --- write the MIDI data to a file

        MidiFile midi     = new MidiFile( resolution, tracks );
        final File output = FileSystem.createFile( Config.OUTPUT_FILENAME );

        try
        {
            midi.writeToFile( output );
            return true;
        }
        catch ( Exception e )
        {
            Output.print( "error occurred while creating MIDI file:" + e.getMessage() );
            return false;
        }
    }

    private static boolean offsetConflictsWithPattern( long noteOffset, Vector<VOPattern> patterns )
    {
        for ( final VOPattern pattern : patterns )
        {
            if ( pattern.notes.size() > 0 &&
                 pattern.offsetConflictsWithPattern( noteOffset ))
            {
                return true;
            }
        }
        return false;
    }

    private static MidiTrack createTrack( String trackName )
    {
        MidiTrack output = new MidiTrack();
        output.insertEvent( new TrackName( 0, 0, trackName ));

        return output;
    }
}
