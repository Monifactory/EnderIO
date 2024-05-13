package com.enderio.api.capacitor;

import java.util.Map;

// TODO: Instead of loot capacitors having lists of specialized machines, have different loot capacitor items for different
//       machine categories.
// TODO: Loot capacitor types (Sculk, Soul) found in respective dungeons/structures.
// TODO: End game capacitor fabrication with mob fighting? Souls? Capacitor sacrifice?

public interface CapacitorData {
    /**
     * Get the base modifier of the capacitor.
     */
    float base();

    /**
     * Get the modifier value for the given capacitor modifier type.
     */
    float getModifier(CapacitorModifier modifier);

    /**
     * Get a map of all modifiers and levels.
     * @implNote When implementing this, only return modifiers that have been changed, don't return 1's as these will pollute tooltips.
     */
    Map<CapacitorModifier, Float> modifiers();
}
