/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Igor Zinken
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
package nl.igorski.mmg.ui;

import nl.igorski.mmg.command.RenderCompositionCommand;
import nl.igorski.mmg.command.CalculatePitchCommand;
import nl.igorski.mmg.model.Properties;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.function.UnaryOperator;
import java.math.BigDecimal;

public class Controller {

    @FXML
    private Slider tempoSlider;
    @FXML
    private TextField tempoInput;
    @FXML
    private TextField timeSigUpper;
    @FXML
    private TextField timeSigLower;
    @FXML
    private TextField firstNoteLength;
    @FXML
    private TextField secondNoteLength;
    @FXML
    private TextField scaleInput;
    @FXML
    private TextField octaveLower;
    @FXML
    private TextField octaveUpper;
    @FXML
    private TextField patternLength;
    @FXML
    private TextField patternAmount;
    @FXML
    private TextField filenameInput;
    @FXML
    private CheckBox trackPerPattern;
    @FXML
    private Button submitButton;
    @FXML
    private Text feedback;

    private DirectoryChooser directoryChooser;
    private Stage stage;

    private Properties props;

    public void setup(Stage stage) {
        directoryChooser = new DirectoryChooser();
        this.stage = stage;

        props = new Properties();

        TextField[] integerInputs = new TextField[] {
            timeSigUpper, timeSigLower, octaveLower, octaveUpper, patternLength, patternAmount
        };
        for (TextField integerInput : integerInputs ) {
            integerInput.setTextFormatter(new TextFormatter<>(integerInputValidator));
        }

        TextField[] floatInputs = new TextField[] { tempoInput, firstNoteLength, secondNoteLength };
        for (TextField floatInput : floatInputs ) {
            floatInput.setTextFormatter(new TextFormatter<>(floatInputValidator));
        }
        scaleInput.setTextFormatter(new TextFormatter<>(noteInputValidator));
        tempoSlider.valueProperty().addListener(new ChangeListener<Number>() {
              @Override
              public void changed( ObservableValue<? extends Number> observableValue,
                                   Number oldValue, Number newValue) {
                  tempoInput.setText(doubleToFixedPrecisionString(newValue.doubleValue()));
              }
        });

        attachTooltip(tempoInput, "The song tempo in BPM");
        attachTooltip(timeSigUpper, "The upper numeral of a time signature (e.g. the \"3\" in \"3/4\")");
        attachTooltip(timeSigLower, "The lower numeral of a time signature (e.g. the \"4\" in \"3/4\")");
        attachTooltip(firstNoteLength, "The duration (as a fraction of a full measure) of the first note");
        attachTooltip(secondNoteLength, "The duration (as a fraction of a full measure) of the second note");
        attachTooltip(scaleInput,
            "Comma separated list of all notes to use for the patterns. This can be\n" +
            "changed to any sequence (or length!) of notes you like, meaning you can\n" +
            "use exotic scales, or dictate the movement by creating a sequence in thirds,\n" +
            "re-introduce a previously defined note, etc. etc.\n" +
            "The scale will be repeated over the determined octave range. You can create sharps\n" +
            "and flats too, e.g.: \"Eb\", \"F#\", etc. are all valid input."
        );
        attachTooltip(octaveLower, "The lowest octave in the note range");
        attachTooltip(octaveUpper, "The highest octave in the note range");
        attachTooltip(patternLength, "The length of each generated pattern (in measures)");
        attachTooltip(patternAmount, "The amount of patterns to generate");
        attachTooltip(trackPerPattern,
            "Whether to separate each pattern into a unique MIDI track.\n" +
            "When the amount of patterns is high enough for the algorithm\n" +
            "to go back down the scale, it is best to have this set active\n" +
            "to avoid conflicts in MIDI notes."
        );
    }

    @FXML
    public void selectOutputDirectory(Event e) {
        File selectedDirectory = directoryChooser.showDialog(stage);
        props.OUTPUT_FOLDER = selectedDirectory.getAbsolutePath();
        submitButton.setDisable(false);
    }

    @FXML
    public void generateMIDI(Event e) {

        // collect all values from the form

        props.OUTPUT_FILENAME = filenameInput.getText();

        double tempoValue = Double.parseDouble(tempoInput.getText());
        props.TEMPO = Float.parseFloat(doubleToFixedPrecisionString(tempoValue));
        props.TIME_SIGNATURE_BEAT_AMOUNT = Integer.parseInt(timeSigUpper.getText());
        props.TIME_SIGNATURE_BEAT_UNIT = Integer.parseInt(timeSigLower.getText());

        props.NOTE1_LENGTH = Float.parseFloat(firstNoteLength.getText());
        props.NOTE2_LENGTH = Float.parseFloat(secondNoteLength.getText());

        props.PATTERN_LENGTH_IN_BARS = Integer.parseInt(patternLength.getText());
        props.AMOUNT_OF_PATTERNS = Integer.parseInt(patternAmount.getText());
        props.MIN_OCTAVE = Integer.parseInt(octaveLower.getText());
        props.MAX_OCTAVE = Integer.parseInt(octaveUpper.getText());
        props.SCALE = scaleInput.getText().split(",");

        props.UNIQUE_TRACK_PER_PATTERN = trackPerPattern.isSelected();

        System.out.println( props.valueOf() );

        // pre-calculate all the pitches

        CalculatePitchCommand.execute( props );

        // actual rendering of the composition into MIDI

        boolean success = RenderCompositionCommand.execute( props );

        if ( success ) {
            feedback.setText("MIDI file '" + props.OUTPUT_FILENAME + "' generated successfully.");
        } else {
            feedback.setText("Could not generate MIDI file, please verify input against the documentation / tooltip suggestions.");
        }
    }

    /* utility methods */

    UnaryOperator<TextFormatter.Change> integerInputValidator = change -> {
        String text = change.getText();
        if (text.matches("[0-9]*")) {
            return change;
        }
        return null;
    };

    UnaryOperator<TextFormatter.Change> floatInputValidator = change -> {
        String text = change.getText();
        if (text.matches("[0-9.]*")) {
            return change;
        }
        return null;
    };

    UnaryOperator<TextFormatter.Change> noteInputValidator = change -> {
        String text = change.getText();
        if (text.matches("[a-gA-G|#|,]*")) {
            return change;
        }
        return null;
    };

    private void attachTooltip( Control element, String tooltipText ) {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText(tooltipText);
        element.setTooltip(tooltip);
    }

    private String doubleToFixedPrecisionString( double value ) {
        BigDecimal numberBigDecimal = new BigDecimal( value );
        numberBigDecimal = numberBigDecimal.setScale( 2, BigDecimal.ROUND_HALF_UP );
        return numberBigDecimal.toString();
    }
}
