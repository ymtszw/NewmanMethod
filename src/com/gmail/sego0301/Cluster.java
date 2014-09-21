package com.gmail.sego0301;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

	int clusterNum;
	EdgeList innerEdgeList = new EdgeList();
	List<EdgeList> ListOfOuterEdgeList = new ArrayList<EdgeList>();
	List<Node> nodeList = new ArrayList<Node>();

	public Cluster() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public Cluster(Cluster cluster) {
		setClusterNum(cluster.getClusteNum());
		setInnerEdgeList(new EdgeList(cluster.getInnerEdgeList()));

		for (EdgeList outerList : cluster.getListOfOuterEdgeList()) {
			addListOfOuterEdgelist(new EdgeList(outerList));
		}
		for (Node node : cluster.getNodeList()) {
			addNode(new Node(node));
		}

	}

	public void setClusterNum(int num) {
		clusterNum = num;
	}

	public int getClusteNum() {
		return clusterNum;
	}

	public void setInnerEdgeList(EdgeList innerList) {
		innerEdgeList = innerList;
	}

	public void setInnerEdgeList(List<Edge> edgeList) {
		innerEdgeList.setEdgeList(edgeList);
	}

	public void addInnerEdgeList(EdgeList innerList) {
		if (!(this.innerEdgeList == null)) {
			this.innerEdgeList.addEdgeList(innerList);
		} else {
			this.innerEdgeList = innerList;
		}
	}

	public void addInnerEdge(Edge edge) {
		this.innerEdgeList.addEdge(edge);

	}

	public EdgeList getInnerEdgeList() {
		return innerEdgeList;
	}

	public void setListOfOuterEdgeList(List<EdgeList> outerList) {
		// ListOfOuterEdgeList.clear();
		ListOfOuterEdgeList = outerList;
	}

	// 向かう先のクラスタが同じ外部エッジリストを取り込む
	public void addListOfOuterEdgelist(EdgeList edgeList) {
		boolean jointDone = false;
		if (!(this.ListOfOuterEdgeList.isEmpty())) {
			for (EdgeList outerEdgeList : this.ListOfOuterEdgeList) {
				if (outerEdgeList.getClusterNum() == edgeList.getClusterNum()) {
					outerEdgeList.addEdgeList(edgeList);
					jointDone = true;
					break;
				}
			}
		}

		if (!jointDone) {
			this.ListOfOuterEdgeList.add(edgeList);
		}

	}

	public List<EdgeList> getListOfOuterEdgeList() {
		return ListOfOuterEdgeList;
	}

	public void setNodeList(List<Node> nodeList) {
		this.nodeList = nodeList;
	}

	public void addNode(Node node) {
		this.nodeList.add(node);
	}

	public void addNodeList(List<Node> nodeList) {
		this.nodeList.addAll(nodeList);
	}

	public List<Node> getNodeList() {
		return nodeList;
	}

}
