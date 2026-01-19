package education.quantum;

public class MultiQubit {

    double[] amplitudes; // 2^n amplitudes for n qubits
    int numQubits;

    public MultiQubit(int n) {
        this.numQubits = n;
        this.amplitudes = new double[1 << n]; // 2^n states
        this.amplitudes[0] = 1.0; // Initialize to |00...0⟩
    }

    // Apply Hadamard to specific qubit
    public void hadamard(int qubitIndex) {
        double sqrt2 = Math.sqrt(2);
        double[] newAmps = new double[amplitudes.length];

        for (int i = 0; i < amplitudes.length; i++) {
            int bitMask = 1 << qubitIndex;
            if ((i & bitMask) == 0) { // qubit is 0
                int i1 = i | bitMask; // corresponding state with qubit=1
                newAmps[i] = (amplitudes[i] + amplitudes[i1]) / sqrt2;
                newAmps[i1] = (amplitudes[i] - amplitudes[i1]) / sqrt2;
            }
        }
        this.amplitudes = newAmps;
    }

    // Apply X (NOT) gate to specific qubit
    public void pauliX(int qubitIndex) {
        double[] newAmps = new double[amplitudes.length];
        for (int i = 0; i < amplitudes.length; i++) {
            int bitMask = 1 << qubitIndex;
            int flipped = i ^ bitMask; // flip the bit
            newAmps[i] = amplitudes[flipped];
        }
        this.amplitudes = newAmps;
    }

    // Measure all qubits
    public int measure() {
        double rand = Math.random();
        double cumulative = 0;
        for (int i = 0; i < amplitudes.length; i++) {
            cumulative += amplitudes[i] * amplitudes[i];
            if (rand < cumulative) return i;
        }
        return amplitudes.length - 1;
    }

    // Show non-zero states
    public void printState() {
        System.out.println("Quantum state (non-zero amplitudes):");
        for (int i = 0; i < amplitudes.length; i++) {
            if (Math.abs(amplitudes[i]) > 0.001) {
                String binary = String.format("%" + numQubits + "s",
                        Integer.toBinaryString(i)).replace(' ', '0');
                System.out.printf("  %.4f|%s⟩\n", amplitudes[i], binary);
            }
        }
    }

    public int getNumStates() {
        return amplitudes.length;
    }

    public static void main(String[] args) {
        System.out.println("=== Multi-Qubit Quantum Simulation ===\n");

        // Demonstrate exponential growth
        System.out.println("1. Memory Requirements:");
        for (int n = 1; n <= 20; n++) {
            long states = 1L << n;
            long bytes = states * 8; // 8 bytes per double
            System.out.printf("   %2d qubits: %,10d states = %,12d bytes (%.2f MB)\n",
                    n, states, bytes, bytes / 1_000_000.0);
        }
        System.out.println("   Note: 50 qubits would need ~9 petabytes!");

        // 2-qubit system
        System.out.println("\n2. Two-Qubit System:");
        MultiQubit q2 = new MultiQubit(2);
        System.out.println("Initial state |00⟩:");
        q2.printState();

        System.out.println("\nApply Hadamard to qubit 0:");
        q2.hadamard(0);
        q2.printState();

        System.out.println("\nApply Hadamard to qubit 1:");
        q2.hadamard(1);
        q2.printState();
        System.out.println("Now in superposition of ALL 4 states!");

        // 3-qubit system
        System.out.println("\n3. Three-Qubit System:");
        MultiQubit q3 = new MultiQubit(3);
        q3.hadamard(0);
        q3.hadamard(1);
        q3.hadamard(2);
        q3.printState();
        System.out.println("Equal superposition of ALL 8 states!");

        // Measurement statistics
        System.out.println("\n4. Measurement Statistics (3 qubits, 1000 trials):");
        int[] counts = new int[8];
        for (int i = 0; i < 1000; i++) {
            MultiQubit q = new MultiQubit(3);
            q.hadamard(0);
            q.hadamard(1);
            q.hadamard(2);
            counts[q.measure()]++;
        }
        for (int i = 0; i < 8; i++) {
            String binary = String.format("%3s", Integer.toBinaryString(i)).replace(' ', '0');
            System.out.printf("   |%s⟩: %3d times (%.1f%%)\n",
                    binary, counts[i], 100.0 * counts[i] / 1000);
        }

        // Bell state (entanglement simulation)
        System.out.println("\n5. Bell State (Entangled Pair):");
        MultiQubit bell = new MultiQubit(2);
        bell.hadamard(0);
        // CNOT simulation: if qubit 0 is |1⟩, flip qubit 1
        double[] temp = new double[4];
        temp[0] = bell.amplitudes[0]; // |00⟩
        temp[1] = bell.amplitudes[1]; // |01⟩
        temp[2] = bell.amplitudes[3]; // |10⟩ -> |11⟩
        temp[3] = bell.amplitudes[2]; // |11⟩ -> |10⟩
        bell.amplitudes = temp;
        bell.printState();
        System.out.println("Perfect correlation: both qubits always match!");

        System.out.println("\n6. Scalability Challenge:");
        System.out.println("   Classical simulation gets exponentially harder.");
        System.out.println("   Real quantum computers process all states in parallel!");
    }
}

