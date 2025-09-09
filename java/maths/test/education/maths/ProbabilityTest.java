package education.maths;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class ProbabilityTest
{
    private Random random = new Random(2);

    enum Strategy
    {
        SureThings,
        FiftyFifty
    }

    @Test
    public void shouldBeRich()
    {
        /*
        2 4 8 16 32 64 128 256 512
         */
        Strategy strategy = Strategy.SureThings;
        double bet = 16;
        double balance = bet * Math.pow(2, 5);
//        System.out.printf("Initial Bet: %.2f, Balance %.2f%n", bet, balance);

        balance = 512;
        double betPower = 1;

        for (int i = 1; i < 100; i++)
        {
            System.out.println("----------------------");
            System.out.println("Day " + i);
            System.out.printf("Balance %.2f%n", balance);

            if(balance - bet < 0)
            {
                System.out.println("Out of the game: " + balance);
                break;
            }

            // Bet
            switch (strategy)
            {
                case SureThings -> {

                    // Bet
                    bet = balance / Math.pow(2, 5);
                    balance -= bet;
                    System.out.printf("Bet: %.2f, Balance %.2f%n", bet, balance);

                    // Result
                    double odds = 1.2;
                    double rand = random.nextDouble(0, 1);
                    if(rand < 0.1)
                    {
                        System.out.println("Lost " + strategy);
                        strategy = Strategy.FiftyFifty;
                        bet *= 2;
                    }
                    else
                    {
                        System.out.println("Won " + strategy);
                        balance += (bet * odds);
                    }
                }
                case FiftyFifty -> {

                    // Bet
                    balance -= bet;
                    System.out.printf("Bet: %.2f, Balance %.2f%n", bet, balance);

                    // Result
                    double odds = 2.2;
                    double rand = random.nextDouble(0, 1);
                    if(rand < 0.5)
                    {
                        System.out.println("Won " + strategy);
                        strategy = Strategy.SureThings;
                        balance += (bet * odds);
                    }
                    else
                    {
                        System.out.println("Lost " + strategy);
                        bet *= 2;
                    }
                }
            }
            System.out.printf("Balance after: %.2f%n", balance);
        }

        System.out.printf("End Balance %.2f%n", balance);
    }
}
