package com.philliphsu.bottomsheetpickers.view.numberpad;

import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * Model that encapsulates data pertaining to the inputted time
 * in a number pad time picker.
 */
final class DigitwiseTimeModel {
    private static final int UNMODIFIED = -1;

    static final int MAX_DIGITS = 4;

    private final int[] mInput = new int[MAX_DIGITS];

    private final @Nullable OnInputChangeListener mListener;

    private int mCount;

    /**
     * Informs clients of new digit insertion and deletion events.
     */
    interface OnInputChangeListener {
        /**
         * @param digit The stored digit.
         */
        void onDigitStored(int digit);

        /**
         * @param digit The removed digit.
         */
        void onDigitRemoved(int digit);

        void onDigitsCleared();
    }

    // TODO: Delete this!
    @Deprecated
    DigitwiseTimeModel() {
        this(null);
    }

    DigitwiseTimeModel(OnInputChangeListener listener) {
        mListener = listener;

        // TOneverDO: Call clearDigits() to do this, otherwise we'll
        // end up calling back to the listener with an unintended
        // onDigitsCleared() event.
        Arrays.fill(mInput, UNMODIFIED);
        mCount = 0;
    }

    void storeDigit(int digit) {
        if (mCount < MAX_DIGITS) {
            mInput[mCount] = digit;
            mCount++;
            if (mListener != null) {
                mListener.onDigitStored(digit);
            }
        }
    }

    int getDigit(int at) {
        return mInput[at];
    }

    /**
     * @return a defensive copy of the internal array of inputted digits
     */
    int[] getDigits() {
        int[] digits = new int[mInput.length];
        System.arraycopy(mInput, 0, digits, 0, mInput.length);
        return digits;
    }

    void removeDigit() {
        if (mCount > 0) {
            mCount--; // move the cursor back
            int digit = mInput[mCount];
            mInput[mCount] = UNMODIFIED;
            if (mListener != null) {
                mListener.onDigitRemoved(digit);
            }
        }
    }

    void clearDigits() {
        Arrays.fill(mInput, UNMODIFIED);
        mCount = 0;
        if (mListener != null) {
            mListener.onDigitsCleared();
        }
    }

    int count() {
        return mCount;
    }

    /**
     * @return the integer represented by the inputted digits
     */
    int getInput() {
        if (mCount <= 0) return UNMODIFIED;

        int result = 0;
        for (int i = 0; i < mCount; i++) {
            result = result * 10 + mInput[i];
        }
        return result;
    }

    /**
     * Inserts as many of the digits in the given sequence
     * into the input as possible.
     */
    void storeDigits(int... digits) {
        if (digits == null)
            return;
        for (int d : digits) {
            if (d < 0 || d > 9)
                throw new IllegalArgumentException("Cannot store digit " + d);
            if (mCount == MAX_DIGITS)
                break;
            storeDigit(d);
        }
    }
}
