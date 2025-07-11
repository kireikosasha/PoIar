package ah.poiar.util.math.phys;

public final class MotionValue {

    private double alpha;

    public MotionValue() {
    }

    public MotionValue(final double alpha) {
        this.alpha = alpha;
    }

    public double get() {
        return alpha;
    }

    public double set(final double beta) {
        return (alpha = beta);
    }

    public double add(final double beta) {
        return (this.alpha += beta);
    }

    public double subtract(final double beta) {
        return (this.alpha -= beta);
    }

    public double multiply(final double beta) {
        return (this.alpha *= beta);
    }

    public double divide(final double beta) {
        return (this.alpha /= beta);
    }
}

