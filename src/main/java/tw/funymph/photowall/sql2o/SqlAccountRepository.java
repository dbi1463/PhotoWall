/* SqlAccountRepository.java created on Mar 1, 2017.
 * 
 * Copyright (C) Funymph all rights reserved.
 *
 * This file is a part of the PhotoWall project.
 */
package tw.funymph.photowall.sql2o;

import java.util.HashMap;
import java.util.Map;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import tw.funymph.photowall.core.Account;
import tw.funymph.photowall.core.repository.AccountRepository;
import tw.funymph.photowall.core.repository.RepositoryException;

/**
 * This class provides the implementation of {@link AccountRepository}
 * with the Sql2o technology.
 * 
 * @author Spirit Tu
 * @version 1.0
 * @since 1.0
 */
public class SqlAccountRepository implements AccountRepository {

	private Sql2o sql2o;

	/**
	 * Construct a <code>SqlAccountRepository</code> instance with
	 * the Sql2o instance.
	 * 
	 * @param sql2o the Sql2o instance
	 */
	public SqlAccountRepository(Sql2o sql2o) {
		this.sql2o = sql2o;
		Map<String, String> mapping = new HashMap<>();
		mapping.put("ID", "identity");
		mapping.put("NICKANME", "nickname");
		mapping.put("PASSWORD", "password");
		sql2o.setDefaultColumnMappings(mapping);
	}

	@Override
	public Account get(String identity) {
		Account account = null;
		try (Connection connection = sql2o.open()) {
			account = connection.createQuery("select * from ACCOUNT where ID=:identity")
				.addParameter("identity", identity)
				.executeAndFetchFirst(Account.class);
		}
		return account;
	}

	@Override
	public void save(Account account) throws RepositoryException {
		try (Connection connection = sql2o.beginTransaction()) {
			connection.createQuery("insert into ACCOUNT(ID, NICKNAME, PASSWORD) values (:identity, :nickname, :password)")
				.bind(account)
				.executeUpdate();
			connection.commit();
		}
		catch (Sql2oException e) {
			throw new RepositoryException(e.getMessage(), e);
		}
	}
}
