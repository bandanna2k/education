package education.quantum;

// Represents a qubit state as |ψ⟩ = α|0⟩ + β|1⟩
public class Qubit {
    double alpha; // amplitude for |0⟩
    double beta;  // amplitude for |1⟩

    public Qubit(double alpha, double beta) {
        // Normalize to ensure |α|² + |β|² = 1
        double norm = Math.sqrt((alpha * alpha) + (beta * beta));
        this.alpha = alpha / norm;
        this.beta = beta / norm;
    }

    // Create qubit in |0⟩ state
    public static Qubit zero() {
        return new Qubit(1, 0);
    }

    // Create qubit in |1⟩ state
    public static Qubit one() {
        return new Qubit(0, 1);
    }

    // Measure the qubit (collapses to |0⟩ or |1⟩)
    public int measure() {
        double prob0 = alpha * alpha;
        return Math.random() < prob0 ? 0 : 1;
    }

    @Override
    public String toString() {
        return String.format("%.3f|0⟩ + %.3f|1⟩", alpha, beta);
    }
}
