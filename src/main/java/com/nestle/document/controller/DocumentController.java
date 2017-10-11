package com.nestle.document.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.box.sdk.BoxAPIConnection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nestle.document.dao.RejectionCodeDao;
import com.nestle.document.model.BoxUtil;
import com.nestle.document.model.Document;
import com.nestle.document.model.Item;
import com.nestle.document.model.RejectionCode;
import com.nestle.document.model.Status;
import com.nestle.document.model.Transaction;
import com.nestle.document.service.DocumentService;
import com.nestle.fabric.TestAPI;

@Controller
public class DocumentController {

	@Autowired
	private DocumentService documentService;
	@Autowired
	private RejectionCodeDao rejectionCodeDao;

	@GetMapping("initiated")
	public String fetchInitiatedDocs(Model model, HttpSession session) {

		System.out.println("Working!!!!!!!!");
		if (session.getAttribute("userRole").equals("warehouse")) {
			List<Document> docList = documentService.searchDocumentByStatus("Initiated");

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();

			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getReasonCode().equals("Document Error")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());

			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				System.out.println("GET METHOD INITIATED estimatedTime ===" + estimatedTime);
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);
			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("action", "initiated");

			return "warehouse";
		} else if (session.getAttribute("userRole").equals("thirdparty")) {
			List<Document> docList = documentService.searchDocumentByStatus("Initiated");

			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);
			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Initiated")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());

			List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
			model.addAttribute("rejections", rejections);
			model.addAttribute("action", "initiated");

			return "thirdparty";
		} else {
			List<Document> docList = documentService.searchDocumentByStatus("Initiated");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			System.out.println("SIZE OF HIGH PRIO LIST IN GET METHOD === " + highpriorityDocList.size());
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Processed")) {
					highpriorityDocList.add(document);
				}
			}

			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());

			List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
			model.addAttribute("rejections", rejections);
			model.addAttribute("action", "initiated");

			return "custom";
		}
	}

	@GetMapping("verified")
	public String fetchVerifiedDocs(Model model, HttpSession session) {

		System.out.println("Working!!!!!!!!");
		if (session.getAttribute("userRole").equals("warehouse")) {
			List<Document> docList = documentService.searchDocumentByStatus("Verified");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getReasonCode().equals("Document Error")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());

			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("action", "verified");

			return "warehouse";
		} else if (session.getAttribute("userRole").equals("thirdparty")) {
			List<Document> docList = documentService.searchDocumentByStatus("Verified");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Initiated")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());

			List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
			model.addAttribute("rejections", rejections);
			model.addAttribute("action", "verified");

			return "thirdparty";
		} else {
			List<Document> docList = documentService.searchDocumentByStatus("Verified");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			System.out.println("SIZE OF HIGH PRIO LIST IN GET METHOD === " + highpriorityDocList.size());
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Processed")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());

			List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
			model.addAttribute("rejections", rejections);
			model.addAttribute("action", "verified");

			return "custom";
		}
	}

	@GetMapping("processed")
	public String fetchProcessedDocs(Model model, HttpSession session) {
		System.out.println("Working!!!!!!!!");
		if (session.getAttribute("userRole").equals("warehouse")) {
			List<Document> docList = documentService.searchDocumentByStatus("Processed");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getReasonCode().equals("Document Error")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			model.addAttribute("action", "processed");

			return "warehouse";
		} else if (session.getAttribute("userRole").equals("thirdparty")) {
			List<Document> docList = documentService.searchDocumentByStatus("Processed");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
			model.addAttribute("rejections", rejections);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Initiated")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			model.addAttribute("action", "processed");

			return "thirdparty";
		} else {
			List<Document> docList = documentService.searchDocumentByStatus("Processed");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
			model.addAttribute("rejections", rejections);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			System.out.println("SIZE OF HIGH PRIO LIST IN GET METHOD === " + highpriorityDocList.size());
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Processed")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			model.addAttribute("action", "processed");

			return "custom";
		}
	}

	@GetMapping("rejected")
	public String fetchRejectedDocs(Model model, HttpSession session) {
		System.out.println("Working!!!!!!!!");
		if (session.getAttribute("userRole").equals("warehouse")) {
			// Map<String, String> rejectCodeIDMap = new HashMap<String,
			// String>();
			List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
			// for (RejectionCode rejectionCode : rejections) {
			// rejectCodeIDMap.put(rejectionCode.getRejection_description(),
			// String.valueOf(rejectionCode.getRejection_Id()));
			// }
			model.addAttribute("rejections", rejections);
			List<Document> docList = documentService.searchDocumentByStatus("Rejected");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);
			List<String> updatedByList = new ArrayList<String>();
			for (com.nestle.document.model.Document document : docList) {
				String docId = document.getDocumentId();
				if (document.getStatus().equals("Rejected")) {
					String doctrxHistList = TestAPI.viewDocumentTrxHistory(docId);
					Gson myGson = new Gson();
					JsonParser jsonParser = new JsonParser();
					JsonArray docTrxArray = jsonParser.parse(doctrxHistList).getAsJsonArray();
					for (JsonElement docTrx : docTrxArray) {
						Transaction transaction = myGson.fromJson(docTrx, Transaction.class);
						if (transaction.getStatus().equals("Rejected")) {
							updatedByList.add(transaction.getUpdatedBy());
							System.out.println("List of Rejected By in if ::::: " + transaction.getUpdatedBy());
							break;
						} else
							continue;
					}
				} else
					updatedByList.add("NA");

			}
			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("updatedByList", updatedByList);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getReasonCode().equals("Document Error")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			model.addAttribute("action", "rejected");

			return "warehouse";
		} else if (session.getAttribute("userRole").equals("thirdparty")) {
			List<Document> docList = documentService.searchDocumentByStatus("Rejected");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			List<String> updatedByList = new ArrayList<String>();
			for (com.nestle.document.model.Document document : docList) {
				String docId = document.getDocumentId();
				if (document.getStatus().equals("Rejected")) {
					String doctrxHistList = TestAPI.viewDocumentTrxHistory(docId);
					Gson myGson = new Gson();
					JsonParser jsonParser = new JsonParser();
					JsonArray docTrxArray = jsonParser.parse(doctrxHistList).getAsJsonArray();
					for (JsonElement docTrx : docTrxArray) {
						Transaction transaction = myGson.fromJson(docTrx, Transaction.class);
						if (transaction.getStatus().equals("Rejected")) {
							updatedByList.add(transaction.getUpdatedBy());
							System.out.println("List of Rejected By in if ::::: " + transaction.getUpdatedBy());
							break;
						} else
							continue;
					}
				} else
					updatedByList.add("NA");

			}

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Initiated")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("updatedByList", updatedByList);
			model.addAttribute("action", "rejected");

			return "thirdparty";
		} else {
			List<Document> docList = documentService.searchDocumentByStatus("Rejected");
			for (Document document : docList) {
				String viewTruckDetailsByDocId = "";
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(document.getDocumentId());
				viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1, viewTruckDetailsByDocId.length() - 1);
				System.out.println("Truck details by doc iD:::: " + viewTruckDetailsByDocId);
				JsonParser jsonParser1 = new JsonParser();
				JsonObject jsonObject = (JsonObject) jsonParser1.parse(viewTruckDetailsByDocId);
				String estimatedTime = jsonObject.get("estimatedTime").getAsString();
				if (!estimatedTime.equals("")) {
					double input = Double.valueOf(estimatedTime);
					int hours = (int) (input / 3600);
					int minutes = (int) ((input % 3600) / 60);
					int seconds = (int) ((input % 3600) % 60);
					String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					System.out.println(timeString);
					document.setEstimatedTime(timeString);
				} else
					document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			List<String> updatedByList = new ArrayList<String>();
			for (com.nestle.document.model.Document document : docList) {
				String docId = document.getDocumentId();
				if (document.getStatus().equals("Rejected")) {
					String doctrxHistList = TestAPI.viewDocumentTrxHistory(docId);
					Gson myGson = new Gson();
					JsonParser jsonParser = new JsonParser();
					JsonArray docTrxArray = jsonParser.parse(doctrxHistList).getAsJsonArray();
					for (JsonElement docTrx : docTrxArray) {
						Transaction transaction = myGson.fromJson(docTrx, Transaction.class);
						if (transaction.getStatus().equals("Rejected")) {
							updatedByList.add(transaction.getUpdatedBy());
							System.out.println("List of Rejected By in if ::::: " + transaction.getUpdatedBy());
							break;
						} else
							continue;
					}
				} else
					updatedByList.add("NA");

			}
			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			System.out.println("SIZE OF HIGH PRIO LIST IN GET METHOD === " + highpriorityDocList.size());
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Processed")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("updatedByList", updatedByList);
			model.addAttribute("action", "rejected");

			return "custom";
		}
	}

	@GetMapping("approved")
	public String fetchApprovedDocs(Model model, HttpSession session) {
		System.out.println("Working!!!!!!!!");
		if (session.getAttribute("userRole").equals("warehouse")) {
			List<Document> docList = documentService.searchDocumentByStatus("Approved");
			for (Document document : docList) {
				document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getReasonCode().equals("Document Error")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());

			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("action", "approved");

			return "warehouse";
		} else if (session.getAttribute("userRole").equals("thirdparty")) {
			List<Document> docList = documentService.searchDocumentByStatus("Approved");
			for (Document document : docList) {
				document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Initiated")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());

			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("action", "approved");

			return "thirdparty";
		} else {
			List<Document> docList = documentService.searchDocumentByStatus("Approved");
			for (Document document : docList) {
				document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			System.out.println("SIZE OF HIGH PRIO LIST IN GET METHOD === " + highpriorityDocList.size());
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Processed")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());

			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("action", "approved");

			return "custom";
		}
	}

	@GetMapping("received")
	public String fetchReceivedDocs(Model model, HttpSession session) {
		System.out.println("Working!!!!!!!!");
		if (session.getAttribute("userRole").equals("warehouse")) {
			List<Document> docList = documentService.searchDocumentByStatus("Received");
			for (Document document : docList) {
				document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getReasonCode().equals("Document Error")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			model.addAttribute("action", "received");

			return "warehouse";
		} else if (session.getAttribute("userRole").equals("thirdparty")) {
			List<Document> docList = documentService.searchDocumentByStatus("Received");
			for (Document document : docList) {
				document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Initiated")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			model.addAttribute("action", "received");

			return "thirdparty";
		} else {
			List<Document> docList = documentService.searchDocumentByStatus("Received");
			for (Document document : docList) {
				document.setEstimatedTime("NA");
			}
			model.addAttribute("documents", docList);

			Status status = documentService.countStatus();
			model.addAttribute("status", status);

			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			System.out.println("SIZE OF HIGH PRIO LIST IN GET METHOD === " + highpriorityDocList.size());
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Processed")) {
					highpriorityDocList.add(document);
				}
			}
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			model.addAttribute("action", "received");

			return "custom";
		}
	}

	@PostMapping("updateStatus")
	public String updateStatusDocs(Model model, @RequestParam("id") String id, @RequestParam("status") String status,
			@RequestParam("reasonCode") String reasonCode, HttpServletRequest request, HttpSession session) {
		String updatedBy = request.getParameter("username");
		String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
		String statusUpdate = documentService.updateStatus(id, status, timeStamp, updatedBy, reasonCode);
		if (session.getAttribute("userRole").equals("warehouse")) {
			String redirectURL = "redirect:/" + request.getParameter("flag");
			System.out.println("redirectURL ::: " + redirectURL);
			System.out.println("DocumentController :: updateStatusDocs :: id = " + id + "status = " + status);
			if (statusUpdate == "success") {
				Status currentStatus = documentService.countStatus();
				model.addAttribute("statusUpdate", "success");
				model.addAttribute("status", currentStatus);
				model.addAttribute("id", id);
				return redirectURL;
				// return "redirect:/warehouseLogin";
			} else {
				model.addAttribute("statusUpdate", "failure");
				return redirectURL;
				// return "redirect:/warehouseLogin";
			}
		} else if (session.getAttribute("userRole").equals("thirdparty")) {
			System.out.println("DocumentController :: updateStatusDocs :: id = " + id + "status = " + status);
			String redirectURL = "";
			if ((request.getParameter("flag")).equals("highPriority")) {
				redirectURL = "redirect:/thirdpartyLogin";
			} else if ((request.getParameter("flag")).equals("initiated")) {
				redirectURL = "redirect:/initiated";
			}
			System.out.println("redirectURL ::: " + redirectURL);
			if (statusUpdate == "success") {
				System.out.println("User updated by ===" + updatedBy);
				Status currentStatus = documentService.countStatus();
				model.addAttribute("statusUpdate", "success");
				model.addAttribute("status", currentStatus);
				model.addAttribute("id", id);
				// return "redirect:/initiated";
				return redirectURL;
			} else {
				model.addAttribute("statusUpdate", "failure");
				// return "redirect:/initiated";
				return redirectURL;
			}
		} else {
			System.out.println("DocumentController :: updateStatusDocs :: id = " + id + "status = " + status);
			String redirectFlag = request.getParameter("flag");
			String redirectURL = "";
			if (redirectFlag.equals("highPriority")) {
				redirectURL = "redirect:/customLogin";
			} else if (redirectFlag.equals("Verified")) {
				redirectURL = "redirect:/verified";
			} else if (redirectFlag.equals("Processed")) {
				redirectURL = "redirect:/processed";
			}
			if (statusUpdate == "success") {
				System.out.println("User ===" + updatedBy);
				Status currentStatus = documentService.countStatus();
				model.addAttribute("statusUpdate", "success");
				model.addAttribute("status", currentStatus);
				model.addAttribute("id", id);
				return redirectURL;
				// return "redirect:/customLogin";
			} else {
				model.addAttribute("statusUpdate", "failure");
				return redirectURL;
				// return "redirect:/customLogin";
			}
		}

	}

	@GetMapping("createNewDoc")
	public String createCBPage(Model model, HttpSession session) {
		session.getAttribute("username");
		return "createNewDoc";
	}

	@GetMapping("updateDoc")
	public String updateCBPage(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam("id") String id) {
		session.getAttribute("username");
		// String docuID = request.getParameter("searchDocId");
		List<Document> docsbyId = documentService.searchDocumentWithID(id);
		List<Item> items = documentService.getItemListByDocumentID(id);
		model.addAttribute("docsbyId", docsbyId);
		model.addAttribute("items", items);
		model.addAttribute("documentId", id);
		System.out.println("New Page!!!!");
		return "updateDoc";
	}

	@GetMapping("searchDocument")
	public String searchDocPage(Model model, HttpSession session) {
		session.getAttribute("username");
		session.getAttribute("userRole");
		return "searchDocument";
	}

	@GetMapping("openDoc")
	public void opebDocs(Model model, HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws Exception {

		if (request.getParameter("code") == null) {
			List<String> arg3 = new ArrayList<String>();
			URI url = null;
			try {
				String docId = request.getParameter("docId");
				String serverURL = request.getRequestURL().toString();
				// System.out.println("serverURL ===" + serverURL);
				String documentURL = serverURL + "?docId=";
				// System.out.println("documentURL===" + documentURL);
				url = new URI(documentURL + docId);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			URL authURL = BoxAPIConnection.getAuthorizationURL("rl0okd37q0nm7ssnbb5x05k0efq1mt5i", url, "authenticated",
					arg3);
			String urlStr = authURL.toString();
			System.out.println("AUTHORIZATION URL ::::::: " + urlStr);
			response.sendRedirect(urlStr);
		} else {
			String code = request.getParameter("code");
			BoxAPIConnection api = new BoxAPIConnection("rl0okd37q0nm7ssnbb5x05k0efq1mt5i",
					"WONwEVxGlyGbzo0juyHftfj1rW0VLHc5", code);
			String docId = request.getParameter("docId");
			String fileName = BoxUtil.downloadFile(api, docId);
			System.out.println("docId===" + docId + "aND dOCnAME :::: " + fileName);
			String downloadName = "";
			String downloadFileHash = "";
			String blockchainFileHash = "";
			try {
				MessageDigest md5Digest = MessageDigest.getInstance("MD5");
				downloadName = System.getProperty("java.io.tmpdir") + fileName;
				downloadFileHash = BoxUtil.getFileChecksum(md5Digest, downloadName);
				String getFileDetailsFromBlockchain = TestAPI.getDetailsByDocumetId(docId);
				getFileDetailsFromBlockchain = getFileDetailsFromBlockchain.replace("[", "");
				getFileDetailsFromBlockchain = getFileDetailsFromBlockchain.replace("]", "");
				Gson gson = new Gson();
				Document document = gson.fromJson(getFileDetailsFromBlockchain, Document.class);
				blockchainFileHash = document.getDocHash();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (downloadFileHash.equals(blockchainFileHash)) {
				response.setContentType("text/html;charset=UTF-8");
				ServletOutputStream outs = response.getOutputStream();
				response.setContentType("application/pdf"); // MIME type for pdf
															// doc
				File file = new File(System.getProperty("java.io.tmpdir") + fileName);
				response.setHeader("Content-disposition", "inline; filename=" + fileName);
				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;
				try {

					InputStream isr = new FileInputStream(file);
					bis = new BufferedInputStream(isr);
					bos = new BufferedOutputStream(outs);
					byte[] buff = new byte[2048];
					int bytesRead;
					// Simple read/write loop.
					while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
						bos.write(buff, 0, bytesRead);
					}

				} catch (Exception e) {
					System.out.println("Exception ----- Message ---" + e);
				} finally {
					if (bis != null)
						bis.close();
					if (bos != null)
						bos.close();
					File tempFile = new File(downloadName);
					tempFile.delete();
					System.out.println("File deleted successfully");
				}

			}

			else
				System.out.println("File altered in Content management server");
		}

	}

	@GetMapping("showTrxHistory")
	public String showTrxHistory(Model model, HttpSession session, HttpServletRequest request) {
		String docuID = request.getParameter("searchDocId").replaceAll("\\s+", "");
		List<Transaction> trxHistory = documentService.searchDocumentByID(docuID);
		String statusList = "";
		for (Transaction transaction : trxHistory) {
			statusList += transaction.getUpdatedBy() + "_";
			statusList += transaction.getStatus() + ",";
		}
		List<Document> docsbyId = documentService.searchDocumentWithID(docuID);
		String lastStatus = "";
		for (Document document : docsbyId) {
			lastStatus = document.getStatus();
		}
		model.addAttribute("docsbyId", docsbyId);
		model.addAttribute("trxHistory", trxHistory);
		model.addAttribute("statusList", statusList);
		model.addAttribute("lastStatus", lastStatus);
		return "transactionHistory";
	}
}