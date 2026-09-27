public class CarWash extends WashService {

    private static final int FIRST_TIER_DAYS = 3;
    private static final int FIRST_TIER_RATE = 35000;
    private static final int EXTRA_DAY_RATE = 25000;
    private static final int SETUP_FEE_PER_UNIT = 15000;

    public CarWash(String id, int days, int units) {
        super(id, days, units);
    }

    @Override
    public int calculateCharge() {
        int days = getDays();
        int dayCost;

        if (days <= FIRST_TIER_DAYS) {
            dayCost = days * FIRST_TIER_RATE;
        } else {
            int extraDays = days - FIRST_TIER_DAYS;
            dayCost = (FIRST_TIER_DAYS * FIRST_TIER_RATE) + (extraDays * EXTRA_DAY_RATE);
        }

        int perUnitCharge = dayCost + SETUP_FEE_PER_UNIT;
        return perUnitCharge * getUnits();
    }

    @Override
    public String label() {
        return "Car";
    }
}
