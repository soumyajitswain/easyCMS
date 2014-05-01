package com.easycms.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.JcrConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

@Service
public class JcrContentService {
	final Logger logger = LoggerFactory.getLogger(JcrContentService.class);
	@Autowired
	private JcrTemplate template = null;

	public String saveContent(final String folderName, 
			final String fileName,final String mimeType,final InputStream in) throws RepositoryException {
		String nodeName = (String) template.execute(new JcrCallback() {
			@SuppressWarnings("deprecation")
			public Object doInJcr(Session session) throws IOException,
			RepositoryException {
				Node resultNode = null;
				Node root = session.getRootNode();
				if(!root.hasNode(folderName)) {
					root.addNode(folderName);
				}
				Node fileNode = root.getNode(folderName);
				if(!fileNode.hasNode(fileName)) {
					Node nfileNode = fileNode.addNode(
							fileName, JcrConstants.NT_FILE);
					resultNode = nfileNode.addNode(
							JcrConstants.JCR_CONTENT, JcrConstants.NT_RESOURCE);
					resultNode.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
					resultNode.setProperty(JcrConstants.JCR_ENCODING, "UTF-8");
					resultNode.setProperty(JcrConstants.JCR_DATA, in);
					Calendar lastModified = Calendar.getInstance();
					lastModified.setTimeInMillis(System.currentTimeMillis());
					resultNode.setProperty(JcrConstants.JCR_LASTMODIFIED, lastModified);
					
					session.save();

                    IOUtils.closeQuietly(in);

                    logger.debug("Created '{}' file in folder.", fileName);
				} else {
					resultNode = fileNode.getNode(fileName);
				}
				return resultNode != null?resultNode.getName():null;
			}

		});
		return nodeName;
	}
	public void deleteContent() {

	}
	public Object getContent(final String folderName,final String fileName) {
		  byte[] result = (byte[]) template.execute(new JcrCallback() {
	            @SuppressWarnings({ "deprecation" })
	            public byte[] doInJcr(Session session) throws RepositoryException,
	                    IOException {
	            	Node resultNode = null;
	            	byte[] result = null;
	            	Node root = session.getRootNode();
	            	if(root.hasNode(folderName)) {
	            		Node folderNode = root.getNode(folderName);
	            		if(folderNode.hasNode(fileName)) {
	            			resultNode = folderNode.getNode(fileName).getNode(JcrConstants.JCR_CONTENT);
	            			Property dataProperty = resultNode.getProperty(JcrConstants.JCR_DATA);
	            			result = IOUtils.toByteArray(dataProperty.getStream());
	            		}
	            	}
	            	return result;
	            }
		  });     
		return result;
	}
	public void updateContent() {

	}
}
