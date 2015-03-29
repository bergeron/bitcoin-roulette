package main.java.pw.bitcoinroulette.server.bitcoin;

import java.rmi.RemoteException;
import java.util.Observable;
import java.util.Observer;

import main.java.pw.bitcoinroulette.server.models.AuthPlayerImpl;
import main.java.pw.bitcoinroulette.server.models.TransactionImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

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
	 * (Intra-wallet transactions will get double called, once for send, once for receive)
	 */
	@Override
	public synchronized void update(Observable o, Object arg) {
		Transaction mainTx = (Transaction) arg;
		String txHash = mainTx.getTxid();
		long conf = mainTx.getConfirmations();

		/* Only care about new transactions */
		if (conf != 0) {
			return;
		}

		Session session = sessionFactory.openSession();

		for (Transaction t : mainTx.getDetails()) {

			/* Only care about incoming transactions */
			if (t.getCategory() == Transaction.Category.RECEIVE) {
				System.out.printf("new incoming tx: %s\n", txHash);

				session.beginTransaction();

				/* Should only be seen if sent & received from within same wallet */
				boolean seen = (long)(session.createCriteria(TransactionImpl.class)
						.add(Restrictions.eq("hash", txHash))
						.setProjection(Projections.rowCount())
						.uniqueResult()) == 1;

				if(seen){
					continue;
				}

				AuthPlayerImpl player = (AuthPlayerImpl) session
						.createQuery("from AuthPlayerImpl where bitcoinAddress = :bitcoinAddress")
						.setString("bitcoinAddress", t.getAddress()).uniqueResult();

				if (player == null) {
					System.err.printf("No player associated with address: %s\n", t.getAddress());
				} else {
					try {
						session.save(new TransactionImpl(txHash, t.getAmount(), conf, mainTx.getTime(), player));
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}

				session.getTransaction().commit();
			}
		}

		session.close();
	}
}
