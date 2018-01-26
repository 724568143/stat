//package cn.batchfile.stat.agent.controller;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.batchfile.stat.agent.domain.Process;
//import cn.batchfile.stat.agent.domain.Stack;
//import cn.batchfile.stat.agent.service.ProcessService;
//
//@Controller
//public class ProcessController {
//
//	@Resource(name="processService")
//	private ProcessService processService;
//	
//	@RequestMapping(value="/process", method=RequestMethod.GET)
//	@ResponseBody
//	public List<Process> findProcesses(@RequestParam(value="query", required=false) String query) {
//		return processService.findProcesses(StringUtils.split(query, ' '));
//	}
//	
//	@RequestMapping(value="/process/{pid}", method=RequestMethod.GET)
//	@ResponseBody
//	public Process getProcess(@PathVariable("pid") long pid) {
//		return processService.getProcess(pid);
//	}
//	
//	@RequestMapping(value="/process/{pid}/info", method=RequestMethod.GET)
//	@ResponseBody
//	public String getInfo(@PathVariable("pid") long pid) {
//		return processService.getInfo(pid);
//	}
//	
//	@RequestMapping(value="/process/{pid}/stack", method=RequestMethod.GET)
//	@ResponseBody
//	public List<Stack> getStacks(@PathVariable("pid") long pid) {
//		return processService.getStacks(pid);
//	}
//	
//	@RequestMapping(value="/process/{pid}", method=RequestMethod.DELETE)
//	@ResponseBody
//	public void delete(@PathVariable("pid") long pid, @RequestParam(value="signum", defaultValue="15") int signum) {
//		processService.kill(pid, signum);
//	}
//}
