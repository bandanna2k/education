package education.designpatterns.chainOfResponsibility.getting;

public class SpreadAdjustment2 {
    private final int bidAdjustment;
    private final int askAdjustment;

    public SpreadAdjustment2(int bidAdjustment, int askAdjustment) {
        this.bidAdjustment = bidAdjustment;
        this.askAdjustment = askAdjustment;
    }

    @Override
    public String toString() {
        return "SpreadAdjustment2{" +
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
