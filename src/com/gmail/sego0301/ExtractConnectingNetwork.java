package com.gmail.sego0301;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ExtractConnectingNetwork {

	public ExtractConnectingNetwork() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO 自動生成されたメソッド・スタブ

		ExtractConnectingNetwork extract = new ExtractConnectingNetwork();
		List<Cluster> clusterList = extract
				.extractConnectingNetwork("./input/edgeListWiki.dat");
		 extract.writeResult("./output/edgeListWikiExtractResult.dat", clusterList);
//		extract.writeResult(
//				"/Users/babaseigou/Desktop/nodeIdSortWikiResult.dat",
//				clusterList);

	}

	public List<Cluster> extractConnectingNetwork(String readInputFileName)
			throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader(readInputFileName));
		String line;

		List<Cluster> clusterList = new ArrayList<Cluster>();
		// List<Edge> edgeList = new ArrayList<Edge>();

		// edgeIDは０からつける
		int edgeID = 0;

		int clusterNum=0;
		while ((line = br.readLine()) != null) {
			String[] l = line.split("	");
			Edge edge = new Edge(Integer.parseInt(l[0]),
					Integer.parseInt(l[1]), edgeID);

			// 読み取ったエッジの左と右がどこに所属するか。
			// -1&-1→新規クラスタ,片方-1→既存に追加,同じクラスタnum→既存クラスタ内,違うクラスタnum→クラスタ接続
			int leftClusterNum = -2;
			int rightClusterNum = -2;

			// このループでクラスター番号特定するよ
			loopCLusterList: for (int i = 0; i < clusterList.size(); i++) {
				for (Node node : clusterList.get(i).getNodeList()) {
					// 左一致
					if (node.getNodeNum() == edge.getNodeNum1()) {
						leftClusterNum = i;
					}
					// 右一致
					if (node.getNodeNum() == edge.getNodeNum2()) {
						rightClusterNum = i;
					}
				}
				if (leftClusterNum > 0 && rightClusterNum > 0) {
					break loopCLusterList;
				}
			}

			// -2&-2→新規クラスタ,片方-2→既存に追加,同じクラスタnum→既存クラスタ内,違うクラスタnum→クラスタ接続

			if (leftClusterNum < 0 && rightClusterNum < 0) {
				Cluster cluster = new Cluster();
				List<Edge> innerList = new ArrayList<Edge>();
				innerList.add(edge);
				cluster.setInnerEdgeList(innerList);
				cluster.addNode(new Node(edge.getNodeNum1()));
				cluster.addNode(new Node(edge.getNodeNum2()));
				cluster.setClusterNum(clusterNum);
				clusterNum++;
				clusterList.add(cluster);
			} else if ((leftClusterNum + 1) * (rightClusterNum + 1) < 0) {
				if (leftClusterNum >= 0) {
					// 要検討。clusterListは参照しか持たないからいちいち取り出さなくともよい？
					clusterList.get(leftClusterNum).addNode(
							new Node(edge.getNodeNum2()));
					clusterList.get(leftClusterNum).addInnerEdge(edge);
				} else {
					clusterList.get(rightClusterNum).addNode(
							new Node(edge.getNodeNum1()));
					clusterList.get(rightClusterNum).addInnerEdge(edge);
				}
			} else if (leftClusterNum == rightClusterNum) {
				clusterList.get(rightClusterNum).addInnerEdge(edge)
				;
			} else {
				NewmanMethod newman = new NewmanMethod();
				System.out.println(leftClusterNum + "	" + rightClusterNum);
				List<Cluster> newClusterList = newman.returnNewCLusterList(
						clusterList, leftClusterNum, rightClusterNum);
				clusterList.clear();
				for (Cluster cluster : newClusterList) {
					clusterList.add(cluster);
				}
			}

			edgeID++;
		}

		br.close();
		return clusterList;
	}

	public void writeResult(String writeFileName, List<Cluster> clusterList)
			throws IOException {
		System.out.println("a");
		// for (Cluster cluster : clusterList) {
		// System.out.println(cluster.getClusteNum());
		// for (Node node : cluster.getNodeList()) {
		// System.out.println(node.getNodeNum());
		// }
		// }
		File f = new File(writeFileName);
		PrintStream ps = new PrintStream(f);
		for (Cluster cluster : clusterList) {
			for (Node node : cluster.getNodeList()) {
				ps.println(cluster.getClusteNum() + "	" + node.getNodeNum());
			}

			ps.println();
			for (Edge edge : cluster.getInnerEdgeList().getEdgeList()) {
				//System.out.println("k");
				ps.println(cluster.getClusteNum() + "	" + edge.getNodeNum1()
						+ "	" + edge.getNodeNum2());
			}

			ps.println();
		}
		ps.close();
	}

}
