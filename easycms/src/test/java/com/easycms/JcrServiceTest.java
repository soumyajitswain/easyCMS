package com.easycms;

import java.io.InputStream;

import javax.jcr.RepositoryException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.easycms.service.JcrContentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/jcr-context.xml"})
public class JcrServiceTest {

	final String folderName="page";
	final String fileName="abc.txt";
	final String mimeType="plain/text";
	
	@Autowired
	private JcrContentService jcrService;
	
	@Test
	public void saveContentTest() {
		InputStream in = this.getClass().getResourceAsStream("/" + fileName);
		try {
			String result=jcrService.saveContent(folderName, fileName, mimeType, in);
			//Assert.assertEquals(result, fileName);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void getContentTest() {
		byte[] result = (byte[]) jcrService.getContent(folderName, fileName);
		System.out.println(new String(result));
	}
}
