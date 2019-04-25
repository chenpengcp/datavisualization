package com.snh48.datavisualization.core;


import com.snh48.datavisualization.dao.DataDao;
import com.snh48.datavisualization.pojo.User;
import com.snh48.datavisualization.repository.GrSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EsTask {
    @Autowired
    private GrSearchRepository grSearchRepository;
    @Autowired
    private DataDao dataDao;
    private final static long SECOND = 1 * 1000;

//    @Scheduled(fixedRate = SECOND * 60 * 5)
    public void update() throws Exception {
        List<User> list = dataDao.findTotal();
        grSearchRepository.saveAll(list);
    }
}
