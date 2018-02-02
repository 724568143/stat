package cn.batchfile.stat.agent.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.batchfile.stat.agent.service.AppService;
import cn.batchfile.stat.agent.types.App;
import cn.batchfile.stat.agent.types.Choreo;
import cn.batchfile.stat.agent.types.RestResponse;

@RestController
public class AppController {
	protected static final Logger log = LoggerFactory.getLogger(AppController.class);
	
	@Autowired
	private AppService appService;
	
	@RequestMapping(value="/v1/app", method=RequestMethod.GET)
	public List<String> getApps() {
		return appService.getApps();
	}
	
	@GetMapping("/v1/app/{name}")
	public App getApp(HttpServletResponse response, @PathVariable("name") String name) throws IOException {
		App app = appService.getApp(name);
		if (app == null) {
			response.setStatus(404);
		} 
		return app;
	}
	
	@PutMapping("/v1/app/{name}")
	public RestResponse<String> putApp(HttpServletResponse response,
			@PathVariable("name") String name, 
			@RequestBody App app) throws UnsupportedEncodingException, IOException {
		
		RestResponse<String> resp = new RestResponse<String>();
		try {
			app.setName(name);
			appService.putApp(app);
			resp.setOk(true);
			resp.setBody(app.getName());
		} catch (Exception e) {
			resp.setOk(false);
			resp.setMessage(e.getMessage());
			response.setStatus(500);
		}
		return resp;
	}
	
	@PostMapping("/v1/app")
	public RestResponse<String> postApp(HttpServletResponse response,
			@RequestBody App app) throws UnsupportedEncodingException, IOException {
		
		RestResponse<String> resp = new RestResponse<String>();
		try {
			appService.postApp(app);
			resp.setOk(true);
			resp.setBody(app.getName());
		} catch (Exception e) {
			resp.setOk(false);
			resp.setMessage(e.getMessage());
			response.setStatus(500);
		}
		return resp;
	}
	
	@DeleteMapping("/v1/app/{name}")
	public RestResponse<String> deleteApp(HttpServletResponse response,
			@PathVariable("name") String name) {
		
		RestResponse<String> resp = new RestResponse<String>();
		try {
			appService.deleteApp(name);
			resp.setOk(true);
			resp.setBody(name);
		} catch (Exception e) {
			resp.setOk(false);
			resp.setMessage(e.getMessage());
			response.setStatus(500);
		}
		return resp;
	}
	
	@GetMapping("/v1/app/{name}/choreo")
	public Choreo getChoreo(HttpServletResponse response,
			@PathVariable("name") String name) throws IOException {
		
		Choreo choreo = appService.getChoreo(name);
		if (choreo == null) {
			response.setStatus(404);
		}
		return choreo;
	}

	@PostMapping("/v1/app/{name}/_scale")
	public RestResponse<Integer> putScale(HttpServletResponse response,
			@PathVariable("name") String name, 
			@RequestParam("num") int scale) throws IOException {
		
		RestResponse<Integer> resp = new RestResponse<Integer>();
		try {
			appService.putScale(name, scale);
			resp.setOk(true);
			resp.setBody(scale);
		} catch (Exception e) {
			resp.setOk(false);
			resp.setMessage(e.getMessage());
			response.setStatus(500);
		}
		return resp;
	}
	
	@PostMapping("/v1/app/{name}/_start")
	public RestResponse<String> startApp(HttpServletResponse response,
			@PathVariable("name") String name) throws IOException {
		
		RestResponse<String> resp = new RestResponse<String>();
		try {
			appService.putStart(name, true);
			resp.setOk(true);
			resp.setBody(name);
		} catch (Exception e) {
			resp.setOk(false);
			resp.setMessage(e.getMessage());
			response.setStatus(500);
		}
		return resp;
	}

	@PostMapping("/v1/app/{name}/_stop")
	public RestResponse<String> stopApp(HttpServletResponse response,
			@PathVariable("name") String name) throws IOException {
		
		RestResponse<String> resp = new RestResponse<String>();
		try {
			appService.putStart(name, false);
			resp.setOk(true);
			resp.setBody(name);
		} catch (Exception e) {
			resp.setOk(false);
			resp.setMessage(e.getMessage());
			response.setStatus(500);
		}
		return resp;
	}
}
