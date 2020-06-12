package com.tenpo.challenge.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.tenpo.challenge.auth.model.User;

/**
 * Entity that represents the user action among the result
 *
 * @author Fermin Recalt
 */

@Entity
@Table(name = "transaction_history")
public class TransactionHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@ManyToOne
	private User user;
	
	private OffsetDateTime createdAt;
	
	@Enumerated(EnumType.ORDINAL)
	private TransactionResult result;
	
	private String operationName;
	
	public TransactionHistory() {
	}
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public TransactionResult getResult() {
		return result;
	}
	
	public void setResult(TransactionResult result) {
		this.result = result;
	}
	
	public String getOperationName() {
		return operationName;
	}
	
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	
	@PrePersist
	public void onPrePersist() {
		OffsetDateTime time = OffsetDateTime.now();
		this.setCreatedAt(time);
	}
}
