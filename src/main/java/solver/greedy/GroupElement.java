package solver.greedy;

import lombok.Getter;
import lombok.Setter;

public class GroupElement {
    @Getter
    private int groupSize;
    @Getter@Setter
    private boolean placed;

    public GroupElement(int groupSize) {
        this.groupSize = groupSize;
        placed = false;
    }
}
