package education.quantum;

public class QuantumSimulator
{

    private static final double SQRT_2 = Math.sqrt(2);

    // Pauli-X gate (NOT gate) - flips |0⟩ ↔ |1⟩
    static Qubit pauliX(Qubit q)
    {
        return new Qubit(q.beta, q.alpha);
    }

    // Hadamard gate - creates superposition
    static Qubit hadamard(Qubit q)
    {
        double sqrt2 = SQRT_2;
        double newAlpha = (q.alpha + q.beta) / sqrt2;
        double newBeta = (q.alpha - q.beta) / sqrt2;
        return new Qubit(newAlpha, newBeta);
    }

    // Pauli-Z gate - phase flip
    static Qubit pauliZ(Qubit q)
    {
        return new Qubit(q.alpha, -q.beta);
    }

    public static void main(String[] args)
    {
        System.out.println("=== Multi-Qubit Quantum Simulation ===\n");

        // Demonstrate exponential growth
        System.out.println("1. Memory Requirements:");
        for (int n = 1; n <= 20; n++)
        {
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
        for (int i = 0; i < 1000; i++)
        {
            MultiQubit q = new MultiQubit(3);
            q.hadamard(0);
            q.hadamard(1);
            q.hadamard(2);
            counts[q.measure()]++;
        }
        for (int i = 0; i < 8; i++)
        {
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

        // 7. Single qubit measurement destroys superposition
        System.out.println("\n7. Measurement Destroys Superposition (Single Qubit):");
        Qubit beforeMeasure = hadamard(Qubit.zero());
        System.out.println("   Created qubit in superposition:");
        System.out.println("   State: " + beforeMeasure);
        System.out.println("   (50% chance of 0, 50% chance of 1)");

        int result1 = beforeMeasure.measure();
        System.out.println("\n   First measurement: " + result1);
        System.out.println("   ⚠ Superposition collapsed! Qubit is now |" + result1 + "⟩");

        System.out.println("\n   If we measure the SAME qubit again (conceptually):");
        System.out.println("   Result would always be: " + result1);
        System.out.println("   (No more randomness - it's stuck at |" + result1 + "⟩)");

        System.out.println("\n   To get superposition back, must recreate from scratch:");
        Qubit fresh = hadamard(Qubit.zero());
        System.out.println("   New qubit state: " + fresh);
        int result2 = fresh.measure();
        System.out.println("   New measurement: " + result2);
        System.out.println("   (Independent result - could be different from " + result1 + ")");

        // 8. Two independent qubits in same superposition
        System.out.println("\n8. Two Independent Qubits (Same Superposition):");
        Qubit qubitA = hadamard(Qubit.zero());
        Qubit qubitB = hadamard(Qubit.zero());

        System.out.println("   Qubit A: " + qubitA);
        System.out.println("   Qubit B: " + qubitB);
        System.out.println("   (Both in identical superposition states)");

        int resultA = qubitA.measure();
        System.out.println("\n   Measure Qubit A: " + resultA);
        System.out.println("   Qubit A collapsed to |" + resultA + "⟩");
        System.out.println("   Qubit B still in superposition: " + qubitB);

        int resultB = qubitB.measure();
        System.out.println("\n   Measure Qubit B: " + resultB);
        System.out.println("   Qubit B collapsed to |" + resultB + "⟩");

        System.out.println("\n   Key insight: Results are INDEPENDENT");
        System.out.println("   A=" + resultA + ", B=" + resultB + " (may differ!)");
        System.out.println("   Measuring A doesn't affect B (not entangled)");

        // Demonstrate independence with statistics
        System.out.println("\n   Statistical proof (100 pairs):");
        int both00 = 0, both11 = 0, different = 0;
        for (int i = 0; i < 100; i++)
        {
            Qubit a = hadamard(Qubit.zero());
            Qubit b = hadamard(Qubit.zero());
            int rA = a.measure();
            int rB = b.measure();
            if (rA == 0 && rB == 0)
            {
                both00++;
            }
            else if (rA == 1 && rB == 1)
            {
                both11++;
            }
            else
            {
                different++;
            }
        }
        System.out.println("   Both measured 0: " + both00 + " (~25% expected)");
        System.out.println("   Both measured 1: " + both11 + " (~25% expected)");
        System.out.println("   Different results: " + different + " (~50% expected)");
        System.out.println("   (Compare to entangled qubits which ALWAYS match!)");
    }
}