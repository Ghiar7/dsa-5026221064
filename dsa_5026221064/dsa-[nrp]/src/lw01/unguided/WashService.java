public abstract class WashService implements Billable {

    private String id;
    private int days;
    private int units;

    protected WashService(String id, int days, int units) {
        if (days <= 0) {
            throw new IllegalArgumentException("days must be a positive number");
        }
        if (units <= 0) {
            throw new IllegalArgumentException("units must be a positive number");
        }
        this.id = id;
        this.days = days;
        this.units = units;
    }

    public String getId() {
        return id;
    }

    public int getDays() {
        return days;
    }

    public int getUnits() {
        return units;
    }

    
    @Override
    public abstract int calculateCharge();

    public int calculateCharge(int units) {
        if (units <= 0) {
            throw new IllegalArgumentException("units must be a positive number");
        }
        return units * calculateCharge();
    }

    public String label() {
        return "Service";
    }

    public String summary() {
        return id + " | " + label() + " | " + calculateCharge();
    }
}
