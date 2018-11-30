package org.sang.mapper;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.TblPlan;
import org.sang.bean.TblPlanExample;

import java.util.List;

public interface TblPlanMapper {
    int countByExample(TblPlanExample example);

    int deleteByExample(TblPlanExample example);

    int deleteByPrimaryKey(Integer planId);

    int insert(TblPlan record);

    int insertSelective(TblPlan record);

    List<TblPlan> selectByExample(TblPlanExample example);

    TblPlan selectByPrimaryKey(Integer planId);

    int updateByExampleSelective(@Param("record") TblPlan record, @Param("example") TblPlanExample example);

    int updateByExample(@Param("record") TblPlan record, @Param("example") TblPlanExample example);

    int updateByPrimaryKeySelective(TblPlan record);

    int updateByPrimaryKey(TblPlan record);
}