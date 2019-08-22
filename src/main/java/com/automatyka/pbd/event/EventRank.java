package com.automatyka.pbd.event;

public enum EventRank {
    MISTRZOSTWA_POLSKI, MISTRZOSTWA_WOJEWODZKIE, REGULARNY_MEETING;


    @Override
    public String toString() {
        switch (this) {
            case REGULARNY_MEETING:
                return "Zwykłe zawody";
            case MISTRZOSTWA_POLSKI:
                return "Mistrzostwa Polski";
            case MISTRZOSTWA_WOJEWODZKIE:
                return "Mistrzostwa Wojewódzkie";
        }
        return super.toString();
    }
}
