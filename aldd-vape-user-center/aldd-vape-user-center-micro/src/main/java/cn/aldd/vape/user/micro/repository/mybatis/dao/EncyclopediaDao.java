package cn.aldd.vape.user.micro.repository.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.aldd.vape.user.micro.vo.EncyclopediaVo;

@Mapper
public interface EncyclopediaDao {

	EncyclopediaVo findEncyclopediaById(@Param("id") String id);

	List<EncyclopediaVo> findEncyclopediaList(@Param("entity") EncyclopediaVo encyclopediaVo);

}
