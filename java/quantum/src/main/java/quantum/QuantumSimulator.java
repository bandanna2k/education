package quantum;

public class QuantumSimulator {

    // Represents a qubit state as |ψ⟩ = α|0⟩ + β|1⟩
    static class Qubit {
        double alpha; // amplitude for |0⟩
        double beta;  // amplitude for |1⟩

        public Qubit(double alpha, double beta) {
            // Normalize to ensure |α|² + |β|² = 1
            double norm = Math.sqrt(alpha * alpha + beta * beta);
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

    // Pauli-X gate (NOT gate) - flips |0⟩ ↔ |1⟩
    static Qubit pauliX(Qubit q) {
        return new Qubit(q.beta, q.alpha);
    }

    // Hadamard gate - creates superposition
    static Qubit hadamard(Qubit q) {
        double sqrt2 = Math.sqrt(2);
        double newAlpha = (q.alpha + q.beta) / sqrt2;
        double newBeta = (q.alpha - q.beta) / sqrt2;
        return new Qubit(newAlpha, newBeta);
    }

    // Pauli-Z gate - phase flip
    static Qubit pauliZ(Qubit q) {
        return new Qubit(q.alpha, -q.beta);
    }

    public static void main(String[] args) {
        System.out.println("=== Quantum Computing Basics ===\n");

        // 1. Basic qubit states
        System.out.println("1. Initial States:");
        Qubit q0 = Qubit.zero();
        Qubit q1 = Qubit.one();
        System.out.println("   |0⟩ state: " + q0);
        System.out.println("   |1⟩ state: " + q1);

        // 2. Pauli-X (NOT) gate
        System.out.println("\n2. Pauli-X (NOT) Gate:");
        Qubit afterX = pauliX(q0);
        System.out.println("   X|0⟩ = " + afterX);
        System.out.println("   Measurement: " + afterX.measure());

        // 3. Hadamard gate - superposition
        System.out.println("\n3. Hadamard Gate (Superposition):");
        Qubit superpos = hadamard(q0);
        System.out.println("   H|0⟩ = " + superpos);
        System.out.print("   10 measurements: ");
        for (int i = 0; i < 10; i++) {
            System.out.print(hadamard(q0).measure() + " ");
        }
        System.out.println();

        // 4. Combining gates
        System.out.println("\n4. Gate Combination (H then X):");
        Qubit combined = pauliX(hadamard(q0));
        System.out.println("   X(H|0⟩) = " + combined);

        // 5. Pauli-Z gate
        System.out.println("\n5. Pauli-Z Gate:");
        Qubit afterZ = pauliZ(superpos);
        System.out.println("   Z(H|0⟩) = " + afterZ);

        // 6. Multiple measurements demonstration
        System.out.println("\n6. Statistical Behavior:");
        int count0 = 0, count1 = 0;
        int trials = 1000;
        for (int i = 0; i < trials; i++) {
            int result = hadamard(q0).measure();
            if (result == 0) count0++;
            else count1++;
        }
        System.out.println("   H|0⟩ measured " + trials + " times:");
        System.out.println("   |0⟩: " + count0 + " (" + (100.0*count0/trials) + "%)");
        System.out.println("   |1⟩: " + count1 + " (" + (100.0*count1/trials) + "%)");
    }
}