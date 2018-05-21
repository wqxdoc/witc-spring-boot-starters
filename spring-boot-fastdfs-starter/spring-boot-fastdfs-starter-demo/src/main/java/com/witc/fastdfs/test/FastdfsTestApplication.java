package com.witc.fastdfs.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.luhuiguo.fastdfs.domain.StorePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.luhuiguo.fastdfs.service.FastFileStorageClient;

@SpringBootApplication
public class FastdfsTestApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(FastdfsTestApplication.class);

	@Autowired
	private FastFileStorageClient storageClient;

	public static void main(String[] args) {
		SpringApplication.run(FastdfsTestApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		String filePath = testUpload();
		System.out.println(filePath);
		testDownload(filePath);
		// testDelete(filePath);testDownload
		// testDownload(filePath);
	}

	private String testUpload() throws IOException {
		String path = this.getClass().getClassLoader().getResource("").getPath();
		File testFile = new File(path + "/dddd.pdf");
		InputStream input = null;
		try {
			input = new FileInputStream(testFile);
		} catch (FileNotFoundException e) {
			logger.error("读取文件异常！" + e);
		}
		byte[] byt = new byte[input.available()];
		input.read(byt);
		StorePath storePath = storageClient.uploadFile(byt, "pdf");
		String uploadPath = storePath.getFullPath();
		logger.info("group:{},path:{}", storePath.getGroup(), storePath.getPath());
		logger.info("uploadPath:{}", uploadPath);
		return uploadPath;
	}

	@SuppressWarnings("unused")
	private void testDownload(String filePath) {
		byte[] bytes = null;
		try {
			// 获取meta信息
			// Set<MetaData> metaMap = storageClient.getMetadata("group1",
			// filePath);
			bytes = storageClient.downloadFile("group1", "M00/00/22/wKgB9Fr5YAiAcHFGAAbJS4awCOM217.pdf");
			if (bytes == null) {
				logger.error("文件不存在！");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("下载失败！" + e);
		}
		BufferedOutputStream bufferedOutputStream = null;
		try {
			File file = new File("D://wKgB9Fr5YAiAcHFGAAbJS4awCOM217.pdf");
			bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			bufferedOutputStream.write(bytes);
		} catch (Exception e) {
			logger.error("保存文件失败！" + e);
		} finally {
			if (bufferedOutputStream != null) {
				try {
					bufferedOutputStream.close();
				} catch (IOException e) {
					logger.error("关闭输出流失败！" + e);
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void testDelete(String filePath) {
		try {
			storageClient.deleteFile(filePath);
		} catch (Exception e) {
			logger.error("删除失败！" + e);
		}
	}
}
