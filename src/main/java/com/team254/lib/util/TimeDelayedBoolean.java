package com.team254.lib.util;

import edu.wpi.first.wpilibj.Timer;

/**
 * It can set its boolean value and return whether the timer is within
 * a set timeout. This returns true if the stored value is true and the timeout has expired.
 */
public class TimeDelayedBoolean {
    private double lastTrueTime = 0;
    private double timeout = 0;
    private boolean m_old = false;
    
    public TimeDelayedBoolean(double _timeout) {
        timeout = _timeout;
    }

    public boolean update(boolean value) {
        if (!m_old && value)
            lastTrueTime = Timer.getFPGATimestamp();
        m_old = value;
        return get();
    }

    public boolean get(double time) {
        return m_old && (time - lastTrueTime) >= timeout;
    }

    public boolean get() {
        return get(Timer.getFPGATimestamp());
    }
}
