package com.yyh.service;

import com.yyh.domain.DoubleRandomResult;
import com.yyh.domain.Sign;
import com.yyh.repository.DoubleRandomResultRepository;
import com.yyh.repository.SignRepository;
import com.yyh.repository.search.DoubleRandomResultSearchRepository;
import com.yyh.service.util.DateUtil;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Service Implementation for managing DoubleRandomResult.
 */
@Service
@Transactional
public class YYHDoubleRandomResultService {

    private final Logger log = LoggerFactory.getLogger(YYHDoubleRandomResultService.class);

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
        Sort sort = new Sort(Sort.Direction.DESC, "signConfig");
        List<Sign> signs = signRepository.findAll(sort);
        /*TODO
        * 这里的signs必须排序
        */
        for (DoubleRandomResult doubleRandomResult : doubleRandomResultList) {
            if (doubleRandomResult.getFinishDate() == null || doubleRandomResult.getFinishDate().trim().equals("")) {
                Integer day = DateUtil.daysBetween(sdf.format(date), doubleRandomResult.getDoubleRandom().getDoubleRandomDate());
                for (Sign sign : signs) {
                    if (day <= sign.getSignConfig()) {
                        doubleRandomResult.setSign(sign);
                    }
                }
            }
        }
        List<DoubleRandomResult> result = doubleRandomResultRepository.save(doubleRandomResultList);
        return result;
    }

}
