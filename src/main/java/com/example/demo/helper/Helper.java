package com.example.demo.helper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;

public class Helper {
    private static Logger logger = LoggerFactory.getLogger(Helper.class.getName());

    public static Date getNextCronDate(String cron) {
        try {
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
            Date date = cronSequenceGenerator.next(new Date());
            return date;
        } catch (Exception e) {
            logger.error("Error getting next date from cron string", e);
            return null;
        }
    }

}
