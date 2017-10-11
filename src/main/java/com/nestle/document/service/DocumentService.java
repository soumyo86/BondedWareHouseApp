package com.nestle.document.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nestle.document.model.Document;
import com.nestle.document.model.Item;
import com.nestle.document.model.Status;
import com.nestle.document.model.Transaction;

@Service
public interface DocumentService {

	public Status countStatus();

	public List<Document> searchDocumentByStatus(String status);

	public String updateStatus(String id, String status, String timeStamp, String username, String reasonCode);

	// public String searchDocTrxHistory(String id);

	public List<Transaction> searchDocumentByID(String id);

	public List<Document> searchDocumentWithID(String id);

	public List<Item> getItemListByDocumentID(String id);

	public List<Document> gethighPriorityDoument();

}
