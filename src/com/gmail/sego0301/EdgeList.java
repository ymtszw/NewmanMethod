package com.gmail.sego0301;

import java.util.ArrayList;
import java.util.List;

public class EdgeList {

	List<Edge> edgeList = new ArrayList<Edge>();
	int clusterNum;

	public EdgeList() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public EdgeList(EdgeList eList) {
		for (Edge edge : eList.getEdgeList()) {
			addEdge(new Edge(edge));
		}
	}

	public void setClusterNum(int clusterNum) {
		this.clusterNum = clusterNum;
	}

	public int getClusterNum() {
		return clusterNum;
	}

	public void setEdgeList(List<Edge> edgeList) {
		this.edgeList = edgeList;
	}

	public List<Edge> getEdgeList() {
		return edgeList;
	}

	public void addEdgeList(EdgeList edgeList) {

		this.edgeList.addAll(edgeList.getEdgeList());

	}

	public void addEdge(Edge edge) {
		this.edgeList.add(edge);
	}

	public int getEdgeListSize() {
		return edgeList.size();
	}
}
