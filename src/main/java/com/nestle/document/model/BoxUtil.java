package com.nestle.document.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.BoxUser;
import com.box.sdk.ProgressListener;

public class BoxUtil {

	private static final String DEVELOPER_TOKEN = "x5nNieKMSifGDb4L45popp7kScyBWMJE";
	private static final int MAX_DEPTH = 1;

	public String interactWithBox(BoxAPIConnection api, String file) {
		// Turn off logging to prevent polluting the output.
		Logger.getLogger("com.box.sdk").setLevel(Level.OFF);

		// BoxAPIConnection api = new BoxAPIConnection(DEVELOPER_TOKEN);

		BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
		System.out.format("Welcome, %s <%s>!\n\n", userInfo.getName(), userInfo.getLogin());

		String filePath = file;
		String uploadFileId = uploadFile(filePath, api);

		return uploadFileId;
		// downloadFile(uploadFileId, api);

		// BoxFolder rootFolder = BoxFolder.getRootFolder(api);
		// listFolder(rootFolder, 0);
	}

	private static void listFolder(BoxFolder folder, int depth) {
		for (BoxItem.Info itemInfo : folder) {
			String indent = "";
			for (int i = 0; i < depth; i++) {
				indent += "    ";
			}

			System.out.println(indent + itemInfo.getName());
			if (itemInfo instanceof BoxFolder.Info) {
				BoxFolder childFolder = (BoxFolder) itemInfo.getResource();
				if (depth < MAX_DEPTH) {
					listFolder(childFolder, depth + 1);
				}
			}
		}
	}

	public static String uploadFile(String url, BoxAPIConnection api) {
		String docId = "";
		try {
			BoxFolder rootFolder = BoxFolder.getRootFolder(api);
			FileInputStream stream = new FileInputStream(url);
			Path p = Paths.get(url);
			String fileName = p.getFileName().toString();
			BoxFile.Info newFileInfo = rootFolder.uploadFile(stream, fileName, 1024, new ProgressListener() {
				public void onProgressChanged(long numBytes, long totalBytes) {
					double percentComplete = numBytes / totalBytes;
				}
			});
			docId = newFileInfo.getID();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docId;
	}

	public void updateBoxDocument(BoxAPIConnection api, String docId, String filePath) {
		try {
			BoxFile file = new BoxFile(api, docId);
			FileInputStream stream = new FileInputStream(filePath);
			file.uploadVersion(stream);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String downloadFile(BoxAPIConnection api, String fileId) {

		String fileName = "";
		try {
			// BoxAPIConnection api = new BoxAPIConnection(DEVELOPER_TOKEN);
			// BoxUser.Info userInfo = BoxUser.getCurrentUser(api).getInfo();
			BoxFile file = new BoxFile(api, fileId);
			BoxFile.Info info = file.getInfo();

			FileOutputStream stream = new FileOutputStream(System.getProperty("java.io.tmpdir") + info.getName());
			fileName = info.getName();
			System.out.println("Application server temp path::: " + System.getProperty("java.io.tmpdir"));
			file.download(stream, new ProgressListener() {
				public void onProgressChanged(long numBytes, long totalBytes) {
					double percentComplete = numBytes / totalBytes;
				}
			});
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public static String getFileChecksum(MessageDigest digest, String filePath) throws IOException {
		// Get file input stream for reading the file content
		FileInputStream fis = new FileInputStream(filePath);

		// Create byte array to read data in chunks
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;

		// Read file data and update in message digest
		while ((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		;

		// close the stream; We don't need it now.
		fis.close();

		// Get the hash's bytes
		byte[] bytes = digest.digest();

		// This bytes[] has bytes in decimal format;
		// Convert it to hexadecimal format
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		// return complete hash
		return sb.toString();
	}

}