package com.nestle.fabric;

public class TestAPI {
	// https://350ee7b2e3fd484c9edae87585e5b52e-vp0.us.blockchain.ibm.com:5002
	public static final String vpUrl = "http://161.202.174.138:7050";
	// 75137519d13ac4c971c1248f41993cd07b71ea049836765610abf9fa1a1dabf94b64d7a6deb76f809123aaed29e0ee9b0584918153a3e78f349aea58ffb02c91
	public static final String chainCodeName = "4cc6635d71c61f46f4bffd7422acd651e1f3909640678a848c102f20102120694fb9bd1caa57395a39984ef5038a1516fe4368c068256a6fbf5867e9cc63b5b8";
	public static final String user = "test_user0";
	// 15a532bf33
	public static final String userSecret = "MS9qrN8hFjlE";

	public String interactWithLedger(String action, String documentId, String status, String timeStamp,
			String updatedBy, String reasonCode) {

		String response = "";

		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		if (action == "update") {
			updateDocumentStatus(apiCallUtil, documentId, status, timeStamp, updatedBy, reasonCode);
			response = "Document status updated successfully";
		} else if (action == "recordCount") {
			response = countDocumentByStatus(apiCallUtil, status);
			return response;
		} else
			response = searchDocumentByStatus(apiCallUtil, status);
		// System.out.println(apiCallUtil.register());
		return response;
	}

	public static String createDocument(String documentId, String source, String destination, String timeStamp,
			String updatedBy, String documentHash, String deliveryDate, String deliveryNo, String custPO,
			String truckId, String customLocation, String sourceLatLong, String destnationLatLong,
			String customLatLong) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { documentId, source, destination, timeStamp, updatedBy, documentHash, deliveryDate, deliveryNo,
				custPO, truckId, customLocation, sourceLatLong, destnationLatLong, customLatLong };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("invoke");
		hyperLedgerRequest.setCallFunction("createDocument");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String resp = response.getMessage();
		return resp;
	}

	public static String createDocumentItemList(String documentId, String itemName, String itemQuantity,
			String itemDesc, String itemVolume) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { documentId, itemName, itemQuantity, itemDesc, itemVolume };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("invoke");
		hyperLedgerRequest.setCallFunction("insertItemList");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String resp = response.getMessage();
		return resp;
	}

	public static String searchDocumentByStatus(APICallUtil apiCallUtil, String key) {
		String[] args = { key };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("query");
		hyperLedgerRequest.setCallFunction("viewDocumentsByStatus");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String details = response.getMessage();
		return details;
	}

	public static String countDocumentByStatus(APICallUtil apiCallUtil, String key) {
		String[] args = { key };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("query");
		hyperLedgerRequest.setCallFunction("countDocumentsByStatus");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String details = response.getMessage();
		String totalCount = details.replace("\"", "");
		return totalCount;
	}

	public static void updateDocumentStatus(APICallUtil apiCallUtil, String documentId, String status, String timeStamp,
			String updatedBy, String reasonCode) {
		String[] args = { documentId, status, timeStamp, updatedBy, reasonCode };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("invoke");
		hyperLedgerRequest.setCallFunction("updateDocumentStatus");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		System.out.println(response.getMessage());
	}

	public static String viewDocumentTrxHistory(String documentId) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { documentId };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("query");
		hyperLedgerRequest.setCallFunction("viewDocumentTransactionHistory");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String details = response.getMessage();
		return details;
	}

	public static String getDetailsByDocumetId(String documentId) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { documentId };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("query");
		hyperLedgerRequest.setCallFunction("viewDetailsByDocId");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String details = response.getMessage();
		return details;
	}

	public static String getItemListByDocumetId(String documentId) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { documentId };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("query");
		hyperLedgerRequest.setCallFunction("viewItemListByDocumentId");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String details = response.getMessage();
		return details;
	}

	public static String updateRejectedDocument(String documentId, String source, String destination,
			String deliveryDate, String deliveryNo, String custPO, String documentHash, String timeStamp,
			String updatedBy, String truckId, String customLocation, String sourceLatLong, String destnationLatLong,
			String customLatLong) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { documentId, source, destination, deliveryDate, deliveryNo, custPO, documentHash, timeStamp,
				updatedBy, truckId, customLocation, sourceLatLong, destnationLatLong, customLatLong };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("invoke");
		hyperLedgerRequest.setCallFunction("updateRejectedDocument");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String resp = response.getMessage();
		return resp;
	}

	public static String viewItemListByDocumentId(String documentId) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { documentId };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("query");
		hyperLedgerRequest.setCallFunction("viewItemListByDocumentId");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String resp = response.getMessage();
		return resp;
	}

	public static String deleteLineItemByItemId(String itemId) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { itemId };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("invoke");
		hyperLedgerRequest.setCallFunction("deleteLineItem");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String resp = response.getMessage();
		return resp;
	}

	public static String gethighPriorityDocumentByStatus(String flagValue) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { flagValue };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("query");
		hyperLedgerRequest.setCallFunction("viewAllHighPriorityDocuments");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String details = response.getMessage();
		System.out.println("gethighPriorityDocumentByStatus === " + details);
		return details;
	}

	public static String viewTruckDetailsByDocId(String documentId) {
		APICallUtil apiCallUtil = new APICallUtil(vpUrl, user, userSecret);
		String[] args = { documentId };
		HyperLedgerRequest hyperLedgerRequest = new HyperLedgerRequest();
		hyperLedgerRequest.setMethod("query");
		hyperLedgerRequest.setCallFunction("viewTruckDetailsByDocId");
		hyperLedgerRequest.setChainCodeName(chainCodeName);
		hyperLedgerRequest.setFunctionArgs(args);
		hyperLedgerRequest.setSecureContext(user);
		HyperLedgerResponse response = apiCallUtil.invokeMethod(hyperLedgerRequest);
		String details = response.getMessage();
		return details;
	}

}