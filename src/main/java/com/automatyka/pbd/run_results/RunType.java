package com.automatyka.pbd.run_results;

public enum RunType {
    M_1500, M_800, M_3000, CROSS_COUNTRY, M_1000, M_5000;


    @Override
    public String toString() {
        switch (this) {
            case M_1500:
                return "1500m";
            case M_800:
                return "800m";
            case M_1000:
                return "1000m";
            case M_3000:
                return "3000m";
            case M_5000:
                return "5000m";
            case CROSS_COUNTRY:
                return "Przełaj";
        }
        return super.toString();
    }
}
