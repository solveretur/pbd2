package com.automatyka.pbd.run_results;


public enum SeasonType {
    OUTDOOR, INDOOR;

    @Override
    public String toString() {
        switch (this) {
            case OUTDOOR:
                return "Outdoor";
            case INDOOR:
                return "Indoor";
        }
        return super.toString();
    }
}
