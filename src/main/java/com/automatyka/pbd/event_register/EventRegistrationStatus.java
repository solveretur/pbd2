package com.automatyka.pbd.event_register;

public enum EventRegistrationStatus {
    NONE, WAITING_FOR_PAYMENT, WAITING_FOR_ACCEPTANCE, REJECTED, REGISTERED;

    @Override
    public String toString() {
        switch (this) {
            case REJECTED:
                return "Odrzucona";
            case WAITING_FOR_PAYMENT:
                return "Czekamy na wpłatę";
            case WAITING_FOR_ACCEPTANCE:
                return "Czekamy na akceptację";
            case REGISTERED:
                return "Zarejestrowany";
            default:
                return "";
        }
    }
}
