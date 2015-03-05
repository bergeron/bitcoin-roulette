package main.java.pw.bitcoinroulette.server.bitcoin;

import java.util.Observable;
import java.util.Observer;

import main.java.pw.bitcoinroulette.server.AuthPlayerImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com._37coins.bcJsonRpc.pojo.Transaction;

/* Listen for new 0 confirmation transactions when user funds account */
public class NewTransactionListener implements Observer {
	
	SessionFactory sessionFactory;
	
	public NewTransactionListener(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/*
	 * Called twice for each incoming or outgoing transaction.
	 * Once when first seen by network (0 confirmations),
	 * Again when in first block (1 confirmation)
	 */
	@Override
	public void update(Observable o, Object arg) {
		Transaction mainTx = (Transaction) arg;
		String txid = mainTx.getTxid();
		long conf = mainTx.getConfirmations();

		/* Only care about new transactions */
		if (conf != 0) {
			return;
		}
		
		Session session = sessionFactory.openSession();

		for (Transaction t : mainTx.getDetails()) {
			/* Only care about incoming transactions */
			if (t.getCategory() == Transaction.Category.RECEIVE) {
				System.out.println(txid);
				
				session.beginTransaction();
				AuthPlayerImpl player = (AuthPlayerImpl) session
						.createQuery("from AuthPlayerImpl where bitcoinAddress = :bitcoinAddress")
						.setString("bitcoinAddress", t.getAddress()).uniqueResult();
				session.getTransaction().commit();
				System.out.println(player);
			}
		}
		
		session.close();
	}
}
