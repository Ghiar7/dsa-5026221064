public class MotorcycleWash extends WashService {

    private static final int DAILY_RATE = 15000;
    private static final int SETUP_FEE_PER_UNIT = 5000;

    public MotorcycleWash(String id, int days, int units) {
        super(id, days, units);
    }

    @Override
    public int calculateCharge() {
        int perUnitCharge = (getDays() * DAILY_RATE) + SETUP_FEE_PER_UNIT;
        return perUnitCharge * getUnits();
    }

    @Override
    public String label() {
        return "Motorcycle";
    }
}
