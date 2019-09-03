/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpletree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael Braz
 */
public class NodeData {

    private List<Object> data;

    public NodeData() {
        data = new ArrayList<>();
    }

    public NodeData(List<Object> data) {
        if (data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = data;
        }
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        if (data == null) {
            return;
        }
        this.data = data;
    }

}
