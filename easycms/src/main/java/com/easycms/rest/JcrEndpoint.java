package com.easycms.rest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.easycms.service.JcrContentService;

@RestController
@RequestMapping("/content")
public class JcrEndpoint {

	@Autowired
	private JcrContentService jcrService;

	@RequestMapping(value = "/{folderName}/{fileName}", 
			method = RequestMethod.GET)
	public @ResponseBody byte[] getContent(@PathVariable String folderName,
			@PathVariable String fileName) {
		byte[] result = (byte[]) jcrService.getContent(folderName, fileName);
		return result;
	}
	@RequestMapping(value = "/save", 
			method = RequestMethod.POST, headers="Accept=*")
	public @ResponseBody String saveContent(HttpServletRequest req) {
		String result = null;
		try {
			InputStream in = new ByteArrayInputStream(
					req.getParameter("content").getBytes("UTF-8"));
			result = jcrService.saveContent(req.getParameter("folderName"), 
					req.getParameter("fileName"), "text/html", in);
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
