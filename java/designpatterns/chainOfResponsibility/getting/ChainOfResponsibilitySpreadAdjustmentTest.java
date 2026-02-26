package education.designpatterns.chainOfResponsibility.getting;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ChainOfResponsibilitySpreadAdjustmentTest {

    private static SpreadAdjustmentProvider spreadCardAdjustmentProvider;
    private static SpreadAdjustmentProvider liquidityAdjustmentProvider;
    private static SpreadAdjustmentProvider brokerSpreadAdjustmentProvider;

    private static SpreadAdjustmentProvider getChainOfAdjustmentProviders(){
        spreadCardAdjustmentProvider = new SpreadAdjustmentProvider() {};
        liquidityAdjustmentProvider = new SpreadAdjustmentProvider() {};
        brokerSpreadAdjustmentProvider = new SpreadAdjustmentProvider() {};

        brokerSpreadAdjustmentProvider.setNextProvider(liquidityAdjustmentProvider);
        liquidityAdjustmentProvider.setNextProvider(spreadCardAdjustmentProvider);

        return brokerSpreadAdjustmentProvider;
    }

    @Test
    public void testChainOfResponsibility() {

        SpreadAdjustmentProvider adjustmentProviders = getChainOfAdjustmentProviders();

        spreadCardAdjustmentProvider.addSpreadAdjustment(1, -1, -1);
        spreadCardAdjustmentProvider.addSpreadAdjustment(2, -1, -1);
        spreadCardAdjustmentProvider.addSpreadAdjustment(3, -1, -1);
        spreadCardAdjustmentProvider.addSpreadAdjustment(9, -1, -1);

        liquidityAdjustmentProvider.addSpreadAdjustment(1, -2, -2);
        liquidityAdjustmentProvider.addSpreadAdjustment(3, -2, -2);
        liquidityAdjustmentProvider.addSpreadAdjustment(5, -2, -2);

        brokerSpreadAdjustmentProvider.addSpreadAdjustment(2, -3, -3);
        brokerSpreadAdjustmentProvider.addSpreadAdjustment(3, -3, -3);
        brokerSpreadAdjustmentProvider.addSpreadAdjustment(4, -3, -3);

        assertThat(adjustmentProviders.getEffectiveSpreadAdjustment(1)).hasValueSatisfying(esa -> {
            assertThat(esa.getBidAdjustment()).isEqualTo(-2);
            assertThat(esa.getAskAdjustment()).isEqualTo(-2);
        });
        assertThat(adjustmentProviders.getEffectiveSpreadAdjustment(2)).hasValueSatisfying(esa -> {
            assertThat(esa.getBidAdjustment()).isEqualTo(-3);
            assertThat(esa.getAskAdjustment()).isEqualTo(-3);
        });
        assertThat(adjustmentProviders.getEffectiveSpreadAdjustment(9)).hasValueSatisfying(esa -> {
            assertThat(esa.getBidAdjustment()).isEqualTo(-1);
            assertThat(esa.getAskAdjustment()).isEqualTo(-1);
        });
        assertThat(adjustmentProviders.getEffectiveSpreadAdjustment(10)).isNotPresent();
    }




    public static class SpreadAdjustment {
        private final int bidAdjustment;
        private final int askAdjustment;

        public SpreadAdjustment(int bidAdjustment, int askAdjustment) {
            this.bidAdjustment = bidAdjustment;
            this.askAdjustment = askAdjustment;
        }

        @Override
        public String toString() {
            return "SpreadAdjustment{" +
                    "bidAdjustment=" + bidAdjustment +
                    ", askAdjustment=" + askAdjustment +
                    '}';
        }

        public int getBidAdjustment() {
            return bidAdjustment;
        }

        public int getAskAdjustment() {
            return askAdjustment;
        }
    }


    public static abstract class SpreadAdjustmentProvider {

        private Map<Integer, SpreadAdjustment> ids = new HashMap<>();

        //next element in chain or responsibility
        protected SpreadAdjustmentProvider nextProvider;

        public void setNextProvider(SpreadAdjustmentProvider next) {
            this.nextProvider = next;
        }

        public Optional<SpreadAdjustment> getEffectiveSpreadAdjustment(int id) {
            Optional<SpreadAdjustment> spreadAdjustment = getInternalSpreadAdjustment(id);
            if (spreadAdjustment.isPresent()) {
                if (spreadAdjustment.get().getBidAdjustment() != 0 || spreadAdjustment.get().getAskAdjustment() != 0) {
                    return spreadAdjustment;
                }
            }
            if (nextProvider != null) {
                return nextProvider.getEffectiveSpreadAdjustment(id);
            }
            return spreadAdjustment;
        }

        public void addSpreadAdjustment(int id, int bidAdjustment, int askAdjustment)
        {
            ids.put(id, new SpreadAdjustment(bidAdjustment, askAdjustment));
        }

        private Optional<SpreadAdjustment> getInternalSpreadAdjustment(int id)
        {
            return Optional.ofNullable(ids.get(id));
        }
    }
}
