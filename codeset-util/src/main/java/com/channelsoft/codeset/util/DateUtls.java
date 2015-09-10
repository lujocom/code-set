package com.channelsoft.codeset.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;

/**
 * <dl>
 * <dt>moduledemo</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 北京青牛风科技有限公司</dd>
 * <dd>CreateDate: 2015年09月10日</dd>
 * </dl>
 *
 * @author LuoHui
 */
public class DateUtls {

    public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getDate(Date date, String pattern) {
        if (ObjectUtils.notEqual(date, null)) {
            return DateFormatUtils.format(date, pattern);
        }
        return null;
    }
}
