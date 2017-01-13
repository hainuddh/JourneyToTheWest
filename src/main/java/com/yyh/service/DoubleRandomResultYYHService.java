package com.yyh.service;

import com.yyh.domain.DoubleRandomResult;
import com.yyh.domain.Sign;
import com.yyh.repository.DoubleRandomResultRepository;
import com.yyh.repository.SignRepository;
import com.yyh.repository.search.DoubleRandomResultSearchRepository;
import com.yyh.service.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing DoubleRandomResult.
 */
@Service
@Transactional
public class DoubleRandomResultYYHService {

    private final Logger log = LoggerFactory.getLogger(DoubleRandomResultYYHService.class);

    @Inject
    private DoubleRandomResultRepository doubleRandomResultRepository;

    @Inject
    private DoubleRandomResultSearchRepository doubleRandomResultSearchRepository;

    @Inject
    private SignRepository signRepository;

    public List<DoubleRandomResult> updateDoubleRandomResult() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        List<DoubleRandomResult> doubleRandomResultList = doubleRandomResultRepository.findAll();
        List<Sign> signs = signRepository.findAll();
        /*TODO
        * 这里的signs必须排序
        */
        for (DoubleRandomResult doubleRandomResult : doubleRandomResultList) {
            if (doubleRandomResult.getFinishDate() == null || doubleRandomResult.getFinishDate().trim().equals("")) {
                Integer day = DateUtil.daysBetween(doubleRandomResult.getDoubleRandom().getDoubleRandomDate(), sdf.format(date));
                for (Sign sign : signs) {
                    if (day > sign.getSignConfig()) {
                        doubleRandomResult.setSign(sign);
                    }
                }
            }
        }
        List<DoubleRandomResult> result = doubleRandomResultRepository.save(doubleRandomResultList);
        return result;
    }

}
