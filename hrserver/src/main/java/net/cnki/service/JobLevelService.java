package net.cnki.service;

import net.cnki.mapper.JobLevelMapper;
import net.cnki.bean.JobLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lizhizhong on 2018/11/28.
 */
@Service
@Transactional
public class JobLevelService {
    @Autowired
    JobLevelMapper jobLevelMapper;

    public int addJobLevel(JobLevel jobLevel) {
        if (jobLevelMapper.getJobLevelByName(jobLevel.getName()) != null) {
            return -1;
        }
        return jobLevelMapper.addJobLevel(jobLevel);
    }

    public List<JobLevel> getAllJobLevels() {
        return jobLevelMapper.getAllJobLevels();
    }

    public boolean deleteJobLevelById(String ids) {
        String[] split = ids.split(",");
        return jobLevelMapper.deleteJobLevelById(split) == split.length;
    }

    public int updateJobLevel(JobLevel jobLevel) {
        return jobLevelMapper.updateJobLevel(jobLevel);
    }
}
