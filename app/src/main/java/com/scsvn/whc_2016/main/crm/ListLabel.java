package com.scsvn.whc_2016.main.crm;

import java.util.ArrayList;

/**
 * Created by tranxuanloc on 8/6/2016.
 */
public class ListLabel {

    private ArrayList<Label> listLabel;

    public ArrayList<Label> getListLabel() {
        ArrayList<Label> listLabel = new ArrayList<>();
        listLabel.add(new Label("#FFFFFF", "None"));
        listLabel.add(new Label("#CC95A2", "Important"));
        listLabel.add(new Label("#849CE7", "Business"));
        listLabel.add(new Label("#A5DE63", "Personal"));
        listLabel.add(new Label("#E7E7D6", "Vacation"));
        listLabel.add(new Label("#FFB573", "Must Attend"));
        listLabel.add(new Label("#84EFF7", "Travel Required"));
        listLabel.add(new Label("#D6CE84", "Needs Preparation"));
        listLabel.add(new Label("#C6A5F7", "Birth day"));
        listLabel.add(new Label("#A5CEC6", "Anniversary"));
        listLabel.add(new Label("#FFE773", "Phone Call"));
        return listLabel;
    }

    public String getColor(String labelName) {
        listLabel = getListLabel();
        for (Label label : listLabel) {
            if (label.getLabel().equals(labelName))
                return label.getColor();
        }
        return "#FFFFFF";
    }

    public int getPosition(String labelName) {
        listLabel = getListLabel();
        for (Label label : listLabel) {
            if (label.getLabel().equals(labelName))
                return listLabel.indexOf(label);
        }
        return 0;
    }
}
