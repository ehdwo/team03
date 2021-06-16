package ksmart39.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart39.mybatis.domain.Goods;

@Mapper
public interface GoodsMapper {

	
		//상품등록 화면까지
	public int addGoods(Goods goods);
	
		//상품목록조회
	public List<Goods> getGoodsList(Map<String, Object> paramMap);
}
