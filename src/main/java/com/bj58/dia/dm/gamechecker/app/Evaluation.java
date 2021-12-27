package com.bj58.dia.dm.gamechecker.app;

import com.bj58.dia.dm.gamechecker.entity.AuntEntity;
import com.bj58.dia.dm.gamechecker.entity.OrderEntity;
import com.bj58.dia.dm.gamechecker.entity.ResultEntity;
import com.bj58.dia.dm.gamechecker.util.FileUtils;
import com.google.common.io.Files;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class Evaluation {


	private static List getOrderAuntList(String orderPath,String auntPath){
		try {
			String orderContent = FileUtils.fileGetContent(orderPath);
			String auntContent = FileUtils.fileGetContent(auntPath);
			List<String> list1 = Arrays.stream(orderContent.split("\n")).collect(Collectors.toList());
			List<String> list2 = Arrays.stream(auntContent.split("\n")).collect(Collectors.toList());;
			List<OrderEntity> orderList = list1.stream().map(x -> {
				String[] split = x.split(",");
				OrderEntity orderEntity = new OrderEntity();
				orderEntity.setId(split[0]);
				orderEntity.setServiceBeginTime(new Date(Long.valueOf(split[1]) * 1000));
				orderEntity.setServiceUnitTime(Integer.valueOf(split[2]));
				orderEntity.setX(Double.valueOf(split[3]));
				orderEntity.setY(Double.valueOf(split[4]));
				return orderEntity;
			}).collect(Collectors.toList());

			List<AuntEntity> auntList = list2.stream().map(x -> {
				String[] split = x.split(",");
				AuntEntity auntEntity = new AuntEntity();
				auntEntity.setId(split[0]);
				auntEntity.setServiceScore(Double.valueOf(split[1]));
				auntEntity.setX(Double.valueOf(split[2]));
				auntEntity.setY(Double.valueOf(split[3]));
				return auntEntity;
			}).collect(Collectors.toList());

			return Arrays.asList(orderList,auntList);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}


	public static ResultEntity eval(String resultFilePath,String orderPath,String auntPath){
		ResultEntity resultEntity=new ResultEntity();
		try {
			resultEntity.setCode(0);
			resultEntity.setMsg("");
			List orderAuntList = getOrderAuntList(orderPath,auntPath);
			List<String> list = Files.readLines(new File(resultFilePath), Charset.forName("utf8"));
			eval(list,(List<OrderEntity>)orderAuntList.get(0),(List<AuntEntity>)orderAuntList.get(1),resultEntity);
		} catch (Throwable e) {
			e.printStackTrace();
			resultEntity.setCode(-1);
			resultEntity.setMsg(e.getMessage());
		}
		return resultEntity;
	}

	public static void  eval(List<String> list,List<OrderEntity> orderEntityList,List<AuntEntity> auntEntityList,ResultEntity resultEntity){
		Map<String, OrderEntity> orderEntityMap = orderEntityList.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
		Map<String, AuntEntity> auntEntityMap = auntEntityList.stream().collect(Collectors.toMap(x -> x.getId(), x -> x,(x1,x2)->x2));

		int size = list.size();
		if(orderEntityList.size()!=size){
			throw new RuntimeException(String.format("输出行数与订单行数不相等 输出行数%s, 订单行数%s",size,orderEntityList.size()));
		}

		list.forEach(x->{
			if(StringUtils.isEmpty(x)){
				throw new RuntimeException("有空行，请检查格式！");
			}

			if(x.split(",").length!=2){
				throw new RuntimeException("每一行逗号分隔，请检查格式！"+x);
			}

			String orderId=x.split(",")[0];
			String auntId=x.split(",")[1];
			AuntEntity auntEntity = auntEntityMap.get(auntId);
			OrderEntity orderEntity = orderEntityMap.get(orderId);
			List<OrderEntity> dispatOrderList = auntEntity.getDispatOrderList();
			if(CollectionUtils.isEmpty(dispatOrderList)){
				dispatOrderList=new ArrayList<>();
				auntEntity.setDispatOrderList(dispatOrderList);
			}
			dispatOrderList.add(orderEntity);
			orderEntity.setAuntEntity(auntEntity);
		});

		orderEntityList.forEach(x->{
			if(null==x.getAuntEntity()){
				throw new RuntimeException("每个订单匹配且只匹配一个阿姨，未匹配的的订单id:"+x.getId());
			}
		});

		auntEntityList.forEach(x->{
			List<OrderEntity> dispatOrderList = x.getDispatOrderList();
			if(CollectionUtils.isEmpty(dispatOrderList)){
				return;
			}

			dispatOrderList.sort(Comparator.comparing(x1->x1.getServiceBeginTime()));
			boolean isValid = checkListIsValid(dispatOrderList);
			if(!isValid){
				throw new RuntimeException("每个订单需要在服务开始时间之前到达客户位置，且时间不能冲突，请检查有问题的阿姨id:"+x.getId());
			}

			for (int i = 0; i < dispatOrderList.size(); i++) {
				OrderEntity orderEntity = dispatOrderList.get(i);
				orderEntity.setServiceScore(x.getServiceScore());
				if(0==i){
					double distance = DispatchService.distance(x, orderEntity);
					orderEntity.setDistanceScore(distance);
					orderEntity.setTimeScore(0.5);
				}else{
					OrderEntity preOrder=dispatOrderList.get(i-1);
					double distance = DispatchService.distance(preOrder, orderEntity);
					orderEntity.setDistanceScore(distance);

					double timeDiffScore = DispatchService.getTimeDiffScore(preOrder, orderEntity);
					orderEntity.setTimeScore(timeDiffScore);
				}
			}
		});

		double A = orderEntityList.stream().mapToDouble(x -> x.getServiceScore()).average().getAsDouble();
		double B = orderEntityList.stream().mapToDouble(x -> x.getDistanceScore()).average().getAsDouble();
		double C = orderEntityList.stream().mapToDouble(x -> x.getTimeScore()).average().getAsDouble();
		double totalScore=A-0.5*B/15-0.25*C;

		//保留6位小数
		int retainNumber=6;
		String msg=String.format("totalScore(%."+retainNumber+"f)=A(%."+retainNumber+"f)-0.5*B(%."+retainNumber+"f)/15-0.25*C(%."+retainNumber+"f)"
				,totalScore,A,B,C);
		BigDecimal bigDecimal = BigDecimal.valueOf(totalScore);
		bigDecimal = bigDecimal.setScale(retainNumber, RoundingMode.HALF_UP);
		totalScore=bigDecimal.doubleValue();

		resultEntity.setMsg(msg);
		resultEntity.setTotalScore(totalScore);
	}

	public static void main(String[] args) {
		if(null==args||args.length!=3){
			throw new RuntimeException("请输入3个文件路径！ 第一个结果文件，第二个订单文件，第三个阿姨文件");
		}

		ResultEntity eval = eval(args[0],args[1],args[2]);
		System.out.println(eval);
	}


	private static boolean checkListIsValid(List<OrderEntity> orderEntities){
		for (int i = 0; i < orderEntities.size()-1; i++) {
			OrderEntity preOrder = orderEntities.get(i);
			OrderEntity afterOrder = orderEntities.get(i+1);
			boolean isValid = DispatchService.checkIsValid(preOrder,afterOrder);
			if(!isValid){
				return false;
			}
		}
		return true;
	}
}
