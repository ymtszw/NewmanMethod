package com.gmail.sego0301;

public class Node {

	/**
	 * @param args
	 */
	// int clusterNum;
	int nodeNum;

	// Node(int clusterNum, int nodeNum) {
	// this.clusterNum = clusterNum;
	// this.nodeNum = nodeNum;
	// }

	public Node(int nodeNum) {
		this.nodeNum = nodeNum;
	}

	public Node(Node node) {
		setNodeNum(node.getNodeNum());
	}

	// public void setClusterNum(int num) {
	// clusterNum = num;
	// }
	//
	// public int getClusterNum() {
	// return clusterNum;
	// }

	public void setNodeNum(int nodeNum) {
		this.nodeNum = nodeNum;
	}

	public int getNodeNum() {
		return nodeNum;
	}

}
