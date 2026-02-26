package education.designpatterns.chainOfResponsibility.getting;

import education.designpatterns.chainOfResponsibility.getting.ChainOfResponsibilitySpreadAdjustmentTest.SpreadAdjustment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class SpreadAdjustmentProvider2 {

    private Map<Integer, SpreadAdjustment> ids = new HashMap<>();

    //next element in chain or responsibility
    protected SpreadAdjustmentProvider2 nextProvider;

    public void setNextProvider(SpreadAdjustmentProvider2 next) {
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