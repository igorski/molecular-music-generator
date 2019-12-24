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
import nl.igorski.mmg.model.Config;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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

    public void setup(Stage stage) {
        directoryChooser = new DirectoryChooser();
        this.stage = stage;

        TextField[] integerInputs = new TextField[] {
            timeSigUpper, timeSigLower, octaveLower, octaveUpper, patternLength, patternAmount
        };
        for (TextField integerInput : integerInputs ) {
            integerInput.setTextFormatter(new TextFormatter<>(integerInputValidator));
        }

        TextField[] floatInputs = new TextField[] { firstNoteLength, secondNoteLength };
        for (TextField floatInput : floatInputs ) {
            floatInput.setTextFormatter(new TextFormatter<>(floatInputValidator));
        }
        scaleInput.setTextFormatter(new TextFormatter<>(noteInputValidator));
    }

    @FXML
    public void selectOutputDirectory(Event e) {
        File selectedDirectory = directoryChooser.showDialog(stage);
        Config.OUTPUT_FOLDER = selectedDirectory.getAbsolutePath();
        submitButton.setDisable(false);
    }

    @FXML
    public void generateMIDI(Event e) {

        // collect all values from the form

        Config.OUTPUT_FILENAME = filenameInput.getText();

        double tempoValue = tempoSlider.getValue();
        BigDecimal numberBigDecimal = new BigDecimal(tempoSlider.getValue());
        numberBigDecimal = numberBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);

        Config.TEMPO = Float.parseFloat(numberBigDecimal.toString());
        Config.TIME_SIGNATURE_BEAT_AMOUNT = Integer.parseInt(timeSigUpper.getText());
        Config.TIME_SIGNATURE_BEAT_UNIT = Integer.parseInt(timeSigLower.getText());

        Config.NOTE1_LENGTH = Float.parseFloat(firstNoteLength.getText());
        Config.NOTE2_LENGTH = Float.parseFloat(secondNoteLength.getText());

        Config.PATTERN_LENGTH_IN_BARS = Integer.parseInt(patternLength.getText());
        Config.AMOUNT_OF_PATTERNS = Integer.parseInt(patternAmount.getText());
        Config.MIN_OCTAVE = Integer.parseInt(octaveLower.getText());
        Config.MAX_OCTAVE = Integer.parseInt(octaveUpper.getText());
        Config.SCALE = scaleInput.getText().split(",");

        Config.UNIQUE_TRACK_PER_PATTERN = trackPerPattern.isSelected();

        System.out.println(Config.valueOf());

        // pre-calculate all the pitches

        CalculatePitchCommand.execute();

        // actual rendering of the composition into MIDI

        boolean success = RenderCompositionCommand.execute();

        if (success) {
            feedback.setText("MIDI file '" + Config.OUTPUT_FILENAME + "' generated successfully.");
        } else {
            feedback.setText("Could not generate MIDI file, please verify input against the documentation.");
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
}
