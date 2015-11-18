package cn.batchfile.stat.server.service;

import java.util.List;

import cn.batchfile.stat.server.domain.Node;
import cn.batchfile.stat.server.domain.NodeData;

public interface NodeService {

	List<Node> getNodes();
	
	Node getNode(String agentId);

	void updateNode(Node node);

	void insertNodeData(NodeData nodeData);
}
