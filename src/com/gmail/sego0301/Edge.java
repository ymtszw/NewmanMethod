package com.gmail.sego0301;

public class Edge {

	int nodeNum1, nodeNum2;
	// int clusterNum;
	int edgeId;

	public Edge(int nodeNum1, int nodeNum2, int edgeId) {
		this.nodeNum1 = nodeNum1;
		this.nodeNum2 = nodeNum2;
		// this.clusterNum = clusterNum;
		this.edgeId = edgeId;
	}

	public Edge() {

	}

	public Edge(Edge edge) {
		setNodeNum1(edge.getNodeNum1());
		setNodeNum2(edge.getNodeNum2());
		setEdgeId(edge.getEdgeId());
	}

	public void setNodeNum1(int num) {
		nodeNum1 = num;
	}

	public int getNodeNum1() {
		return nodeNum1;
	}

	public void setNodeNum2(int num) {
		nodeNum2 = num;
	}

	public int getNodeNum2() {
		return nodeNum2;
	}

	// public void setClusterNum(int num) {
	// clusterNum = num;
	// }
	//
	// public int getClusterNum() {
	// return clusterNum;
	// }

	public void setEdgeId(int i) {
		edgeId = i;
	}

	public int getEdgeId() {
		return edgeId;
	}
}
