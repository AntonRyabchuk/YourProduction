package com.yourproduction;

import com.yourproduction.db.dao.ComponentItemDAO;
import com.yourproduction.db.dao.FooDAO;
import com.yourproduction.db.repository.IComponentItemRepository;
import com.yourproduction.entities.ComponentItem;

public class Application {
    public static void main(String[] args) {
        IComponentItemRepository repository = new ComponentItemDAO();

        ComponentItem item = repository.findById(1);
    }
}
