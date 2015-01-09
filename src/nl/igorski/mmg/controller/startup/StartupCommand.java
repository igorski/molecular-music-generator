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
package nl.igorski.mmg.controller.startup;

import nl.igorski.mmg.config.Config;
import nl.igorski.mmg.utils.Output;

public final class StartupCommand
{
    public static boolean execute( String[] arguments )
    {
        /* // OLD : multi line arguments
        for ( final String arg : arguments )
        {
            if ( containsArgument( arg, Parameters.INPUT_FILENAME ))
                Config.INPUT_FILENAME = getArgumentValue( arg, Parameters.INPUT_FILENAME );

        }
        */

        // the only accepted argument is the input filename, the validation
        // occurs in the ReadConfigurationCommand

        if ( arguments.length > 0 )
        {
            Config.INPUT_FILENAME = arguments[ 0 ];
        }

        // only input filename is required

        if ( Config.INPUT_FILENAME == null )
        {
            Output.print( "ERROR::no input filename provided" );
            return false;
        }
        return true;
    }

    private static boolean containsArgument( String arg, String parameter )
    {
        try {
            return arg.substring( 0, parameter.length() ).equals( parameter ) && arg.length() > parameter.length();
        }
        catch ( Exception e ) {} // likely StringIndexOutOfBounds

        return false;
    }

    private static String getArgumentValue( String arg, String parameter )
    {
        return arg.substring( parameter.length() );
    }
}
