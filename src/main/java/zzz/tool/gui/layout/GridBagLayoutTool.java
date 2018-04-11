package zzz.tool.gui.layout;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import zss.tool.Version;

@Version("2018.04.11")
public class GridBagLayoutTool {
    public static ConstraintsBuilder newConstraintsBuilder() {
        return new ConstraintsBuilder();
    }

    public static class ConstraintsBuilder {
        private final GridBagConstraints constraints = new GridBagConstraints();

        public ConstraintsBuilder location(int x, int y) {
            constraints.gridx = x;
            constraints.gridy = y;
            return this;
        }

        public ConstraintsBuilder fill(int fill) {
            constraints.fill = fill;
            return this;
        }

        public ConstraintsBuilder weight(double x, double y) {
            constraints.weightx = x;
            constraints.weighty = y;
            return this;
        }

        public ConstraintsBuilder insets(final Insets insets) {
            constraints.insets = insets;
            return this;
        }

        public GridBagConstraints build() {
            return constraints;
        }
    }
}
