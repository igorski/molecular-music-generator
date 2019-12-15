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
package nl.igorski.mmg;

import nl.igorski.mmg.config.Config;
import nl.igorski.mmg.controller.render.RenderCompositionCommand;
import nl.igorski.mmg.controller.startup.CalculatePitchCommand;
import nl.igorski.mmg.controller.config.ReadConfigurationCommand;
import nl.igorski.mmg.controller.startup.StartupCommand;
import nl.igorski.mmg.utils.Output;

public class Main
{
    public static void main( String[] args )
    {
        // process command line arguments

        if ( StartupCommand.execute( args ))
        {
            // read the input JSON file

            if ( ReadConfigurationCommand.execute() )
            {
                // pre-calculate all the pitches

                CalculatePitchCommand.execute();

                // actual rendering of the composition into MIDI

                boolean success = RenderCompositionCommand.execute();

                if ( success )
                    Output.print( "MIDI file '" + Config.OUTPUT_FILENAME + "' created successfully" );
            }
        }
    }
}
