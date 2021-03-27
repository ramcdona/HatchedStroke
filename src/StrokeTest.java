/*******************************************************************************
 * Copyright (c) 2006-2021 Robert A. McDonald.
 *
 * This code is released under the terms described in LICENSE.txt
 ******************************************************************************/

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;


public class StrokeTest extends JPanel {

    private static final long serialVersionUID = 1L;
    Stroke[] strokes = new Stroke[]{new BasicStroke(4.0f), // The standard stroke
            new HatchedStroke(1.0f, 5.0f)};

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        Font f = new Font("Serif", Font.BOLD, 100);
        GlyphVector gv = f.createGlyphVector(g2d.getFontRenderContext(), "Hello World");
        Shape shape = gv.getOutline();

        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(30, 100);

        for (int i = 0; i < strokes.length; i++) {
            g2d.setStroke(strokes[i]);
            g2d.draw(shape);
            g2d.translate(0, 125);
        }
    }

    public static void main(String[] a) {
        JFrame f = new JFrame();

        final StrokeTest st = new StrokeTest();

        f.setContentPane(st);
        f.setSize(600, 300);
        f.setVisible(true);
    }
}
