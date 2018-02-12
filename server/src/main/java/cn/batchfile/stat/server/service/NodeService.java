package cn.batchfile.stat.server.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.batchfile.stat.domain.Node;

@Service
public class NodeService {
	protected static final Logger log = LoggerFactory.getLogger(NodeService.class);
	private static final String INDEX_NAME = "node-data";
	private static final String TYPE_NAME = "node";
	private static final ThreadLocal<DateFormat> TIME_FORMAT = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		}
	};

	@Autowired
	private ElasticService elasticService;
	
	public void putNode(Node node) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", node.getId());
		map.put("hostname", node.getHostname());
		map.put("agentAddress", node.getAgentAddress());
		map.put("os", node.getOs());
		map.put("memory", node.getMemory());
		map.put("networks", node.getNetworks());
		map.put("disks", node.getDisks());
		map.put("tags", node.getTags());
		map.put("timestamp", TIME_FORMAT.get().format(new Date()));
		String json = JSON.toJSONString(map);

		IndexResponse resp = elasticService.getNode().client().prepareIndex().setIndex(INDEX_NAME).setType(TYPE_NAME)
				.setId(node.getId()).setSource(json, XContentType.JSON).execute().actionGet();
		
		long version = resp.getVersion();
		log.debug("index node data, id: {}, version: {}", node.getId(), version);
	}

	//TODO 清理不可用节点，30秒以上没有握手的节点清理掉。不清理会造成任务无法正常分配
	//清理范围包括：节点数据，进程数据，分配数据
}
