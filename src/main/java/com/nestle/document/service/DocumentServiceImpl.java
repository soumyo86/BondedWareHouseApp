package com.nestle.document.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nestle.document.model.Document;
import com.nestle.document.model.Item;
import com.nestle.document.model.Status;
import com.nestle.document.model.Transaction;
import com.nestle.fabric.TestAPI;

@Component
@Service("loginService")
public class DocumentServiceImpl implements DocumentService {

	private Status status;
	private TestAPI testAPI;

	public DocumentServiceImpl() {
		testAPI = new TestAPI();
	}

	@Override
	public Status countStatus() {
		// testAPI = new TestAPI();
		status = new Status();

		status.setInitiated(testAPI.interactWithLedger("recordCount", "", "Initiated", "", "", ""));
		status.setRejected(testAPI.interactWithLedger("recordCount", "", "Rejected", "", "", ""));
		status.setApproved(testAPI.interactWithLedger("recordCount", "", "Approved", "", "", ""));
		status.setProcessed(testAPI.interactWithLedger("recordCount", "", "Processed", "", "", ""));
		status.setReceived(testAPI.interactWithLedger("recordCount", "", "Received", "", "", ""));
		status.setVerified(testAPI.interactWithLedger("recordCount", "", "Verified", "", "", ""));
		return status;
	}

	@Override
	public List<Document> searchDocumentByStatus(String status) {

		String searchResult = testAPI.interactWithLedger("searchDocumentByStatus", "", status, "", "", "");

		Gson myGson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonArray docArray = jsonParser.parse(searchResult).getAsJsonArray();
		List<Document> docList = new ArrayList<Document>();
		for (JsonElement doc : docArray) {
			Document orderDocument = myGson.fromJson(doc, Document.class);
			docList.add(orderDocument);
			System.out.println(orderDocument.getDocumentId());
			System.out.println(orderDocument.getSource());
			System.out.println(orderDocument.getDestination());
			System.out.println(orderDocument.getStatus());
		}

		return docList;

	}

	@Override
	public String updateStatus(String id, String status, String timeStamp, String username, String reasonCode) {
		System.out.println("document Id" + id);
		System.out.println("doc status" + status);
		try {
			testAPI.interactWithLedger("update", id, status, timeStamp, username, reasonCode);
		} catch (Exception e) {
			return "fail";
		}
		return "success";

	}

	/*
	 * @Override public String searchDocTrxHistory(String id){
	 * System.out.println("Searched document id::: "+ id); String result = "";
	 * try { result = testAPI.viewDocumentTrxHistory(id);
	 * 
	 * } catch (Exception e) { return "fail"; } return result; }
	 */

	@Override
	public List<Transaction> searchDocumentByID(String id) {

		String result = testAPI.viewDocumentTrxHistory(id);

		Gson myGson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonArray docArray = jsonParser.parse(result).getAsJsonArray();
		List<Transaction> transactions = new ArrayList<Transaction>();
		for (JsonElement doc : docArray) {
			Transaction transactionlist = myGson.fromJson(doc, Transaction.class);
			transactions.add(transactionlist);
			System.out.println(transactionlist.getDocumentId());
			System.out.println(transactionlist.getStatus());
			System.out.println(transactionlist.getTimeStamp());
			System.out.println(transactionlist.getTrxId());
			System.out.println(transactionlist.getUpdatedBy());
		}

		return transactions;

	}

	@Override
	public List<Document> searchDocumentWithID(String id) {
		String docResult = testAPI.getDetailsByDocumetId(id);

		Gson myGson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonArray docArray = jsonParser.parse(docResult).getAsJsonArray();
		List<Document> doclist = new ArrayList<Document>();
		for (JsonElement doc : docArray) {
			Document doclists = myGson.fromJson(doc, Document.class);
			doclist.add(doclists);
			System.out.println("search by id " + doclists.getDocumentId());
			System.out.println(doclists.getSource());
			System.out.println(doclists.getDestination());
			System.out.println(doclists.getStatus());
		}

		return doclist;
	}

	@Override
	public List<Item> getItemListByDocumentID(String id) {
		String itemListResult = testAPI.getItemListByDocumetId(id);
		System.out.println("Item list String populated from blockchain :::: " + itemListResult);
		Gson myGson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonArray itemArray = jsonParser.parse(itemListResult).getAsJsonArray();
		List<Item> items = new ArrayList<Item>();
		for (JsonElement item : itemArray) {
			Item itemElem = myGson.fromJson(item, Item.class);
			items.add(itemElem);
		}

		return items;
	}

	@Override
	public List<Document> gethighPriorityDoument() {
		String flagValue = "Y";
		String searchResult = testAPI.gethighPriorityDocumentByStatus(flagValue);

		Gson myGson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonArray docArray = jsonParser.parse(searchResult).getAsJsonArray();
		List<Document> docList = new ArrayList<Document>();
		for (JsonElement doc : docArray) {
			Document orderDocument = myGson.fromJson(doc, Document.class);
			docList.add(orderDocument);

		}

		return docList;

	}

}
