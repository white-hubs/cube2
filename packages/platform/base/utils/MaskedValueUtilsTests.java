package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MaskedValueUtilsTests {

    @Test
    public void containsMask() {
        assertTrue(MaskedValueUtils.containsMask("138****5678"));
        assertFalse(MaskedValueUtils.containsMask("13812345678"));
        assertFalse(MaskedValueUtils.containsMask(null));
    }

    @Test
    public void decide() {
        assertEquals(MaskedValueUtils.UpdateDecision.UNCHANGED,
                MaskedValueUtils.decide("138****5678", null, false));
        assertEquals(MaskedValueUtils.UpdateDecision.UNCHANGED,
                MaskedValueUtils.decide("138****5678", "138****5678", false));
        assertEquals(MaskedValueUtils.UpdateDecision.UPDATE,
                MaskedValueUtils.decide("138****5678", "13812349999", false));
        assertEquals(MaskedValueUtils.UpdateDecision.CLEAR,
                MaskedValueUtils.decide("410***********001X", "", true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decideRejectsChangedMaskedValue() {
        MaskedValueUtils.decide("138****5678", "138****9999", false);
    }
}
