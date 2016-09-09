package net;

/**
 * Created by Magina on 16/9/8.
 */
public enum Priority {
    LOW(1), NORMAL(2), HIGN(3), IMMEDIATE(4);

    private int mPriority;

    private Priority(int value) {
        mPriority = value;
    }

    public int getValue() {
        return mPriority;
    }

}
