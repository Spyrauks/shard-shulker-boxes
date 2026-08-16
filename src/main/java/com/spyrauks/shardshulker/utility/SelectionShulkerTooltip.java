package com.spyrauks.shardshulker.utility;

public class SelectionShulkerTooltip {
    private static int selectedIndex = 0;
    private static int lastContainerSize = 0;

    public static int getSelectedIndex() {return selectedIndex;};

    public static void setSelectedIndex(int index) {selectedIndex = index;};

    public static void scroll(double delta, int containerSize) {
        if (delta < 0) {
            selectedIndex = (selectedIndex + 1) % containerSize;
        } else if (delta > 0) {
            selectedIndex = (selectedIndex - 1 + containerSize) % containerSize;
        }

    }

    public static void checkAndReset(int containerSize) {
        if (lastContainerSize != containerSize) {
            selectedIndex = 0;
            lastContainerSize = containerSize;
        }
    }
    // Just to reset the selected slot to the first slot when the player looks at a different type of shulker box

}
