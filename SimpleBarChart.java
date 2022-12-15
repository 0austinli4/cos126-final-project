// file that implements TextGenerator and Markov and displays graphic
// of frequencies

// CODE FROM
// https://www.roseindia.net/java/example/java/swing/draw-simple-bar-chart.shtml

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;

public class SimpleBarChart extends JPanel {
    private double[] value;
    private String[] languages;
    private String title;

    public SimpleBarChart(double[] val, String[] lang, String t) {
        languages = lang;
        value = val;
        title = t;
    }

    // CODE FROM ROSEINDIA .NET
    // initializing graphic component from Java Swing
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (value == null || value.length == 0)
            return;
        double minValue = 0;
        double maxValue = 0;
        for (int i = 0; i < value.length; i++) {
            if (minValue > value[i])
                minValue = value[i];
            if (maxValue < value[i])
                maxValue = value[i];
        }
        Dimension dim = getSize();
        int clientWidth = dim.width;
        int clientHeight = dim.height;
        int barWidth = clientWidth / value.length;
        Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
        FontMetrics titleFontMetrics = graphics.getFontMetrics(titleFont);
        Font labelFont = new Font("Book Antiqua", Font.PLAIN, 10);
        FontMetrics labelFontMetrics = graphics.getFontMetrics(labelFont);
        int titleWidth = titleFontMetrics.stringWidth(title);
        int q = titleFontMetrics.getAscent();
        int p = (clientWidth - titleWidth) / 2;
        graphics.setFont(titleFont);
        graphics.drawString(title, p, q);
        int top = titleFontMetrics.getHeight();
        int bottom = labelFontMetrics.getHeight();
        if (maxValue == minValue)
            return;
        double scale = (clientHeight - top - bottom) / (maxValue - minValue);
        q = clientHeight - labelFontMetrics.getDescent();
        graphics.setFont(labelFont);
        for (int j = 0; j < value.length; j++) {
            int valueP = j * barWidth + 1;
            int valueQ = top;
            int height = (int) (value[j] * scale);
            if (value[j] >= 0)
                valueQ += (int) ((maxValue - value[j]) * scale);
            else {
                valueQ += (int) (maxValue * scale);
                height = -height;
            }
            graphics.setColor(Color.blue);
            graphics.fillRect(valueP, valueQ, barWidth - 2, height);
            graphics.setColor(Color.black);
            graphics.drawRect(valueP, valueQ, barWidth - 2, height);
            int labelWidth = labelFontMetrics.stringWidth(languages[j]);
            p = j * barWidth + (barWidth - labelWidth) / 2;
            graphics.drawString(languages[j], p, q);
        }
    }

    // generates Markov Model simulating texts and frequencies
    public static void main(String[] args) {
        // markov model
        int k = Integer.parseInt(args[0]);  // order k for markov model
        int t = Integer.parseInt(args[1]);  // length of generated trajectory

        // organizing text file by person and simulating markov
        String[] names = {"Jasmine", "Sol", "irene", "David", "Joshua", "Sophie"};
        String[] organizedByPerson = TextGenerator.cleanText(names);
        TextGenerator.deleteNull(organizedByPerson);
        System.out.println("MARKOV SIMULATION TEXTS BETWEEN 2 PEOPLE");

        //3, 5 have to do with the index of array in names
        TextGenerator.simulateMarkov(k, t, organizedByPerson, 3, 5, "eat");

        System.out.println(); //readability

        // updating frequency tables
        for (String personString : organizedByPerson) {
            TextGenerator.splitString(personString);
        }

        // finding top 10 frequencies
        Map<String, Integer> top10Main = TextGenerator.topFrequencies();

        // printing out statistics
        TextGenerator.displayStats(organizedByPerson, names, top10Main);

        // generating bar graph
        JFrame frame = new JFrame();
        frame.setSize(350, 300);
        double[] value = new double[10];
        String[] words = new String[10];
        value[0] = 1;
        int counter = 0;
        for (String key : top10Main.keySet()) {
            words[counter] = key;
            value[counter] = top10Main.get(key);
            counter++;
        }
        frame.getContentPane().add(new SimpleBarChart(value, words,
                "Most Common Words"));

        WindowListener winListener = new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        };
        frame.addWindowListener(winListener);
        frame.setVisible(true);
    }
}
