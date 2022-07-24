package net.guizhanss.guizhanlib.slimefun.machines;

import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
public final class MachineLayout {
    public static final MachineLayout MACHINE_DEFAULT = new MachineLayout()
        .setInputBorder(
            9, 10, 11, 12,
            18, 21,
            27, 28, 29, 30
        )
        .setInputSlots(19, 20)
        .setOutputBorder(
            14, 15, 16, 17,
            23, 26,
            32, 33, 34, 35
        )
        .setOutputSlots(24, 25)
        .setBackground(
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            13, 31,
            36, 37, 38, 39, 40, 41, 42, 43, 44
        )
        .setStatusSlot(22);

    public static final MachineLayout CRAFTING_DEFAULT = new MachineLayout()
        .setInputBorder(
            0, 1, 2, 3, 4,
            9, 13,
            18, 22,
            27, 31,
            36, 37, 38, 39, 40
        )
        .setInputSlots(
            10, 11, 12,
            19, 20, 21,
            28, 29, 30
        )
        .setOutputBorder(
            15, 16, 17,
            24,     26,
            33, 34, 35
        )
        .setOutputSlots(25)
        .setBackground(
            5, 6, 7, 8,
            14,
            32,
            41, 42, 43, 44
        )
        .setStatusSlot(23);

    private int[] inputBorder;
    private int[] inputSlots;
    private int[] outputBorder;
    private int[] outputSlots;
    private int[] background;
    private int statusSlot;

    public MachineLayout setInputBorder(int... inputBorder) {
        this.inputBorder = inputBorder;
        return this;
    }

    public MachineLayout setInputSlots(int... inputSlots) {
        this.inputSlots = inputSlots;
        return this;
    }

    public MachineLayout setOutputBorder(int... outputBorder) {
        this.outputBorder = outputBorder;
        return this;
    }

    public MachineLayout setOutputSlots(int... outputSlots) {
        this.outputSlots = outputSlots;
        return this;
    }

    public MachineLayout setBackground(int... background) {
        this.background = background;
        return this;
    }

    public MachineLayout setStatusSlot(int statusSlot) {
        this.statusSlot = statusSlot;
        return this;
    }
}
