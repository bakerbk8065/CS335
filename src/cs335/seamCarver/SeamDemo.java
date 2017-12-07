package cs335.seamCarver;

public class SeamDemo {
    public static void main(String[] args) {

        // input file:
        String strImageIn = "./projectFiles/test.jpg";
        UWECImage imageIn = new UWECImage(strImageIn);

        // initialize seamCarver:
        Seam seamCarver = new Seam(imageIn);

        // carve some seams:
        for (int i=0; i<50; i++) {
            imageIn.switchImage(seamCarver.horizontalSeamShrink(imageIn));
            imageIn.switchImage(seamCarver.verticalSeamShrink(imageIn));
            imageIn.switchImage(seamCarver.verticalSeamShrink(imageIn));
        }
    }
}
