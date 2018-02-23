package cn.batchfile.stat.server.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.batchfile.stat.domain.Node;
import cn.batchfile.stat.domain.PaginationList;

@Service
public class NodeService {
	protected static final Logger log = LoggerFactory.getLogger(NodeService.class);
	public static final String INDEX_NAME = "node-data";
	public static final String TYPE_NAME = "node";
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

	public PaginationList<Node> searchNodes(String query, int from, int size) {
		List<Node> nodes = new ArrayList<Node>();
		
		//查询数据
		SearchResponse resp = elasticService.getNode().client().prepareSearch()
				.setIndices(INDEX_NAME).setTypes(TYPE_NAME)
				.setQuery(QueryBuilders.queryStringQuery(query))
				.setFrom(from).setSize(size).execute().actionGet();
		long total = resp.getHits().getTotalHits();
		SearchHit[] hits = resp.getHits().getHits();

		//解析查询结果
		for (SearchHit hit : hits) {
			String json = hit.getSourceAsString();
			if (StringUtils.isNotEmpty(json)) {
				Node node = JSON.parseObject(json, Node.class);
				nodes.add(node);
			}
		}
		
		return new PaginationList<Node>(total, nodes);
	}

	public void deleteNode(String id) {
		DeleteResponse resp = elasticService.getNode().client().prepareDelete().setIndex(INDEX_NAME).setType(TYPE_NAME)
				.setId(id).execute().actionGet();
		log.debug("delete node: {}", resp.getId());
	}
}
