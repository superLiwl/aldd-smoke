package cn.aldd.vape.user.micro.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.aldd.vape.constants.CommonConstants;
import cn.aldd.vape.user.micro.domain.DynamicReward;
import cn.aldd.vape.user.micro.domain.UserRewardCount;
import cn.aldd.vape.user.micro.repository.jpa.DynamicRewardRepository;
import cn.aldd.vape.user.micro.repository.jpa.UserRewardCountRepository;
import cn.aldd.vape.user.micro.repository.mybatis.dao.DynamicRewardDao;
import cn.aldd.vape.user.micro.repository.mybatis.dao.UserRewardCountDao;
import cn.aldd.vape.user.micro.service.DynamicRewardService;
import cn.aldd.vape.user.micro.vo.DynamicRewardVo;
import cn.aldd.vape.user.micro.vo.UserRewardCountVo;
import cn.aldd.vape.util.Utils;

@Service("dynamicRewardService")
public class DynamicRewardServiceImpl implements DynamicRewardService {

	@Autowired
	private DynamicRewardDao dynamicRewardDao;
	@Autowired
	private UserRewardCountDao userRewardCountDao;
	@Autowired
	private DynamicRewardRepository dynamicRewardRepository;
	@Autowired
	private UserRewardCountRepository userRewardCountRepository;

	@Override
	public DynamicReward addDynamicReward(DynamicReward dynamicReward) {
		//判断打赏的次数
		UserRewardCountVo countVo = userRewardCountDao.findTodayUserRewardCountByUserId(dynamicReward.getCreateUserId());
		//只有当使用的次数小于拥有的次数时候才能打赏
		if(null == countVo|| Integer.parseInt(countVo.getUsedRewardCount()) >= Integer.parseInt(countVo.getHaveRewardCount())){
			return null;
		}
		//获取随机的烟油数量
		dynamicReward.setRewardNum(CommonConstants.REWARDNUM_ARRY[Utils.getRandomNum(10)]);
		dynamicReward.setCreateTime(new Date());
		dynamicReward = dynamicRewardRepository.save(dynamicReward);
		
		//减少一次打赏机会
		UserRewardCount userRewardCount = new UserRewardCount();
		userRewardCount.setHaveRewardCount(countVo.getHaveRewardCount());
		userRewardCount.setId(countVo.getId());
		userRewardCount.setCreateTime(countVo.getCreateTime());
		userRewardCount.setUserId(dynamicReward.getCreateUserId());
		userRewardCount.setUsedRewardCount(Utils.add("1", countVo.getUsedRewardCount()));
		userRewardCount = userRewardCountRepository.save(userRewardCount);
		
		return dynamicReward;
	}

	@Override
	public DynamicReward updateDynamicReward(DynamicReward dynamicReward) {
		DynamicReward oldUSser = dynamicRewardRepository.findOne(dynamicReward.getId());
		dynamicReward.setCreateTime(oldUSser.getCreateTime());
		dynamicReward = dynamicRewardRepository.save(dynamicReward);
		return dynamicReward;
	}

	@Override
	public DynamicRewardVo findDynamicRewardById(String id) {
		return dynamicRewardDao.findDynamicRewardById(id);
	}

	@Override
	public PageInfo<DynamicRewardVo> findDynamicRewardList(DynamicRewardVo dynamicRewardVo, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		PageInfo<DynamicRewardVo> result = new PageInfo<DynamicRewardVo>(dynamicRewardDao.findDynamicRewardList(dynamicRewardVo));
		if (pageNum > result.getPages()) {
			result.setList(null);
			result.setSize(0);
			result.setPageNum(pageNum);
		}
		return result;
	}

	@Override
	public void deleteDynamicRewardById(String id) {
		dynamicRewardRepository.delete(id);
	}

}