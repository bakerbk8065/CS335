package cs335.seamCarver;

import java.util.ArrayList;
import java.util.List;

public class Seam {

    // Constructor opens new image display window.
    public Seam(UWECImage seamImage) {
        seamImage.openNewDisplayWindow();
    }

    // Public seam shrinking methods: these call the private generic method.
    public static UWECImage horizontalSeamShrink(UWECImage seamImage) {
        return seamShrink(seamImage, true);
    }
    public static UWECImage verticalSeamShrink(UWECImage seamImage) {
        return seamShrink(seamImage, false);
    }

    /*=================  seamShrink(UWECImage, boolean)  =================*/
    // Generic seam cut method: cuts both horizontal and vertical seams.
    // Use boolean argument to specify type of seam:
    //   * true  = horizontal seam
    //   * false = vertical seam
    private static UWECImage seamShrink(UWECImage image,
                                       boolean seamHorizontal) {
        // method variables:
        int W;                      // image width
        int Wt;                     // temp width
        int H;                      // image height
        List<List<Integer>> energy; // pixel energies
        List<List<Integer>> path;   // best paths (right to left)
        List<Integer> seam;         // seam (left to right)
        List<Integer> score;        // current best scores (x-column)
        List<Integer> score_;       // temp scores
        int scoreEq;                // temp score
        int scoreLt;                // temp score
        int scoreGt;                // temp score
        int scoreBest;              // best final score
        int pixelBest;              // pixel of best final score
        int waitMS;                 // wait time between each new seam (ms)

        // set wait time:
        waitMS = 50;

        // get image dimensions:
        W = image.getWidth();
        H = image.getHeight();

        // redraw image:
        image.repaintCurrentDisplayWindow();

        // adjust of for vertical seam:
        if (seamHorizontal == false) {
            image.rotate90();
            Wt = W;
            W = H;
            H = Wt;
        }

        // build array of pixel energies for entire image:
        energy = new ArrayList<List<Integer>>(W);
        for (int x=0; x<W; x++) {
            energy.add(new ArrayList<Integer>(H));
            for (int y=0; y<H; y++) {
                // calculate energy and add to array in one step:
                energy.get(x).add(y, (int)
                    (Math.pow((image.getRed((x+W-1)%W, y) -
                        image.getRed((x+1)%W, y)), 2) +
                    Math.pow((image.getGreen((x+W-1)%W, y) -
                        image.getGreen((x+1)%W, y)), 2)+
                    Math.pow((image.getBlue((x+W-1)%W, y) -
                        image.getBlue((x+1)%W, y)), 2) +
                    Math.pow((image.getRed(x,(y+H-1)%H) -
                        image.getRed(x,(y+1)%H)), 2) +
                    Math.pow((image.getGreen(x,(y+H-1)%H) -
                        image.getGreen(x,(y+1)%H)), 2) +
                    Math.pow((image.getBlue(x,(y+H-1)%H) -
                        image.getBlue(x,(y+1)%H)), 2)));
            }
        }

        // build array of best paths and get best final score:
        path = new ArrayList<List<Integer>>(W);
        score = new ArrayList<Integer>(H);
        score_ = new ArrayList<Integer>(H);
        // add initial score and default path 0 for first row:
        path.add(new ArrayList<Integer>(H));
        for (int y=0; y<H; y++) {
            path.get(0).add(0);
            //score.add(0);
        }
        score = energy.get(0);
        for (int x=1; x<W; x++) {
            path.add(new ArrayList<Integer>(H));
            for (int y=0; y<H; y++) {
                score_ = score;
                // get scores for each path choice:
                scoreEq = score_.get(y) + energy.get(x-1).get(y);
                scoreLt = score_.get(y) + energy.get(x-1).get((y-1+H)%H);
                scoreGt = score_.get(y) + energy.get(x-1).get((y+1)%H);
                // don't allow paths to cross image edges:
                if (y == 0) {
                    scoreLt = Integer.MAX_VALUE;
                }
                if (y == H-1) {
                    scoreGt = Integer.MAX_VALUE;
                }
                // add current best score and record path:
                if ((scoreEq <= scoreLt) && (scoreEq <= scoreGt)) {
                    score.set(y, scoreEq);
                    path.get(x).add(0);
                }
                else if ((scoreLt < scoreEq) && (scoreLt <= scoreGt)) {
                    score.set(y, scoreLt);
                    path.get(x).add(-1);
                }
                else {
                    score.set(y, scoreGt);
                    path.get(x).add(1);
                }
            }
        }

        // find best score and final pixel it occurs on:
        scoreBest = Integer.MAX_VALUE;
        pixelBest = 0;
        for (int y=0; y<H; y++) {
            if (score.get(y) < scoreBest) {
                scoreBest = score.get(y);
                pixelBest = y;
            }
        }

        // work backwards to get seam:
        seam = new ArrayList<Integer>(W);
        for (int x=0; x<W; x++) {
            seam.add(0);
        }
        seam.set(W-1, pixelBest);
        for (int x=W-2; x>=0; x--) {
            seam.set(x, seam.get(x+1) + path.get(x+1).get(seam.get(x+1)));
        }

        // draw red seam
        for (int x=0; x<W-1; x++) {
            image.setRGB(x, seam.get(x), 255, 0, 0);
        }

        // adjust of for vertical seam:
        if (seamHorizontal == false) {
            image.rotate270();
            Wt = W;
            W = H;
            H = Wt;
        }

        // redraw image:
        image.repaintCurrentDisplayWindow();

        try {
            Thread.sleep(waitMS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // adjust of for vertical seam:
        if (seamHorizontal == false) {
            image.rotate90();
            Wt = W;
            W = H;
            H = Wt;
        }

        // cut seam (write to new image all pixels but seam, then
        // switch images
        UWECImage newImage = new UWECImage(W,H-1);
        for (int x=0; x<W; x++) {
            for (int y=0; y<seam.get(x); y++){
                newImage.setRGB(x, y,
                    image.getRed(x,y),
                    image.getGreen(x,y),
                    image.getBlue(x,y));
            }
            for (int y=seam.get(x); y<H-1; y++){
                newImage.setRGB(x, y,
                    image.getRed(x, y+1),
                    image.getGreen(x, y+1),
                    image.getBlue(x,y+1));
            }
        }
        image.switchImage(newImage);

        // adjust of for vertical seam:
        if (seamHorizontal == false) {
            image.rotate270();
            Wt = W;
            W = H;
            H = Wt;
        }

        // redraw image:
        image.repaintCurrentDisplayWindow();

        return image;
    }
}
