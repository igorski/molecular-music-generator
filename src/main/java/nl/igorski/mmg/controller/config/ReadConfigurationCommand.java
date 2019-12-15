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
package nl.igorski.mmg.controller.config;

import nl.igorski.mmg.config.Config;
import nl.igorski.mmg.definitions.JSONProperties;
import nl.igorski.mmg.utils.Output;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public final class ReadConfigurationCommand
{
    public static boolean execute()
    {
        try
        {
            final File jsonFile = new File( Config.INPUT_FILENAME );

            if ( !jsonFile.exists() || !jsonFile.isFile() ) {
                Output.print( "could not find " + Config.INPUT_FILENAME + " (is the path correct?)" );
                return false;
            }

            JSONParser parser = new JSONParser();
            Object obj        = parser.parse( new FileReader( jsonFile ));
            JSONObject json   = ( JSONObject ) obj;

            // if no tempo or note lengths were set, we cannot do anything !

            if ( !hasProperty( json, JSONProperties.TEMPO ) ||
                 !hasProperty( json, JSONProperties.NOTE_LENGTHS ))
            {
                Output.print( Config.INPUT_FILENAME + " contained no tempo or note lengths properties" );
                return false;
            }

            // same goes for patter length and amount of patterns

            if ( !hasProperty( json, JSONProperties.AMOUNT_OF_PATTERNS ) ||
                 !hasProperty( json, JSONProperties.PATTERN_LENGTH ))
            {
                Output.print( Config.INPUT_FILENAME + " contained no pattern length or has an undefined pattern amount" );
                return false;
            }

            // tempo
            Config.TEMPO = getFloat( json.get( JSONProperties.TEMPO ));

            // note lengths
            JSONArray lengths = ( JSONArray ) json.get( JSONProperties.NOTE_LENGTHS );

            Config.NOTE1_LENGTH = getFloat( lengths.get( 0 ));
            Config.NOTE2_LENGTH = getFloat( lengths.get( 1 ));

            // patterns
            Config.AMOUNT_OF_PATTERNS     = getInteger( json, JSONProperties.AMOUNT_OF_PATTERNS );
            Config.PATTERN_LENGTH_IN_BARS = getInteger( json, JSONProperties.PATTERN_LENGTH );

            // meter

            if ( hasProperty( json, JSONProperties.TIME_SIGNATURE ))
            {
                JSONArray ts = ( JSONArray ) json.get( JSONProperties.TIME_SIGNATURE );
                Config.TIME_SIGNATURE_BEAT_AMOUNT = getInteger( ts.get( 0 ));
                Config.TIME_SIGNATURE_BEAT_UNIT   = getInteger( ts.get( 1 ));
            }

            // ranges

            if ( hasProperty( json, JSONProperties.MIN_OCTAVE ))
                Config.MIN_OCTAVE = getInteger( json, JSONProperties.MIN_OCTAVE ) + 1;

            if ( hasProperty( json, JSONProperties.MAX_OCTAVE ))
                Config.MAX_OCTAVE = getInteger( json, JSONProperties.MAX_OCTAVE ) + 1;

            // scale

            if ( hasProperty( json, JSONProperties.MUSICAL_SCALE ))
            {
                JSONArray scale = ( JSONArray ) json.get( JSONProperties.MUSICAL_SCALE );
                Config.SCALE    = new String[ scale.size() ];

                for ( int i = 0, l = scale.size(); i < l; ++i )
                    Config.SCALE[ i ] = getString( scale.get( i ));
            }

            // additional configurations
            if ( hasProperty( json, JSONProperties.TRACK_PER_PATTERN ))
                Config.UNIQUE_TRACK_PER_PATTERN = getBoolean( json, JSONProperties.TRACK_PER_PATTERN );

            if ( hasProperty( json, JSONProperties.OUTPUT_FILENAME ))
                Config.OUTPUT_FILENAME = getString( json, JSONProperties.OUTPUT_FILENAME );

            return true;
        }
        catch ( Exception e ) {
            Output.print( "Error occurred while reading configuration." );
        }

        Output.print( "error while reading JSON from " + Config.INPUT_FILENAME + ", check the formatting" );

        return false;
    }

    /**
     * checks whether given propertyName is
     * present in given JSONObject json
     *
     * @param {JSONObject} json
     * @param {String}     propertyName
     * @return {boolean}
     */
    private static boolean hasProperty( JSONObject json, String propertyName )
    {
        return json.get( propertyName ) != null;
    }

    /**
     * retrieves a floating point value stored in
     * key propertyName from given JSONObject json
     *
     * @param {JSONObject} jsonValue
     * @param {String}     propertyName
     * @return {float}
     */
    private static float getFloat( JSONObject json, String propertyName )
    {
        return getFloat( json.get( propertyName ));
    }

    private static float getFloat( Object value )
    {
        return (( Number ) value ).floatValue();
    }

    /**
     * retrieves an integer value stored in
     * key propertyName from given JSONObject json
     *
     * @param {JSONObject} jsonValue
     * @param {String}     propertyName
     * @return {int}
     */
    private static int getInteger( JSONObject json, String propertyName )
    {
        return getInteger(json.get(propertyName));
    }

    private static int getInteger( Object value )
    {
        return (( Number ) value ).intValue();
    }

    /**
     * retrieves a floating point value stored in
     * key propertyName from given JSONObject json
     *
     * @param {JSONObject} jsonValue
     * @param {String}     propertyName
     * @return {boolean}
     */
    private static boolean getBoolean( JSONObject json, String propertyName )
    {
        return ( Boolean ) json.get( propertyName );
    }

    /**
     * retrieves a String value stored in
     * key propertyName from given JSONObject json
     *
     * @param {JSONObject} jsonValue
     * @param {String}     propertyName
     * @return {String}
     */
    private static String getString( JSONObject json, String propertyName )
    {
        return getString( json.get( propertyName ));
    }

    private static String getString( Object value )
    {
        return (( String ) value );
    }
}
