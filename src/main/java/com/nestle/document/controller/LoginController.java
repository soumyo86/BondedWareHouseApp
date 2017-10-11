package com.nestle.document.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.box.sdk.BoxAPIConnection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nestle.document.dao.LoginDao;
import com.nestle.document.dao.RejectionCodeDao;
import com.nestle.document.model.BoxUtil;
import com.nestle.document.model.Item;
import com.nestle.document.model.RejectionCode;
import com.nestle.document.model.Status;
import com.nestle.document.model.Transaction;
import com.nestle.document.service.DocumentService;
import com.nestle.fabric.TestAPI;

@Controller
public class LoginController {

	@Autowired
	private LoginDao loginDao;
	@Autowired
	private RejectionCodeDao rejectionCodeDao;
	@Autowired
	private DocumentService documentService;

	private Status status;

	/*
	 * @PostMapping("warehouseLogin") public String warehouseLogin(Model model
	 * , @RequestParam("username") String username , @RequestParam("password")
	 * String password) { if(documentService.validateLogin(username, password))
	 * { model.addAttribute("username", username);
	 * model.addAttribute("password", password); return "welcome"; } else {
	 * return "fail"; } }
	 */

	/*
	 * @GetMapping("allLoginPage") public String allLoginAction(Model model,
	 * HttpSession session) { return "index"; }
	 */

	@GetMapping("home")
	public String home(Model model, HttpSession session) {
		return "home";
	}

	@GetMapping("warehouseLogin")
	public String warehousePageAction(Model model, HttpSession session) {
		if (session.getAttribute("username") != null) {
			status = documentService.countStatus();
			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();

			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			System.out.println("SIZE OF HIGH PRIO LIST IN GET METHOD === " + highpriorityDocList.size());
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getReasonCode().equals("Document Error")) {
					highpriorityDocList.add(document);
				}
			}

			List<String> updatedByList = new ArrayList<String>();
			String viewTruckDetailsByDocId = "";
			for (com.nestle.document.model.Document document : highpriorityDocList) {
				String docId = document.getDocumentId();
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(docId);
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
				if (document.getStatus().equals("Rejected")) {
					String doctrxHistList = TestAPI.viewDocumentTrxHistory(docId);
					Gson myGson = new Gson();
					JsonParser jsonParser = new JsonParser();
					JsonArray docTrxArray = jsonParser.parse(doctrxHistList).getAsJsonArray();
					for (JsonElement docTrx : docTrxArray) {
						Transaction transaction = myGson.fromJson(docTrx, Transaction.class);
						if (transaction.getStatus().equals("Rejected")) {
							updatedByList.add(transaction.getUpdatedBy());
							System.out.println("List of Rejected By in if GET ::::: " + transaction.getUpdatedBy());
							break;
						} else
							continue;
					}
				} else
					updatedByList.add("NA");

			}
			model.addAttribute("documents", highpriorityDocList);
			model.addAttribute("status", status);
			model.addAttribute("action", "warehouseLogin");
			model.addAttribute("updatedByList", updatedByList);
			model.addAttribute("username", session.getAttribute("username"));
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			session.getAttribute("username");
			session.getAttribute("userRole");
			return "warehouse";
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("thirdpartyLogin")
	public String thirdPartyPageAction(Model model, HttpSession session) {
		if (session.getAttribute("username") != null) {
			status = documentService.countStatus();
			model.addAttribute("status", status);
			session.getAttribute("username");
			session.getAttribute("userRole");
			// List<com.nestle.document.model.Document> highpriorityDocList =
			// documentService.gethighPriorityDoument();
			List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
			List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
			for (com.nestle.document.model.Document document : tempDocList) {
				if (document.getStatus().equals("Initiated")) {
					highpriorityDocList.add(document);
				}
			}
			System.out.println("SIZE OF HIGH PRIO LIST IN GET METHOD === " + highpriorityDocList.size());
			String viewTruckDetailsByDocId = "";
			List<String> updatedByList = new ArrayList<String>();
			for (com.nestle.document.model.Document document : highpriorityDocList) {
				String docId = document.getDocumentId();
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(docId);
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
				if (document.getStatus().equals("Rejected")) {
					String doctrxHistList = TestAPI.viewDocumentTrxHistory(docId);
					Gson myGson = new Gson();
					JsonParser jsonParser = new JsonParser();
					JsonArray docTrxArray = jsonParser.parse(doctrxHistList).getAsJsonArray();
					for (JsonElement docTrx : docTrxArray) {
						Transaction transaction = myGson.fromJson(docTrx, Transaction.class);
						if (transaction.getStatus().equals("Rejected")) {
							updatedByList.add(transaction.getUpdatedBy());
							System.out.println("List of Rejected By in if GET ::::: " + transaction.getUpdatedBy());
							break;
						} else
							continue;
					}
				} else
					updatedByList.add("NA");

			}
			model.addAttribute("documents", highpriorityDocList);
			model.addAttribute("action", "thirdpartyLogin");
			model.addAttribute("updatedByList", updatedByList);
			model.addAttribute("username", session.getAttribute("username"));

			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
			model.addAttribute("rejections", rejections);
			return "thirdparty";
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("customLogin")
	public String customsPageAction(Model model, HttpSession session) {
		if (session.getAttribute("username") != null) {
			status = documentService.countStatus();
			model.addAttribute("status", status);
			session.getAttribute("username");
			session.getAttribute("userRole");

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
			String viewTruckDetailsByDocId = "";
			List<String> updatedByList = new ArrayList<String>();
			for (com.nestle.document.model.Document document : highpriorityDocList) {
				String docId = document.getDocumentId();
				viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(docId);
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
				if (document.getStatus().equals("Rejected")) {
					String doctrxHistList = TestAPI.viewDocumentTrxHistory(docId);
					Gson myGson = new Gson();
					JsonParser jsonParser = new JsonParser();
					JsonArray docTrxArray = jsonParser.parse(doctrxHistList).getAsJsonArray();
					for (JsonElement docTrx : docTrxArray) {
						Transaction transaction = myGson.fromJson(docTrx, Transaction.class);
						if (transaction.getStatus().equals("Rejected")) {
							updatedByList.add(transaction.getUpdatedBy());
							System.out.println("List of Rejected By in if GET ::::: " + transaction.getUpdatedBy());
							break;
						} else
							continue;
					}
				} else
					updatedByList.add("NA");

			}
			model.addAttribute("documents", highpriorityDocList);
			model.addAttribute("action", "customLogin");
			model.addAttribute("updatedByList", updatedByList);
			model.addAttribute("username", session.getAttribute("username"));

			Status status = documentService.countStatus();
			model.addAttribute("status", status);
			model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
			List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
			model.addAttribute("rejections", rejections);
			return "custom";
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("logout")
	public String logout(HttpSession session, Model model) {
		session.invalidate();
		return "redirect:/";
	}

	@PostMapping("warehouseLogin")
	public String warehouseLogin(Model model, @RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		if (loginDao.validateLogin(username, password)) {
			String userRole = loginDao.getRoleByUserName(username);
			System.out.println("CHK 1 == " + userRole);
			String submittedRole = request.getParameter("warehouse");
			System.out.println("CHK 2 == " + submittedRole);
			if (submittedRole.equals(userRole)) {
				status = documentService.countStatus();
				model.addAttribute("status", status);
				session.setAttribute("username", username);
				/*
				 * String userRole = loginDao.getRoleByUserName(username);
				 * System.out.println("ROLE ==" + userRole); if
				 * (userRole.equals("warehouse")) {
				 */
				session.setAttribute("userRole", userRole);
				List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
				List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
				for (com.nestle.document.model.Document document : tempDocList) {
					if (document.getReasonCode().equals("Document Error")) {
						highpriorityDocList.add(document);
					}
				}
				System.out.println("SIZE OF HIGH PRIO LIST IN POST METHOD === " + highpriorityDocList.size());
				String viewTruckDetailsByDocId = "";
				List<String> updatedByList = new ArrayList<String>();
				for (com.nestle.document.model.Document document : highpriorityDocList) {
					System.out.println("Doc Status 18 July === " + document.getStatus());
					System.out.println("Doc Status 18 July === " + document.getReasonCode());
					String docId = document.getDocumentId();
					viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(docId);
					viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1,
							viewTruckDetailsByDocId.length() - 1);
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
					if (document.getStatus().equals("Rejected")) {
						String doctrxHistList = TestAPI.viewDocumentTrxHistory(docId);
						// System.out.println("HISTORY : : :" + doctrxHist);
						Gson myGson = new Gson();
						JsonParser jsonParser = new JsonParser();
						JsonArray docTrxArray = jsonParser.parse(doctrxHistList).getAsJsonArray();
						// Transaction transaction = new Transaction();
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
				model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
				model.addAttribute("updatedByList", updatedByList);
				model.addAttribute("action", "warehouseLogin");
				model.addAttribute("documents", highpriorityDocList);
				model.addAttribute("username", session.getAttribute("username"));
				Status status = documentService.countStatus();
				model.addAttribute("status", status);
				return "warehouse";
			} else {
				String errorStatus = "Access denied!! Please select your right Role!!";
				model.addAttribute("errorStatus", errorStatus);
				redirectAttributes.addFlashAttribute("errorStatus", errorStatus);
				return "redirect:/home";
			}
		} else {
			String errorStatus = "Login failed due to invalid credentials!! Please put your right credentials!!";
			model.addAttribute("errorStatus", errorStatus);
			redirectAttributes.addFlashAttribute("errorStatus", errorStatus);
			return "redirect:/home";
		}

	}

	@PostMapping("customLogin")
	public String customLogin(Model model, @RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		// List<String> rejectionCode = new ArrayList<String>();
		// List<String> rejectionDesc = new ArrayList<String>();
		if (loginDao.validateLogin(username, password)) {
			String userRole = loginDao.getRoleByUserName(username);
			System.out.println("CHK 1 == " + userRole);
			String submittedRole = request.getParameter("customs");
			System.out.println("CHK 2 == " + submittedRole);
			if (submittedRole.equals(userRole)) {
				model.addAttribute("username", username);
				model.addAttribute("password", password);
				status = documentService.countStatus();
				model.addAttribute("status", status);
				System.out.println("ROLE ==" + userRole);
				/* if (userRole.equals("customs")) { */
				session.setAttribute("userRole", userRole);
				session.setAttribute("username", username);
				List<com.nestle.document.model.Document> docList = documentService.searchDocumentByStatus("Processed");
				model.addAttribute("documents", docList);

				// List<com.nestle.document.model.Document> highpriorityDocList
				// = documentService.gethighPriorityDoument();
				List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
				System.out.println("SIZE OF HIGH PRIO LIST IN GET METHOD === " + highpriorityDocList.size());
				List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
				for (com.nestle.document.model.Document document : tempDocList) {
					if (document.getStatus().equals("Processed")) {
						highpriorityDocList.add(document);
					}
				}
				System.out.println("SIZE OF HIGH PRIO LIST IN POST METHOD === " + highpriorityDocList.size());
				String viewTruckDetailsByDocId = "";
				List<String> updatedByList = new ArrayList<String>();
				for (com.nestle.document.model.Document document : highpriorityDocList) {
					System.out.println("Doc Status 18 July === " + document.getStatus());
					System.out.println("Doc Status 18 July === " + document.getReasonCode());
					String docId = document.getDocumentId();
					viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(docId);
					viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1,
							viewTruckDetailsByDocId.length() - 1);
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
					if (document.getStatus().equals("Rejected")) {
						String doctrxHistList = TestAPI.viewDocumentTrxHistory(docId);
						// System.out.println("HISTORY : : :" + doctrxHist);
						Gson myGson = new Gson();
						JsonParser jsonParser = new JsonParser();
						JsonArray docTrxArray = jsonParser.parse(doctrxHistList).getAsJsonArray();
						// Transaction transaction = new Transaction();
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
				model.addAttribute("documents", highpriorityDocList);
				model.addAttribute("updatedByList", updatedByList);

				Status status = documentService.countStatus();
				model.addAttribute("status", status);
				model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
				List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
				model.addAttribute("rejections", rejections);
				model.addAttribute("action", "customLogin");
				model.addAttribute("username", session.getAttribute("username"));
				return "custom";
			}
			/* } */
			else {
				String errorStatus = "Access denied!! Please select your right Role!!";
				model.addAttribute("errorStatus", errorStatus);
				redirectAttributes.addFlashAttribute("errorStatus", errorStatus);
				return "redirect:/home";
			}
		} else {
			String errorStatus = "Login failed due to invalid credentials!! Please put your right credentials!!";
			model.addAttribute("errorStatus", errorStatus);
			redirectAttributes.addFlashAttribute("errorStatus", errorStatus);
			return "redirect:/home";
		}
	}

	@PostMapping("thirdpartyLogin")
	public String thirdpartyLogin(Model model, @RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		if (loginDao.validateLogin(username, password)) {
			String userRole = loginDao.getRoleByUserName(username);
			System.out.println("CHK 1 == " + userRole);
			String submittedRole = request.getParameter("thirdparty");
			System.out.println("CHK 2 == " + submittedRole);
			if (submittedRole.equals(userRole)) {
				status = documentService.countStatus();
				model.addAttribute("status", status);
				model.addAttribute("username", username);
				model.addAttribute("password", password);
				/*
				 * String userRole = loginDao.getRoleByUserName(username);
				 * System.out.println("ROLE ==" + userRole); if
				 * (userRole.equals("thirdparty")) {
				 */
				session.setAttribute("userRole", userRole);
				session.setAttribute("username", username);
				model.addAttribute("action", "thirdpartyLogin");
				// List<com.nestle.document.model.Document> highpriorityDocList
				// = documentService.gethighPriorityDoument();
				List<com.nestle.document.model.Document> highpriorityDocList = new ArrayList<com.nestle.document.model.Document>();
				List<com.nestle.document.model.Document> tempDocList = documentService.gethighPriorityDoument();
				for (com.nestle.document.model.Document document : tempDocList) {
					if (document.getStatus().equals("Initiated")) {
						highpriorityDocList.add(document);
					}
				}
				System.out.println("SIZE OF HIGH PRIO LIST IN POST METHOD === " + highpriorityDocList.size());
				String viewTruckDetailsByDocId = "";
				List<String> updatedByList = new ArrayList<String>();
				for (com.nestle.document.model.Document document : highpriorityDocList) {
					String docId = document.getDocumentId();
					viewTruckDetailsByDocId = TestAPI.viewTruckDetailsByDocId(docId);
					viewTruckDetailsByDocId = viewTruckDetailsByDocId.substring(1,
							viewTruckDetailsByDocId.length() - 1);
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
					if (document.getStatus().equals("Rejected")) {
						String doctrxHistList = TestAPI.viewDocumentTrxHistory(docId);
						// System.out.println("HISTORY : : :" + doctrxHist);
						Gson myGson = new Gson();
						JsonParser jsonParser = new JsonParser();
						JsonArray docTrxArray = jsonParser.parse(doctrxHistList).getAsJsonArray();
						// Transaction transaction = new Transaction();
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
				model.addAttribute("documents", highpriorityDocList);
				model.addAttribute("updatedByList", updatedByList);
				model.addAttribute("highpriorityDocListCount", highpriorityDocList.size());
				Status status = documentService.countStatus();
				model.addAttribute("status", status);
				model.addAttribute("username", session.getAttribute("username"));

				List<RejectionCode> rejections = rejectionCodeDao.getAllRejection();
				model.addAttribute("rejections", rejections);
				return "thirdparty";
			} else {
				String errorStatus = "Access denied!! Please select your right Role!!";
				model.addAttribute("errorStatus", errorStatus);
				redirectAttributes.addFlashAttribute("errorStatus", errorStatus);
				return "redirect:/home";
			}
		} else {
			String errorStatus = "Login failed due to invalid credentials!! Please put your right credentials!!";
			model.addAttribute("errorStatus", errorStatus);
			redirectAttributes.addFlashAttribute("errorStatus", errorStatus);
			return "redirect:/home";
		}
	}

	@PostMapping("createcbpdf")
	public void createPDFDoc(HttpSession session, ModelMap modelMap, @ModelAttribute("pdfbean") Item pdfbean,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String source = request.getParameter("source");
		String destination = request.getParameter("destination");
		String deliveryDate = request.getParameter("deliveryDate");
		String deliveryNo = request.getParameter("deliveryNo");
		String custPO = request.getParameter("custPO");
		String truckId = request.getParameter("truckId");
		String customLocation = request.getParameter("customLocation");
		String userName = request.getParameter("username");
		System.out.println("username= " + userName);
		int noOfRows = 0;
		if (request.getParameter("tableRowCount").equals("")) {
			noOfRows = 1;
		} else {
			String tableRowCount = request.getParameter("tableRowCount");
			noOfRows = Integer.valueOf(tableRowCount);
		}
		String FILE = System.getProperty("java.io.tmpdir") + "CBPdf_" + System.currentTimeMillis() + ".pdf";
		String fileName = "CBPdf_" + System.currentTimeMillis() + ".pdf";
		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
		Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
		Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();

		List<String> arg3 = new ArrayList<String>();
		URI url = null;
		List<Item> itemList = new ArrayList<Item>();
		try {
			itemList = pdfCreate(request, source, destination, deliveryDate, deliveryNo, custPO, truckId,
					customLocation, noOfRows, FILE, subFont, smallBold, dateFormat, date);
			String checksum = "";
			MessageDigest md5Digest = MessageDigest.getInstance("MD5");
			checksum = BoxUtil.getFileChecksum(md5Digest, FILE);
			String serverURL = request.getRequestURL().toString();
			String str = serverURL;
			str = str.replace("createcbpdf", "");
			// System.out.println("MY_URL" + str);
			// url = new URI(str + "createBoxDocument?source=" + source +
			// "&destination=" + destination + "&userName="
			// + userName + "&fileName=" + fileName + "&fileHash=" + checksum);

			url = new URI(str + "createBoxDocument?" + "deliveryDate=" + deliveryDate + "&deliveryNo=" + deliveryNo
					+ "&custPO=" + custPO + "&userName=" + userName + "&fileName=" + fileName + "&fileHash=" + checksum
					+ "&truckId=" + truckId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		URL authURL = BoxAPIConnection.getAuthorizationURL("rl0okd37q0nm7ssnbb5x05k0efq1mt5i", url, "authenticated",
				arg3);
		String urlStr = authURL.toString();
		System.out.println("AUTHORIZATION URL ::::::: " + urlStr);
		session.setAttribute("itemList", itemList);
		session.setAttribute("source", source);
		session.setAttribute("destination", destination);
		session.setAttribute("customLocation", customLocation);
		session.setAttribute("SourceLatLong", request.getParameter("lat1"));
		session.setAttribute("DestinationLatLong", request.getParameter("lat2"));
		session.setAttribute("CustomLatLong", request.getParameter("lat3"));
		response.sendRedirect(urlStr);

		// return "redirect:/warehouseLogin";
	}

	@GetMapping("createBoxDocument")
	public String createBoxDocument(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String code = request.getParameter("code");
		BoxAPIConnection api = new BoxAPIConnection("rl0okd37q0nm7ssnbb5x05k0efq1mt5i",
				"WONwEVxGlyGbzo0juyHftfj1rW0VLHc5", code);
		try {

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String source = (String) session.getAttribute("source");
			String destination = (String) session.getAttribute("destination");
			String userName = request.getParameter("userName");
			String checksum = request.getParameter("fileHash");
			String fileName = request.getParameter("fileName");
			String deliveryDate = request.getParameter("deliveryDate");
			String deliveryNo = request.getParameter("deliveryNo");
			String custPO = request.getParameter("custPO");
			String truckId = request.getParameter("truckId");
			String customLocation = (String) session.getAttribute("customLocation");
			String sourceLatLong = (String) session.getAttribute("SourceLatLong");
			String destnationLatLong = (String) session.getAttribute("DestinationLatLong");
			String customLatLong = (String) session.getAttribute("CustomLatLong");
			String FILE = System.getProperty("java.io.tmpdir") + fileName;
			String documentId;
			BoxUtil boxUtil = new BoxUtil();
			documentId = boxUtil.interactWithBox(api, FILE);
			TestAPI testAPI = new TestAPI();
			String result = testAPI.createDocument(documentId, source, destination, dateFormat.format(date), userName,
					checksum, deliveryDate, deliveryNo, custPO, truckId, customLocation, sourceLatLong,
					destnationLatLong, customLatLong);
			List<Item> itemList = (List<Item>) session.getAttribute("itemList");
			System.out.println("ITEMLIST SIZE === " + itemList.size());
			for (Item item : itemList) {
				String itemName = item.getName();
				String itemQuantity = item.getQuantity();
				String itemDesc = item.getDescription();
				String itemVolume = item.getWeightvolume();
				System.out.println("EACH LINE ITEM ::: " + item.getName());
				testAPI.createDocumentItemList(documentId, itemName, itemQuantity, itemDesc, itemVolume);
			}
			session.removeAttribute("itemList");
			session.removeAttribute("SourceLatLong");
			session.removeAttribute("DestinationLatLong");
			session.removeAttribute("CustomLatLong");
			session.removeAttribute("source");
			session.removeAttribute("destination");
			session.removeAttribute("customLocation");

			File tempFile = new File(FILE);
			tempFile.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/warehouseLogin";
	}

	@PostMapping("updatecbpdf")
	public void updatePDFDoc(HttpSession session, ModelMap modelMap, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String source = request.getParameter("source");
		String destination = request.getParameter("destination");
		String deliveryDate = request.getParameter("deliveryDate");
		String deliveryNo = request.getParameter("deliveryNo");
		String custPO = request.getParameter("custPO");
		String userName = request.getParameter("username");
		String documentId = request.getParameter("documentId");
		String truckId = request.getParameter("truckId");
		String customLocation = request.getParameter("customLocation");
		System.out.println("DocumentID for update= " + documentId);
		int noOfRows = 0;
		System.out.println("tableRowCount : : : " + request.getParameter("tableRowCount"));
		if (request.getParameter("tableRowCount").equals("")) {
			noOfRows = 1;
		} else {
			String tableRowCount = request.getParameter("tableRowCount");
			noOfRows = Integer.valueOf(tableRowCount);
		}
		String FILE = System.getProperty("java.io.tmpdir") + "CBPdf_" + System.currentTimeMillis() + ".pdf";
		String fileName = "CBPdf_" + System.currentTimeMillis() + ".pdf";
		Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
		Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
		Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();

		List<String> arg3 = new ArrayList<String>();
		URI url = null;
		List<Item> itemList = new ArrayList<Item>();
		try {
			itemList = pdfCreate(request, source, destination, deliveryDate, deliveryNo, custPO, truckId,
					customLocation, noOfRows, FILE, subFont, smallBold, dateFormat, date);
			String checksum = "";
			MessageDigest md5Digest = MessageDigest.getInstance("MD5");
			checksum = BoxUtil.getFileChecksum(md5Digest, FILE);
			String serverURL = request.getRequestURL().toString();
			String str = serverURL;
			str = str.replace("updatecbpdf", "");
			url = new URI(str + "updateBoxDocument?" + "deliveryDate=" + deliveryDate + "&deliveryNo=" + deliveryNo
					+ "&custPO=" + custPO + "&userName=" + userName + "&fileName=" + fileName + "&fileHash=" + checksum
					+ "&documentId=" + documentId + "&truckId=" + truckId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		URL authURL = BoxAPIConnection.getAuthorizationURL("rl0okd37q0nm7ssnbb5x05k0efq1mt5i", url, "authenticated",
				arg3);
		String urlStr = authURL.toString();
		System.out.println("AUTHORIZATION URL ::::::: " + urlStr);
		session.setAttribute("itemList", itemList);
		session.setAttribute("source", source);
		session.setAttribute("destination", destination);
		session.setAttribute("customLocation", customLocation);
		session.setAttribute("SourceLatLong", request.getParameter("lat1"));
		session.setAttribute("DestinationLatLong", request.getParameter("lat2"));
		session.setAttribute("CustomLatLong", request.getParameter("lat3"));
		response.sendRedirect(urlStr);

		// return "redirect:/warehouseLogin";
	}

	@GetMapping("updateBoxDocument")
	public String updateBoxDocument(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String code = request.getParameter("code");
		BoxAPIConnection api = new BoxAPIConnection("rl0okd37q0nm7ssnbb5x05k0efq1mt5i",
				"WONwEVxGlyGbzo0juyHftfj1rW0VLHc5", code);
		try {

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String source = (String) session.getAttribute("source");
			String destination = (String) session.getAttribute("destination");
			String userName = request.getParameter("userName");
			String checksum = request.getParameter("fileHash");
			String fileName = request.getParameter("fileName");
			String deliveryDate = request.getParameter("deliveryDate");
			String deliveryNo = request.getParameter("deliveryNo");
			String custPO = request.getParameter("custPO");
			String truckId = request.getParameter("truckId");
			String customLocation = (String) session.getAttribute("customLocation");
			String sourceLatLong = (String) session.getAttribute("SourceLatLong");
			String destnationLatLong = (String) session.getAttribute("DestinationLatLong");
			String customLatLong = (String) session.getAttribute("CustomLatLong");

			String FILE = System.getProperty("java.io.tmpdir") + fileName;
			System.out.println("FILE : : :  " + FILE);
			String documentId = request.getParameter("documentId");
			BoxUtil boxUtil = new BoxUtil();
			// String filePath = System.getProperty("java.io.tmpdir") +
			// fileName;
			boxUtil.updateBoxDocument(api, documentId, FILE);

			TestAPI testAPI = new TestAPI();
			String result = testAPI.updateRejectedDocument(documentId, source, destination, deliveryDate, deliveryNo,
					custPO, checksum, dateFormat.format(date), userName, truckId, customLocation, sourceLatLong,
					destnationLatLong, customLatLong);
			String getLedgerItemList = testAPI.viewItemListByDocumentId(documentId);
			Gson myGson = new Gson();
			JsonParser jsonParser = new JsonParser();
			JsonArray itemArray = jsonParser.parse(getLedgerItemList).getAsJsonArray();
			for (JsonElement itemDetails : itemArray) {
				Item item = myGson.fromJson(itemDetails, Item.class);
				String itemId = item.getItemId();
				testAPI.deleteLineItemByItemId(itemId);
			}
			List<Item> itemList = (List<Item>) session.getAttribute("itemList");
			for (Item item : itemList) {
				String itemName = item.getName();
				String itemQuantity = item.getQuantity();
				String itemDesc = item.getDescription();
				String itemVolume = item.getWeightvolume();
				System.out.println("EACH LINE ITEM ::: " + item.getName());
				testAPI.createDocumentItemList(documentId, itemName, itemQuantity, itemDesc, itemVolume);
			}

			session.removeAttribute("itemList");
			session.removeAttribute("SourceLatLong");
			session.removeAttribute("DestinationLatLong");
			session.removeAttribute("CustomLatLong");
			session.removeAttribute("source");
			session.removeAttribute("destination");
			session.removeAttribute("customLocation");
			File tempFile = new File(FILE);
			tempFile.delete();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return "redirect:/warehouseLogin";
	}

	private List<Item> pdfCreate(HttpServletRequest request, String source, String destination, String deliveryDate,
			String deliveryNo, String custPO, String truckId, String customLocation, int noOfRows, String FILE,
			Font subFont, Font smallBold, DateFormat dateFormat, Date date)
			throws DocumentException, FileNotFoundException {
		Document document = new Document();
		// PdfWriter.getInstance(document, new FileOutputStream(FILE));
		PdfWriter.getInstance(document, new FileOutputStream(FILE, false));
		document.open();

		Paragraph p = new Paragraph();
		p.add("Custom Book Document");
		p.setAlignment(Element.ALIGN_CENTER);
		p.setFont(subFont);
		document.add(p);

		Chunk linebreak = new Chunk();
		document.add(linebreak);

		Paragraph p4 = new Paragraph();
		p4.add("Receipt Generated On : " + dateFormat.format(date));
		p4.setFont(smallBold);
		document.add(p4);

		Paragraph p5 = new Paragraph();
		p5.add("Source : " + source);
		p5.setFont(smallBold);
		document.add(p5);

		Paragraph p6 = new Paragraph();
		p6.add("Destination : " + destination);
		p6.setFont(smallBold);
		document.add(p6);

		Paragraph p7 = new Paragraph();
		p7.add("Delivery Date : " + deliveryDate);
		p7.setFont(smallBold);
		document.add(p7);

		Paragraph p8 = new Paragraph();
		p8.add("Delivery No : " + deliveryNo);
		p8.setFont(smallBold);
		document.add(p8);

		Paragraph p9 = new Paragraph();
		p9.add("Customer PO No : " + custPO);
		p9.setFont(smallBold);
		document.add(p9);

		Paragraph p12 = new Paragraph();
		p12.add("Truck ID : " + truckId);
		p12.setFont(smallBold);
		document.add(p12);

		Paragraph p11 = new Paragraph();
		p11.add("Custom Location : " + customLocation);
		p11.setFont(smallBold);
		document.add(p11);

		Chunk linebreak1 = new Chunk();
		document.add(linebreak1);

		Chunk linebreak5 = new Chunk();
		document.add(linebreak5);

		Paragraph p10 = new Paragraph();
		p10.add("PRODUCT DETAILS");
		p10.setAlignment(Element.ALIGN_CENTER);
		p10.setFont(subFont);
		document.add(p10);

		Chunk linebreak2 = new Chunk();
		document.add(linebreak2);

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(70);

		PdfPCell c1 = new PdfPCell(new Phrase("Serial No.", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Item Name", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Quantity", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Description", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Weight & Volume", smallBold));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(c1);
		table.setHeaderRows(1);
		List<Item> items = new ArrayList<Item>();
		for (int i = 1; i <= noOfRows; i++) {
			Item item = new Item();
			table.addCell(Integer.toString(i));
			table.addCell(request.getParameter("txtbox_" + i + "_1"));
			item.setName(request.getParameter("txtbox_" + i + "_1"));
			table.addCell(request.getParameter("txtbox_" + i + "_2"));
			item.setQuantity(request.getParameter("txtbox_" + i + "_2"));
			table.addCell(request.getParameter("txtbox_" + i + "_3"));
			item.setDescription(request.getParameter("txtbox_" + i + "_3"));
			table.addCell(request.getParameter("txtbox_" + i + "_4"));
			item.setWeightvolume(request.getParameter("txtbox_" + i + "_4"));
			items.add(item);
		}
		document.add(table);
		document.close();
		System.out.println("ITEMLIST === " + items);
		return items;
	}

}