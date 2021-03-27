/*******************************************************************************
 * Copyright (c) 2006-2021 Robert A. McDonald.
 *
 * This code is released under the terms described in LICENSE.txt
 ******************************************************************************/

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

public class HatchedStroke implements Stroke {

    protected float len;
    protected float sep;

    private ArrayList<float[]> heads = new ArrayList<float[]>();
    private ArrayList<float[]> tails = new ArrayList<float[]>();

    private transient BasicStroke linestroke;

    protected AffineTransform xform;

    public HatchedStroke(float thickness, float tic) {
        linestroke = new BasicStroke(thickness);
        len = (float) (Math.sqrt(2.0) * tic);
        sep = tic;
        xform = AffineTransform.getRotateInstance(-45.0 * Math.PI / 180.0);
    }

    public Shape createStrokedShape(Shape p) {

        PathIterator path = p.getPathIterator(new AffineTransform(), 0.5);
        GeneralPath res = new GeneralPath();

        float[] points;
        float[] current = null;
        float[] start = null;

        heads.clear();
        tails.clear();

        float step = sep;

        for (; !path.isDone(); path.next()) {
            points = new float[2];

            switch (path.currentSegment(points)) {
                case PathIterator.SEG_MOVETO:
                    res.moveTo(points[0], points[1]);
                    start = points;
                    current = points;
                    break;
                case PathIterator.SEG_CLOSE:
                    points = start;
                case PathIterator.SEG_LINETO:

                    float[] u = new float[2];
                    u[0] = points[0] - current[0];
                    u[1] = points[1] - current[1];
                    float dist = (float) Math.sqrt(u[0] * u[0] + u[1] * u[1]);
                    u[0] = u[0] / dist;
                    u[1] = u[1] / dist;

                    float[] v = new float[2];
                    xform.transform(u, 0, v, 0, 1);

                    while (step < dist) {
                        float[] anchor = new float[2];
                        anchor[0] = current[0] + step * u[0];
                        anchor[1] = current[1] + step * u[1];

                        res.lineTo(anchor[0], anchor[1]);
                        heads.add(anchor);

                        float[] end = new float[2];
                        end[0] = anchor[0] + len * v[0];
                        end[1] = anchor[1] + len * v[1];
                        tails.add(end);

                        dist -= step;
                        step = sep;
                        current = anchor;
                    }

                    if (dist > 0) {
                        res.lineTo(points[0], points[1]);
                        step -= dist;
                        current = points;
                    }
            }
        }

        for (int i = 0; i < heads.size(); i++) {
            res.moveTo(heads.get(i)[0], heads.get(i)[1]);
            res.lineTo(tails.get(i)[0], tails.get(i)[1]);
        }

        return (linestroke.createStrokedShape(res));
    }
}
