package com.gmail.sego0301;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class NewmanMethod {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();

		NewmanMethod newman = new NewmanMethod();

		List<Cluster> clusterList = newman.readInputFile(
				"./input/edgeListWiki.dat", "./input/nodeIdSortWiki.dat");

		double sumOfTotalEdge = newman.returnSumOfTotalEdge(clusterList) / 2;
		System.out.println("sumOfTotalEdge完了" + sumOfTotalEdge);
		ItsTime.time();

		List<Cluster> outputClusterList = newman.clusteringByUsingDeltaOnly(
				clusterList, sumOfTotalEdge);

		// newman.writeResult("./output/clusterTest.dat", outputClusterList);

		List<Cluster> lastOutputClusterList = newman.clustering(
				outputClusterList, sumOfTotalEdge);

		newman.writeResult("./output/clusterTest.dat", lastOutputClusterList);

		long stop = System.currentTimeMillis();
		System.out.println((stop - start) + "ミリ秒");

	}

	// 読み込んでクラスターリストを返す
	public List<Cluster> readInputFile(String edgeListFileName,
			String idListName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(idListName));
		String line;
		BufferedReader br1 = new BufferedReader(
				new FileReader(edgeListFileName));
		String line1;

		List<Cluster> ClusterList = new ArrayList<Cluster>();
		// List<Edge> edgeList = new ArrayList<Edge>();

		// 初期クラスターリストを作成
		int clusterNum = 0;
		while ((line = br.readLine()) != null) {
			String[] l = line.split("	");
			List<Node> nodeList = new ArrayList<Node>();
			nodeList.add(new Node(Integer.parseInt(l[0])));
			Cluster cluster = new Cluster();
			cluster.setClusterNum(clusterNum);
			cluster.setNodeList(nodeList);
			ClusterList.add(cluster);
			clusterNum++;
		}

		int edgeId = 0;
		while ((line1 = br1.readLine()) != null) {
			String[] l = line1.split("	");
			// l0,1のクラスター番号を特定
			int l0ClusterNum = 0, l1ClusterNum = 0;
			// edgeをclusterListの該当clusterに格納
			for (int i = 0; i < ClusterList.size(); i++) {
				Cluster cluster = ClusterList.get(i);
				boolean l0mada = true;
				boolean l1mada = true;

				// l0orl1がどのクラスターに所属しているかを調べる
				if (l0mada
						&& cluster.getNodeList().get(0).getNodeNum() == Integer
								.parseInt(l[0])) {
					l0ClusterNum = i;
					l0mada = false;
				}
				if (l1mada
						&& cluster.getNodeList().get(0).getNodeNum() == Integer
								.parseInt(l[1])) {
					l1ClusterNum = i;
					l1mada = false;
				}

				if (!(l0mada) && !(l1mada)) {
					break;
				}

			}
			Edge edge01 = new Edge();
			edge01.setNodeNum1(Integer.parseInt(l[0]));
			edge01.setNodeNum2(Integer.parseInt(l[1]));
			edge01.setEdgeId(edgeId);

			Edge edge10 = new Edge();
			edge10.setNodeNum1(Integer.parseInt(l[1]));
			edge10.setNodeNum2(Integer.parseInt(l[0]));
			edge10.setEdgeId(edgeId);

			// ここでのクラスター番号は向かう先のクラスタ番号のため注意
			EdgeList outerEdgeList01 = new EdgeList();
			// System.out.println("clusternumは "+l1ClusterNum);
			List<Edge> eList1 = new ArrayList<Edge>();
			eList1.add(edge01);
			outerEdgeList01.setEdgeList(eList1);
			outerEdgeList01.setClusterNum(l1ClusterNum);

			ClusterList.get(l0ClusterNum).addListOfOuterEdgelist(
					outerEdgeList01);

			EdgeList outerEdgeList10 = new EdgeList();
			outerEdgeList10.setClusterNum(l0ClusterNum);
			List<Edge> eList2 = new ArrayList<Edge>();
			eList2.add(edge10);
			outerEdgeList10.setEdgeList(eList2);

			ClusterList.get(l1ClusterNum).addListOfOuterEdgelist(
					outerEdgeList10);

			edgeId++;
		}

		br.close();
		br1.close();
		return ClusterList;

	}

	// clusterのiとjを統合する
	public Cluster jointClusters(Cluster clusterI, Cluster clusterJ) {

		// System.out.println(clusterJ.getInnerEdgeList());

		// ノードを取り込む
		clusterI.addNodeList(clusterJ.getNodeList());

		// 外部エッジの内eijは内部エッジに、それ以外は外部エッジのまま
		List<EdgeList> newListOfEdgeListI = new ArrayList<EdgeList>();

		// Iの外部エッジリストの中でJに向かうエッジを内部エッジに。そうでなければ新たな外部エッジリストに
		for (EdgeList outerEdgeListI : clusterI.getListOfOuterEdgeList()) {
			if (outerEdgeListI.getClusterNum() == clusterJ.clusterNum) {
				clusterI.addInnerEdgeList(outerEdgeListI);
			} else {
				newListOfEdgeListI.add(outerEdgeListI);
			}
		}

		// Jに向かう外部エッジが除かれた外部エッジリストをセット
		clusterI.setListOfOuterEdgeList(newListOfEdgeListI);

		// クラスタJの外部エッジの内eij以外を取り込む
		for (EdgeList outerEdgeListJ : clusterJ.getListOfOuterEdgeList()) {
			if (outerEdgeListJ.getClusterNum() != clusterI.getClusteNum()) {
				clusterI.addListOfOuterEdgelist(outerEdgeListJ);
			}
		}

		// 内部エッジを取り込む
		if (!(clusterJ.getInnerEdgeList() == null)) {
			clusterI.addInnerEdgeList(clusterJ.getInnerEdgeList());
		}
		return clusterI;
	}

	//

	public List<Cluster> clusteringByUsingDeltaOnly(List<Cluster> clusterList,
			double sumOfTotalEdge) throws IOException {

		NewmanMethod newman = new NewmanMethod();

		// dModが更新される限りループをする
		boolean henkaAri = true;
		int countLoop = 0;
		int keta = 1;
		while (henkaAri) {
			// このループ内で最もモデュラリティが高いクラスタのペアを記憶

			int maxClusterI = -1;
			int maxClusterJ = -1;
			double maxDmodularity = 0;
			countLoop++;
			henkaAri = false;
			for (int i = 0; i < clusterList.size(); i++) {
				Cluster clusterI = clusterList.get(i);
				for (EdgeList eijList : clusterI.getListOfOuterEdgeList()) {
					for (int j = 0; j < clusterList.size(); j++) {
						Cluster clusterJ = clusterList.get(j);
						if (eijList.getClusterNum() == clusterJ.getClusteNum()) {
							double dmod = newman.calDelatModularity(clusterI,
									clusterJ, eijList, sumOfTotalEdge);
							if (dmod > maxDmodularity) {
								maxDmodularity = dmod;
								maxClusterI = i;
								maxClusterJ = j;

								henkaAri = true;
							}
							// break;
						}
					}

				}
			}

			if (maxClusterI >= 0 && maxClusterJ >= 0) {
				List<Cluster> newClusterList = newman.returnNewCLusterList(
						clusterList, maxClusterI, maxClusterJ);
				clusterList.clear();
				for (Cluster cluster : newClusterList) {
					clusterList.add(cluster);
				}

			}

			if (countLoop > keta * 1000) {
				keta++;
				System.out.println(countLoop + "loop目	" + maxClusterI + "	"
						+ maxClusterJ + "	" + maxDmodularity + "	size"
						+ clusterList.size());
				ItsTime.time();
			}
		}

		return clusterList;
	}

	// clsuteringByUsingDelaOnlyの拡張版。最終的に１つのクラスタを目指す
	public List<Cluster> clustering(List<Cluster> clusterList,
			double sumOfTotalEdge) throws IOException {

		NewmanMethod newman = new NewmanMethod();
		int countLoop = 0;
		int keta = 1;

		List<Cluster> maxClusterList = new ArrayList<Cluster>();

		// maxClusterList.addAll(clusterList);

		for (Cluster cluster : clusterList) {
			maxClusterList.add(new Cluster(cluster));
		}

		// 現段階のmaxMod。つまりdeltaOnlyが終わった段階で適用
		double maxMod = newman.calModularity(clusterList, sumOfTotalEdge);
		System.out.println("始める前Modularity	" + maxMod + "	"
				+ maxClusterList.get(0).getNodeList().size());

		while (clusterList.size() > 1) {
			// ループ内で最もモデュラリティが高いクラスタのペアを記憶
			int maxClusterI = -1;
			int maxClusterJ = -1;
			double maxDmodularity = -100;
			countLoop++;
			for (int i = 0; i < clusterList.size(); i++) {
				Cluster clusterI = clusterList.get(i);
				for (EdgeList eijList : clusterI.getListOfOuterEdgeList()) {
					for (int j = 0; j < clusterList.size(); j++) {
						Cluster clusterJ = clusterList.get(j);
						if (eijList.getClusterNum() == clusterJ.getClusteNum()) {
							double dmod = newman.calDelatModularity(clusterI,
									clusterJ, eijList, sumOfTotalEdge);

							if (dmod > maxDmodularity) {
								maxDmodularity = dmod;
								maxClusterI = i;
								maxClusterJ = j;
							}
							// break;
						}
					}

				}
			}

			if (maxClusterI >= 0 && maxClusterJ >= 0) {
				List<Cluster> newClusterList = newman.returnNewCLusterList(
						clusterList, maxClusterI, maxClusterJ);
				clusterList.clear();
				for (Cluster cluster : newClusterList) {
					clusterList.add(cluster);
				}
			}

			if (maxDmodularity > 0) {
				maxMod = maxMod + maxDmodularity;
				maxClusterList.clear();
				maxClusterList = new ArrayList<Cluster>(clusterList);
				System.out.print("maxMod更新	");
				ItsTime.time();
			}

			if (countLoop > keta * 1000) {
				keta++;
				System.out.println(countLoop + "loop目	" + maxClusterI + "	"
						+ maxClusterJ + "	" + maxDmodularity + "	size"
						+ clusterList.size());
				ItsTime.time();
			}
		}

		System.out.println("最終maxModularity	" + maxMod + "	"
				+ maxClusterList.get(0).getNodeList().size());
		return maxClusterList;
	}

	// クラスタIにクラスタJを加える。Jの所は飛ばすようにする
	public List<Cluster> returnNewCLusterList(List<Cluster> clusterList, int i,
			int j) {
		List<Cluster> newClusterList = new ArrayList<Cluster>();
		NewmanMethod newman = new NewmanMethod();
		Cluster clusterI = clusterList.get(i);
		Cluster clusterJ = clusterList.get(j);

		// Jと昔つながっていたクラスタがあればエッジリストの向かう先をJからIに変更させる。
		// Jの外部エッジリストの接続先クラスタの番号を控えておく
		List<Integer> JLinkingList = new ArrayList<Integer>();
		for (EdgeList outerEdgeList : clusterJ.getListOfOuterEdgeList()) {
			JLinkingList.add(outerEdgeList.getClusterNum());
		}

		for (int k = 0; k < clusterList.size(); k++) {
			Cluster cluster = new Cluster();
			if (k != j) {
				if (k == i) {
					cluster = newman.jointClusters(clusterI, clusterJ);
				} else {
					cluster = clusterList.get(k);
					// Jと昔つながっていたクラスタがあればエッジリストの向かう先をJからIに変更させる。

					for (Integer jLinkingNum : JLinkingList) {
						if (jLinkingNum == cluster.getClusteNum()) {
							for (EdgeList outerEdgeList : cluster
									.getListOfOuterEdgeList()) {
								if (outerEdgeList.getClusterNum() == clusterJ
										.getClusteNum()) {
									outerEdgeList.setClusterNum(clusterI
											.getClusteNum());
								}
							}

							break;
						}
					}
				}
				newClusterList.add(cluster);
			}
		}

		return newClusterList;
	}

	public double calDelatModularity(Cluster clusterI, Cluster clusterJ,
			EdgeList eijList, double sumOfTotalEdge) {
		double countNaibuI = clusterI.getInnerEdgeList().getEdgeListSize();
		double countNaibuJ = clusterJ.getInnerEdgeList().getEdgeListSize();

		double countGaibuI = 0;
		double countGaibuJ = 0;

		for (EdgeList outerEdgeList : clusterI.getListOfOuterEdgeList()) {
			countGaibuI += outerEdgeList.getEdgeListSize();
		}

		for (EdgeList outerEdgeList : clusterJ.getListOfOuterEdgeList()) {
			countGaibuJ += outerEdgeList.getEdgeListSize();
		}

		double dMod = 2 * (eijList.getEdgeListSize() / sumOfTotalEdge * 1 / 2 - (countNaibuI * 2 + countGaibuI)
				/ sumOfTotalEdge
				* (countNaibuJ * 2 + countGaibuJ)
				/ sumOfTotalEdge * 1 / 4);
		return dMod;
	}

	public double calModularity(List<Cluster> clusterList, double sumOfTotalEdge) {
		double mod = 0;
		for (Cluster cluster : clusterList) {
			double countNaibuI = cluster.getInnerEdgeList().getEdgeListSize();

			double countGaibuI = 0;

			for (EdgeList outerEdgeList : cluster.getListOfOuterEdgeList()) {
				countGaibuI += outerEdgeList.getEdgeListSize();
			}

			// 内部エッジが占める割合+cluster内ノードのどれかと接続しているエッジの割合
			mod += countNaibuI / sumOfTotalEdge
					- (countNaibuI * 2 + countGaibuI) / sumOfTotalEdge
					* (countNaibuI * 2 + countGaibuI) / sumOfTotalEdge * 1 / 4;
			System.out.println(cluster.getClusteNum() + "	内部	" + countNaibuI
					+ "外部	" + countGaibuI);
		}
		return mod;
	}

	public double returnSumOfTotalEdge(List<Cluster> clusterList) {
		double sumOfTotalEdge = 0;
		for (Cluster cluster : clusterList) {
			for (EdgeList outerEdgeList : cluster.getListOfOuterEdgeList()) {
				sumOfTotalEdge += outerEdgeList.getEdgeListSize();
			}
		}
		return sumOfTotalEdge;
	}

	// clusteringが上手く行っているか調べるメソッド。クラスタの中身を全て吐き出す。
	public void checkClustering(List<Cluster> clusterList) {
		for (Cluster cluster : clusterList) {
			System.out.println("");
			for (Node node : cluster.getNodeList()) {
				System.out.println(cluster.getClusteNum() + "	"
						+ node.getNodeNum());
			}
			if (cluster.getInnerEdgeList() != null) {
				for (Edge edge : cluster.getInnerEdgeList().getEdgeList()) {
					System.out.println(cluster.getClusteNum() + "	inner	"
							+ edge.getNodeNum1() + "	" + edge.getNodeNum2());
				}
			}
			for (EdgeList edgeList : cluster.getListOfOuterEdgeList()) {
				for (Edge edge : edgeList.getEdgeList()) {
					System.out.println(cluster.getClusteNum() + "	outer	"
							+ edge.getNodeNum1() + "	" + edge.getNodeNum2());
				}
			}

		}
	}

	public void writeResult(String writeFileName, List<Cluster> clusterList)
			throws IOException {
		File f = new File(writeFileName);
		PrintStream ps = new PrintStream(f);
		for (Cluster cluster : clusterList) {
			for (Node node : cluster.getNodeList()) {
				ps.println(cluster.getClusteNum() + "	" + node.getNodeNum());
			}
		}
		ps.close();
	}

}