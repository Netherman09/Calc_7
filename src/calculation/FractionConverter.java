package calculation;

public class FractionConverter {
    public double[] convertToFraction(double number) {
        double nominator = 0, lastNominator = 1, lastNominator2 = 0;
        double denominator = 0, lastDenominator = 0, lastDenominator2 = 1;
        double startX = number;
        double x = startX;
        double a, f;
        double accuracy = 0.000001;

        while (true) {
            a = Math.floor(x);
            nominator = a * lastNominator + lastNominator2;
            denominator = a * lastDenominator + lastDenominator2;

            if (Math.abs(startX - (nominator / denominator)) < accuracy) {
                break;
            }

            lastNominator2 = lastNominator;
            lastDenominator2 = lastDenominator;
            lastNominator = nominator;
            lastDenominator = denominator;

            f = x- a;

            if (f < 1e-9) {
                break;
            }

            x = 1/f;
        }

        return new double[]{nominator, denominator};
    }
}
