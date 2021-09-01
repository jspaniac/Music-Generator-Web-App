import java.util.*;
import java.io.*;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;

// NOTE: This code is provided from a previous 14X TA
// Generates random music using a music grammar located at GRAMMAR_LOCATION.
public class MusicGenerator {
    private static final boolean RHYTHM = true;
    private static final boolean MELODY = true;
    private static final boolean CHORD = true;
    
    private static final int REPEAT = 4;
    private static final Pattern EMPTY_PATTERN = new Pattern();
    private static final String[] INSTRUMENTS = {"bassdrum", "snare", "crash", "claps"};

    public static final String GRAMMAR_LOCATION = "music.txt";
    public static final String SAVE_TO = "midi/saved.mid";

    private GrammarSolver generator;

    /**
    * Loads in a music grammar located at GRAMMAR_LOCATION
    */
    public MusicGenerator() {
        generator = new GrammarSolver();
        try (Scanner input = new Scanner(new File(GRAMMAR_LOCATION))) {
            generator.addRules(input);
        } catch (FileNotFoundException ext) {
            throw new IllegalStateException("Constant FILE_NAME isn't " + 
                                            "a valid path for a BNF music file");
        }
    }

    /**
    * Generates some random music and plays the result out loud. 
    * Currently doesn't work in Ed :(
    */
    public void playOutLoud() {
        new Player().play(generate());
    }

    /**
    * Generates a random music file and saves it to SAVE_TO. 
    *
    * @return whether or not the save was successful 
    */
    public boolean generateFile() {
        return saveToFile(generate());
    }

    /**
    * Overwrites the previous .mid file located at SAVE_TO, combining 
    * the provided jfugue.pattern.Patterns before saving
    *
    * @return whether or not the save was successful
    * @param 0 to many jfugue.pattern.Pattern that are combined 
    * and then saved to the file located at SAVE_TO
    */
    private boolean saveToFile(Pattern... patterns) {
        try {
            File f = new File(SAVE_TO);
            Pattern combined = new Pattern();
            for (Pattern p : patterns) {
                combined.add(p);  
            }
            MidiFileManager.savePatternToMidi(combined, f);
        } catch (IOException ex) {return false;}
        return true;
    }

    /**
    * Builds various patterns that are combined in the final generated product
    *
    * @return an array of jfugue.pattern.Pattern representing the different layers 
    * of the generated music
    */
    private Pattern[] generate() {
        Pattern p1 = EMPTY_PATTERN;
        if (RHYTHM) {
            Rhythm rhythm = buildRhythm(generator, INSTRUMENTS);
            p1 = rhythm.getPattern().setVoice(0).repeat(2);
        }
        Pattern p2 = EMPTY_PATTERN;
        if (MELODY) {
            String melody = String.join(" ", generator.generate("measure", REPEAT));
            p2 = new Pattern(melody).setInstrument("clarinet").setVoice(1);
        }
        Pattern p3 = EMPTY_PATTERN;
        if (CHORD) {
            String chord = String.join(" ", generator.generate("chordmeasure", REPEAT));
            p3 = new Pattern(chord).setInstrument("electric_piano").setVoice(2);
        }
        return new Pattern[]{p1, p2, p3};
    }

    /**
    * Add multiple layers at random using the given generator.
    */
    private static Rhythm buildRhythm(GrammarSolver generator, String... instruments) {
        Rhythm rhythm = new Rhythm();
        for (String instrument : instruments) {
            String layer = "";
            for (String pattern : generator.generate(instrument, REPEAT)) {
                layer += pattern;
            }
            rhythm.addLayer(layer);
        }
        return rhythm;
    }

    /**
    * Allows the user to run MusicGenerator from the console with two options: play / save
    *
    * Play - plays the randomly generated music out-loud on the local machine (Doesn't work in Ed)
    * Save - saves the generated music into midi/saved.mid, overwriting the previous generated file
    */
    public static void main(String args[]) {
        if (args.length != 1 || !(args[0].equals("play") || args[0].equals("save"))) {
            throw new IllegalArgumentException("java MusicGenerator [play||save]");
        }

        MusicGenerator gen = new MusicGenerator();
        if (args[0].equals("play")) {
            gen.playOutLoud();
        } else {
            gen.saveToFile(gen.generate());
        }
    }
}

