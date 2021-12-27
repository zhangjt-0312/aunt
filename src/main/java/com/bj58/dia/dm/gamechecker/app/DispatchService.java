package com.bj58.dia.dm.gamechecker.app;

import com.bj58.dia.dm.gamechecker.entity.AuntEntity;
import com.bj58.dia.dm.gamechecker.entity.OrderEntity;

public class DispatchService {

	public static double getTimeDiffScore(OrderEntity preOrder, OrderEntity afterOrder) {
		long end = preOrder.getServiceBeginTime().getTime() + preOrder.getServiceUnitTime() * 60 * 1000;
		return (afterOrder.getServiceBeginTime().getTime() - end) / (3600 * 1000.0);
	}

	public static double distance(AuntEntity auntEntity, OrderEntity orderEntity) {
		return Math.sqrt(Math.pow(auntEntity.getX() - orderEntity.getX(), 2)
				+ Math.pow(auntEntity.getY() - orderEntity.getY(), 2)) / 1000;
	}

	public static double distance(OrderEntity orderEntity1, OrderEntity orderEntity2) {
		return Math.sqrt(Math.pow(orderEntity1.getX() - orderEntity2.getX(), 2)
				+ Math.pow(orderEntity1.getY() - orderEntity2.getY(), 2)) / 1000;
	}

	private static boolean checkStockIsValid(long start1, long end1, long start2, long end2) {
		// 判断是否有交集
		boolean isHasIntersection = !(end2 <= start1 || start2 >= end1);
		if (isHasIntersection) {// 有交集
			return false;// 有一个时间有冲突就返回检测失败
		}

		return true;
	}

	public static boolean checkIsValid(OrderEntity orderEntity1, OrderEntity orderEntity2) {
		double speed = 15.0 * 1000 / 3600;
		double distance = distance(orderEntity1, orderEntity2);
		long buffer = Double.valueOf(1000 * Math.ceil(1000 * distance / speed)).longValue();

		long start1 = orderEntity1.getServiceBeginTime().getTime();
		long end1 = orderEntity1.getServiceBeginTime().getTime() + orderEntity1.getServiceUnitTime() * 60 * 1000
				+ buffer;

		long start2 = orderEntity2.getServiceBeginTime().getTime();
		long end2 = orderEntity2.getServiceBeginTime().getTime() + orderEntity1.getServiceUnitTime() * 60 * 1000;
		return checkStockIsValid(start1, end1, start2, end2);
	}
}
