package ru.maxryazan.bankofryazan.models;


public enum Status {
    ACTIVE {
        @Override
        public String toString() {
            return "АКТИВЕН";
        }
    },
    CLOSED {
        @Override
        public String toString() {
            return "ПОГАШЕН";
        }
    }

}
