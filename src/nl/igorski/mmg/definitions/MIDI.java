package nl.igorski.mmg.definitions;

/**
 * Created with IntelliJ IDEA.
 * User: igorzinken
 * Date: 06-01-15
 * Time: 21:20
 * To change this template use File | Settings | File Templates.
 */
public final class MIDI
{
    // "tick" sizes translated to musical values

    public static long WHOLE_NOTE         = 1920;
    public static long HALF_NOTE          = WHOLE_NOTE / 2;
    public static long QUARTER_NOTE       = HALF_NOTE / 2;
    public static long EIGHT_NOTE         = QUARTER_NOTE / 2;
    public static long SIXTEENTH_NOTE     = EIGHT_NOTE / 2;
    public static long THIRTY_SECOND_NOTE = SIXTEENTH_NOTE / 2;
    public static long SIXTY_FOURTH_NOTE  = THIRTY_SECOND_NOTE / 2;
}
