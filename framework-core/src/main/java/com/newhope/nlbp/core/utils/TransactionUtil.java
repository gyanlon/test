package com.newhope.nlbp.core.utils;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class TransactionUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionUtil.class);
	@Autowired
	private PlatformTransactionManager transactionManager;

	public boolean transact(Consumer<?> consumer) {
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			consumer.accept(null);
			transactionManager.commit(status);
			return true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			LOGGER.error("Transaction error", e);
			e.printStackTrace();
			return false;
		}

	}
}
