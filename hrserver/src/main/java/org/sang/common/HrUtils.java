package org.sang.common;

import org.sang.bean.HrDetails;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by sang on 2017/12/30.
 */
public class HrUtils {
    public static HrDetails getCurrentHr() {
        return (HrDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
