package entities;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Operation {
    private String type;
    private List<Map.Entry<Component, Double>> components;
    private Date operationDeadline;
    private String comments;
    private boolean isComplete;
}
